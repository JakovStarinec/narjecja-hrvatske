package com.example.narjecja_hrvatske

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.narjecja_hrvatske.ui.theme.AppElevation
import com.example.narjecja_hrvatske.ui.theme.AppRadius
import com.example.narjecja_hrvatske.ui.theme.AppSpacing
import com.example.narjecja_hrvatske.ui.theme.SemanticColors

@Composable
fun GuessMeaningScreen(
    question: GameQuestion,
    wordBank: List<WordEntry>,
    score: Int,
    lives: Int,
    timer: Int,
    feedbackMessage: String,
    isAnswerLocked: Boolean,
    onAnswerSelected: (String) -> Unit,
    onBackToMenu: () -> Unit
) {
    val sourceText = question.entry.textForDialect(question.sourceDialect)
    val correctAnswer = question.entry.textForDialect(question.targetDialect)

    val options = remember(question) {
        val wrongOptions = wordBank
            .map { it.textForDialect(question.targetDialect) }
            .filter { normalizeAnswer(it) != normalizeAnswer(correctAnswer) }
            .distinct()
            .shuffled()
            .take(3)

        (wrongOptions + correctAnswer).shuffled()
    }

    BaseGameLayout(
        score = score,
        lives = lives,
        timer = timer,
        feedbackMessage = feedbackMessage,
        onBackToMenu = onBackToMenu
    ) {
        GameModeHeader(
            icon = Icons.Filled.CheckCircle,
            title = "Odaberi prijevod",
            subtitle = "${question.sourceDialect.title} → ${question.targetDialect.title}"
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        WordCard(
            title = "Prevedi riječ",
            word = sourceText,
            subtitle = "Odaberi točan oblik."
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        options.forEachIndexed { index, option ->
            AnswerOptionCard(
                number = index + 1,
                text = option,
                enabled = !isAnswerLocked,
                onClick = { onAnswerSelected(option) }
            )

            if (index != options.lastIndex) {
                Spacer(modifier = Modifier.height(AppSpacing.xs))
            }
        }
    }
}

@Composable
fun TranslateScreen(
    question: GameQuestion,
    targetDialect: Dialect,
    score: Int,
    lives: Int,
    timer: Int,
    feedbackMessage: String,
    isAnswerLocked: Boolean,
    onSubmitAnswer: (String) -> Unit,
    onBackToMenu: () -> Unit
) {
    var answer by remember(question) { mutableStateOf("") }

    val sourceText = question.entry.textForDialect(question.sourceDialect)

    BaseGameLayout(
        score = score,
        lives = lives,
        timer = timer,
        feedbackMessage = feedbackMessage,
        onBackToMenu = onBackToMenu
    ) {
        GameModeHeader(
            icon = Icons.Filled.Edit,
            title = "Upiši prijevod",
            subtitle = "${question.sourceDialect.title} → ${targetDialect.title}"
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        WordCard(
            title = "Prevedi riječ",
            word = sourceText
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.l),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.m)
            ) {
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    placeholder = { Text("Upiši prijevod...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isAnswerLocked,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Button(
                    onClick = {
                        if (answer.isNotBlank()) {
                            onSubmitAnswer(answer)
                        }
                    },
                    enabled = !isAnswerLocked && answer.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(AppRadius.s),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(
                        text = "Provjeri",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun ImageMatchScreen(
    question: GameQuestion,
    wordBank: List<WordEntry>,
    score: Int,
    lives: Int,
    timer: Int,
    feedbackMessage: String,
    isAnswerLocked: Boolean,
    onEmojiSelected: (String) -> Unit,
    onBackToMenu: () -> Unit
) {
    val sourceText = question.entry.textForDialect(question.sourceDialect)
    val correctEmoji = question.entry.imageEmoji

    // Emoji ostaje identitet opcije (radi kompatibilnosti s ostatkom igre), a ovdje
    // gledamo ima li ta riječ vlastitu ikonu pa je prikazujemo umjesto emojija.
    val emojiToImageRes = remember(wordBank) {
        wordBank.associate { it.imageEmoji to it.imageRes }
    }

    val options = remember(question) {
        val wrongOptions = wordBank
            .map { it.imageEmoji }
            .filter { it != correctEmoji }
            .distinct()
            .shuffled()
            .take(3)

        (wrongOptions + correctEmoji).shuffled()
    }

    BaseGameLayout(
        score = score,
        lives = lives,
        timer = timer,
        feedbackMessage = feedbackMessage,
        onBackToMenu = onBackToMenu
    ) {
        GameModeHeader(
            icon = Icons.Filled.Image,
            title = "Poveži sa slikom",
            subtitle = "Odaberi sliku za riječ."
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        WordCard(
            title = "${question.sourceDialect.title}",
            word = sourceText,
            subtitle = "Koja slika odgovara?"
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        options.chunked(2).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
            ) {
                rowItems.forEach { emoji ->
                    ImageOptionCard(
                        emoji = emoji,
                        imageRes = emojiToImageRes[emoji],
                        enabled = !isAnswerLocked,
                        modifier = Modifier.weight(1f),
                        onClick = { onEmojiSelected(emoji) }
                    )
                }
            }

            if (rowIndex == 0) {
                Spacer(modifier = Modifier.height(AppSpacing.s))
            }
        }
    }
}

@Composable
fun BaseGameLayout(
    score: Int,
    lives: Int,
    timer: Int,
    feedbackMessage: String,
    onBackToMenu: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = AppSpacing.m, vertical = AppSpacing.s),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopGameBar(
            score = score,
            lives = lives,
            timer = timer,
            timeLimit = 20,
            onBackToMenu = onBackToMenu
        )

        if (feedbackMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(AppSpacing.s))
            FeedbackCard(feedbackMessage = feedbackMessage)
        }

        Spacer(modifier = Modifier.height(AppSpacing.s))

        content()
    }
}

@Composable
fun GameModeHeader(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.m, vertical = AppSpacing.s),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconChip(
                icon = icon,
                background = MaterialTheme.colorScheme.primaryContainer,
                tint = MaterialTheme.colorScheme.primary,
                size = 40.dp
            )

            Spacer(modifier = Modifier.width(AppSpacing.s))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun AnswerOptionCard(
    number: Int,
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(AppRadius.s),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (enabled) AppElevation.medium else AppElevation.flat)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSpacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth(0.11f)
                    .clip(RoundedCornerShape(AppRadius.s))
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppSpacing.m),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                maxLines = 1
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ImageOptionCard(
    emoji: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    imageRes: Int? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(86.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (enabled) AppElevation.medium else AppElevation.flat)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Text(
                    text = emoji,
                    fontSize = 40.sp
                )
            }
        }
    }
}

