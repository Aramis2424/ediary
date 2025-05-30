import asyncio
from datetime import date, timedelta
from faker import Faker
from random import randint
from pathlib import Path
import bcrypt


class DataGenerator:
    DATA_FOLDER = "./data/"
    OWNERS_FILE = DATA_FOLDER + "owners.sql"
    DIARIES_FILE = DATA_FOLDER + "diaries.sql"
    MOODS_FILE = DATA_FOLDER + "moods.sql"
    ENTRY_FILE = DATA_FOLDER + "entry.sql"
    IVAN_FILE = DATA_FOLDER + "ivan.sql"

    gender = ["male", "female"]
    faker = Faker("en_US")

    def __init__(self, max_count=10):
        self.MAX_COUNT = max_count

        self.owners_cnt = 0
        self.diaries_cnt = 0
        self.moods_cnt = 0
        self.entries_cnt = 0

        self.cnt_entries_per_diary = {}

        directory = Path(self.DATA_FOLDER)
        directory.mkdir(parents=True, exist_ok=True)

    async def generate_all(self):
        print("=== Start generation ===")
        await asyncio.gather(self.generate_owners())
        await asyncio.gather(
            self.generate_moods(),
            self.generate_diaries()
        )
        await asyncio.gather(self.generate_entries())
        self.generate_ivan()
        print("=== Generations successfully ===")

    def generate_ivan(self, cnt_diaries=2, cnt_moods=3, cnt_entries=3):
        generator = IvanGenerator(self.owners_cnt, self.diaries_cnt, cnt_diaries,
                                  self.moods_cnt, cnt_moods, self.entries_cnt, cnt_entries)
        sql_lines = generator.generate_all()
        with open(self.IVAN_FILE, "w") as file:
            for line in sql_lines:
                file.write(line)
        print("--- generate ivan successfully ---")

    async def generate_owners(self):
        self.owners_cnt = self.MAX_COUNT
        with open(self.OWNERS_FILE, "w") as file:
            file.write(SQLTruncateCommand("owners").get())
            for i in range(self.MAX_COUNT):
                line = self.create_owner_attributes_line()
                file.write(line)
        print("--- generate owners successfully ---")

    def create_owner_attributes_line(self):
        name = self.faker.first_name()
        birth_date = self.faker.date_between_dates(date(1970, 1, 1), date(2010, 1, 1))
        login = DataGenerator.get_login_from_name(name)
        password = DataGenerator.bcrypt_password(self.faker.word() + str(randint(1, 199)))
        created_date = self.faker.date_between_dates(date(2010, 1, 1), date(2015, 1, 1))

        table = "owners"
        columns = ["name", "birth_date", "login", "password", "created_date"]
        values = [name, birth_date, login, password, created_date]

        return DataGenerator.create_insert_command_line(table, columns, values)

    async def generate_diaries(self):
        with open(self.DIARIES_FILE, "w") as file:
            file.write(SQLTruncateCommand("diaries").get())
            diary_id = 1
            for owner_id in range(1, self.owners_cnt + 1):
                cnt_per_owner = DataGenerator.get_number_of_diaries()
                for j in range(cnt_per_owner):
                    line = self.create_diary_attributes_line(owner_id, diary_id)
                    file.write(line)
                    diary_id += 1
                self.diaries_cnt += cnt_per_owner
        print("--- generate diaries successfully ---")

    def create_diary_attributes_line(self, owner_id, diary_id):
        title = self.faker.sentence(nb_words=2)
        description = " ".join(self.faker.sentences(nb=1))
        cnt_entries = DataGenerator.get_number_of_entries()
        created_date = self.faker.date_between_dates(date(2015, 1, 1), date(2020, 1, 1))

        self.cnt_entries_per_diary[diary_id] = cnt_entries

        table = "diaries"
        columns = ["owner_fk", "title", "description", "cnt_entries", "created_date"]
        values = [owner_id, title, description, cnt_entries, created_date]

        return DataGenerator.create_insert_command_line(table, columns, values)

    async def generate_moods(self):
        with open(self.MOODS_FILE, "w") as file:
            file.write(SQLTruncateCommand("moods").get())
            for owner_id in range(1, self.owners_cnt + 1):
                cnt_per_owner = DataGenerator.get_number_of_moods()
                for j in range(cnt_per_owner):
                    line = self.create_mood_attributes_line(owner_id)
                    file.write(line)
                self.moods_cnt += cnt_per_owner
        print("--- generate moods successfully ---")

    def create_mood_attributes_line(self, owner_id):
        score_mood = randint(1, 10)
        score_productivity = randint(1, 10)
        created_date = self.faker.date_between_dates(date(2020, 1, 1), date(2024, 1, 1))

        bedtime = self.faker.date_time_between(date(2020, 1, 1), date(2024, 1, 1))
        wake_up_time = bedtime + timedelta(hours=randint(1, 8))

        bedtime = DataGenerator.get_formatted_datetime(bedtime)
        wake_up_time = DataGenerator.get_formatted_datetime(wake_up_time)

        table = "moods"
        columns = ["owner_fk", "score_mood", "score_productivity", "bedtime", "wake_up_time", "created_date"]
        values = [owner_id, score_mood, score_productivity, bedtime, wake_up_time, created_date]

        return DataGenerator.create_insert_command_line(table, columns, values)

    async def generate_entries(self):
        with open(self.ENTRY_FILE, "w") as file:
            file.write(SQLTruncateCommand("entries").get())
            for diary_id, cnt_entries in self.cnt_entries_per_diary.items():
                for i in range(cnt_entries):
                    line = self.create_entry_attributes_line(diary_id)
                    file.write(line)
                self.entries_cnt += cnt_entries
        print("--- generate entries successfully ---")

    def create_entry_attributes_line(self, diary_id):
        title = self.faker.sentence(nb_words=2)
        content = " ".join(self.faker.sentences(nb=3))
        created_date = self.faker.date_between_dates(date(2020, 1, 1), date(2024, 1, 1))

        table = "entries"
        columns = ["diary_fk", "title", "content", "created_date"]
        values = [diary_id, title, content, created_date]

        return DataGenerator.create_insert_command_line(table, columns, values)

    @staticmethod
    def get_login_from_name(name):
        return name.lower() + str(randint(100, 999))

    @staticmethod
    def bcrypt_password(plain_password):
        salt = bcrypt.gensalt()
        hashed_password = bcrypt.hashpw(plain_password.encode('utf-8'), salt)
        return hashed_password.decode('utf-8')

    @staticmethod
    def get_number_of_diaries():
        chance = randint(1, 100)
        if chance <= 50:
            return 1
        elif chance <= 75:
            return 2
        elif chance <= 90:
            return 3
        else:
            return 0

    @staticmethod
    def get_number_of_entries():
        chance = randint(1, 100)
        if chance <= 80:
            return randint(1, 4)
        else:
            return 0

    @staticmethod
    def get_number_of_moods():
        chance = randint(1, 100)
        if chance <= 80:
            return randint(1, 4)
        else:
            return 0

    @staticmethod
    def get_formatted_datetime(datetime):
        return datetime.strftime('%Y-%m-%dT%H:%M:%S')

    @staticmethod
    def create_insert_command_line(table, columns, values):
        line_builder = SQLInsertCommandBuilder()
        line = (
            line_builder
            .set_table(table)
            .set_columns(columns)
            .set_values(values)
            .build()
        )
        line = line.get() if line else " "
        line += "\n"
        return line


