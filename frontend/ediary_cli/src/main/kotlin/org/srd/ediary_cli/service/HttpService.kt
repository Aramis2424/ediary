package org.srd.ediary_cli.service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.datetime.LocalDate

import org.srd.ediary_cli.config.ConfigApp
import org.srd.ediary_cli.util.LocalDateSerializer

object HttpService {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    serializersModule = SerializersModule {
                        contextual(LocalDate::class, LocalDateSerializer)
                    }
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    val BASE_URL: String = ConfigApp.get("server.url")
}