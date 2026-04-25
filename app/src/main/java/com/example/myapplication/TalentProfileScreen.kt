package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun TalentProfileScreen() {
    var selectedTalent by remember { mutableStateOf<Talent?>(null) }

    if (selectedTalent == null) {
        TalentListScreen(onTalentClick = { selectedTalent = it })
    } else {
        TalentDetailScreen(talent = selectedTalent!!, onBack = { selectedTalent = null })
    }
}

@Composable
fun TalentListScreen(onTalentClick: (Talent) -> Unit) {
    val allTalents = TalentProvider.talents
    val categories = listOf(
        "ALL", "Gen 0", "Gen 1", "Gen 2", "GAMERS", "Gen 3", "Gen 4", "Gen 5", "HoloX", 
        "Indonesia", "English", "Myth", "Promise", "Advent", "Justice", "ReGLOSS", "FLOW GLOW", "holoAN", "Alum", "Staff"
    )
    var selectedCategory by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredTalents = remember(selectedCategory, searchQuery, allTalents) {
        val categoryFiltered = if (selectedCategory == "ALL") {
            allTalents
        } else {
            allTalents.filter { talent ->
                when (selectedCategory) {
                    "Indonesia" -> talent.generation.contains("ID", ignoreCase = true)
                    "English" -> talent.generation.contains("EN", ignoreCase = true)
                    "Alum" -> talent.status == "Alum"
                    "Staff" -> talent.status == "Affiliate" || talent.generation.contains("Staff")
                    else -> talent.generation.contains(selectedCategory, ignoreCase = true)
                }
            }
        }
        
        if (searchQuery.isBlank()) {
            categoryFiltered
        } else {
            categoryFiltered.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLightBlue)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Logo", tint = PrimaryBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Text("TALENT", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 24.sp)
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("ค้นหาโอชิของคุณ...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryBlue) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = PrimaryBlue.copy(alpha = 0.3f),
                focusedBorderColor = PrimaryBlue,
                unfocusedContainerColor = CardWhite,
                focusedContainerColor = CardWhite
            ),
            singleLine = true
        )

        // Filter Buttons (Scrollable Row)
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

        // Talent List
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(filteredTalents) { talent ->
                TalentListItem(talent = talent, onClick = { onTalentClick(talent) })
            }
        }
    }
}

@Composable
fun FilterChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) PrimaryBlue else CardWhite,
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
fun TalentListItem(talent: Talent, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle Avatar with Oshi Mark instead of Image
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = talent.oshiMark,
                    fontSize = 32.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = talent.name,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextDark,
                    fontSize = 18.sp
                )
                Text(
                    text = talent.generation,
                    fontSize = 12.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                StatusBadge(status = talent.status)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val color = when (status) {
        "Active" -> Color(0xFF4CAF50)
        "Alum" -> Color(0xFF9E9E9E)
        "Affiliate" -> Color(0xFF2196F3)
        else -> Color.Gray
    }
    Surface(
        color = color,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = status,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun TalentDetailScreen(talent: Talent, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLightBlue)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with Back Button
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryBlue)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("รายละเอียดเมมเบอร์", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 20.sp)
        }

        // Talent Oshi Header
        Box(
            modifier = Modifier.fillMaxWidth().height(200.dp).background(PrimaryBlue.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = talent.oshiMark,
                    fontSize = 80.sp
                )
            }
        }
        
        // Info Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            modifier = Modifier.fillMaxWidth().padding(16.dp).offset(y = (-30).dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(talent.name, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(status = talent.status)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("รุ่น : ${talent.generation}", fontSize = 14.sp, color = TextMainBlue)
                Text("วันเดบิวต์ : ${talent.debutDate}", fontSize = 14.sp, color = TextMainBlue)
                Text("อิโมจิ : ${talent.oshiMark}", fontSize = 14.sp, color = TextMainBlue)
                
                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = BackgroundLightBlue)
                
                Text(talent.bio, color = TextDark, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                val uriHandler = LocalUriHandler.current
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { uriHandler.openUri("https://www.youtube.com/channel/${talent.youtubeChannelId}") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000))
                    ) { Text("YouTube") }
                    Button(
                        onClick = { uriHandler.openUri("https://twitter.com/${talent.twitter.removePrefix("@")}") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DA1F2))
                    ) { Text(talent.twitter) }
                }
            }
        }
    }
}
