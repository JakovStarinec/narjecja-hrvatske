# Hrvatska narječja

Android igra za učenje i prepoznavanje hrvatskih narječja. Izrađena kao završni rad na prijediplomskom studiju Računarstvo, FERIT Osijek (2026.): *Mobilna aplikacija za usvajanje i prepoznavanje hrvatskih narječja*.

## Značajke

- Kategorije i krugovi igre s riječima iz različitih narječja
- Pregled napretka i ponavljanje naučenog
- Registracija i prijava korisnika
- Online ljestvica najboljih rezultata
- Postavke i lokalno spremanje napretka
- Vlastita Material 3 tema (boje, tipografija, dimenzije)

## Tehnologije

| Područje | Tehnologija |
| --- | --- |
| Jezik | Kotlin |
| Sučelje | Jetpack Compose, Material Design 3 |
| Autentifikacija | Firebase Authentication |
| Baza | Cloud Firestore |
| Asinkronost | Kotlin Coroutines |
| Lokalna pohrana | SharedPreferences |
| Build | Gradle (Kotlin DSL), Android Studio |

Minimalna verzija: Android 7.0 (API 24) · Ciljana verzija: Android 16 (API 36)

## Pokretanje

1. Otvoriti projekt u Android Studiju.
2. Napraviti Firebase projekt, uključiti Authentication (e-mail/lozinka) i Firestore.
3. Preuzeti `google-services.json` i staviti ga u mapu `app/`.
4. U Firebase konzoli objaviti pravila iz datoteke `firestore.rules`.
5. Pokrenuti aplikaciju na emulatoru ili uređaju.

Autor: Jakov Starinec
