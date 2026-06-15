package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.data.remote.PlaceDto

interface PlaceRepository {
    suspend fun getPlaces(): Result<List<PlaceDto>>
    suspend fun voteForPlace(placeId: Int, carnet: String): Result<Unit>
}
