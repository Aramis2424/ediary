package org.srd.ediary_cli.view

class DiaryView {
    private val backCommand = 0
    private val getDiaryCommand = 1
    private val createDiaryCommand = 2
    private val deleteDiaryCommand = 3

    fun getStartView() {
        println(
            """Управление дневниками
                |   $getDiaryCommand) Перейти к дневнику
                |   $createDiaryCommand) Создать дневник
                |   $deleteDiaryCommand) Удалить дневник
                |   $backCommand) Назад
            """.trimMargin()
        )
    }
}