class SQLTruncateCommand:
    def __init__(self, table_name):
        self.table = table_name

    def get(self):
        return f"TRUNCATE TABLE {self.table} RESTART IDENTITY CASCADE;\n"


class SQLInsertCommand:
    def __init__(self):
        self.prefix = "INSERT INTO"
        self.table = None
        self.columns = None
        self.key_word = "VALUES"
        self.values = None
        self.postfix = ";"

    def get(self):
        return f"{self.prefix} {self.table} {self.columns} {self.key_word} {self.values}{self.postfix}"


class SQLInsertCommandBuilder:
    def __init__(self):
        self.sql_command = SQLInsertCommand()

    def set_table(self, table):
        self.sql_command.table = table
        return self

    def set_columns(self, columns):
        str_columns = "(" + ", ".join(columns) + ")"
        self.sql_command.columns = str_columns
        return self

    def set_values(self, values):
        values = [str(val) if isinstance(val, (int, float)) else f"'{str(val)}'" for val in values]
        str_values = "(" + ", ".join(values) + ")"
        self.sql_command.values = str_values
        return self

    def build(self):
        if not self.sql_command.table or not self.sql_command.columns or not self.sql_command.values:
            return None
        return self.sql_command


class IvanGenerator:
    faker = Faker("en_US")

    def __init__(self, ivan_id, diary_id, diary_cnt, mood_id, mood_cnt, entry_id, entry_cnt):
        self.ivan_id = ivan_id + 1
        self.diary_id = diary_id + 1
        self.mood_id = mood_id + 1
        self.entry_id = entry_id + 1

        self.diary_cnt = diary_cnt
        self.mood_cnt = mood_cnt
        self.entry_cnt = entry_cnt

        self.sql_lines = []

    def generate_all(self):
        self.generate_account()
        self.generate_diaries(self.ivan_id, self.diary_cnt, self.entry_cnt)
        self.generate_moods(self.ivan_id, self.mood_cnt)
        self.generate_entries(self.diary_id, self.entry_cnt)
        return self.sql_lines

    def generate_account(self):
        name = "Ivan"
        login = "ivan01"
        password = DataGenerator.bcrypt_password("navi01")
        birth_date = date(2000, 1, 1)
        created_date = date(2020, 1, 1)

        table = "owners"
        columns = ["name", "birth_date", "login", "password", "created_date"]
        values = [name, birth_date, login, password, created_date]
        line = DataGenerator.create_insert_command_line(table, columns, values)
        self.sql_lines.append(line)

    def generate_diaries(self, owner_id, diary_cnt, entry_cnt):
        for _ in range(diary_cnt):
            title = self.faker.sentence(nb_words=2)
            description = " ".join(self.faker.sentences(nb=1))
            cnt_entries = entry_cnt
            created_date = self.faker.date_between_dates(date(2020, 2, 1), date(2020, 2, 20))

            table = "diaries"
            columns = ["owner_fk", "title", "description", "cnt_entries", "created_date"]
            values = [owner_id, title, description, cnt_entries, created_date]
            line = DataGenerator.create_insert_command_line(table, columns, values)
            self.sql_lines.append(line)

    def generate_moods(self, owner_id, mood_cnt):
        for _ in range(mood_cnt):
            score_mood = randint(1, 10)
            score_productivity = randint(1, 10)
            created_date = self.faker.date_between_dates(date(2020, 3, 1), date(2020, 3, 20))

            bedtime = self.faker.date_between_dates(date(2020, 2, 1), date(2020, 2, 20))
            wake_up_time = bedtime + timedelta(hours=randint(1, 8))

            bedtime = DataGenerator.get_formatted_datetime(bedtime)
            wake_up_time = DataGenerator.get_formatted_datetime(wake_up_time)

            table = "moods"
            columns = ["owner_fk", "score_mood", "score_productivity", "bedtime", "wake_up_time", "created_date"]
            values = [owner_id, score_mood, score_productivity, bedtime, wake_up_time, created_date]

            line = DataGenerator.create_insert_command_line(table, columns, values)
            self.sql_lines.append(line)

    def generate_entries(self, diary_id, entries_cnt):
        for _ in range(entries_cnt):
            title = self.faker.sentence(nb_words=2)
            content = " ".join(self.faker.sentences(nb=3))
            created_date = self.faker.date_between_dates(date(2020, 3, 1), date(2020, 3, 20))

            table = "entries"
            columns = ["diary_fk", "title", "content", "created_date"]
            values = [diary_id, title, content, created_date]

            line = DataGenerator.create_insert_command_line(table, columns, values)
            self.sql_lines.append(line)


def main():
    generator = DataGenerator()
    asyncio.run(generator.generate_all())


if __name__ == "__main__":
    main()
