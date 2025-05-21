package org.srd.ediary_cli.view

class OwnerView {
    private val loginCommand = 1
    private val registerCommand = 2
    private val backCommand = 0

    fun getEntranceView() {
        println(
            """Вход на сервер
                |   $loginCommand) Войти
                |   $registerCommand) Зарегистрироваться
                |   $backCommand) Назад
        """.trimMargin()
        )
    }
}