@Composable
fun FeedbackCard(
    feedbackMessage: String
) {
    val isCorrect = feedbackMessage.startsWith("Točno")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(AppRadius.m),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) SemanticColors.successContainer else SemanticColors.dangerContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSpacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isCorrect) Icons.Filled.Celebration else Icons.Filled.Lightbulb,
                contentDescription = null,
                tint = if (isCorrect) SemanticColors.successStrong else SemanticColors.dangerStrong,
                modifier = Modifier.padding(end = AppSpacing.s)
            )

            Text(
                text = feedbackMessage,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelLarge,
                color = if (isCorrect) SemanticColors.successText else SemanticColors.dangerText,
                textAlign = TextAlign.Start,
                maxLines = 2
            )
        }
    }
}

@Composable
fun TopGameBar(
    score: Int,
    lives: Int,
    timer: Int,
    timeLimit: Int,
    onBackToMenu: () -> Unit
) {
    val safeLives = lives.coerceAtLeast(0)
    val progress = if (timeLimit > 0) {
        timer.toFloat() / timeLimit.toFloat()
    } else {
        0f
    }.coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.medium)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.m)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameStatPill(
                    title = "Bodovi",
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.xs))
                    Text(
                        text = "$score",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1
                    )
                }

                GameStatPill(
                    title = "Životi",
                    modifier = Modifier.weight(1f)
                ) {
                    LivesIndicator(lives = safeLives)
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$timer s",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (timer <= 5) SemanticColors.dangerContainer else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = onBackToMenu) {
                    Text(
                        text = "Kategorije",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            TimerProgressBar(
                progress = progress,
                isDanger = timer <= 5
            )
        }
    }
}

