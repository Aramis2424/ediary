package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.model.TokenRequest
import org.srd.ediary_cli.model.TokenResponse
import org.srd.ediary_cli.service.HttpService
import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.util.LocalStorage
import org.srd.ediary_cli.view.OwnerView

class OwnerController {
    private val executor = OwnerExec()
    private val view = OwnerView()
    private val ioUtil = IOUtil()
    private val localStorage = LocalStorage

    suspend fun entrance() {
        while (true) {
            view.getEntranceView()

            when (ioUtil.input()) {
                "1" -> {
                    val token = executor.loginRequest()
                    if (token == null) {
                        println("Неверные данные. Пожалуйста повторите попытку\n")
                        continue
                    }
                    localStorage.token = token.token
                    println("Вход выполнен успешно") // TODO в случае неверных данных возвращается bat cre...
                    // TODO передать управление контроллеру diary
                }
                "2" -> executor.registerRequest()
                "0" -> return
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class OwnerExec {
    private val ioUtil = IOUtil()
    private val client = HttpService.client

    suspend fun loginRequest(): TokenResponse? {
        val login = ioUtil.inputString("Введите логин: ")
        val password = ioUtil.inputString("Введите пароль: ")
        val loginDto = TokenRequest(login, password)
        return sendLoginRequest(loginDto)
    }

    private suspend fun sendLoginRequest(loginDto: TokenRequest): TokenResponse? {
        return client.post("${HttpService.BASE_URL}/token/create") {
            contentType(ContentType.Application.Json)
            setBody(loginDto)
        }.body()
    }

    fun registerRequest() {
        val name = ioUtil.inputString("Введите Ваше имя: ")
        val login = ioUtil.inputString("Придумайте логин: ")
        val password = ioUtil.inputString("Придумайте пароль пароль: ")
    }

}