package org.srd.ediary_cli.view

class MenuView {
    private val helloWorldCommand = 24
    private val exitCommand = 0
    fun getStartView() {
        println(
            """=== Меню ===
                |   $helloWorldCommand) Тестовый Hello, World
                |   $exitCommand) Завершение программы
                |
            """.trimMargin()
        )
    }
}