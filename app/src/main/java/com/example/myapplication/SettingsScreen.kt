package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    var notificationEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLightBlue)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Logo", tint = PrimaryBlue)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ตั้งค่า", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 20.sp)
        }

        // Settings Container
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Language
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("ภาษา", color = TextDark, fontSize = 16.sp)
                    Text("ภาษาไทย >", color = TextLight, fontSize = 14.sp)
                }
                HorizontalDivider(color = BackgroundLightBlue)
                
                // Sound
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("เสียงเตือนที่ตั้งไว้", color = TextDark, fontSize = 16.sp)
                    Text("🎵", fontSize = 16.sp)
                }
                HorizontalDivider(color = BackgroundLightBlue)

                // Alerts
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("การแจ้งเตือน", color = TextDark, fontSize = 16.sp)
                    Text(">", color = TextLight, fontSize = 14.sp)
                }
                HorizontalDivider(color = BackgroundLightBlue)

                // Notification Time
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("แจ้งเตือนล่วงหน้า 30 นาที", color = TextDark, fontSize = 16.sp)
                    Switch(checked = notificationEnabled, onCheckedChange = { notificationEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = PrimaryBlue, checkedTrackColor = BackgroundLightBlue))
                }
                HorizontalDivider(color = BackgroundLightBlue)

                // Dark Mode
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Dark Mode", color = TextDark, fontSize = 16.sp)
                    Switch(checked = darkModeEnabled, onCheckedChange = { darkModeEnabled = it }, colors = SwitchDefaults.colors(checkedThumbColor = PrimaryBlue, checkedTrackColor = BackgroundLightBlue))
                }
            }
        }
    }
}
