package org.srd.ediary_cli.controller

import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.util.LocalStorage
import org.srd.ediary_cli.view.AuthMenuView

class AuthMenuController {
    private val executor = AuthMenuExec()
    private val view = AuthMenuView()
    private val ioUtil = IOUtil()

    suspend fun start() {
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "0" -> executor.exitApp()
                "2" -> executor.launchMoodController()
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class AuthMenuExec {
    private val moodController = MoodController()

    fun exitApp() {
        println("Пока-пока, ${LocalStorage.currentOwner?.name}!")
        kotlin.system.exitProcess(0)
    }

    suspend fun launchMoodController() {
        moodController.start()
    }
}