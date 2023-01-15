package com.example.models

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class Response(val statusCode: Int, val data: ResponseData)
@Serializable
data class ResponseData(val status: String = "success", val message: String = "operation completed successFully", val accessToken: String, val refreshToken: String)
