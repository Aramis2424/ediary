from datetime import date
from faker import Faker
from random import randint


class DataGenerator:
    OWNERS_FILE = "./owners.sql"

    gender = ["male", "female"]
    faker = Faker("en_US")

    def __init__(self, max_count=10):
        self.MAX_COUNT = max_count

    def generate_all(self):
        print("=== Start generation ===")
        self.generate_owners()
        print("=== Generations successfully ===")

    def generate_owners(self):
        with open(self.OWNERS_FILE, "w") as file:
            for i in range(self.MAX_COUNT):
                name = self.faker.first_name()
                birth_date = self.faker.date_between_dates(date(1970, 1, 1), date(2010, 1, 1))
                login = DataGenerator.generate_login_from_name(name)
                password = self.faker.word() + str(randint(1, 199))
                created_date = self.faker.date_between_dates(date(2020, 1, 1), date(2024, 1, 1))

                table = "owners"
                columns = ["name", "birth_date", "login", "password", "created_date"]
                values = [name, birth_date, login, password, created_date]

                line_builder = SQLInsertCommandBuilder()
                line = (
                    line_builder
                    .set_table(table)
                    .set_columns(columns)
                    .set_values(values)
                    .build()
                )
                line = line.get() if line is not None else " "
                line += "\n"
                file.write(line)

    @staticmethod
    def generate_login_from_name(name):
        return name.lower() + str(randint(100, 999))


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
        values = [str(val) for val in values]
        str_values = "(" + ", ".join(values) + ")"
        self.sql_command.values = str_values
        return self

    def build(self):
        if not self.sql_command.table or not self.sql_command.columns or not self.sql_command.values:
            return None
        if len(self.sql_command.columns.split()) != len(self.sql_command.values.split()):
            return None
        return self.sql_command


def main():
    generator = DataGenerator()
    generator.generate_all()


if __name__ == "__main__":
    main()
