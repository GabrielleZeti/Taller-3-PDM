package com.pdmcourse2026.basictemplate.data.repository

import com.pdmcourse2026.basictemplate.BuildConfig
import com.pdmcourse2026.basictemplate.data.remote.PlaceDto
import com.pdmcourse2026.basictemplate.data.remote.VoteRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PlaceRepositoryImpl(private val client: HttpClient) : PlaceRepository {

    private val baseUrl = BuildConfig.BASE_URL
    private val apiKey = BuildConfig.API_KEY

    override suspend fun getPlaces(): Result<List<PlaceDto>> {
        return try {
            val response = client.get("${baseUrl}options") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                header("apikey", apiKey)
                header(HttpHeaders.CacheControl, "no-cache")
            }
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error al obtener lugares: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun voteForPlace(placeId: Int, carnet: String): Result<Unit> {
        return try {
            val response = client.post("${baseUrl}register") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                header("apikey", apiKey)
                contentType(ContentType.Application.Json)
                setBody(VoteRequest(placeId, carnet))
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al votar: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}