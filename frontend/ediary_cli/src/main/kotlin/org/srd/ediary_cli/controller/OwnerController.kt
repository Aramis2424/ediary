package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.model.OwnerCreateDTO
import org.srd.ediary_cli.model.OwnerInfoDTO
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
                    LocalStorage.token = token.token
                    println("Вход выполнен успешно")

                    executor.launchAuthMenuController()
                }
                "2" -> {
                    if (executor.registerRequest() == null) {
                        println("Пользователь с таким логином уже существует. Повторите попытку\n")
                    } else {
                        println("Вы успешно зарегистрированы. Теперь можете войти")
                    }
                }
                "0" -> return
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class OwnerExec {
    private val ioUtil = IOUtil()
    private val client = HttpService.client
    private val authMenuController = AuthMenuController()

    suspend fun loginRequest(): TokenResponse? {
        val login = ioUtil.inputString("Введите логин: ")
        val password = ioUtil.inputString("Введите пароль: ")
        val loginDto = TokenRequest(login, password)

        LocalStorage.currentOwnerName = login
        return sendLoginRequest(loginDto)
    }

    private suspend fun sendLoginRequest(loginDto: TokenRequest): TokenResponse? {
        val res = client.post("${HttpService.BASE_URL}/token/create") {
            contentType(ContentType.Application.Json)
            setBody(loginDto)
        }
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }
    }

    suspend fun registerRequest() : OwnerInfoDTO? {
        val name = ioUtil.inputString("Введите Ваше имя: ")
        val login = ioUtil.inputString("Придумайте логин: ")
        val password = ioUtil.inputString("Придумайте пароль: ")
        val birthDate = ioUtil.inputLocalDate("Введите дату рождения: ")

        val newOwner = OwnerCreateDTO(name, birthDate, login, password)
        return sendRegisterRequest(newOwner)
    }

    private suspend fun sendRegisterRequest(registerDTO: OwnerCreateDTO): OwnerInfoDTO? {
        val res = client.post("${HttpService.BASE_URL}/owner/register") {
            contentType(ContentType.Application.Json)
            setBody(registerDTO)
        }
        return try {
            res.body()
        } catch (ex : Exception) {
            null
        }

    }

    suspend fun launchAuthMenuController() {
        authMenuController.start()
    }
}