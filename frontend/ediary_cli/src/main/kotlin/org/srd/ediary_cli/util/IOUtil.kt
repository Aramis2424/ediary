package org.srd.ediary_cli.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.lang.Exception
import java.time.LocalDateTime as JavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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

    fun inputLocalDate(msg: String?): LocalDate {
        println("Формат даты: гггг-мм-дд")
        print(msg ?: "")

        val res : LocalDate
        while(true) {
            val input = readlnOrNull()
            val date = parseDate(input)
            if (date == null) {
                println("Неверный формат даты. Повторите ввод")
                continue
            }
            res = date
            break
        }
        return res
    }

    fun inputLocalDateTime(msg: String?): LocalDateTime {
        println("Формат даты и времени: гггг-мм-ддTчч:мм")
        println("Пример: 2020-12-30T22:00")
        print(msg ?: "")

        val res : LocalDateTime
        while(true) {
            val input = readlnOrNull()
            val date = parseDateTime(input)
            if (date == null) {
                println("Неверный формат даты. Повторите ввод")
                continue
            }
            res = date
            break
        }
        return res
    }

    fun inputNumber(msg: String?): Int {
        print(msg ?: "")

        val res : Int
        while(true) {
            val input = readlnOrNull()
            val number = parseNumber(input)
            if (number == null) {
                println("Вы ввели не число. Повторите ввод")
                continue
            }
            res = number
            break
        }
        return res
    }

    private fun parseNumber(input: String?): Int? {
        return try {
            input?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun parseDate(input: String?): LocalDate? {
        return try {
            input?.let {
                LocalDate.parse(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun parseDateTime(input: String?): LocalDateTime? {
        return try {
            input?.let {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                val javaDateTime = JavaLocalDateTime.parse(it, formatter)
                javaDateTime.toKotlinLocalDateTime()
            }
        } catch (e: Exception) {
            null
        }
    }
}