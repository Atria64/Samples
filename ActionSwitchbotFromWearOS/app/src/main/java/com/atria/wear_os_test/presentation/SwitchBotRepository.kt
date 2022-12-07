package com.atria.wear_os_test.presentation

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SwitchBotRepository {
    suspend fun turnOff(){
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.switch-bot.com/v1.0/devices/*ここにデバイスID*/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val service = retrofit.create(SwitchBotAPI::class.java)
        service.turn(
            token = "",
            params = SwitchBotAPI.Params(
                command ="turnOff",
                parameter = "default",
                commandType = "command",
            )
        )
    }
    suspend fun turnOn(){
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.switch-bot.com/v1.0/devices/*ここにデバイスID*/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val service = retrofit.create(SwitchBotAPI::class.java)
        service.turn(
            token = "",
            params = SwitchBotAPI.Params(
                command ="turnOn",
                parameter = "default",
                commandType = "command",
            )
        )
    }
}
