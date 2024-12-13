package org.srd.ediary_cli.controller

import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.view.MenuView

class MenuController {
    private val executor = MenuExec()
    private val view = MenuView()
    private val ioUtil = IOUtil()

    suspend fun start() {
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "24" -> executor.launchHelloWorldController()
                "0" -> executor.exitApp()
                "1" -> executor.launchOwnerController()
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class MenuExec {
    private val helloWorldController = HelloWorldController()
    private val ownerController = OwnerController()

    fun exitApp() {
        println("Пока-пока!")
        kotlin.system.exitProcess(0)
    }

    suspend fun launchHelloWorldController() {
        helloWorldController.start()
    }

    suspend fun launchOwnerController() {
        ownerController.entrance()
    }
}
