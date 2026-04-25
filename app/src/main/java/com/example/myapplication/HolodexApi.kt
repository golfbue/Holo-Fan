package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class HolodexVideo(
    val id: String,
    val title: String,
    val status: String,
    val channel: HolodexChannel
)

data class HolodexChannel(
    val id: String,
    val name: String,
    val photo: String? = null
)

interface HolodexApi {
    @GET("live")
    suspend fun getLiveStreams(
        @Query("org") org: String = "Hololive",
        @Query("status") status: String = "live"
    ): List<HolodexVideo>

    companion object {
        private const val BASE_URL = "https://holodex.net/api/v2/"

        fun create(): HolodexApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HolodexApi::class.java)
        }
    }
}
