package org.srd.ediary_cli.view

class HelloWorldView {
    private val helloWorldCommand = 1
    private val backCommand = 0
    fun getStartView() {
        println(
            """Тестовый Hello, World!
                |   Если хочешь получить "Привет, Мир!" нажми $helloWorldCommand
                |   Чтобы выйти нажми $backCommand
        """.trimMargin()
        )
    }
}