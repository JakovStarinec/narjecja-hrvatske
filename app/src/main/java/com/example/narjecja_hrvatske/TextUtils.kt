package com.example.narjecja_hrvatske

fun normalizeAnswer(answer: String): String {
    // Napomena: dijakritici (č, ć, š, đ, ž) se namjerno NE uklanjaju - odgovor
    // mora biti otipkan s ispravnim hrvatskim slovima da bi se priznao kao točan.
    return answer
        .trim()
        .lowercase()
        .replace(".", "")
        .replace(",", "")
        .replace("!", "")
        .replace("?", "")
        .replace(":", "")
        .replace(";", "")
        .replace("\"", "")
        .replace("'", "")
        .replace("\\s+".toRegex(), " ")
        .trim()
}

fun String.uppercaseFirstSimple(): String {
    return if (isBlank()) {
        this
    } else {
        substring(0, 1).uppercase() + substring(1)
    }
}
