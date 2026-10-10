package com.example.narjecja_hrvatske

import android.content.SharedPreferences

const val PREFS_NAME = "narjecja_hrvatske_prefs"
const val KEY_USER_DIALECT = "user_dialect"
const val KEY_BEST_SCORE = "best_score"
const val KEY_LAST_SCORE = "last_score"
const val KEY_LAST_MASTERED_WORDS = "last_mastered_words"
const val KEY_LAST_WRONG_ANSWERS = "last_wrong_answers"
const val KEY_GAMES_PLAYED = "games_played"
const val KEY_MISSED_WORD_MEANINGS = "missed_word_meanings"

data class LeaderboardEntry(
    val username: String,
    val bestScore: Int,
    val isCurrentUser: Boolean
)

fun readSavedDialect(preferences: SharedPreferences): Dialect? {
    return preferences.getString(KEY_USER_DIALECT, null)?.let { savedValue ->
        runCatching {
            Dialect.valueOf(savedValue)
        }.getOrNull()
    }
}

fun getMissedMeanings(preferences: SharedPreferences): Set<String> {
    return preferences.getStringSet(KEY_MISSED_WORD_MEANINGS, emptySet()) ?: emptySet()
}

fun saveMissedMeanings(
    preferences: SharedPreferences,
    meanings: Set<String>
) {
    preferences.edit()
        .putStringSet(KEY_MISSED_WORD_MEANINGS, meanings.toMutableSet())
        .apply()
}

fun addMissedMeaning(
    preferences: SharedPreferences,
    meaning: String
) {
    saveMissedMeanings(preferences, getMissedMeanings(preferences) + meaning)
}

fun removeMissedMeaning(
    preferences: SharedPreferences,
    meaning: String
) {
    saveMissedMeanings(preferences, getMissedMeanings(preferences) - meaning)
}

fun normalizeUsername(username: String): String {
    return username
        .trim()
        .lowercase()
        .replace("\\s+".toRegex(), "_")
}

/**
 * Najbolji rezultat prijavljenog korisnika, lokalno keširan na ovom uređaju radi trenutnog
 * prikaza. Izvor istine za tablicu (svi korisnici, svi uređaji) je Firestore -
 * vidi [fetchCloudLeaderboard] i [submitScoreToCloud] u FirebaseBackend.kt.
 */
fun getBestScoreForCurrentUser(preferences: SharedPreferences): Int {
    return preferences.getInt(KEY_BEST_SCORE, 0)
}

/** Upiši lokalni keš najboljeg rezultata nakon prijave/registracije (npr. na drugom uređaju). */
fun setLocalBestScore(preferences: SharedPreferences, bestScore: Int) {
    preferences.edit().putInt(KEY_BEST_SCORE, bestScore).apply()
}

suspend fun saveFinishedGameForCurrentUser(
    preferences: SharedPreferences,
    score: Int,
    masteredWords: Int,
    wrongAnswers: Int
) {
    val oldBestScore = preferences.getInt(KEY_BEST_SCORE, 0)
    val gamesPlayedBefore = preferences.getInt(KEY_GAMES_PLAYED, 0)

    preferences.edit()
        .putInt(KEY_LAST_SCORE, score)
        .putInt(KEY_LAST_MASTERED_WORDS, masteredWords)
        .putInt(KEY_LAST_WRONG_ANSWERS, wrongAnswers)
        .putInt(KEY_GAMES_PLAYED, gamesPlayedBefore + 1)
        .putInt(KEY_BEST_SCORE, maxOf(oldBestScore, score))
        .apply()

    submitScoreToCloud(score)
}
