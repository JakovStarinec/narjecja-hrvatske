package com.example.narjecja_hrvatske

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.narjecja_hrvatske.ui.theme.AppElevation
import com.example.narjecja_hrvatske.ui.theme.AppRadius
import com.example.narjecja_hrvatske.ui.theme.AppSpacing
import com.example.narjecja_hrvatske.ui.theme.SemanticColors
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    val scope = rememberCoroutineScope()

    var isRegisterMode by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun clearMessage() {
        message = ""
    }

    fun handleLogin() {
        if (isLoading) return
        isLoading = true

        scope.launch {
            loginCloudUser(username = username, password = password)
                .onSuccess {
                    setLocalBestScore(preferences, fetchCloudBestScore())
                    isLoading = false
                    onAuthSuccess()
                }
                .onFailure { error ->
                    isLoading = false
                    message = error.message ?: "Prijava nije uspjela."
                }
        }
    }

    fun handleRegister() {
        when {
            normalizeUsername(username).length < 3 -> {
                message = "Korisničko ime mora imati barem 3 znaka."
            }

            password.length < 6 -> {
                message = "Lozinka mora imati barem 6 znakova."
            }

            password != repeatedPassword -> {
                message = "Lozinke se ne podudaraju."
            }

            else -> {
                if (isLoading) return
                isLoading = true

                scope.launch {
                    registerCloudUser(username = username, password = password)
                        .onSuccess {
                            // Svježi račun uvijek kreće od 0 - spriječi da stari lokalni keš
                            // (npr. od prethodnog korisnika na ovom uređaju) procuri u novi račun.
                            setLocalBestScore(preferences, 0)
                            isLoading = false
                            onAuthSuccess()
                        }
                        .onFailure { error ->
                            isLoading = false
                            message = error.message ?: "Registracija nije uspjela."
                        }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Translate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.m))

                Text(
                    text = "Narječja Hrvatske",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                Text(
                    text = "Prijavi se ili napravi račun da se tvoj rezultat sprema u tablicu.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.xl))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppRadius.xl),
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
                    text = if (isRegisterMode) "Registracija" else "Prijava",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(AppSpacing.l))

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        clearMessage()
                    },
                    label = { Text("Korisničko ime") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isLoading,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        keyboardType = KeyboardType.Text
                    )
                )

                Spacer(modifier = Modifier.height(AppSpacing.s))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        clearMessage()
                    },
                    label = { Text("Lozinka") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !isLoading,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        keyboardType = KeyboardType.Password
                    )
                )

                if (isRegisterMode) {
                    Spacer(modifier = Modifier.height(AppSpacing.s))

                    OutlinedTextField(
                        value = repeatedPassword,
                        onValueChange = {
                            repeatedPassword = it
                            clearMessage()
                        },
                        label = { Text("Ponovi lozinku") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isLoading,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            keyboardType = KeyboardType.Password
                        )
                    )
                }

                if (message.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(AppSpacing.m))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.labelLarge,
                        color = SemanticColors.dangerStrong,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.l))

                Button(
                    onClick = {
                        if (isRegisterMode) {
                            handleRegister()
                        } else {
                            handleLogin()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(AppRadius.m),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isRegisterMode) "Registriraj se" else "Prijavi se",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                TextButton(
                    enabled = !isLoading,
                    onClick = {
                        isRegisterMode = !isRegisterMode
                        message = ""
                        repeatedPassword = ""
                    }
                ) {
                    Text(
                        text = if (isRegisterMode) {
                            "Već imaš račun? Prijavi se"
                        } else {
                            "Nemaš račun? Registriraj se"
                        },
                        color = MaterialTheme.colorScheme.primary
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
            )
        ) {
            Text(
                text = "Račun i tablica rezultata su dijeljeni preko interneta - prijaviš se s istim korisničkim imenom na bilo kojem uređaju.",
                modifier = Modifier.padding(AppSpacing.l),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

