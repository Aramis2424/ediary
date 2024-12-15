package org.srd.ediary_cli.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MoodCreateDTO(
    val ownerID: Long,
    val scoreMood: Int,
    val scoreProductivity: Int,
    val bedtime: LocalDateTime,
    val wakeUpTime: LocalDateTime
)

@Serializable
data class MoodUpdateDTO(
    val scoreMood: Int,
    val scoreProductivity: Int,
    val bedtime: LocalDateTime,
    val wakeUpTime: LocalDateTime
)

@Serializable
data class MoodInfoDTO(
    val id: Long,
    val scoreMood: Int,
    val scoreProductivity: Int,
    val bedtime: LocalDateTime,
    val wakeUpTime: LocalDateTime,
    val createdAt: LocalDate
)
