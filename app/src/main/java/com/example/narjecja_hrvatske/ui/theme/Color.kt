package com.example.narjecja_hrvatske.ui.theme

import androidx.compose.ui.graphics.Color

/*
    "Zagorje" - jedinstvena paleta cijele aplikacije.
    Sve boje na ekranima trebaju dolaziti odavde (ili iz MaterialTheme.colorScheme
    koji se puni ovim vrijednostima u Theme.kt), umjesto pojedinačnih Color(0xFF...)
    razbacanih po svakom ekranu.
*/

// Rust / terakota - primarna boja brenda
val Rust10 = Color(0xFFFBEFE6)
val Rust20 = Color(0xFFF2E3CE)
val Rust40 = Color(0xFFC97A4A)
val Rust50 = Color(0xFFA6532E)
val Rust60 = Color(0xFF8A4526)
val Rust80 = Color(0xFF4A2A18)

// Espresso - tekst i najtamnije površine
val Espresso90 = Color(0xFF3B2A20)
val Espresso70 = Color(0xFF6B4F3D)

// Taupe - sekundarni tekst / ikone
val Taupe50 = Color(0xFF8A7355)
val Taupe30 = Color(0xFFC7A882)
val Taupe20 = Color(0xFFE8DFD0)

// Krem - pozadine
val Cream10 = Color(0xFFFCF9F3)
val Cream20 = Color(0xFFF7F2E9)
val Cream30 = Color(0xFFF2E9DA)

// Zlatna - istaknuti detalji, nagrade
val Gold40 = Color(0xFFD9A441)
val Gold20 = Color(0xFFF6E4BB)
val Gold80 = Color(0xFF5C4110)

// Semantički tonovi (uspjeh / greška), ugođeni u top paletu
val Success10 = Color(0xFFE7F3E1)
val Success50 = Color(0xFF3B7A3B)
val Success80 = Color(0xFF1F4A1F)

val Danger10 = Color(0xFFFBEAE5)
val Danger50 = Color(0xFFC1503D)
val Danger80 = Color(0xFF7A2E1F)

// Tamna varijanta (za sustavski dark mode, pripremljeno za budućnost)
val DarkBackground = Color(0xFF1E1712)
val DarkSurface = Color(0xFF29201A)
val DarkSurfaceVariant = Color(0xFF362A22)
val RustDark80 = Color(0xFFE8B08C)

/**
 * Semantički tonovi koji ne pripadaju standardnim Material3 ulogama
 * (uspjeh/greška/zlato), na jednom mjestu umjesto raštrkanih Color(0xFF...).
 */
object SemanticColors {
    val successText = Success80
    val successContainer = Success10
    val successStrong = Success50

    val dangerText = Danger80
    val dangerContainer = Danger10
    val dangerStrong = Danger50

    val goldText = Gold80
    val goldContainer = Gold20
    val goldStrong = Gold40
}
