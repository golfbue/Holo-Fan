package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun MerchStoreScreen() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon Section
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = PrimaryBlue.copy(alpha = 0.1f)
        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.padding(32.dp).fillMaxSize(),
                tint = PrimaryBlue
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Official Hololive Shop",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Check out official merchandise and special collaboration items from your favorite talents!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://shop.hololivepro.com/"))
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerSize,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "GO TO OFFICIAL SHOP", 
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }
    }
}
