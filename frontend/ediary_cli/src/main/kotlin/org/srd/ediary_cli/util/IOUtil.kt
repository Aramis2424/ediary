package org.srd.ediary_cli.util

class IOUtil {
    fun input(): String? {
        print("Ввод: ")
        return readlnOrNull()?.trim()
    }

    fun outputInvalidChoice() {
        println("Неверный выбор, попробуйте снова")
    }

    fun inputString(msg: String?): String {
        print(msg ?: "")
        var res : String
        while(true) {
            res = readln().trim()
            if (res.isBlank()) {
                println("Строка не может быть пустой. Повторите ввод")
                continue
            }
            break
        }
        return res
    }

    fun inputLocalDate(msg: String?): String {
        print(msg ?: "")
        TODO()
    }

    fun inputLocalDateTime(msg: String?): String {
        print(msg ?: "")
        TODO()
    }

    fun inputNumber(msg: String?): String {
        print(msg ?: "")
        TODO()
    }
}