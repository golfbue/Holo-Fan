package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = CardWhite) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "หน้าหลัก") },
                    label = { Text("หน้าหลัก") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "ตารางไลฟ์") },
                    label = { Text("ตารางไลฟ์") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "ข้อมูลเมม") },
                    label = { Text("ข้อมูลเมม") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "สินค้า") },
                    label = { Text("สินค้า") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "ตั้งค่า") },
                    label = { Text("ตั้งค่า") },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = BackgroundLightBlue, selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> HomeScreen()
                1 -> ScheduleScreen()
                2 -> TalentProfileScreen()
                3 -> MerchStoreScreen()
                4 -> SettingsScreen()
            }
        }
    }
}