package org.srd.ediary_cli.view

class UnauthMenuView {
    private val helloWorldCommand = 24
    private val exitCommand = 0
    private val ownerCommand = 1
    fun getStartView() {
        println(
            """=== Меню ===
                |   $ownerCommand) Вход на сервер
                |   $helloWorldCommand) Тестовый Hello, World
                |   $exitCommand) Завершение программы
                |
            """.trimMargin()
        )
    }
}