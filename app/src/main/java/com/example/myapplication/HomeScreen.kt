package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.clickable

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Main content in LazyColumn for better performance
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Welcome Header
            item {
                Column(modifier = Modifier.padding(16.dp).padding(top = 24.dp)) {
                    Text(
                        strings.hello,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        strings.discover,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryBlue
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Functional Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(strings.searchOshi, fontSize = 14.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryBlue) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.1f),
                            focusedBorderColor = PrimaryBlue,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryBlue)
                        }
                    }
                }
                is HomeUiState.Error -> {
                    item {
                        Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                    }
                }
                is HomeUiState.Success -> {
                    // Filter out shorts from the main live list
                    val allLive = state.liveStreams.filter { stream ->
                        val titleLower = stream.title.lowercase()
                        !titleLower.contains("#shorts") && 
                        !titleLower.contains("#reel") &&
                        !titleLower.contains("shorts") &&
                        !titleLower.contains("reel") &&
                        !titleLower.contains("tiktok") &&
                        !titleLower.contains("ショート") &&
                        !titleLower.contains("ショーツ")
                    }
                    
                    val allUpcoming = state.upcomingStreams.filter { stream ->
                        val titleLower = stream.title.lowercase()
                        !titleLower.contains("#shorts") && 
                        !titleLower.contains("#reel") &&
                        !titleLower.contains("shorts") &&
                        !titleLower.contains("reel")
                    }

                    // Apply Search Filter and ensure Distinct by ID
                    val liveStreams = (if (searchQuery.isBlank()) allLive else allLive.filter { 
                        it.title.contains(searchQuery, ignoreCase = true) || it.channel.name.contains(searchQuery, ignoreCase = true) 
                    }).distinctBy { it.id }
                    
                    val upcoming = (if (searchQuery.isBlank()) allUpcoming else allUpcoming.filter { 
                        it.title.contains(searchQuery, ignoreCase = true) || it.channel.name.contains(searchQuery, ignoreCase = true) 
                    }).distinctBy { it.channel.id } // Filter by Channel ID to prevent duplicate icons
                    
                    if (liveStreams.isNotEmpty()) {
                        // 2. Featured Live (Big Card)
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(strings.liveNow, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(LiveRed))
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                LiveStreamBigCard(liveStreams.first())
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }

                        // 3. Other Live (Horizontal Scroll)
                        if (liveStreams.size > 1) {
                            item {
                                Column {
                                    Text(
                                        strings.otherLive, 
                                        fontWeight = FontWeight.ExtraBold, 
                                        color = MaterialTheme.colorScheme.onBackground, 
                                        fontSize = 18.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    androidx.compose.foundation.lazy.LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(liveStreams.drop(1).size) { index ->
                                            LiveStreamMiniCard(liveStreams.drop(1)[index])
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }
                        }
                    }

                    // 4. Upcoming Highlights
                    if (upcoming.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    if (LocalStrings.current == ThaiStrings) "ไฮไลท์สตรีมเร็วๆ นี้" else "Upcoming Highlights", 
                                    fontWeight = FontWeight.ExtraBold, 
                                    color = MaterialTheme.colorScheme.onBackground, 
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                androidx.compose.foundation.lazy.LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(upcoming.size) { index ->
                                        UpcomingHorizontalCard(upcoming[index])
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }

                    // 5. Latest Updates (New Section from API)
                    if (state.latestVideos.isNotEmpty()) {
                        item {
                            Column {
                                Text(
                                    strings.latestNews, 
                                    fontWeight = FontWeight.ExtraBold, 
                                    color = MaterialTheme.colorScheme.onBackground, 
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                androidx.compose.foundation.lazy.LazyRow(
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(state.latestVideos.size) { index ->
                                        LatestVideoCard(state.latestVideos[index])
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                        }
                    }
                }
            }

            // 6. News Section (Link)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (LocalStrings.current == ThaiStrings) "ข่าวสารอย่างเป็นทางการ" else "Official News", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground, fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("🌐", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    NewsBannerCard()
                }
            }
        }
    }
}

@Composable
fun LiveStreamBigCard(video: HolodexVideo) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.id}"))
                context.startActivity(intent)
            }
    ) {
        Column {
            Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
                val thumbUrl = "https://img.youtube.com/vi/${video.id}/maxresdefault.jpg"
                coil.compose.AsyncImage(
                    model = thumbUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                
                // Live Indicator Overlay
                Surface(
                    color = LiveRed,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("LIVE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Channel Info Overlay (Bottom)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        ))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        coil.compose.AsyncImage(
                            model = video.channel.photo,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(video.channel.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(video.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 2, fontSize = 16.sp, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun LiveStreamMiniCard(video: HolodexVideo) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .width(240.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.id}"))
                context.startActivity(intent)
            }
    ) {
        Column {
            Box(modifier = Modifier.height(130.dp).fillMaxWidth()) {
                val thumbUrl = "https://img.youtube.com/vi/${video.id}/mqdefault.jpg"
                coil.compose.AsyncImage(
                    model = thumbUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Surface(
                    color = LiveRed,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                ) {
                    Text("LIVE", color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(video.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground, maxLines = 1, fontSize = 13.sp)
                Text(video.channel.name, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f), fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun LatestVideoCard(video: HolodexVideo) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .width(200.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.id}"))
                context.startActivity(intent)
            }
    ) {
        Column {
            Box(modifier = Modifier.height(110.dp).fillMaxWidth()) {
                val thumbUrl = "https://img.youtube.com/vi/${video.id}/mqdefault.jpg"
                coil.compose.AsyncImage(
                    model = thumbUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(video.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, fontSize = 12.sp)
                Text(video.channel.name, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun UpcomingHorizontalCard(video: HolodexVideo) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .width(160.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.id}"))
                context.startActivity(intent)
            }
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            coil.compose.AsyncImage(
                model = video.channel.photo,
                contentDescription = null,
                modifier = Modifier.size(60.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(video.channel.name, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, maxLines = 1)
            Text("Upcoming", color = PrimaryBlue, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun NewsBannerCard() {
    val context = LocalContext.current
    val strings = LocalStrings.current
    Card(
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hololivepro.com/news_en/"))
                context.startActivity(intent)
            }
    ) {
        Box(
            modifier = Modifier
                .background(
                    androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(PrimaryBlue, Color(0xFF00B0FF), Color(0xFF00E5FF))
                    )
                )
        ) {
            // Decorative Icon in background
            Text(
                "🌐", 
                fontSize = 120.sp, 
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 30.dp, y = 20.dp)
                    .alpha(0.15f)
            )
            
            Row(
                modifier = Modifier.padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (LocalStrings.current == ThaiStrings) "ข่าวสาร Hololive" else "Hololive News", 
                        color = Color.White.copy(alpha = 0.9f), 
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        if (LocalStrings.current == ThaiStrings) "อัปเดตประกาศทางการล่าสุด" else "Official News & Updates", 
                        color = Color.White, 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 22.sp,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://hololivepro.com/news_en/"))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            if (LocalStrings.current == ThaiStrings) "อ่านเพิ่มเติม" else "Read More", 
                            color = PrimaryBlue, 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
