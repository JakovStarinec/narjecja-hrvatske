package com.example.narjecja_hrvatske

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.narjecja_hrvatske.ui.theme.AppElevation
import com.example.narjecja_hrvatske.ui.theme.AppRadius
import com.example.narjecja_hrvatske.ui.theme.AppSpacing
import com.example.narjecja_hrvatske.ui.theme.SemanticColors

@Composable
fun ReviewWordsScreen(
    refreshKey: Int,
    onWordsChanged: () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    val allWords = remember { getWordBank() }

    var missedMeanings by remember(refreshKey) {
        mutableStateOf(getMissedMeanings(preferences))
    }

    val missedWords = remember(missedMeanings, allWords) {
        allWords.filter { it.meaning in missedMeanings }
    }

    fun saveMissedMeaningsLocal(meanings: Set<String>) {
        missedMeanings = meanings
        saveMissedMeanings(preferences, meanings)
        onWordsChanged()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconChip(
            icon = Icons.Filled.Repeat,
            background = MaterialTheme.colorScheme.primaryContainer,
            tint = MaterialTheme.colorScheme.primary,
            size = 56.dp
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Vježba promašenih riječi",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Text(
            text = if (missedWords.isEmpty()) {
                "Nema riječi za ponavljanje. Kada pogriješiš u igri, riječ će se automatski spremiti ovdje."
            } else {
                "Ovdje su riječi koje si promašio. Kada ih naučiš, označi ih kao savladane."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        if (missedWords.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppRadius.l),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.xl),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconChip(
                        icon = Icons.Filled.CheckCircle,
                        background = SemanticColors.successContainer,
                        tint = SemanticColors.successStrong,
                        size = 48.dp
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.s))

                    Text(
                        text = "Sve je čisto!",
                        style = MaterialTheme.typography.titleLarge,
                        color = SemanticColors.successText
                    )

                    Text(
                        text = "Nastavi igrati i ovdje će se pojaviti riječi koje treba ponoviti.",
                        modifier = Modifier.padding(top = AppSpacing.xs),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppRadius.l),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = "Ukupno za ponavljanje: ${missedWords.size}",
                    modifier = Modifier.padding(AppSpacing.l),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.l))

            missedWords.forEach { word ->
                ReviewWordCard(
                    word = word,
                    onRemove = {
                        saveMissedMeaningsLocal(missedMeanings - word.meaning)
                    }
                )

                Spacer(modifier = Modifier.height(AppSpacing.m))
            }

            Spacer(modifier = Modifier.height(AppSpacing.s))

            TextButton(
                onClick = {
                    saveMissedMeaningsLocal(emptySet())
                }
            ) {
                Text("Očisti sve riječi za ponavljanje", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ReviewWordCard(
    word: WordEntry,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.l)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.meaning.uppercaseFirstSimple(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (word.imageRes != null) {
                        Image(
                            painter = painterResource(id = word.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        Text(
                            text = word.imageEmoji,
                            fontSize = 28.sp
                        )
                    }
                }

                Button(
                    onClick = onRemove,
                    shape = RoundedCornerShape(AppRadius.s),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SemanticColors.successStrong,
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    Text("Znam")
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.m))

            DialectLine(
                title = "Štokavsko",
                value = word.stokavsko
            )

            DialectLine(
                title = "Kajkavsko",
                value = word.kajkavsko
            )

            DialectLine(
                title = "Čakavsko",
                value = word.cakavsko
            )
        }
    }
}

@Composable
fun DialectLine(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.4f)
        )
    }
}


@Composable
fun ProgressScreen(
    refreshKey: Int,
    onOpenReview: () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    val lastScore = remember(refreshKey) { preferences.getInt(KEY_LAST_SCORE, 0) }
    val bestScore = remember(refreshKey) { preferences.getInt(KEY_BEST_SCORE, 0) }
    val lastMasteredWords = remember(refreshKey) { preferences.getInt(KEY_LAST_MASTERED_WORDS, 0) }
    val lastWrongAnswers = remember(refreshKey) { preferences.getInt(KEY_LAST_WRONG_ANSWERS, 0) }
    val gamesPlayed = remember(refreshKey) { preferences.getInt(KEY_GAMES_PLAYED, 0) }
    val reviewCount = remember(refreshKey) { getMissedMeanings(preferences).size }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconChip(
            icon = Icons.Filled.BarChart,
            background = MaterialTheme.colorScheme.primaryContainer,
            tint = MaterialTheme.colorScheme.primary,
            size = 56.dp
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Tvoj napredak",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Text(
            text = "Napredak je sada povezan s ekranom Vježba.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            ProgressCard(
                title = "Najbolji rezultat",
                value = "$bestScore",
                icon = Icons.Filled.Star,
                modifier = Modifier.weight(1f)
            )

            ProgressCard(
                title = "Zadnja igra",
                value = "$lastScore",
                icon = Icons.Filled.SportsEsports,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            ProgressCard(
                title = "Savladano zadnju igru",
                value = "$lastMasteredWords",
                icon = Icons.Filled.CheckCircle,
                modifier = Modifier.weight(1f)
            )

            ProgressCard(
                title = "Klikni za vježbu",
                value = "$reviewCount",
                icon = Icons.Filled.Repeat,
                modifier = Modifier.weight(1f),
                onClick = onOpenReview
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.m)
        ) {
            ProgressCard(
                title = "Greške zadnju igru",
                value = "$lastWrongAnswers",
                icon = Icons.Filled.Close,
                modifier = Modifier.weight(1f)
            )

            ProgressCard(
                title = "Odigrano igara",
                value = "$gamesPlayed",
                icon = Icons.Filled.VideogameAsset,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenReview() },
            shape = RoundedCornerShape(AppRadius.l),
            colors = CardDefaults.cardColors(
                containerColor = if (reviewCount > 0) SemanticColors.dangerContainer else MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = if (reviewCount > 0) {
                    "Imaš $reviewCount riječi koje moraš ponoviti. Dodirni ovdje za ekran Vježba."
                } else {
                    "Nema riječi za ponavljanje. Kad pogriješiš, riječ će se spremiti u Vježbu."
                },
                modifier = Modifier.padding(AppSpacing.l),
                style = MaterialTheme.typography.titleMedium,
                color = if (reviewCount > 0) SemanticColors.dangerText else MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val cardModifier = if (onClick != null) {
        modifier
            .height(130.dp)
            .clickable { onClick() }
    } else {
        modifier.height(130.dp)
    }

    Card(
        modifier = cardModifier,
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.m),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
