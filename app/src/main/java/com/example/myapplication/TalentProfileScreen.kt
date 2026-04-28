package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun TalentProfileScreen(viewModel: TalentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    
    when (val state = uiState) {
        is TalentUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        }
        is TalentUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.message}", color = Color.Red)
            }
        }
        is TalentUiState.Success -> {
            TalentListScreen(talents = state.talents, onTalentClick = { talent ->
                val url = "https://www.youtube.com/channel/${talent.id}"
                uriHandler.openUri(url)
            })
        }
    }
}

@Composable
fun TalentListScreen(talents: List<HolodexChannel>, onTalentClick: (HolodexChannel) -> Unit) {
    val strings = LocalStrings.current
    val categories = listOf(
        "ALL", "Gen 0", "Gen 1", "Gen 2", "GAMERS", "Gen 3", "Gen 4", "Gen 5", "HoloX",
        "ID 1", "ID 2", "ID 3", "Myth", "Promise", "Advent", "Justice", "ReGLOSS", "FLOW GLOW"
    )
    var selectedCategory by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    // Helper to determine generation based on name mapping
    fun getMemberGeneration(name: String): List<String> {
        val n = name.lowercase()
        val gens = mutableListOf<String>()
        
        // JP Gen 0
        if (n.contains("sora") || n.contains("roboco") || n.contains("miko") || n.contains("suisei") || n.contains("azki")) {
            gens.add("Gen 0")
        }
        // JP Gen 1
        if (n.contains("yozora") || n.contains("fubuki") || n.contains("matsuri") || n.contains("rosenthal") || n.contains("haato")) {
            gens.add("Gen 1")
        }
        // JP Gen 2
        if (n.contains("aqua") || n.contains("shion") || n.contains("ayame") || n.contains("choco") || n.contains("subaru")) {
            gens.add("Gen 2")
        }
        // GAMERS
        if (n.contains("fubuki") || n.contains("mio") || n.contains("okayu") || n.contains("korone")) {
            gens.add("GAMERS")
        }
        // JP Gen 3
        if (n.contains("pekora") || n.contains("flare") || n.contains("noel") || n.contains("marine")) {
            gens.add("Gen 3")
        }
        // JP Gen 4
        if (n.contains("kanata") || n.contains("watame") || n.contains("towa") || n.contains("luna")) {
            gens.add("Gen 4")
        }
        // JP Gen 5
        if (n.contains("lamy") || n.contains("nene") || n.contains("botan") || n.contains("polka")) {
            gens.add("Gen 5")
        }
        // HoloX
        if (n.contains("laplus") || n.contains(" lui ") || n.contains("koyori") || n.contains("chloe") || n.contains("iroha")) {
            gens.add("HoloX")
        }
        
        // EN Myth
        if (n.contains("calliope") || n.contains("kiara") || n.contains("ina'nis") || n.contains("gura") || n.contains("amelia")) {
            gens.add("Myth")
        }
        // EN Promise
        if (n.contains("irys") || n.contains("fauna") || n.contains("kronii") || n.contains("mumei") || n.contains("baels") || n.contains("baelz")) {
            gens.add("Promise")
        }
        // EN Advent
        if (n.contains("shiori") || n.contains("bijou") || n.contains("nerissa") || n.contains("fuwawa") || n.contains("mococo")) {
            gens.add("Advent")
        }
        // EN Justice
        if (n.contains("bloodflame") || (n.contains("elizabeth") && n.contains("rose")) || n.contains("gigi") || n.contains("murin") || n.contains("cecilia") || n.contains("immergreen") || n.contains("raora") || n.contains("panthera")) {
            gens.add("Justice")
        }
        
        // ID 1
        if (n.contains("risu") || n.contains("moona") || n.contains("iofi")) {
            gens.add("ID 1")
        }
        // ID 2
        if (n.contains("ollie") || n.contains("melfissa") || n.contains("reine")) {
            gens.add("ID 2")
        }
        // ID 3
        if (n.contains("zeta") || n.contains("kaela") || n.contains("kobo")) {
            gens.add("ID 3")
        }
        
        // ReGLOSS
        if (n.contains("hiodoshi") || (n.contains("ao") && !n.contains("raora")) || n.contains("otonose") || n.contains("kanade") || n.contains("ichijou") || n.contains("ririka") || n.contains("juufuutei") || n.contains("raden") || n.contains("todoroki") || n.contains("hajime")) {
            gens.add("ReGLOSS")
        }
        // FLOW GLOW
        if (n.contains("rindo") || n.contains("chihaya") || n.contains("kikirara") || n.contains("vivi") || n.contains("kokage") || n.contains("minami") || n.contains("nami") || n.contains("mizumiya") || n.contains(" su ")) {
            gens.add("FLOW GLOW")
        }
        
        return gens
    }

    val filteredTalents = remember(selectedCategory, searchQuery, talents) {
        val categoryFiltered = if (selectedCategory == "ALL") {
            talents
        } else {
            talents.filter { talent ->
                val memberGens = getMemberGeneration(talent.name + (talent.english_name ?: ""))
                memberGens.contains(selectedCategory)
            }
        }

        if (searchQuery.isBlank()) {
            categoryFiltered
        } else {
            categoryFiltered.filter { 
                it.name.contains(searchQuery, ignoreCase = true) || 
                it.english_name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Logo", tint = PrimaryBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.talents.uppercase(), fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 24.sp)
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text(strings.searchOshi) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryBlue) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = PrimaryBlue.copy(alpha = 0.3f),
                focusedBorderColor = PrimaryBlue,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    label = category,
                    isSelected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredTalents) { talent ->
                val gens = getMemberGeneration(talent.name + (talent.english_name ?: ""))
                TalentGridItem(talent = talent, generations = gens, onClick = { onTalentClick(talent) })
            }
        }
    }
}

@Composable
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f)),
        modifier = Modifier.height(36.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = label,
                color = if (isSelected) Color.White else PrimaryBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TalentGridItem(talent: HolodexChannel, generations: List<String>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp) // กำหนดความสูงคงที่เพื่อให้ทุกใบเท่ากัน
            .clickable { onClick() },
        shape = RoundedCornerSize,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // จัดให้อยู่กลางแนวตั้งเพื่อให้สมดุล
        ) {
            AsyncImage(
                model = talent.photo,
                contentDescription = talent.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = talent.english_name ?: talent.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Generation Tags
            if (generations.isNotEmpty()) {
                Surface(
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = generations.first(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Spacer เพื่อรักษาความสูงให้เท่ากับคนที่มีป้ายรุ่น
                Spacer(modifier = Modifier.height(22.dp))
            }
        }
    }
}