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
                // Fetch from both organizations
                // Set limit back to 100 to avoid HTTP 400, but keep DEV_IS separate
                val holoChannels = api.getChannels(org = "Hololive", limit = 100)
                val devIsChannels = api.getChannels(org = "DEV_IS", limit = 50)
                
                val allChannels = (holoChannels + devIsChannels).filter { channel ->
                    val name = channel.name.lowercase()
                    val englishName = channel.english_name?.lowercase() ?: ""
                    val fullName = "$name $englishName"
                    
                    // 1. Whitelist: Must match one of our official talents
                    val isOfficialTalent = 
                        // Gen 0 & 1
                        fullName.contains("sora") || fullName.contains("roboco") || fullName.contains("miko") || 
                        fullName.contains("suisei") || fullName.contains("azki") || fullName.contains("yozora") || 
                        fullName.contains("fubuki") || fullName.contains("matsuri") || fullName.contains("aki") || 
                        fullName.contains("rosenthal") || fullName.contains("haato") || 
                        // Gen 2 & Gamers
                        fullName.contains("aqua") || fullName.contains("shion") || fullName.contains("ayame") || 
                        fullName.contains("choco") || fullName.contains("subaru") || fullName.contains("mio") || 
                        fullName.contains("okayu") || fullName.contains("korone") || 
                        // Gen 3, 4, 5, 6
                        fullName.contains("pekora") || fullName.contains("flare") || fullName.contains("noel") || 
                        fullName.contains("marine") || fullName.contains("kanata") || fullName.contains("watame") || 
                        fullName.contains("towa") || fullName.contains("luna") || fullName.contains("lamy") || 
                        fullName.contains("nene") || fullName.contains("botan") || fullName.contains("polka") || 
                        fullName.contains("laplus") || fullName.contains(" lui ") || fullName.contains("koyori") || 
                        fullName.contains("chloe") || fullName.contains("iroha") ||
                        // EN
                        fullName.contains("calliope") || fullName.contains("kiara") || fullName.contains("ina'nis") || 
                        fullName.contains("gura") || fullName.contains("amelia") || fullName.contains("irys") || 
                        fullName.contains("fauna") || fullName.contains("kronii") || fullName.contains("mumei") || 
                        fullName.contains("baels") || fullName.contains("baelz") || fullName.contains("shiori") || 
                        fullName.contains("bijou") || fullName.contains("nerissa") || fullName.contains("fuwawa") || 
                        fullName.contains("mococo") || fullName.contains("elizabeth") || fullName.contains("rose") || 
                        fullName.contains("gigi") || fullName.contains("murin") || fullName.contains("cecilia") || 
                        fullName.contains("raora") ||
                        // ID
                        fullName.contains("risu") || fullName.contains("moona") || fullName.contains("iofi") || 
                        fullName.contains("ollie") || fullName.contains("melfissa") || fullName.contains("reine") || 
                        fullName.contains("zeta") || fullName.contains("kaela") || fullName.contains("kobo") ||
                        // DEV_IS
                        fullName.contains("hiodoshi") || fullName.contains("ao") || fullName.contains("kanade") || 
                        fullName.contains("ririka") || fullName.contains("raden") || fullName.contains("hajime") || 
                        fullName.contains("chihaya") || fullName.contains("vivi") || fullName.contains("kokage") || 
                        fullName.contains("nami") || fullName.contains(" su ")
                    
                    // 2. Blacklist: Strictly exclude clippers even if they contain the name
                    val isClipper = fullName.contains("(sub)") || 
                                   fullName.contains("subtitles") || 
                                   fullName.contains("clip") || 
                                   fullName.contains("fan") || 
                                   fullName.contains("แปล") ||
                                   // "sub" but not "subaru"
                                   (fullName.contains("sub") && !fullName.contains("subaru"))
                    
                    isOfficialTalent && !isClipper
                }.distinctBy { it.id }
                
                _uiState.value = TalentUiState.Success(allChannels)
            } catch (e: Exception) {
                _uiState.value = TalentUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
