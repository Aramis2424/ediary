package org.srd.ediary_cli.view

class MoodView {
    private val backCommand = 0
    private val allMoodsCommand = 1
    private val createMoodCommand = 2

    fun getStartView() {
        println(
            """Управление настроениями
                |   $allMoodsCommand) Посмотреть все настроения
                |   $createMoodCommand) Отметить настроение 
                |   $backCommand) Назад
            """.trimMargin()
        )
    }
}