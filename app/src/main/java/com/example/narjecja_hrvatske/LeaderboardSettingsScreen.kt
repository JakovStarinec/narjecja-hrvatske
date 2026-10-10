package com.example.narjecja_hrvatske

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.narjecja_hrvatske.ui.theme.AppElevation
import com.example.narjecja_hrvatske.ui.theme.AppRadius
import com.example.narjecja_hrvatske.ui.theme.AppSpacing
import com.example.narjecja_hrvatske.ui.theme.SemanticColors

@Composable
fun LeaderboardScreen(refreshKey: Int) {
    var players by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var loadFailed by remember { mutableStateOf(false) }

    val currentUsername = remember(refreshKey) {
        currentCloudUsername()
    }

    LaunchedEffect(refreshKey) {
        isLoading = true
        loadFailed = false

        try {
            players = fetchCloudLeaderboard()
        } catch (e: Exception) {
            loadFailed = true
        }

        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconChip(
            icon = Icons.Filled.EmojiEvents,
            background = SemanticColors.goldContainer,
            tint = SemanticColors.goldText,
            size = 56.dp
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Tablica rezultata",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Text(
            text = "Najbolji rezultati registriranih korisnika.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else if (loadFailed) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppRadius.l),
                colors = CardDefaults.cardColors(
                    containerColor = SemanticColors.dangerContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
            ) {
                Text(
                    text = "Tablica se nije uspjela učitati. Provjeri internetsku vezu i pokušaj ponovno.",
                    modifier = Modifier.padding(AppSpacing.xl),
                    style = MaterialTheme.typography.bodyLarge,
                    color = SemanticColors.dangerText,
                    textAlign = TextAlign.Center
                )
            }
        } else if (players.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppRadius.l),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
            ) {
                Text(
                    text = "Još nema registriranih korisnika u tablici.",
                    modifier = Modifier.padding(AppSpacing.xl),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            players.forEachIndexed { index, player ->
                ScoreRow(
                    position = index + 1,
                    name = player.username,
                    score = player.bestScore,
                    isCurrentUser = player.isCurrentUser
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.l),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = if (currentUsername != null) {
                    "Prijavljen si kao $currentUsername. Nakon svake igre tvoj najbolji rezultat automatski se sprema u tablicu."
                } else {
                    "Prijavi se da bi se tvoj rezultat spremao u tablicu."
                },
                modifier = Modifier.padding(AppSpacing.l),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ScoreRow(
    position: Int,
    name: String,
    score: Int,
    isCurrentUser: Boolean
) {
    val containerColor = when {
        isCurrentUser -> SemanticColors.successContainer
        position == 1 -> SemanticColors.goldContainer
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius.m),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(34.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                when (position) {
                    1 -> Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = SemanticColors.goldText, modifier = Modifier.height(24.dp))
                    2 -> Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = Color(0xFF9E9E9E), modifier = Modifier.height(24.dp))
                    3 -> Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = Color(0xFFB2743A), modifier = Modifier.height(24.dp))
                    else -> Text(
                        text = "$position.",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = if (isCurrentUser) "$name (ti)" else name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = if (isCurrentUser) "Tvoj profil" else "Registrirani korisnik",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "$score",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DialectSettingsScreen(
    refreshKey: Int,
    onSettingsChanged: () -> Unit,
    onGoToGame: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var selectedDialect by remember(refreshKey) {
        mutableStateOf(readSavedDialect(preferences))
    }

    var savedMessage by remember {
        mutableStateOf("")
    }

    val currentUsername = remember(refreshKey) {
        currentCloudUsername()
    }

    val currentUserBestScore = remember(refreshKey) {
        getBestScoreForCurrentUser(preferences)
    }

    fun saveDialect(dialect: Dialect) {
        selectedDialect = dialect
        preferences.edit()
            .putString(KEY_USER_DIALECT, dialect.name)
            .apply()

        savedMessage = "Spremljeno: ${dialect.title}"
        onSettingsChanged()
    }

    fun clearDialect() {
        selectedDialect = null
        preferences.edit()
            .remove(KEY_USER_DIALECT)
            .apply()

        savedMessage = "Odabir je obrisan."
        onSettingsChanged()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconChip(
            icon = Icons.Filled.Settings,
            background = MaterialTheme.colorScheme.primaryContainer,
            tint = MaterialTheme.colorScheme.primary,
            size = 56.dp
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Profil i odabir",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Text(
            text = "Ovdje mijenjaš narječje i upravljaš prijavom.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.l),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.l),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconChip(
                    icon = Icons.Filled.Person,
                    background = MaterialTheme.colorScheme.secondaryContainer,
                    tint = MaterialTheme.colorScheme.secondary,
                    size = 44.dp
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Text(
                    text = "Prijavljeni korisnik",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = currentUsername ?: "Nije prijavljen",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = "Najbolji rezultat: $currentUserBestScore",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                TextButton(onClick = onLogout) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        tint = SemanticColors.dangerStrong,
                        modifier = Modifier.height(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.xs))
                    Text(
                        text = "Odjavi se",
                        color = SemanticColors.dangerStrong,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.l),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.l),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Trenutno narječje",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = selectedDialect?.title ?: "Nije odabrano",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )

                if (savedMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(AppSpacing.xs))

                    Text(
                        text = savedMessage,
                        style = MaterialTheme.typography.labelLarge,
                        color = SemanticColors.successStrong
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Dialect.values().forEach { dialect ->
            val selected = selectedDialect == dialect

            Button(
                onClick = { saveDialect(dialect) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(AppRadius.m),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (selected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.height(18.dp)
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.xs))
                }

                Text(
                    text = dialect.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Button(
            onClick = onGoToGame,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            shape = RoundedCornerShape(AppRadius.m),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Idi na igru",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        TextButton(onClick = { clearDialect() }) {
            Text("Obriši odabir narječja", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
