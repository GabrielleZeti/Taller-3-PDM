package com.pdmcourse2026.basictemplate.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VoteRequest(
    @SerialName("option_id")
    val placeId: Int,
    @SerialName("carnet")
    val carnet: String
)
