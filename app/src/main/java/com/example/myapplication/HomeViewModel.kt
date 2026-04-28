package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val liveStreams: List<HolodexVideo>,
        val upcomingStreams: List<HolodexVideo>,
        val latestVideos: List<HolodexVideo> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {
    private val api = HolodexApi.create(BuildConfig.HOLODEX_API_KEY)
    
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                // Fetch from Hololive
                val liveHolo = api.getLiveStreams(org = "Hololive")
                val upcomingHolo = api.getVideos(status = "upcoming", org = "Hololive", limit = 20)
                
                // Fetch from DEV_IS (ReGLOSS & FLOW GLOW)
                val liveDevIs = api.getLiveStreams(org = "DEV_IS")
                val upcomingDevIs = api.getVideos(status = "upcoming", org = "DEV_IS", limit = 10)
                
                // Combine and filter
                val allLive = (liveHolo + liveDevIs).filter { isOfficialHololive(it) }
                val allUpcoming = (upcomingHolo + upcomingDevIs).filter { isOfficialHololive(it) }
                
                val allLatest = api.getVideos(status = "past", org = "Hololive", limit = 50)
                
                // Filter specifically for "HoloAn" and official news channels
                val filteredLatest = allLatest.filter { 
                    (it.channel.name.contains("HoloAn", ignoreCase = true) || 
                     it.channel.name.contains("hololive", ignoreCase = true)) &&
                    isOfficialHololive(it)
                }.take(5)
                
                _uiState.value = HomeUiState.Success(allLive, allUpcoming.take(20), filteredLatest)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun isOfficialHololive(video: HolodexVideo): Boolean {
        // Double check: Must be Hololive or DEV_IS organization
        val org = video.channel.org?.lowercase()
        val name = video.channel.name.lowercase()
        
        // Ensure it's part of official Hololive groups and NOT a clipper
        // Include 'dev_is' for ReGLOSS and FLOW GLOW members
        val isOfficialOrg = org == "hololive" || org == "dev_is"
        
        return isOfficialOrg && 
               !name.contains("clipper") && 
               !name.contains("切り抜き") &&
               !name.contains("fan") &&
               !name.contains("translation")
    }
}