@Composable
fun LivesIndicator(lives: Int, maxLives: Int = 3) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        for (i in 1..maxLives) {
            Icon(
                imageVector = if (i <= lives) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                tint = if (i <= lives) SemanticColors.dangerContainer else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun GameStatPill(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Card(
        modifier = modifier.height(54.dp),
        shape = RoundedCornerShape(AppRadius.s),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppSpacing.s),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primaryContainer
            )

            Row(verticalAlignment = Alignment.CenterVertically, content = content)
        }
    }
}

@Composable
fun TimerProgressBar(
    progress: Float,
    isDanger: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(AppRadius.pill))
            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(
                    if (isDanger) SemanticColors.dangerContainer else MaterialTheme.colorScheme.tertiary
                )
        )
    }
}

@Composable
fun WordCard(
    title: String,
    word: String,
    subtitle: String? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppRadius.xl),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.medium)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = AppSpacing.l, vertical = AppSpacing.m),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(AppRadius.pill),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = AppSpacing.m, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(AppSpacing.s))

            Text(
                text = word,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            if (subtitle != null) {
                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun GameOverScreen(
    score: Int,
    masteredWords: Int,
    wrongAnswers: Int,
    reviewCount: Int,
    onRestartSameSettings: () -> Unit,
    onBackToMenu: () -> Unit,
    onOpenReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(AppSpacing.l),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.xl),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.medium)
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Celebration,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Text(
                    text = "Kraj igre!",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "$score bodova",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            EndStatCard(
                icon = Icons.Filled.CheckCircle,
                value = "$masteredWords",
                title = "Savladano",
                modifier = Modifier.weight(1f)
            )

            EndStatCard(
                icon = Icons.Filled.Repeat,
                value = "$wrongAnswers",
                title = "Za ponoviti",
                modifier = Modifier.weight(1f)
            )
        }

        if (reviewCount > 0) {
            Spacer(modifier = Modifier.height(AppSpacing.m))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenReview() },
                shape = RoundedCornerShape(AppRadius.l),
                colors = CardDefaults.cardColors(
                    containerColor = SemanticColors.dangerContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
            ) {
                Text(
                    text = "$reviewCount riječi za vježbu — dodirni za ponavljanje",
                    modifier = Modifier.padding(AppSpacing.l),
                    style = MaterialTheme.typography.titleMedium,
                    color = SemanticColors.dangerText,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Button(
            onClick = onRestartSameSettings,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(AppRadius.m),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = "Igraj ponovno",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        TextButton(onClick = onBackToMenu) {
            Text("Početni ekran igre", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun EndStatCard(
    icon: ImageVector,
    value: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(104.dp),
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.s),
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

@Composable
fun EmptyGameStateScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(AppSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconChip(
            icon = Icons.Filled.SearchOff,
            background = MaterialTheme.colorScheme.secondaryContainer,
            tint = MaterialTheme.colorScheme.secondary,
            size = 56.dp
        )

        Spacer(modifier = Modifier.height(AppSpacing.m))

        Text(
            text = "Nema dostupnih pitanja za ovaj odabir.",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Text(
            text = "Probaj drugo narječje ili dodaj još riječi u WordBank.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Button(
            onClick = onBack,
            shape = RoundedCornerShape(AppRadius.m),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Natrag", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
