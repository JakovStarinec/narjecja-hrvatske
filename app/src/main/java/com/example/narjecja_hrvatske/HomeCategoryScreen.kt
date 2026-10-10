package com.example.narjecja_hrvatske

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cottage
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.narjecja_hrvatske.ui.theme.AppElevation
import com.example.narjecja_hrvatske.ui.theme.AppRadius
import com.example.narjecja_hrvatske.ui.theme.AppSpacing
import com.example.narjecja_hrvatske.ui.theme.SemanticColors

@Composable
fun PlayHomeScreen(
    userDialect: Dialect?,
    bestScore: Int,
    reviewCount: Int,
    wordCount: Int,
    onOpenSettings: () -> Unit,
    onOpenReview: () -> Unit,
    onTargetSelected: (Dialect) -> Unit
) {
    val availableDialects = userDialect?.let { selected ->
        Dialect.values().filter { it != selected }
    } ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.xxl),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
        ) {
            Column(
                modifier = Modifier.padding(AppSpacing.xxl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconChip(
                    icon = Icons.Filled.Translate,
                    background = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    size = 56.dp
                )

                Spacer(modifier = Modifier.height(AppSpacing.m))

                Text(
                    text = "Narječja Hrvatske",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Text(
                    text = "Uči štokavsko, kajkavsko i čakavsko kroz igru, bodove i ponavljanje.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.l))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.s)
        ) {
            SmallStatCard(
                title = "Najbolje",
                value = "$bestScore",
                icon = Icons.Filled.Star,
                modifier = Modifier.weight(1f)
            )

            SmallStatCard(
                title = "Za vježbu",
                value = "$reviewCount",
                icon = Icons.Filled.Repeat,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenReview() }
            )

            SmallStatCard(
                title = "Riječi",
                value = "$wordCount",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        if (userDialect == null) {
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
                    Text(
                        text = "Prvo odaberi svoje narječje",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.s))

                    Text(
                        text = "To se sada mijenja samo u donjoj navigaciji pod “Odabir”, da početak igre ne bude dosadan svaki put.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.l))

                    Button(
                        onClick = onOpenSettings,
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
                            text = "Otvori odabir narječja",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        } else {
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
                        text = "Tvoje narječje",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = userDialect.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.s))

                    TextButton(onClick = onOpenSettings) {
                        Text("Promijeni u Odabiru", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.l))

            Text(
                text = "Što želiš učiti?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppSpacing.m))

            availableDialects.forEach { dialect ->
                LearningDialectCard(
                    userDialect = userDialect,
                    targetDialect = dialect,
                    onClick = {
                        onTargetSelected(dialect)
                    }
                )

                Spacer(modifier = Modifier.height(AppSpacing.m))
            }

            if (reviewCount > 0) {
                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenReview() },
                    shape = RoundedCornerShape(AppRadius.l),
                    colors = CardDefaults.cardColors(
                        containerColor = SemanticColors.dangerContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.l),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconChip(
                            icon = Icons.Filled.Repeat,
                            background = SemanticColors.dangerStrong.copy(alpha = 0.14f),
                            tint = SemanticColors.dangerStrong,
                            size = 44.dp
                        )

                        Spacer(modifier = Modifier.width(AppSpacing.m))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Imaš $reviewCount riječi za ponavljanje",
                                style = MaterialTheme.typography.titleMedium,
                                color = SemanticColors.dangerText
                            )

                            Text(
                                text = "Dodirni ovdje i ponovi promašene riječi.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconChip(
    icon: ImageVector,
    background: androidx.compose.ui.graphics.Color,
    tint: androidx.compose.ui.graphics.Color,
    size: androidx.compose.ui.unit.Dp = 44.dp
) {
    Box(
        modifier = Modifier
            .size(size)
            .background(background, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Composable
fun SmallStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(112.dp),
        shape = RoundedCornerShape(AppRadius.m),
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
                modifier = Modifier.size(20.dp)
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

private fun iconForDialect(dialect: Dialect): ImageVector = when (dialect) {
    Dialect.STOKAVSKO -> Icons.Filled.Terrain
    Dialect.KAJKAVSKO -> Icons.Filled.Cottage
    Dialect.CAKAVSKO -> Icons.Filled.Waves
}

@Composable
fun LearningDialectCard(
    userDialect: Dialect,
    targetDialect: Dialect,
    onClick: () -> Unit
) {
    val subtitle = "${userDialect.title} ↔ ${targetDialect.title}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconChip(
                icon = iconForDialect(targetDialect),
                background = MaterialTheme.colorScheme.primaryContainer,
                tint = MaterialTheme.colorScheme.primary,
                size = 48.dp
            )

            Spacer(modifier = Modifier.width(AppSpacing.m))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = targetDialect.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Pitanja će se miješati u oba smjera: $subtitle",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategorySelectionScreen(
    userDialect: Dialect,
    targetDialect: Dialect,
    onCategorySelected: (GameCategory) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconChip(
            icon = Icons.Filled.SportsEsports,
            background = MaterialTheme.colorScheme.primaryContainer,
            tint = MaterialTheme.colorScheme.primary,
            size = 56.dp
        )

        Spacer(modifier = Modifier.height(AppSpacing.s))

        Text(
            text = "Odaberi način igre",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Text(
            text = "${userDialect.title} ↔ ${targetDialect.title}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        GameCategory.values().forEach { category ->
            CategoryCard(
                category = category,
                onClick = {
                    onCategorySelected(category)
                }
            )

            Spacer(modifier = Modifier.height(AppSpacing.m))
        }

        Spacer(modifier = Modifier.height(AppSpacing.s))

        TextButton(onClick = onBack) {
            Text("Natrag na početak igre", color = MaterialTheme.colorScheme.primary)
        }
    }
}

private fun iconForCategory(category: GameCategory): ImageVector = when (category) {
    GameCategory.GUESS_MEANING -> Icons.Filled.CheckCircle
    GameCategory.TRANSLATE -> Icons.Filled.Edit
    GameCategory.IMAGE_MATCH -> Icons.Filled.Image
}

private data class CategoryTint(
    val background: androidx.compose.ui.graphics.Color,
    val tint: androidx.compose.ui.graphics.Color
)

@Composable
private fun tintForCategory(category: GameCategory): CategoryTint = when (category) {
    GameCategory.GUESS_MEANING -> CategoryTint(
        background = SemanticColors.successStrong.copy(alpha = 0.14f),
        tint = SemanticColors.successStrong
    )

    GameCategory.TRANSLATE -> CategoryTint(
        background = SemanticColors.goldStrong.copy(alpha = 0.18f),
        tint = SemanticColors.goldText
    )

    GameCategory.IMAGE_MATCH -> CategoryTint(
        background = MaterialTheme.colorScheme.primaryContainer,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun CategoryCard(
    category: GameCategory,
    onClick: () -> Unit
) {
    val tint = tintForCategory(category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(AppRadius.l),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = AppElevation.low)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconChip(
                icon = iconForCategory(category),
                background = tint.background,
                tint = tint.tint,
                size = 48.dp
            )

            Spacer(modifier = Modifier.width(AppSpacing.m))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Osnovni bodovi: ${category.points}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
