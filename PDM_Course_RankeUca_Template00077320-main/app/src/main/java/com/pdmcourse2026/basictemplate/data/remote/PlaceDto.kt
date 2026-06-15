package com.pdmcourse2026.basictemplate.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val votes: Int? = 0
)
