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
                val live = api.getLiveStreams()
                val upcoming = api.getVideos(status = "upcoming", limit = 10)
                val allLatest = api.getVideos(status = "past", limit = 50) 
                
                // Strict filter for "HoloAn" related channels only
                val filteredLatest = allLatest.filter { 
                    it.channel.name.contains("HoloAn", ignoreCase = true)
                }.take(5)
                
                _uiState.value = HomeUiState.Success(live, upcoming, filteredLatest)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
