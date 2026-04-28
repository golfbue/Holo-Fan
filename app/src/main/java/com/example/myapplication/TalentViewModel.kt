package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class TalentUiState {
    object Loading : TalentUiState()
    data class Success(val talents: List<HolodexChannel>) : TalentUiState()
    data class Error(val message: String) : TalentUiState()
}

class TalentViewModel : ViewModel() {
    private val api = HolodexApi.create(BuildConfig.HOLODEX_API_KEY)
    
    private val _uiState = MutableStateFlow<TalentUiState>(TalentUiState.Loading)
    val uiState: StateFlow<TalentUiState> = _uiState

    init {
        fetchTalents()
    }

    fun fetchTalents() {
        viewModelScope.launch {
            _uiState.value = TalentUiState.Loading
            try {
                val channels = api.getChannels(org = "Hololive", limit = 100)
                _uiState.value = TalentUiState.Success(channels)
            } catch (e: Exception) {
                _uiState.value = TalentUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
