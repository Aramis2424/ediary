package org.srd.ediary_cli.controller

import org.srd.ediary_cli.service.HttpService
import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.view.DiaryView

class DiaryController {
    private val view = DiaryView()
    private val ioUtil = IOUtil()

    suspend fun start() {
        while (true) {
            view.getStartView()

            when (ioUtil.input()) {
                "0" -> return
                "1" -> {
                }
                "2" -> {
                }
                "3" -> {
                }
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

private class DiaryExec {
    private val ioUtil = IOUtil()
    private val client = HttpService.client
}
