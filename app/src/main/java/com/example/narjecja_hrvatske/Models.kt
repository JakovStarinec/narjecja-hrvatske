package com.example.narjecja_hrvatske

enum class Dialect(val title: String) {
    STOKAVSKO("Štokavsko"),
    KAJKAVSKO("Kajkavsko"),
    CAKAVSKO("Čakavsko")
}

enum class GameCategory(
    val title: String,
    val description: String,
    val emoji: String,
    val points: Int
) {
    GUESS_MEANING(
        title = "Odaberi prijevod",
        description = "Dobiješ riječ i biraš točan prijevod.",
        emoji = "✅",
        points = 10
    ),
    TRANSLATE(
        title = "Upiši prijevod",
        description = "Sam upisuješ riječ ili izraz.",
        emoji = "✍️",
        points = 15
    ),
    IMAGE_MATCH(
        title = "Poveži sa slikom",
        description = "Pogodi sliku koja odgovara riječi.",
        emoji = "🖼️",
        points = 10
    )
}

enum class AppTab(
    val title: String,
    val emoji: String
) {
    PLAY("Igra", "🎮"),
    REVIEW("Vježba", "🔁"),
    LEADERBOARD("Tablica", "🏆"),
    PROGRESS("Napredak", "📊"),
    SETTINGS("Odabir", "⚙️")
}


data class WordEntry(
    val stokavsko: String,
    val kajkavsko: String,
    val cakavsko: String,
    val meaning: String,
    val imageEmoji: String,
    // Probna zamjena za emoji: ID vektorske ikone (npr. R.drawable.ic_word_kuca).
    // Ostaje null dok svaka riječ ne dobije svoju ikonu - tad se i dalje koristi imageEmoji kao fallback.
    val imageRes: Int? = null
)

fun WordEntry.textForDialect(dialect: Dialect): String {
    return when (dialect) {
        Dialect.STOKAVSKO -> stokavsko
        Dialect.KAJKAVSKO -> kajkavsko
        Dialect.CAKAVSKO -> cakavsko
    }
}

data class GameQuestion(
    val entry: WordEntry,
    val sourceDialect: Dialect,
    val targetDialect: Dialect
)
