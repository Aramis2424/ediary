package org.srd.ediary_cli.view

class EntryView {
    private val backCommand = 0
    private val getEntryCommand = 1
    private val createEntryCommand = 2
    private val updateEntryCommand = 3
    private val deleteEntryCommand = 4

    fun getStartView() {
        println(
            """Управление записями
                |   $getEntryCommand) Перейти к записи
                |   $createEntryCommand) Создать запись
                |   $updateEntryCommand) Изменить запись
                |   $deleteEntryCommand) Удалить запись
                |   $backCommand) Назад
            """.trimMargin()
        )
    }
}