package org.srd.ediary_cli.controller

import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.view.MoodView

class MoodController {
    private val view = MoodView()
    private val ioUtil = IOUtil()

    suspend fun start() {
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "0" -> return
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}