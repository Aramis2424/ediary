package org.srd.ediary_cli.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

import org.srd.ediary_cli.util.LocalDateSerializer

@Serializable
data class OwnerLoginDTO(val login: String,
                         val password: String)

@Serializable
data class OwnerInfoDTO(val id: Long,
                        val name: String,
                        @Serializable(with = LocalDateSerializer::class)
                        val birthDate: LocalDate,
                        val login: String,
                        @Serializable(with = LocalDateSerializer::class)
                        val createdDate: LocalDate)

@Serializable
data class OwnerCreateDTO(val name: String,
                          @Serializable(with = LocalDateSerializer::class)
                          val birthDate: LocalDate,
                          val login: String,
                          val password: String)
