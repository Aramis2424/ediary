package org.srd.ediary_cli


import com.github.ajalt.clikt.core.CliktCommand

class EdiaryCLI : CliktCommand() {
    override fun run() {
        println("Добро пожаловать! Это CLI frontend проекта ediary")
    }
}

fun main(args: Array<String>) = EdiaryCLI().main(args)