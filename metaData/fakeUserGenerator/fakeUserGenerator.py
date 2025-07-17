import random
from datetime import date, timedelta, datetime
from faker import Faker
from random import randint
from pathlib import Path
import bcrypt


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


class JsonLineMaker:
    def __init__(self):
        self.attributes = None
        self.values = None

    def set_attributes(self, attributes):
        attributes = [f'"{str(attr)}"' for attr in attributes]
        self.attributes = attributes
        return self

    def set_values(self, values):
        self.values = [str(val) if isinstance(val, (int, float)) else f'"{str(val)}"' for val in values]
        return self

    def build(self):
        if not self.attributes or not self.values:
            return " "
        pairs = [f"{attr}:{val}" for attr, val in zip(self.attributes, self.values)]

        return '{ ' + ", ".join(pairs) + ' }'


class IdealUserGenerator:
    DATA_FOLDER = "./data/"
    FILE_SQL = DATA_FOLDER + "ideal.sql"
    FILE_JSON = DATA_FOLDER + "ideal.json"

    faker = Faker("ru_RU")

    def __init__(self, mood_cnt=21, entry_cnt=20):
        directory = Path(self.DATA_FOLDER)
        directory.mkdir(parents=True, exist_ok=True)

        self.owner_id = 1
        self.diary_id = 1

        self.diary_cnt = 1
        self.mood_cnt = mood_cnt
        self.entry_cnt = entry_cnt

        self.sql_lines = []
        self.json_lines = []

    def generate_all(self):
        self.generate_account(self.owner_id)
        self.generate_diaries(self.owner_id, self.entry_cnt)
        self.generate_moods(self.owner_id, self.mood_cnt)
        self.generate_entries(self.diary_id, self.entry_cnt)
        self.write_sql()
        self.write_json()

    def write_sql(self):
        with open(self.FILE_SQL, "w") as file:
            for line in self.sql_lines:
                file.write(line)

    def write_json(self):
        with open(self.FILE_JSON, "w") as file:
            for line in self.json_lines:
                file.write(str(line))

    def generate_account(self, owner_id):
        name = "Иван"
        login = "ideal01"
        password = self.bcrypt_password("ideal01")
        birth_date = date(2000, 1, 1)
        created_date = date.today() - timedelta(days=30)

        table = "owners"
        columns = ["name", "birth_date", "login", "password", "created_date"]
        values = [name, birth_date, login, password, created_date]

        line = self.create_insert_command_line(table, columns, values)
        self.sql_lines.append(line)

        attributes = ["id", "name", "birthDate", "login", "password", "createdDate"]
        json_values = [owner_id, name, birth_date, login, password, created_date]
        json_line_maker = JsonLineMaker()
        self.json_lines.append(json_line_maker.set_attributes(attributes).set_values(json_values).build() + "\n")

    def generate_diaries(self, owner_id, entry_cnt):
        json_line_maker = JsonLineMaker()

        diary_id = 1
        title = "Дневник 1"
        description = "Мой личный дневник"
        cnt_entries = entry_cnt
        created_date = date.today() - timedelta(days=30)

        table = "diaries"
        columns = ["owner_fk", "title", "description", "cnt_entries", "created_date"]
        values = [owner_id, title, description, cnt_entries, created_date]
        line = self.create_insert_command_line(table, columns, values)
        self.sql_lines.append(line)

        attributes = ["id", "ownerId", "title", "description", "cntEntries", "createdDate"]
        json_values = [diary_id, owner_id, title, description, cnt_entries, created_date]

        self.json_lines.append(json_line_maker.set_attributes(attributes).set_values(json_values).build() + "\n")

    def generate_moods(self, owner_id, mood_cnt):
        json_line_maker = JsonLineMaker()
        json_lines = []
        for i in range(mood_cnt):
            score_mood = randint(1, 10)
            score_productivity = randint(1, 10)
            created_date = date.today() - timedelta(days=(mood_cnt - i))

            bedtime = self.get_random_night_datetime(created_date)
            wake_up_time = self.get_random_morning_datetime(created_date)

            table = "moods"
            columns = ["owner_fk", "score_mood", "score_productivity", "bedtime", "wake_up_time", "created_date"]
            values = [owner_id, score_mood, score_productivity, bedtime, wake_up_time, created_date]

            line = self.create_insert_command_line(table, columns, values)
            self.sql_lines.append(line)

            attributes = ["id", "ownerId", "scoreMood",
                          "scoreProductivity", "bedtime", "wakeUpTime", "createdDate"]
            json_values = [i, owner_id, score_mood, score_productivity, bedtime, wake_up_time, created_date]
            json_lines.append(json_line_maker.set_attributes(attributes).set_values(json_values).build() + "\n")

        self.json_lines.extend(json_lines)

    def generate_entries(self, diary_id, entries_cnt):
        json_line_maker = JsonLineMaker()
        json_lines = []
        for i in range(entries_cnt):
            title = self.faker.sentence(nb_words=2)[:-1]
            content = " ".join(self.faker.sentences(nb=10))
            created_date = date.today() - timedelta(days=(entries_cnt - i))

            table = "entries"
            columns = ["diary_fk", "title", "content", "created_date"]
            values = [diary_id, title, content, created_date]

            line = self.create_insert_command_line(table, columns, values)
            self.sql_lines.append(line)

            attributes = ["id", "diaryId", "title", "content", "createdDate"]
            json_values = [i, diary_id, title, content, created_date]

            json_lines.append(json_line_maker.set_attributes(attributes).set_values(json_values).build() + "\n")
        self.json_lines.extend(json_lines)

    @staticmethod
    def get_random_morning_datetime(input_date):
        start_time = datetime.combine(input_date, datetime.min.time()).replace(hour=7)
        end_time = datetime.combine(input_date, datetime.min.time()).replace(hour=10)

        delta = timedelta(minutes=15)
        slots = []
        current = start_time
        while current <= end_time:
            slots.append(current)
            current += delta

        return random.choice(slots)

    @staticmethod
    def get_random_night_datetime(input_date):
        if isinstance(input_date, str):
            input_date = datetime.strptime(input_date, "%Y-%m-%d").date()

        start_time = datetime.combine(input_date - timedelta(days=1), datetime.min.time()).replace(hour=23)
        end_time = datetime.combine(input_date, datetime.min.time()).replace(hour=2)

        delta = timedelta(minutes=30)

        slots = []
        current = start_time
        while current <= end_time:
            slots.append(current)
            current += delta

        return random.choice(slots)

    @staticmethod
    def bcrypt_password(plain_password):
        salt = bcrypt.gensalt()
        hashed_password = bcrypt.hashpw(plain_password.encode('utf-8'), salt)
        return hashed_password.decode('utf-8')

    @staticmethod
    def get_formatted_datetime(datetime_str):
        return datetime_str.strftime('%Y-%m-%dT%H:%M:%S')

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


def main():
    generator = IdealUserGenerator()
    generator.generate_all()


if __name__ == "__main__":
    main()
