package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScheduleScreen() {
    val dateTabs = listOf("วันนี้", "พรุ่งนี้")
    var selectedDateTab by remember { mutableStateOf(0) }
    val genTabs = listOf("Gen 0", "Gen 1", "EN", "ID")
    var selectedGenTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLightBlue)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
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

        // Today/Tomorrow Switch
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Surface(
                shape = RoundedCornerShape(50),
                color = CardWhite,
                modifier = Modifier.fillMaxWidth().height(40.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    dateTabs.forEachIndexed { index, title ->
                        val isSelected = selectedDateTab == index
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight().background(if (isSelected) PrimaryBlue else Color.Transparent)
                                .clickable { selectedDateTab = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(title, color = if (isSelected) Color.White else TextMainBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Gen Tabs
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            genTabs.forEachIndexed { index, title ->
                val isSelected = selectedGenTab == index
                Surface(
                    color = if (isSelected) PrimaryBlue else CardWhite,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable { selectedGenTab = index }
                ) {
                    Text(
                        title, 
                        color = if (isSelected) Color.White else TextMainBlue, 
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Schedule List
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item { ScheduleItemCard("17:00", "Marine Ch.", "เล่นเกม Outlast", "30 นาที") }
            item { ScheduleItemCard("19:00", "Suisei Channel", "Karaoke Time!", "10 นาที") }
        }
    }
}

@Composable
fun ScheduleItemCard(time: String, channel: String, title: String, duration: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Text(time, fontWeight = FontWeight.Bold, color = LiveRed, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(channel, fontWeight = FontWeight.SemiBold, color = TextMainBlue)
                Text(title, fontWeight = FontWeight.Bold, color = TextDark)
            }
            Text(duration, fontSize = 12.sp, color = TextLight, modifier = Modifier.align(Alignment.Bottom))
        }
    }
}
