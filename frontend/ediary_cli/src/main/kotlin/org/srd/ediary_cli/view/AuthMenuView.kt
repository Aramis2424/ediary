package org.srd.ediary_cli.view

class AuthMenuView {
    private val exitCommand = 0
    private val diaryCommand = 1
    private val moodCommand = 2

    fun getStartView() {
        println(
            """Личный кабинет
                |   $diaryCommand) Перейти к дневникам
                |   $moodCommand) Перейти к настроению
                |   $exitCommand) Выйти из личного кабинета
            """.trimMargin()
        )
    }
}