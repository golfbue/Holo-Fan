package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ScheduleUiState {
    object Loading : ScheduleUiState()
    data class Success(
        val liveStreams: List<HolodexVideo>,
        val upcomingStreams: List<HolodexVideo>,
        val pastStreams: List<HolodexVideo>
    ) : ScheduleUiState()
    data class Error(val message: String) : ScheduleUiState()
}

class ScheduleViewModel : ViewModel() {
    private val api = HolodexApi.create(BuildConfig.HOLODEX_API_KEY)
    
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState

    init {
        refreshSchedule()
        // Auto-refresh every 5 minutes
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(5 * 60 * 1000)
                refreshSchedule()
            }
        }
    }

    fun refreshSchedule() {
        viewModelScope.launch {
            if (_uiState.value !is ScheduleUiState.Success) {
                _uiState.value = ScheduleUiState.Loading
            }
            try {
                // Fetch from Hololive
                val liveHolo = api.getLiveStreams(org = "Hololive")
                val upcomingHolo = api.getVideos(org = "Hololive", status = "upcoming", limit = 50)
                val pastHolo = api.getVideos(org = "Hololive", status = "past", limit = 30)
                
                // Fetch from DEV_IS
                val liveDevIs = api.getLiveStreams(org = "DEV_IS")
                val upcomingDevIs = api.getVideos(org = "DEV_IS", status = "upcoming", limit = 20)
                val pastDevIs = api.getVideos(org = "DEV_IS", status = "past", limit = 10)
                
                // Combine and apply strict filter
                val allLive = (liveHolo + liveDevIs).filter { isOfficialHololive(it) }
                val allUpcoming = (upcomingHolo + upcomingDevIs).filter { isOfficialHololive(it) }
                val allPast = (pastHolo + pastDevIs).filter { isOfficialHololive(it) }
                
                _uiState.value = ScheduleUiState.Success(allLive, allUpcoming, allPast)
            } catch (e: Exception) {
                if (_uiState.value !is ScheduleUiState.Success) {
                    _uiState.value = ScheduleUiState.Error(e.message ?: "Unknown error")
                }
            }
        }
    }

    private fun isOfficialHololive(video: HolodexVideo): Boolean {
        val org = video.channel.org?.lowercase()
        val name = video.channel.name.lowercase()
        val isOfficialOrg = org == "hololive" || org == "dev_is"
        
        return isOfficialOrg && 
               !name.contains("clipper") && 
               !name.contains("切り抜き") &&
               !name.contains("fan") &&
               !name.contains("translation")
    }
}
