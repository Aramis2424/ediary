package org.srd.ediary_cli.config

import java.util.*

object ConfigApp {
    private val properties: Properties = Properties()

    init {
        val inputStream = this::class.java.classLoader.getResourceAsStream("application.properties")
            ?: throw IllegalStateException("Did not found application.properties file")
        properties.load(inputStream)
    }

    fun get(key: String): String = properties.getProperty(key)
}
