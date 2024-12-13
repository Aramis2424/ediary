package org.srd.ediary_cli.controller

import org.srd.ediary_cli.util.IOUtil
import org.srd.ediary_cli.view.OwnerView

class OwnerController {
    private val executor = OwnerExec()
    private val view = OwnerView()
    private val ioUtil = IOUtil()

    suspend fun entrance() {
        while (true) {
            view.getEntranceView()

            when (ioUtil.input()) {
                "1" -> executor.loginRequest()
                "2" -> executor.registerRequest()
                "0" -> return
                else -> ioUtil.outputInvalidChoice()
            }
        }
    }
}

class OwnerExec {
    fun loginRequest() {
        TODO("Not yet implemented")
    }

    fun registerRequest() {
        TODO("Not yet implemented")
    }

}