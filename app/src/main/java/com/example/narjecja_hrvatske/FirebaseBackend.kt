package com.example.narjecja_hrvatske

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/*
    FirebaseBackend.kt

    Prava, dijeljena prijava i tablica rezultata preko Firebasea (zamjena za staru
    lokalnu prijavu koja je čuvala korisnike i lozinke samo u SharedPreferences
    jednog uređaja):
    - Firebase Authentication čuva korisnički račun i lozinku - lozinka nikad ne
      prolazi kroz naš kod niti se sprema na uređaj.
    - Firestore kolekcija "leaderboard" drži javno vidljivu tablicu najboljih
      rezultata, po jedan dokument po korisniku (ID dokumenta = Firebase UID).

    Napomena o korisničkom imenu: Firebase Auth email/password prijava traži e-mail
    format, ali app i dalje traži samo "korisničko ime" da ne kompliciramo UI. Zato
    svako korisničko ime interno postane "ime@narjecjahrvatske.app" - ta domena se
    nigdje stvarno ne koristi za slanje maila, služi samo kao jedinstveni identifikator.
*/

private const val FAKE_EMAIL_DOMAIN = "narjecjahrvatske.app"
private const val LEADERBOARD_COLLECTION = "leaderboard"
private const val FIELD_USERNAME = "username"
private const val FIELD_BEST_SCORE = "bestScore"

private fun usernameToEmail(username: String): String {
    return normalizeUsername(username) + "@" + FAKE_EMAIL_DOMAIN
}

fun currentCloudUsername(): String? {
    val user = Firebase.auth.currentUser ?: return null
    return user.displayName ?: user.email?.substringBefore("@")
}

suspend fun registerCloudUser(username: String, password: String): Result<Unit> {
    val cleanUsername = normalizeUsername(username)

    return try {
        val authResult = Firebase.auth
            .createUserWithEmailAndPassword(usernameToEmail(cleanUsername), password)
            .await()

        val user = authResult.user
            ?: return Result.failure(Exception("Registracija nije uspjela."))

        user.updateProfile(
            userProfileChangeRequest { displayName = cleanUsername }
        ).await()

        Firebase.firestore
            .collection(LEADERBOARD_COLLECTION)
            .document(user.uid)
            .set(mapOf(FIELD_USERNAME to cleanUsername, FIELD_BEST_SCORE to 0))
            .await()

        Result.success(Unit)
    } catch (e: FirebaseAuthUserCollisionException) {
        Result.failure(Exception("Korisničko ime već postoji."))
    } catch (e: FirebaseAuthWeakPasswordException) {
        Result.failure(Exception("Lozinka mora imati barem 6 znakova."))
    } catch (e: Exception) {
        Result.failure(Exception("Registracija nije uspjela. Provjeri internetsku vezu."))
    }
}

suspend fun loginCloudUser(username: String, password: String): Result<Unit> {
    return try {
        Firebase.auth
            .signInWithEmailAndPassword(usernameToEmail(username), password)
            .await()

        Result.success(Unit)
    } catch (e: FirebaseAuthInvalidUserException) {
        Result.failure(Exception("Neispravno korisničko ime ili lozinka."))
    } catch (e: FirebaseAuthInvalidCredentialsException) {
        Result.failure(Exception("Neispravno korisničko ime ili lozinka."))
    } catch (e: Exception) {
        Result.failure(Exception("Prijava nije uspjela. Provjeri internetsku vezu."))
    }
}

fun logoutCloudUser() {
    Firebase.auth.signOut()
}

/** Povuci najbolji rezultat prijavljenog korisnika iz Firestorea (npr. odmah nakon prijave). */
suspend fun fetchCloudBestScore(): Int {
    val uid = Firebase.auth.currentUser?.uid ?: return 0

    return try {
        val snapshot = Firebase.firestore
            .collection(LEADERBOARD_COLLECTION)
            .document(uid)
            .get()
            .await()

        snapshot.getLong(FIELD_BEST_SCORE)?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

/** Pošalji novi rezultat u tablicu - sprema se samo ako je bolji od dosadašnjeg. */
suspend fun submitScoreToCloud(score: Int) {
    val user = Firebase.auth.currentUser ?: return
    val docRef = Firebase.firestore.collection(LEADERBOARD_COLLECTION).document(user.uid)

    try {
        val existingBest = docRef.get().await().getLong(FIELD_BEST_SCORE)?.toInt() ?: 0
        val newBest = maxOf(existingBest, score)
        val username = user.displayName ?: normalizeUsername(user.email.orEmpty().substringBefore("@"))

        docRef.set(mapOf(FIELD_USERNAME to username, FIELD_BEST_SCORE to newBest)).await()
    } catch (e: Exception) {
        // Bez interneta rezultat ostaje spremljen samo lokalno - poslat će se
        // sljedeći put kad korisnik završi igru uz uspješnu vezu.
    }
}

/**
 * Baca iznimku ako dohvat ne uspije (npr. nema interneta) - pozivatelj (UI) treba
 * razlikovati "tablica je stvarno prazna" od "dohvat nije uspio" pa ne gutamo grešku ovdje.
 */
suspend fun fetchCloudLeaderboard(limit: Long = 50): List<LeaderboardEntry> {
    val currentUid = Firebase.auth.currentUser?.uid

    val snapshot = Firebase.firestore
        .collection(LEADERBOARD_COLLECTION)
        .orderBy(FIELD_BEST_SCORE, Query.Direction.DESCENDING)
        .limit(limit)
        .get()
        .await()

    return snapshot.documents.mapNotNull { doc ->
        val username = doc.getString(FIELD_USERNAME) ?: return@mapNotNull null
        val bestScore = doc.getLong(FIELD_BEST_SCORE)?.toInt() ?: 0

        LeaderboardEntry(
            username = username,
            bestScore = bestScore,
            isCurrentUser = doc.id == currentUid
        )
    }
}
