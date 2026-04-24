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

import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun TalentProfileScreen(talent: Talent = TalentProvider.talents.first()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLightBlue)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Logo", tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ข้อมูลเมม", fontWeight = FontWeight.ExtraBold, color = PrimaryBlue, fontSize = 20.sp)
            }
            Icon(Icons.Default.Notifications, contentDescription = "Bell", tint = PrimaryBlue)
        }

        // Talent Image Header (Big Image)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(PrimaryBlue),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = talent.profileImage,
                contentDescription = talent.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
        
        // Info Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            modifier = Modifier.fillMaxWidth().padding(16.dp).offset(y = (-30).dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(talent.name, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
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
