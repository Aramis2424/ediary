package org.srd.ediary_cli


import com.github.ajalt.clikt.core.CliktCommand
import kotlinx.coroutines.runBlocking
import org.srd.ediary_cli.controller.MenuController

class EdiaryCLI : CliktCommand() {
    private val menu = MenuController()
    override fun run() = runBlocking {
        println("Добро пожаловать! Это CLI frontend проекта ediary")

        menu.start()
    }
}

fun main(args: Array<String>) = EdiaryCLI().main(args)
