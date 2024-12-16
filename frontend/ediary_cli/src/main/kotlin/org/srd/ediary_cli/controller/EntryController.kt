package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.model.EntryInfoDTO
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
        println(diaryId)
        println(res)
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }
    }
}
