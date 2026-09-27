package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Base Palette — IT GIRL Aesthetic Notebook
val CreamBackground = Color(0xFFFBF7F2)
val CreamSurface = Color(0xFFFFFCF9)
val GlassCardWhite = Color(0xEBFFFFFF)
val WarmBorder = Color(0xFFEFE6DD)
val SoftShadowColor = Color(0x1F524A45)

// Text Colors — Warm Slate-Brown
val WarmSlateBrown = Color(0xFF524A45)
val MutedCocoa = Color(0xFF857A73)
val SoftTaupe = Color(0xFFB5A9A1)

// Section Pastels
val BlushPink = Color(0xFFF8D7E3)
val BlushPinkDeep = Color(0xFFC87A96)

val SageGreen = Color(0xFFC7D9C0)
val SageGreenDeep = Color(0xFF6B8E61)

val LavenderMist = Color(0xFFE3D4EC)
val LavenderMistDeep = Color(0xFF8C6EA0)

val DustyRose = Color(0xFFE9C6D0)
val DustyRoseDeep = Color(0xFFB06D80)

val ButterYellow = Color(0xFFF5E3B3)
val ButterYellowDeep = Color(0xFFB8923A)

val Periwinkle = Color(0xFFD6E0F0)
val PeriwinkleDeep = Color(0xFF637CA6)

val MintGreen = Color(0xFFC9E4D8)
val MintGreenDeep = Color(0xFF579178)

// The 6 Pastel Color Palettes (لوحة ألوان السمات الستة)
enum class PastelThemePalette(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val descEn: String,
    val descAr: String,
    val background: Color,
    val cardSurface: Color,
    val primaryPastel: Color,
    val secondaryPastel: Color,
    val deepAccent: Color,
    val borderTint: Color
) {
    LAVENDER_HAZE(
        id = "lavender_haze",
        nameEn = "Lavender Haze",
        nameAr = "اللافندر الحالم",
        descEn = "Soft mauve, off-white & pastel purple",
        descAr = "درجات الموف الهادئ، الأوف وايت، والبنفسجي الباستيل",
        background = Color(0xFFF9F6FC),
        cardSurface = Color(0xF2FFFFFF),
        primaryPastel = Color(0xFFE3D4EC),
        secondaryPastel = Color(0xFFF1E8F7),
        deepAccent = Color(0xFF8A65A8),
        borderTint = Color(0xFFE5D8EE)
    ),
    ROSE_WATER(
        id = "rose_water",
        nameEn = "Rose Water",
        nameAr = "ماء الورد",
        descEn = "Blush pink, creamy white & rose gold",
        descAr = "درجات الوردي الفاتح، الأبيض الكريمي، والذهبي الوردي",
        background = Color(0xFFFFF8FA),
        cardSurface = Color(0xF2FFFFFF),
        primaryPastel = Color(0xFFF8D7E3),
        secondaryPastel = Color(0xFFFCE9F0),
        deepAccent = Color(0xFFC4748E),
        borderTint = Color(0xFFF3D5E0)
    ),
    LEMON_CHIFFON(
        id = "lemon_chiffon",
        nameEn = "Lemon Chiffon",
        nameAr = "شيفون الليمون",
        descEn = "Soft pastel yellow, ivory & warm gray",
        descAr = "الأصفر الباستيل الناعم، العاجي، والرمادي الدافئ",
        background = Color(0xFFFFFDF5),
        cardSurface = Color(0xF2FFFFFF),
        primaryPastel = Color(0xFFF5E3B3),
        secondaryPastel = Color(0xFFFAF0D1),
        deepAccent = Color(0xFFB8923A),
        borderTint = Color(0xFFEDE0BD)
    ),
    MATCHA_DREAM(
        id = "matcha_dream",
        nameEn = "Matcha Dream",
        nameAr = "حلم الماتشا",
        descEn = "Sage green, warm beige & pure white",
        descAr = "الأخضر المريمية، البيج، والأبيض النقي",
        background = Color(0xFFF6F9F4),
        cardSurface = Color(0xF2FFFFFF),
        primaryPastel = Color(0xFFC7D9C0),
        secondaryPastel = Color(0xFFE2EDE0),
        deepAccent = Color(0xFF628758),
        borderTint = Color(0xFFD2E0CC)
    ),
    VANILLA_OAT(
        id = "vanilla_oat",
        nameEn = "Vanilla Oat",
        nameAr = "فانيليا الشوفان",
        descEn = "Ivory, soft taupe & warm cream",
        descAr = "العاجي، التوب، والكريمي الدافئ",
        background = Color(0xFFFBF7F2),
        cardSurface = Color(0xF2FFFCF9),
        primaryPastel = Color(0xFFE8DCCB),
        secondaryPastel = Color(0xFFF3ECE1),
        deepAccent = Color(0xFF8C7762),
        borderTint = Color(0xFFE6DBD0)
    ),
    CLOUD_BLUE(
        id = "cloud_blue",
        nameEn = "Cloud Blue",
        nameAr = "أزرق الغيم",
        descEn = "Very light sky blue, white & soft gray",
        descAr = "الأزرق السماوي الفاتح جداً، الأبيض، والرمادي الفاتح",
        background = Color(0xFFF5F9FD),
        cardSurface = Color(0xF2FFFFFF),
        primaryPastel = Color(0xFFD6E5F5),
        secondaryPastel = Color(0xFFE8F1FA),
        deepAccent = Color(0xFF5D82A8),
        borderTint = Color(0xFFD5E3F0)
    );

    fun displayName(isArabic: Boolean): String = if (isArabic) nameAr else nameEn
    fun displayDesc(isArabic: Boolean): String = if (isArabic) descAr else descEn

    companion object {
        fun fromId(id: String?): PastelThemePalette =
            entries.find { it.id == id } ?: ROSE_WATER
    }
}
