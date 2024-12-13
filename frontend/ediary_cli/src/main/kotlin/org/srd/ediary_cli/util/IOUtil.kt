package org.srd.ediary_cli.util

class IOUtil {
    fun input(): String? {
        print("Ввод: ")
        return readlnOrNull()?.trim()
    }

    fun outputInvalidChoice() {
        println("Неверный выбор, попробуйте снова")
    }
}