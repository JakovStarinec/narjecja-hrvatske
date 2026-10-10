package com.example.narjecja_hrvatske

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.narjecja_hrvatske.ui.theme.Narjecja_HrvatskeTheme

@Composable
fun NarjecjaHrvatskeApp() {
    Narjecja_HrvatskeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            GameScreen()
        }
    }
}

@Composable
fun GameScreen() {
    var selectedTab by remember { mutableStateOf(AppTab.PLAY) }
    var refreshKey by remember { mutableStateOf(0) }

    fun refreshData() {
        refreshKey++
    }

    val currentUsername = remember(refreshKey) {
        currentCloudUsername()
    }

    if (currentUsername == null) {
        AuthScreen(
            onAuthSuccess = {
                selectedTab = AppTab.PLAY
                refreshData()
            }
        )
        return
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            AppBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                AppTab.PLAY -> PlayGameScreen(
                    refreshKey = refreshKey,
                    onOpenSettings = { selectedTab = AppTab.SETTINGS },
                    onOpenReview = { selectedTab = AppTab.REVIEW },
                    onStatsChanged = { refreshData() }
                )

                AppTab.REVIEW -> ReviewWordsScreen(
                    refreshKey = refreshKey,
                    onWordsChanged = { refreshData() }
                )

                AppTab.LEADERBOARD -> LeaderboardScreen(refreshKey = refreshKey)

                AppTab.PROGRESS -> ProgressScreen(
                    refreshKey = refreshKey,
                    onOpenReview = { selectedTab = AppTab.REVIEW }
                )

                AppTab.SETTINGS -> DialectSettingsScreen(
                    refreshKey = refreshKey,
                    onSettingsChanged = { refreshData() },
                    onGoToGame = { selectedTab = AppTab.PLAY },
                    onLogout = {
                        logoutCloudUser()
                        selectedTab = AppTab.PLAY
                        refreshData()
                    }
                )
            }
        }
    }
}

private fun iconForTab(tab: AppTab): ImageVector = when (tab) {
    AppTab.PLAY -> Icons.Filled.SportsEsports
    AppTab.REVIEW -> Icons.Filled.Repeat
    AppTab.LEADERBOARD -> Icons.Filled.EmojiEvents
    AppTab.PROGRESS -> Icons.Filled.BarChart
    AppTab.SETTINGS -> Icons.Filled.Settings
}

@Composable
fun AppBottomBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 12.dp
    ) {
        AppTab.values().forEach { tab ->
            val selected = selectedTab == tab

            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = iconForTab(tab),
                        contentDescription = tab.title,
                        modifier = Modifier.size(if (selected) 24.dp else 22.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
