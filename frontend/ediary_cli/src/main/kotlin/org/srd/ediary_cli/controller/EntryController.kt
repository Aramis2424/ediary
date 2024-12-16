package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.model.EntryCreateDTO
import org.srd.ediary_cli.model.EntryInfoDTO
import org.srd.ediary_cli.model.EntryUpdateDTO
import org.srd.ediary_cli.service.HttpService
import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.util.LocalStorage
import org.srd.ediary_cli.view.EntryView

class EntryController(diaryId: Long) {
    private val view = EntryView()
    private val ioUtil = IOUtil()
    private val executor = EntryExec(diaryId)

    suspend fun start() {
        val entries = executor.getEntriesRequest()
        if (entries.isNullOrEmpty()) {
            println("В этом дневнике пока нет записей")
        } else {
            println("Записи в дневнике")
            entries.forEach{
                entry -> println("id = ${entry.id}; заглавие = ${entry.title}; создана ${entry.createdDate}")
            }
        }

        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "0" -> return
                "1" -> {
                    val entry = executor.getEntryRequest()
                    if (entry == null) {
                        println("Что-то пошло не так")
                    } else {
                        println("""id = ${entry.id}
                            |заглавие = ${entry.title}
                            |содержание = ${entry.content}
                        """.trimMargin())
                        println()
                    }
                }
                "2" -> {
                    val entry = executor.createEntryRequest()
                    if (entry == null) {
                        println("Что-то пошло не так...")
                    } else {
                        println("Запись успешно создана")
                    }
                }
                "3" -> {
                    val entry = executor.updateEntryRequest()
                    if (entry == null) {
                        println("Что-то пошло не так...")
                    } else {
                        println("Запись успешно изменена")
                    }
                }
                "4" -> {
                    if (executor.deleteEntryRequest()) {
                        println("Запись успешно удалена")
                    } else {
                        println("Что-то пошло не так")
                    }
                }
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

private class EntryExec(val diaryId: Long) {
    private val ioUtil = IOUtil()
    private val client = HttpService.client

    suspend fun getEntriesRequest(): List<EntryInfoDTO>? {
        return sendGetEntriesRequest()
    }

    private suspend fun sendGetEntriesRequest(): List<EntryInfoDTO>? {
        val res = client.get("${HttpService.BASE_URL}/entries/diary/$diaryId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${LocalStorage.token}")
            }
        }
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }
    }

    suspend fun createEntryRequest(): EntryInfoDTO? {
        val title = ioUtil.inputString("Введите заглавие записи: ")
        val content = ioUtil.inputString("Введите содержание записи: ")

        val entryCreateDTO = EntryCreateDTO(diaryId, title, content)

        return sendEntryRequest(entryCreateDTO)
    }

    private suspend fun sendEntryRequest(entryCreateDTO: EntryCreateDTO): EntryInfoDTO? {
        val res = client.post("${HttpService.BASE_URL}/entries") {
            contentType(ContentType.Application.Json)
            setBody(entryCreateDTO)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${LocalStorage.token}")
            }
        }
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }
    }

    suspend fun getEntryRequest(): EntryInfoDTO? {
        val entryId = ioUtil.inputNumber("Укажите id записи, которую хотите открыть: ")
        return sendGetEntryRequest(entryId)
    }

    private suspend fun sendGetEntryRequest(entryId: Int): EntryInfoDTO? {
        val res = client.get("${HttpService.BASE_URL}/entries/$entryId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${LocalStorage.token}")
            }
        }
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }
    }

    suspend fun deleteEntryRequest(): Boolean {
        val entryId = ioUtil.inputNumber("Укажите id записи, которую хотите удалить: ")
        return sendDeleteDiaryRequest(entryId)
    }

    private suspend fun sendDeleteDiaryRequest(entryId: Int): Boolean {
        val res = client.delete("${HttpService.BASE_URL}/entries/$entryId") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${LocalStorage.token}")
            }
        }
        if (res.status.value != 200) {
            return false
        }
        return true
    }

    suspend fun updateEntryRequest(): EntryInfoDTO? {
        val entryId = ioUtil.inputNumber("Укажите id записи, которую хотите изменить: ")
        val title = ioUtil.inputString("Введите заглавие записи: ")
        val content = ioUtil.inputString("Введите содержание записи: ")

        val entryUpdateDto = EntryUpdateDTO(title, content)
        return sendUpdateDiaryRequest(entryId, entryUpdateDto)
    }

    private suspend fun sendUpdateDiaryRequest(entryId: Int, entryUpdateDto: EntryUpdateDTO): EntryInfoDTO? {
        val res = client.put("${HttpService.BASE_URL}/entries/$entryId") {
            contentType(ContentType.Application.Json)
            setBody(entryUpdateDto)
            headers {
                append(HttpHeaders.Authorization, "Bearer ${LocalStorage.token}")
            }
        }
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }
    }
}
