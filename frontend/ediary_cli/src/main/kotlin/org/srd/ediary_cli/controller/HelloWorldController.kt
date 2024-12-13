package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.service.HttpService
import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.view.HelloWorldView

class HelloWorldController {
    private val executor = HelloWorldExec()
    private val view = HelloWorldView()
    private val ioUtil = IOUtil()

    suspend fun start() {
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "1" -> executor.helloWorldRequest()
                "0" -> return
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class HelloWorldExec {
    private val client = HttpService.client

    suspend fun helloWorldRequest() {
        println(sendRequest())
        println()
    }

    private suspend fun sendRequest(): String {
        return client.get("${HttpService.BASE_URL}/hello") {
            headers {
                append(HttpHeaders.Accept, ContentType.Text.Plain.toString())
            }
        }.body()
    }
}