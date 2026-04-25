package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.getValue

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(BackgroundLightBlue)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Logo", tint = PrimaryBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Holo Fan Companion", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 20.sp)
                }
                Icon(Icons.Default.Notifications, contentDescription = "Bell", tint = PrimaryBlue)
            }

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is HomeUiState.Error -> {
                    Text("Error: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
                }
                is HomeUiState.Success -> {
                    val liveStreams = state.liveStreams
                    
                    if (liveStreams.isNotEmpty()) {
                        // Live Now Section
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Live Now", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(LiveRed))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Show the first live stream as a big card
                        val firstLive = liveStreams.first()
                        LiveStreamBigCard(firstLive)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Other Live Section
                        if (liveStreams.size > 1) {
                            Text("Other Live", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 18.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            liveStreams.drop(1).forEach { stream ->
                                UpcomingStreamCard(stream.channel.name, "LIVE NOW", stream.channel.photo, stream.id)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    } else {
                        Text("ไม่มีใครกำลังไลฟ์ในขณะนี้", color = TextLight, modifier = Modifier.padding(vertical = 16.dp))
                    }
                }
            }

            // News Panel
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("ข่าวล่าสุด", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("📣")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardWhite), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp, 40.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Hololive Fes 2022!", fontWeight = FontWeight.Bold, color = TextDark, fontSize = 14.sp)
                        Text("มหกรรมคอนเสิร์ตใหญ่...", color = TextLight, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LiveStreamBigCard(video: HolodexVideo) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${video.id}"))
                context.startActivity(intent)
            }
    ) {
        Column {
            Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                // In a real app, we'd use AsyncImage with YouTube thumbnail
                val thumbUrl = "https://img.youtube.com/vi/${video.id}/maxresdefault.jpg"
                coil.compose.AsyncImage(
                    model = thumbUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
                Surface(
                    color = LiveRed,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(12.dp).align(Alignment.BottomStart)
                ) {
                    Text("LIVE", color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(video.title, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 2)
                Text(video.channel.name, color = TextLight, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun UpcomingStreamCard(talent: String, time: String, imageUrl: String? = null, videoId: String? = null) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                if (videoId != null) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                    context.startActivity(intent)
                }
            }
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circle Placeholder instead of Image
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = talent.take(1),
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(talent, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(time, fontSize = 14.sp, color = LiveRed, fontWeight = FontWeight.Bold)
            }
        }
    }
}
