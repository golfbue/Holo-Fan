package com.example.myapplication

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Talent(
    val name: String,
    val generation: String,
    val status: String, // Active, Alum, Affiliate
    val debutDate: String,
    val oshiMark: String,
    val bio: String,
    val twitter: String,
    val youtubeChannelId: String
)

object TalentProvider {
    var talents: List<Talent> = emptyList()

    fun loadTalents(context: Context) {
        if (talents.isNotEmpty()) return
        
        try {
            val jsonString = context.assets.open("talents.json").bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Talent>>() {}.type
            talents = Gson().fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
