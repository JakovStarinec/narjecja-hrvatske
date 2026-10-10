package com.example.narjecja_hrvatske

/*
    WordBank.kt - veliki upgrade

    Napomena:
    - Ovo je edukativni wordbank za igru, zato su oblici malo pojednostavljeni.
    - Hrvatska narječja imaju puno lokalnih varijanti, posebno čakavsko.
    - Namjerno su izbačene riječi koje se jednako pišu u sva tri narječja.
    - Funkcija hasThreeDifferentDialectForms() dodatno čuva igru od "glupih" pitanja
      gdje bi odgovor bio isti kao prikazana riječ.

    Korišteni tipovi izvora:
    - čakavski / primorsko-dalmatinski rječnici
    - kajkavski rječnici
    - postojeći osnovni wordbank iz projekta
*/

private fun normalizeWordBankText(text: String): String {
    return text
        .trim()
        .lowercase()
        .replace(".", "")
        .replace(",", "")
        .replace("!", "")
        .replace("?", "")
}

private fun WordEntry.hasThreeDifferentDialectForms(): Boolean {
    val forms = listOf(
        normalizeWordBankText(stokavsko),
        normalizeWordBankText(kajkavsko),
        normalizeWordBankText(cakavsko)
    )

    return forms.distinct().size == 3
}

// Ova funkcija ti može kasnije koristiti u MainActivity ako želiš filtrirati pitanja po paru narječja.
fun WordEntry.isDifferentBetween(
    sourceDialect: Dialect,
    targetDialect: Dialect
): Boolean {
    return normalizeWordBankText(textForDialect(sourceDialect)) !=
            normalizeWordBankText(textForDialect(targetDialect))
}

