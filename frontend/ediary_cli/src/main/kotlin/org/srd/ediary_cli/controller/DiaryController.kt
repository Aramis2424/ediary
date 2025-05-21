package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.model.DiaryCreateDTO
import org.srd.ediary_cli.model.DiaryInfoDTO
import org.srd.ediary_cli.service.HttpService
import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.util.LocalStorage
import org.srd.ediary_cli.view.DiaryView

class DiaryController {
    private val view = DiaryView()
    private val ioUtil = IOUtil()
    private val executor = DiaryExec()

    suspend fun start() {
        val diaries = executor.getDiariesRequest()
        if (diaries.isNullOrEmpty()) {
            println("У Вас пока нет дневников ...")
        } else {
            println("Ваши дневники:")
            diaries.forEach {
                println(it)
            }
        }
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "0" -> return
                "1" -> {
                    val diary = executor.getDiaryRequest()
                    if (diary == null) {
                        println("Что-то пошло не так")
                    } else {
                        executor.launchEntryController(diary.id)
                    }
                }
                "2" -> {
                    val diary = executor.createDiaryRequest()
                    if (diary == null) {
                        println("Что-то пошло не так...")
                    } else {
                        println("Дневник успешно создан")
                    }
                }
                "3" -> {
                    if (executor.deleteDiaryRequest()) {
                        println("Дневник успешно удален")
                    } else {
                        println("Что-то пошло не так")
                    }
                }
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

private class DiaryExec {
    private val ioUtil = IOUtil()
    private val client = HttpService.client

    suspend fun getDiariesRequest(): List<DiaryInfoDTO>? {
        val ownerId = LocalStorage.currentOwner?.id
        return sendGetDiariesRequest(ownerId)
    }

    private suspend fun sendGetDiariesRequest(ownerId: Long?): List<DiaryInfoDTO>? {
        val res = client.get("${HttpService.BASE_URL}/diaries/owner/$ownerId") {
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

    suspend fun createDiaryRequest(): DiaryInfoDTO? {
        val ownerId = LocalStorage.currentOwner?.id
        val title = ioUtil.inputString("Введите название дневника: ")
        val description = ioUtil.inputString("Введите краткое описание дневника: ")

        val diaryCreateDto = DiaryCreateDTO(ownerId!!, title, description)

        return sendCreateDiaryRequest(diaryCreateDto)
    }

    private suspend fun sendCreateDiaryRequest(diaryCreateDTO: DiaryCreateDTO): DiaryInfoDTO? {
        val res = client.post("${HttpService.BASE_URL}/diaries") {
            contentType(ContentType.Application.Json)
            setBody(diaryCreateDTO)
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

    suspend fun deleteDiaryRequest(): Boolean {
        val diaryId = ioUtil.inputNumber("Укажите id дневника, который хотите удалить: ")
        return sendDeleteDiaryRequest(diaryId)
    }

    private suspend fun sendDeleteDiaryRequest(diaryId: Int): Boolean {
        val res = client.delete("${HttpService.BASE_URL}/diaries/$diaryId") {
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

    suspend fun launchEntryController(diaryId: Long) {
        val entryController = EntryController(diaryId)
        entryController.start()
    }

    suspend fun getDiaryRequest(): DiaryInfoDTO? {
        val diaryId = ioUtil.inputNumber("Укажите id дневника, который хотите открыть: ")
        return sendGetDiaryRequest(diaryId)
    }

    private suspend fun sendGetDiaryRequest(diaryId: Int): DiaryInfoDTO? {
        val res = client.get("${HttpService.BASE_URL}/diaries/$diaryId") {
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
}
