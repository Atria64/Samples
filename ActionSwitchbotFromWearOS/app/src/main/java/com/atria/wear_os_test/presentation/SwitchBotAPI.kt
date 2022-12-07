package com.atria.wear_os_test.presentation

import com.squareup.moshi.Json
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface SwitchBotAPI {
    @POST("commands")
    suspend fun turn(@Header("Authorization") token: String, @Body params:Params)

    data class Params(
        @Json(name = "command")
        val command : String,
        @Json(name = "parameter")
        val parameter : String,
        @Json(name = "commandType")
        val commandType : String,
    )
}