fun getWordBank(): List<WordEntry> {
    val entries = listOf(
        WordEntry("Što", "Kaj", "Ča", "što", "❓"),
        WordEntry("Tko", "Gdo", "Ki", "tko", "👤"),
        WordEntry("Gdje", "Gde", "Di", "gdje", "📍"),
        WordEntry("Zašto", "Zakaj", "Zač", "zašto", "❔"),
        WordEntry("Ovdje", "Tukaj", "Ovdi", "ovdje", "📌"),
        WordEntry("Ondje", "Tam", "Tamo", "ondje", "📍"),
        WordEntry("Onamo", "Tam prek", "Tamo doli", "onamo", "➡️"),
        WordEntry("Dom", "Hiža", "Kuća", "dom / kuća", "🏠", imageRes = R.drawable.ic_word_kuca),
        WordEntry("Prozor", "Oblok", "Poneštra", "prozor", "🪟"),
        WordEntry("Vrata", "Dveri", "Portun", "vrata", "🚪", imageRes = R.drawable.ic_word_vrata),
        WordEntry("Stol", "Astal", "Tavola", "stol", "🪑", imageRes = R.drawable.ic_word_stol),
        WordEntry("Stolica", "Stolec", "Katrega", "stolica", "💺"),
        WordEntry("Krevet", "Postela", "Posteja", "krevet", "🛏️"),
        WordEntry("Jastuk", "Blazina", "Kušin", "jastuk", "🛏️"),
        WordEntry("Ormar", "Kredenec", "Armar", "ormar", "🗄️"),
        WordEntry("Kuhinja", "Kuhnja", "Kužina", "kuhinja", "🍳"),
        WordEntry("Tava", "Ponjglica", "Prsura", "tava", "🍳"),
        WordEntry("Tanjur", "Taler", "Pijat", "tanjur", "🍽️"),
        WordEntry("Vilica", "Ražlje", "Pirun", "vilica", "🍴"),
        WordEntry("Čaša", "Glaž", "Žmul", "čaša", "🥛"),
        WordEntry("Boca", "Flaša", "Botija", "boca", "🍾"),
        WordEntry("Čep", "Štopl", "Tapun", "čep", "🧴"),
        WordEntry("Soba", "Fiža", "Kamera", "soba", "🛋️"),
        WordEntry("Vrt", "Bašča", "Vrtal", "vrt", "🌿"),
        WordEntry("Ulica", "Vulica", "Kala", "ulica", "🏘️"),
        WordEntry("Trg", "Plac", "Pjaca", "trg", "🏛️"),
        WordEntry("Crkva", "Cirkva", "Crikva", "crkva", "⛪"),
        WordEntry("Trgovina", "Štacun", "Butiga", "trgovina", "🏪"),
        WordEntry("Novac", "Penezi", "Šoldi", "novac", "💰"),
        WordEntry("Torba", "Ceker", "Borša", "torba", "👜"),
        WordEntry("Kišobran", "Ambrela", "Lumbrela", "kišobran", "☂️"),
        WordEntry("Vješalica", "Afinger", "Takapan", "vješalica", "🧥"),
        WordEntry("Hlače", "Gače", "Brageše", "hlače", "👖"),
        WordEntry("Cipela", "Cokelj", "Postol", "cipela", "👞"),
        WordEntry("Čarapa", "Fozetlin", "Kalceta", "čarapa", "🧦"),
        WordEntry("Pregača", "Fertuh", "Traversa", "pregača", "🍳"),
        WordEntry("Marama", "Rubec", "Facol", "marama", "🧣"),
        WordEntry("Djevojka", "Dekla", "Divčina", "djevojka", "👧"),
        WordEntry("Dječak", "Dečec", "Mulac", "dječak", "👦"),
        WordEntry("Dijete", "Dete", "Dite", "dijete", "🧒"),
        WordEntry("Otac", "Japa", "Ćaća", "otac", "👨"),
        WordEntry("Majka", "Mamica", "Mati", "majka", "👩"),
        WordEntry("Djed", "Dedek", "Nonot", "djed", "👴"),
        WordEntry("Baka", "Babica", "Nona", "baka", "👵"),
        WordEntry("Prijatelj", "Pajdaš", "Kumpanjon", "prijatelj", "🤝"),
        WordEntry("Čovjek", "Človek", "Čovik", "čovjek", "🧍"),
        WordEntry("Osoba", "Oseba", "Pršona", "osoba", "🧑"),
        WordEntry("Mjesec", "Mesec", "Misec", "mjesec", "🌙"),
        WordEntry("Zvijezda", "Zvezda", "Zvazda", "zvijezda", "⭐", imageRes = R.drawable.ic_word_zvijezda),
        WordEntry("Rijeka", "Reka", "Rika", "rijeka", "🏞️"),
        WordEntry("Cvijet", "Cvet", "Cvit", "cvijet", "🌸", imageRes = R.drawable.ic_word_cvijet),
        WordEntry("Snijeg", "Sneg", "Snig", "snijeg", "❄️"),
        WordEntry("Vjetar", "Veter", "Vitar", "vjetar", "💨"),
        WordEntry("Kiša", "Dežđ", "Daž", "kiša", "🌧️", imageRes = R.drawable.ic_word_kisa),
        WordEntry("Zemlja", "Zemla", "Zemja", "zemlja", "🌍"),
        WordEntry("Drvo", "Drevo", "Drivo", "drvo", "🌳"),
        WordEntry("Munja", "Strela", "Šajeta", "munja", "🌩️"),
        WordEntry("Ptica", "Ftič", "Tica", "ptica", "🐦"),
        WordEntry("Vrabac", "Vrabec", "Jerebac", "vrabac", "🐦"),
        WordEntry("Mačka", "Cicek", "Maška", "mačka", "🐱", imageRes = R.drawable.ic_word_macka),
        WordEntry("Svinja", "Pajcek", "Gudan", "svinja", "🐷"),
        WordEntry("Pijetao", "Pevec", "Kokot", "pijetao", "🐓"),
        WordEntry("Pile", "Picek", "Pišćenac", "pile", "🐥"),
        WordEntry("Magarac", "Osel", "Tovar", "magarac", "🐴"),
        WordEntry("Ovca", "Birka", "Brav", "ovca", "🐑"),
        WordEntry("Gušter", "Ješterica", "Kušćer", "gušter", "🦎"),
        WordEntry("Pčela", "Čmela", "Čela", "pčela", "🐝"),
        WordEntry("Mlijeko", "Mleko", "Mliko", "mlijeko", "🥛"),
        WordEntry("Šećer", "Cukor", "Cukar", "šećer", "🍬"),
        WordEntry("Maslac", "Puter", "Maslo", "maslac", "🧈"),
        WordEntry("Rajčica", "Paradajz", "Pomidor", "rajčica", "🍅"),
        WordEntry("Grah", "Bažul", "Fažol", "grah", "🫘"),
        WordEntry("Grašak", "Grašec", "Biži", "grašak", "🫛"),
        WordEntry("Krastavac", "Vugorek", "Kukumar", "krastavac", "🥒"),
        WordEntry("Mrkva", "Korenje", "Karota", "mrkva", "🥕"),
        WordEntry("Luk", "Čebula", "Kapula", "luk", "🧅"),
        WordEntry("Češnjak", "Česnek", "Česan", "češnjak", "🧄"),
        WordEntry("Juha", "Župa", "Maneštra", "juha", "🍲"),
        WordEntry("Ručak", "Obed", "Obid", "ručak", "🍽️"),
        WordEntry("Doručak", "Fruštuk", "Marenda", "doručak", "🥐"),
        WordEntry("Komad", "Falat", "Bokun", "komad", "🍞"),
        WordEntry("Slanina", "Špek", "Panceta", "slanina", "🥓"),
        WordEntry("Kobasica", "Čurka", "Kunbasica", "kobasica", "🌭"),
        WordEntry("Grožđe", "Grozdje", "Grozje", "grožđe", "🍇", imageRes = R.drawable.ic_word_grozdje),
        WordEntry("Vino s vodom", "Gemišt", "Bevanda", "vino s vodom", "🍷"),
        WordEntry("Čašica", "Štamperlek", "Bićerin", "čašica", "🥃"),
        WordEntry("Raditi", "Delati", "Delat", "raditi", "🛠️"),
        WordEntry("Ići", "Iti", "Greti", "ići", "🚶"),
        WordEntry("Doći", "Dojti", "Prit", "doći", "➡️"),
        WordEntry("Dovesti", "Dopeljati", "Pripejat", "dovesti", "🚗"),
        WordEntry("Govoriti", "Povedati", "Povedat", "govoriti", "🗣️"),
        WordEntry("Gledati", "Gledeti", "Gledat", "gledati", "👀"),
        WordEntry("Spavati", "Spati", "Spat", "spavati", "😴"),
        WordEntry("Voljeti", "Imati rad", "Volit", "voljeti", "❤️"),
        WordEntry("Zaboraviti", "Pozabiti", "Pozabit", "zaboraviti", "🤦"),
        WordEntry("Otvoriti", "Otpreti", "Ofnat", "otvoriti", "🔓"),
        WordEntry("Zatvoriti", "Zapreti", "Zaprit", "zatvoriti", "🔒"),
        WordEntry("Plakati", "Cendrati", "Cvilit", "plakati", "😢"),
        WordEntry("Pričati", "Spominati", "Ćakulat", "pričati", "💬"),
        WordEntry("Plesati", "Tancati", "Tancat", "plesati", "💃"),
        WordEntry("Pjevati", "Popejvati", "Kantat", "pjevati", "🎶"),
        WordEntry("Kuhati", "Kuvati", "Kuhat", "kuhati", "🍳"),
        WordEntry("Prati suđe", "Pomivati", "Pomivat", "prati suđe", "🧽"),
        WordEntry("Trčati", "Trkati", "Trkat", "trčati", "🏃"),
        WordEntry("Tražiti", "Iskati", "Iskat", "tražiti", "🔎"),
        WordEntry("Uzeti", "Zeti", "Uzet", "uzeti", "✋"),
        WordEntry("Baciti", "Hititi", "Hitit", "baciti", "🗑️"),
        WordEntry("Vratiti", "Vrnuti", "Vrnut", "vratiti", "↩️"),
        WordEntry("Reći", "Reči", "Reć", "reći", "🗣️"),
        WordEntry("Bojati", "Farbati", "Piturat", "bojati", "🎨"),
        WordEntry("Lijepo", "Lepo", "Lipo", "lijepo", "✨"),
        WordEntry("Bijelo", "Belo", "Bilo", "bijelo", "⚪"),
        WordEntry("Vrijeme", "Vreme", "Vrime", "vrijeme", "⏰"),
        WordEntry("Malo", "Malčice", "Pinku", "malo", "👌"),
        WordEntry("Polako", "Pomalo", "Destežo", "polako", "🐢"),
        WordEntry("Hladno", "Zima", "Studeno", "hladno", "🥶"),
        WordEntry("Tamno", "Črno", "Škuro", "tamno", "🌑"),
        WordEntry("Prljavo", "Grintavo", "Šporko", "prljavo", "🧼"),
        WordEntry("Gladan", "Gladen", "Lačan", "gladan", "🍽️"),
        WordEntry("Žedan", "Žeđen", "Žejan", "žedan", "💧"),
        WordEntry("Umoran", "Zmoren", "Stufan", "umoran", "😴"),
        WordEntry("Bolestan", "Betežen", "Betežan", "bolestan", "🤒"),
        WordEntry("Sretan", "Vesel", "Kuntenat", "sretan", "😊"),
        WordEntry("Lud", "Bedast", "Manit", "lud", "🤪"),
        WordEntry("Skupo", "Drogo", "Karo", "skupo", "💸"),
        WordEntry("Usta", "Gobec", "Gubica", "usta", "👄"),
        WordEntry("Leđa", "Hrbet", "Škina", "leđa", "🔙"),
        WordEntry("Trbuh", "Želudec", "Štumik", "trbuh", "🫄"),
        WordEntry("Noga", "Cambula", "Gamba", "noga", "🦵"),
        WordEntry("Sat", "Vura", "Ura", "sat", "🕰️", imageRes = R.drawable.ic_word_sat),
        WordEntry("Bicikl", "Beciklin", "Bicikleta", "bicikl", "🚲", imageRes = R.drawable.ic_word_bicikl),
        WordEntry("Čamac", "Čun", "Barka", "čamac", "⛵"),
        WordEntry("Brod", "Ladja", "Vapor", "brod", "🚢"),
        WordEntry("Stepenice", "Štenge", "Škaline", "stepenice", "🪜"),
        WordEntry("Svjetiljka", "Lampa", "Lampadina", "svjetiljka", "💡"),
        WordEntry("Svijeća", "Sveča", "Šterika", "svijeća", "🕯️"),
        WordEntry("Kutija", "Škatlja", "Škatula", "kutija", "📦"),
        WordEntry("Vreća", "Cula", "Športa", "vreća", "🛍️"),
        WordEntry("Ograda", "Plot", "Groja", "ograda", "🚧"),
        WordEntry("Bunar", "Zdenec", "Gustirna", "bunar", "💧"),
        WordEntry("Dimnjak", "Raufank", "Komin", "dimnjak", "🏠"),
        WordEntry("Pepeljara", "Čikobernica", "Čikopelnica", "pepeljara", "🚬"),
        WordEntry("Suknja", "Rokla", "Vuštan", "suknja", "👗"),
        WordEntry("Ručnik", "Rubec", "Šugaman", "ručnik", "🧺"),
        WordEntry("Zavjesa", "Feringa", "Tenda", "zavjesa", "🪟"),
        WordEntry("Idem kući.", "Idem dimo.", "Gren doma.", "idem kući", "🏠"),
        WordEntry("Idem u školu.", "Idem v školu.", "Gren va školu.", "idem u školu", "🏫"),
        WordEntry("Gdje si bio?", "Gde si bil?", "Di si bil?", "gdje si bio", "📍"),
        WordEntry("Što radiš?", "Kaj delaš?", "Ča delaš?", "što radiš", "🛠️"),
        WordEntry("Kako si?", "Kak si?", "Kako gre?", "kako si", "🙂"),
        WordEntry("Volim more.", "Imam rad morje.", "Volin more.", "volim more", "🌊"),
        WordEntry("Danas je lijep dan.", "Denes je lep den.", "Danas je lip dan.", "danas je lijep dan", "☀️"),
        WordEntry("Vidim prozor.", "Vidim oblok.", "Vidin poneštru.", "vidim prozor", "🪟"),
        WordEntry("Imam malo novca.", "Imam malčice penez.", "Iman pinku šoldi.", "imam malo novca", "💰"),
        WordEntry("Otvori vrata.", "Otpri dveri.", "Ofnaj portun.", "otvori vrata", "🚪"),
        WordEntry("Zatvori prozor.", "Zapri oblok.", "Zapri poneštru.", "zatvori prozor", "🪟"),
        WordEntry("Idemo polako.", "Idemo pomalo.", "Gremo destežo.", "idemo polako", "🐢"),
        WordEntry("Pogledaj zvijezdu.", "Poglej zvezdu.", "Poglej zvazdu.", "pogledaj zvijezdu", "⭐"),
        WordEntry("Donesi tanjur.", "Donesi taler.", "Donesi pijat.", "donesi tanjur", "🍽️"),
        WordEntry("Treba mi kišobran.", "Treba mi ambrela.", "Rabi mi lumbrela.", "treba mi kišobran", "☂️"),

        // Novi unosi - obitelj jat refleksa (ije/e/i), isti obrazac kao mlijeko/mleko/mliko
        WordEntry("Cijena", "Cena", "Cina", "cijena", "🏷️"),
        WordEntry("Sijeno", "Seno", "Sino", "sijeno", "🌾"),
        WordEntry("Lijek", "Lek", "Lik", "lijek", "💊"),
        WordEntry("Dijeliti", "Deliti", "Diliti", "dijeliti", "➗"),
        WordEntry("Djeca", "Deca", "Dica", "djeca", "🧒"),
        WordEntry("Sjediti", "Sedeti", "Sidit", "sjediti", "🪑"),
        WordEntry("Vidjeti", "Videti", "Vidit", "vidjeti", "👁️"),
        WordEntry("Htjeti", "Hteti", "Htit", "htjeti", "🙋"),
        WordEntry("Živjeti", "Živeti", "Živit", "živjeti", "🌱"),
        WordEntry("Lijen", "Len", "Lin", "lijen", "🦥"),
        WordEntry("Cijeli", "Celi", "Cili", "cijeli", "⭕"),
        WordEntry("Vrijedan", "Vredan", "Vridan", "vrijedan", "💪"),
        WordEntry("Nedjelja", "Nedelja", "Nedija", "nedjelja", "📅"),
        WordEntry("Dvije", "Dve", "Dvi", "dvije", "2️⃣"),
        WordEntry("Mjesto", "Mesto", "Misto", "mjesto", "🏙️"),
        WordEntry("Svijet", "Svet", "Svit", "svijet", "🌐"),

        // Nove fraze, građene od gornjih i postojećih oblika
        WordEntry("Volim te.", "Ljubim te.", "Volin te.", "volim te", "💕"),
        WordEntry("Hvala lijepa.", "Hvala lepa.", "Hvala lipa.", "hvala lijepa", "🙏"),
        WordEntry("Sjedni ovdje.", "Sedni tukaj.", "Sidni ovdi.", "sjedni ovdje", "🪑")
    )

    return entries.filter { it.hasThreeDifferentDialectForms() }
}
