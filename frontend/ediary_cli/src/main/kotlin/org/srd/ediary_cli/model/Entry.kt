package org.srd.ediary_cli.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class EntryInfoDTO(val id: Long, val title: String, val content: String, val createdDate: LocalDate)

@Serializable
data class EntryCreateDTO(val diaryID: Long, val title: String, val content: String)

@Serializable
data class EntryUpdateDTO(val title: String, val content: String)
