package org.srd.ediary_cli.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class DiaryCreateDTO(val ownerID: Long, val title: String, val description: String)

@Serializable
data class DiaryInfoDTO(
    val id: Long,
    val title: String,
    val description: String,
    val cntEntry: Int,
    val createdDate: LocalDate
)
