package com.example.narjecja_hrvatske

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PlayGameScreen(
    refreshKey: Int,
    onOpenSettings: () -> Unit,
    onOpenReview: () -> Unit,
    onStatsChanged: () -> Unit
) {
    val wordBank = remember { getWordBank().shuffled() }
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    val savedUserDialect = remember(refreshKey) {
        readSavedDialect(preferences)
    }

    val bestScore = remember(refreshKey) {
        getBestScoreForCurrentUser(preferences)
    }

    val reviewCount = remember(refreshKey) {
        getMissedMeanings(preferences).size
    }

    var targetDialect by remember(savedUserDialect) { mutableStateOf<Dialect?>(null) }
    var selectedCategory by remember(savedUserDialect) { mutableStateOf<GameCategory?>(null) }

    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var lives by remember { mutableStateOf(3) }
    var timer by remember { mutableStateOf(20) }
    var gameOver by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var masteredWords by remember { mutableStateOf(setOf<String>()) }
    var wrongAnswers by remember { mutableStateOf(setOf<String>()) }

    var isAnswerLocked by remember { mutableStateOf(false) }
    var shouldMoveToNextQuestion by remember { mutableStateOf(false) }
    var shouldFinishGame by remember { mutableStateOf(false) }

    val questions = remember(savedUserDialect, targetDialect, wordBank) {
        val nativeDialect = savedUserDialect
        val learningDialect = targetDialect

        if (nativeDialect != null && learningDialect != null) {
            wordBank
                .filter { entry ->
                    normalizeAnswer(entry.textForDialect(nativeDialect)) !=
                            normalizeAnswer(entry.textForDialect(learningDialect))
                }
                .flatMap { entry ->
                    listOf(
                        GameQuestion(
                            entry = entry,
                            sourceDialect = nativeDialect,
                            targetDialect = learningDialect
                        ),
                        GameQuestion(
                            entry = entry,
                            sourceDialect = learningDialect,
                            targetDialect = nativeDialect
                        )
                    )
                }
                .shuffled()
        } else {
            emptyList()
        }
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    fun resetGameValues() {
        currentQuestionIndex = 0
        score = 0
        lives = 3
        timer = 20
        gameOver = false
        feedbackMessage = ""
        masteredWords = emptySet()
        wrongAnswers = emptySet()
        isAnswerLocked = false
        shouldMoveToNextQuestion = false
        shouldFinishGame = false
    }

    fun resetToHome() {
        targetDialect = null
        selectedCategory = null
        resetGameValues()
    }

    fun resetToCategoryMenu() {
        selectedCategory = null
        resetGameValues()
    }

    fun restartSameSettings() {
        resetGameValues()
    }

    fun nextQuestion() {
        if (questions.isNotEmpty()) {
            currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
        }

        feedbackMessage = ""
        isAnswerLocked = false
        shouldMoveToNextQuestion = false
        shouldFinishGame = false
    }

    fun handleWrongAnswer(correctAnswerText: String, meaning: String) {
        val newLives = lives - 1
        lives = newLives
        feedbackMessage = "Netočno! Točan odgovor je: $correctAnswerText"
        wrongAnswers = wrongAnswers + meaning
        addMissedMeaning(preferences, meaning)
        onStatsChanged()
        isAnswerLocked = true

        if (newLives <= 0) {
            shouldFinishGame = true
        } else {
            shouldMoveToNextQuestion = true
        }
    }

    fun handleCorrectAnswer(points: Int, meaning: String) {
        score += points + timer
        feedbackMessage = "Točno! Bravo!"
        masteredWords = masteredWords + meaning
        removeMissedMeaning(preferences, meaning)
        onStatsChanged()
        isAnswerLocked = true
        shouldMoveToNextQuestion = true
    }

    LaunchedEffect(selectedCategory, currentQuestionIndex, gameOver) {
        if (selectedCategory != null && !gameOver && currentQuestion != null) {
            timer = 20

            while (timer > 0 && !gameOver && !isAnswerLocked) {
                delay(1000)
                timer--
            }

            if (timer == 0 && !gameOver && !isAnswerLocked) {
                val correctAnswer = currentQuestion.entry.textForDialect(currentQuestion.targetDialect)
                val newLives = lives - 1
                lives = newLives
                feedbackMessage = "Vrijeme je isteklo! Točan odgovor je: $correctAnswer"
                wrongAnswers = wrongAnswers + currentQuestion.entry.meaning
                addMissedMeaning(preferences, currentQuestion.entry.meaning)
                onStatsChanged()
                isAnswerLocked = true

                if (newLives <= 0) {
                    shouldFinishGame = true
                } else {
                    shouldMoveToNextQuestion = true
                }
            }
        }
    }

    LaunchedEffect(shouldMoveToNextQuestion, shouldFinishGame) {
        if (shouldMoveToNextQuestion && !gameOver) {
            delay(1300)
            nextQuestion()
        }

        if (shouldFinishGame && !gameOver) {
            delay(1300)
            gameOver = true
            shouldFinishGame = false
        }
    }

    LaunchedEffect(gameOver) {
        if (gameOver) {
            saveFinishedGameForCurrentUser(
                preferences = preferences,
                score = score,
                masteredWords = masteredWords.size,
                wrongAnswers = wrongAnswers.size
            )

            onStatsChanged()
        }
    }

    when {
        savedUserDialect == null -> {
            PlayHomeScreen(
                userDialect = null,
                bestScore = bestScore,
                reviewCount = reviewCount,
                wordCount = wordBank.size,
                onOpenSettings = onOpenSettings,
                onOpenReview = onOpenReview,
                onTargetSelected = {}
            )
        }

        targetDialect == null -> {
            PlayHomeScreen(
                userDialect = savedUserDialect,
                bestScore = bestScore,
                reviewCount = reviewCount,
                wordCount = wordBank.size,
                onOpenSettings = onOpenSettings,
                onOpenReview = onOpenReview,
                onTargetSelected = { selected ->
                    targetDialect = selected
                }
            )
        }

        selectedCategory == null -> {
            CategorySelectionScreen(
                userDialect = savedUserDialect,
                targetDialect = targetDialect!!,
                onCategorySelected = { category ->
                    selectedCategory = category
                    resetGameValues()
                },
                onBack = {
                    resetToHome()
                }
            )
        }

        gameOver -> {
            GameOverScreen(
                score = score,
                masteredWords = masteredWords.size,
                wrongAnswers = wrongAnswers.size,
                reviewCount = getMissedMeanings(preferences).size,
                onRestartSameSettings = {
                    restartSameSettings()
                },
                onBackToMenu = {
                    resetToHome()
                },
                onOpenReview = onOpenReview
            )
        }

        currentQuestion != null && selectedCategory != null -> {
            when (selectedCategory) {
                GameCategory.GUESS_MEANING -> {
                    GuessMeaningScreen(
                        question = currentQuestion,
                        wordBank = wordBank,
                        score = score,
                        lives = lives,
                        timer = timer,
                        feedbackMessage = feedbackMessage,
                        isAnswerLocked = isAnswerLocked,
                        onAnswerSelected = { selectedAnswer ->
                            if (!isAnswerLocked) {
                                val correctAnswer =
                                    currentQuestion.entry.textForDialect(currentQuestion.targetDialect)

                                if (normalizeAnswer(selectedAnswer) == normalizeAnswer(correctAnswer)) {
                                    handleCorrectAnswer(
                                        points = selectedCategory!!.points,
                                        meaning = currentQuestion.entry.meaning
                                    )
                                } else {
                                    handleWrongAnswer(
                                        correctAnswerText = correctAnswer,
                                        meaning = currentQuestion.entry.meaning
                                    )
                                }
                            }
                        },
                        onBackToMenu = {
                            resetToCategoryMenu()
                        }
                    )
                }

                GameCategory.TRANSLATE -> {
                    TranslateScreen(
                        question = currentQuestion,
                        targetDialect = currentQuestion.targetDialect,
                        score = score,
                        lives = lives,
                        timer = timer,
                        feedbackMessage = feedbackMessage,
                        isAnswerLocked = isAnswerLocked,
                        onSubmitAnswer = { typedAnswer ->
                            if (!isAnswerLocked) {
                                val correctAnswer =
                                    currentQuestion.entry.textForDialect(currentQuestion.targetDialect)

                                if (normalizeAnswer(typedAnswer) == normalizeAnswer(correctAnswer)) {
                                    handleCorrectAnswer(
                                        points = selectedCategory!!.points,
                                        meaning = currentQuestion.entry.meaning
                                    )
                                } else {
                                    handleWrongAnswer(
                                        correctAnswerText = correctAnswer,
                                        meaning = currentQuestion.entry.meaning
                                    )
                                }
                            }
                        },
                        onBackToMenu = {
                            resetToCategoryMenu()
                        }
                    )
                }

                GameCategory.IMAGE_MATCH -> {
                    ImageMatchScreen(
                        question = currentQuestion,
                        wordBank = wordBank,
                        score = score,
                        lives = lives,
                        timer = timer,
                        feedbackMessage = feedbackMessage,
                        isAnswerLocked = isAnswerLocked,
                        onEmojiSelected = { selectedEmoji ->
                            if (!isAnswerLocked) {
                                val correctEmoji = currentQuestion.entry.imageEmoji
                                val correctWord =
                                    currentQuestion.entry.textForDialect(currentQuestion.targetDialect)

                                if (selectedEmoji == correctEmoji) {
                                    handleCorrectAnswer(
                                        points = selectedCategory!!.points,
                                        meaning = currentQuestion.entry.meaning
                                    )
                                } else {
                                    handleWrongAnswer(
                                        correctAnswerText = "$correctEmoji ($correctWord)",
                                        meaning = currentQuestion.entry.meaning
                                    )
                                }
                            }
                        },
                        onBackToMenu = {
                            resetToCategoryMenu()
                        }
                    )
                }

                null -> {}
            }
        }

        else -> {
            EmptyGameStateScreen(
                onBack = {
                    resetToHome()
                }
            )
        }
    }
}

