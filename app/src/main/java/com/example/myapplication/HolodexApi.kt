package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class HolodexVideo(
    val id: String,
    val title: String,
    val status: String,
    val channel: HolodexChannel,
    val start_scheduled: String? = null,
    val available_at: String? = null,
    val type: String? = null,
    val topic_id: String? = null
)

data class HolodexChannel(
    val id: String,
    val name: String,
    val photo: String? = null,
    val org: String? = null,
    val suborg: String? = null,
    val twitter: String? = null,
    val english_name: String? = null
)

interface HolodexApi {
    @GET("live")
    suspend fun getLiveStreams(
        @Query("org") org: String = "Hololive",
        @Query("status") status: String = "live"
    ): List<HolodexVideo>

    @GET("videos")
    suspend fun getVideos(
        @Query("org") org: String = "Hololive",
        @Query("status") status: String, // live, upcoming, past
        @Query("type") type: String? = null,
        @Query("limit") limit: Int = 50
    ): List<HolodexVideo>

    @GET("channels")
    suspend fun getChannels(
        @Query("org") org: String = "Hololive",
        @Query("type") type: String = "vtuber",
        @Query("limit") limit: Int = 100
    ): List<HolodexChannel>

    companion object {
        private const val BASE_URL = "https://holodex.net/api/v2/"

        fun create(apiKey: String? = null): HolodexApi {
            val finalApiKey = if (apiKey.isNullOrBlank()) "78913e84-33bd-45d0-bf43-d872a6543ae8" else apiKey
            val client = okhttp3.OkHttpClient.Builder().apply {
                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("Accept-Language", "en-US,en;q=0.9")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .apply {
                            if (!finalApiKey.isNullOrBlank()) {
                                // Try different header name casing just in case
                                addHeader("X-APIKEY", finalApiKey)
                            }
                        }
                        .build()
                    chain.proceed(request)
                }
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HolodexApi::class.java)
        }
    }
}
