package org.srd.ediary_cli.controller

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.srd.ediary_cli.model.MoodCreateDTO
import org.srd.ediary_cli.model.MoodInfoDTO
import org.srd.ediary_cli.service.HttpService
import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.util.LocalStorage
import org.srd.ediary_cli.view.MoodView

class MoodController {
    private val view = MoodView()
    private val ioUtil = IOUtil()
    private val executor = MoodExec()

    suspend fun start() {
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "0" -> return
                "1" -> {
                    val moods = executor.getMoodsRequest()
                    if (moods == null) {
                        println("У Вас пока нет ни одного отмеченного настроения")
                    } else {
                        println("Ваши настроения")
                        moods.forEach {
                            println(it)
                        }
                    }
                }
                "2" -> {
                    val mood = executor.createMoodRequest()
                    if (mood == null) {
                        println("Что-то пошло не так...")
                    } else {
                        println("Настроение успешно отмечено")
                    }
                }
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class MoodExec() {
    private val ioUtil = IOUtil()
    private val client = HttpService.client

    suspend fun createMoodRequest(): MoodInfoDTO? {
        val scoreMood = ioUtil.inputNumber("Введи оценку Вашего настроения: ")
        val scoreProductivity = ioUtil.inputNumber("Введите оценку Вашей работоспособности: ")
        val bedtime = ioUtil.inputLocalDateTime("Введи время, когда Вы уснули: ")
        val wakeUpTime = ioUtil.inputLocalDateTime("Введите время, когда вы проснулись: ")

        val moodCreateDTO = MoodCreateDTO(LocalStorage.currentOwner!!.id,
            scoreMood, scoreProductivity, bedtime, wakeUpTime)

        return sendCreateMoodRequest(moodCreateDTO)
    }

    private suspend fun sendCreateMoodRequest(moodCreateDTO: MoodCreateDTO): MoodInfoDTO? {
        val res = client.post("${HttpService.BASE_URL}/moods") {
            contentType(ContentType.Application.Json)
            setBody(moodCreateDTO)
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

    suspend fun getMoodsRequest(): List<MoodInfoDTO>? {
        val ownerId = LocalStorage.currentOwner?.id
        return sendGetMoodsRequest(ownerId)
    }

    private suspend fun sendGetMoodsRequest(ownerId: Long?): List<MoodInfoDTO>? {
        val res = client.get("${HttpService.BASE_URL}/moods/owner/$ownerId") {
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