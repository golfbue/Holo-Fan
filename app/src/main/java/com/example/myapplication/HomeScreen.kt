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

@Composable
fun HomeScreen() {
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

            // Live Now Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Live Now", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(LiveRed))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) // Placeholder
                    Surface(
                        color = LiveRed,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(12.dp).align(Alignment.BottomStart)
                    ) {
                        Text("LIVE", color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Upcoming Live Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Upcoming Live", fontWeight = FontWeight.Bold, color = PrimaryBlue, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(LiveRed))
            }
            Spacer(modifier = Modifier.height(8.dp))
            UpcomingStreamCard("Pekora", "18:00")
            UpcomingStreamCard("Watame", "20:00")

            Spacer(modifier = Modifier.height(24.dp))

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
fun UpcomingStreamCard(talent: String, time: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.LightGray))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(talent, fontWeight = FontWeight.SemiBold, color = TextDark)
                Text(time, fontSize = 14.sp, color = TextLight)
            }
        }
    }
}
