package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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
        if (diaries == null) {
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
                }
                "2" -> {
                }
                "3" -> {
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
        return sendGetdiariesRequest(ownerId)
    }

    private suspend fun sendGetdiariesRequest(ownerId: Long?): List<DiaryInfoDTO>? {
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
}
