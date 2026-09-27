package com.example.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ui.theme.BlushPink
import com.example.ui.theme.BlushPinkDeep
import com.example.ui.theme.ButterYellow
import com.example.ui.theme.ButterYellowDeep
import com.example.ui.theme.DustyRose
import com.example.ui.theme.DustyRoseDeep
import com.example.ui.theme.LavenderMist
import com.example.ui.theme.LavenderMistDeep
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MintGreenDeep
import com.example.ui.theme.Periwinkle
import com.example.ui.theme.PeriwinkleDeep
import com.example.ui.theme.SageGreen
import com.example.ui.theme.SageGreenDeep

enum class AppLanguage(val code: String, val label: String) {
    EN("en", "EN"),
    AR("ar", "AR");

    val isArabic: Boolean get() = this == AR
}

enum class QuickAddMode(
    val titleEn: String,
    val titleAr: String,
    val descEn: String,
    val descAr: String
) {
    ADD_TEXT(
        titleEn = "Add Text",
        titleAr = "إضافة نص",
        descEn = "Create a note, product card, study tip, or gratitude thought",
        descAr = "إضافة بطاقة نصية، منتج، ملاحظة مذاكرة، أو تدوينة امتنان"
    ),
    ADD_LIST(
        titleEn = "Add List",
        titleAr = "إضافة قائمة",
        descEn = "Create numbered steps, DIY recipes, or Kanban checklist items",
        descAr = "إضافة خطوات متسلسلة، مقادير وصفة منزلية، أو قائمة مشتريات"
    ),
    UPLOAD_MEDIA(
        titleEn = "Upload Photo / Video",
        titleAr = "رفع صورة/فيديو",
        descEn = "Convert & save local media (Base64) for offline airplane mode",
        descAr = "تحويل وحفظ الصورة محلياً (Base64) للعمل دون اتصال في وضع الطيران"
    );

    fun title(isArabic: Boolean): String = if (isArabic) titleAr else titleEn
    fun desc(isArabic: Boolean): String = if (isArabic) descAr else descEn
}

enum class DailyMoodOption(
    val id: String,
    val emoji: String,
    val labelEn: String,
    val labelAr: String
) {
    RADIANT("radiant", "✨", "Radiant", "متألقة"),
    LOVED("loved", "🥰", "Grateful", "ممتنة"),
    BLOSSOM("blossom", "🌸", "Soft & Calm", "هادئة"),
    MATCHA("matcha", "🍵", "Focused", "مركزة"),
    COZY("cozy", "😌", "Peaceful", "مطمئنة"),
    SLEEPY("sleepy", "🌙", "Resting", "تحتاج راحة");

    fun label(isArabic: Boolean): String = if (isArabic) labelAr else labelEn

    companion object {
        fun fromId(id: String?): DailyMoodOption =
            entries.find { it.id == id } ?: RADIANT
    }
}

data class DailyQuoteItem(
    val quoteAr: String,
    val quoteEn: String,
    val authorAr: String,
    val authorEn: String
) {
    fun quote(isArabic: Boolean): String = if (isArabic) quoteAr else quoteEn
    fun author(isArabic: Boolean): String = if (isArabic) authorAr else authorEn
}

val DefaultDailyQuotes = listOf(
    DailyQuoteItem(
        quoteAr = "«بيتي جنتي بذكاء لا بجهد»",
        quoteEn = "“My home is my sanctuary — curated with grace, not stress.”",
        authorAr = "همسة اليوم ✨",
        authorEn = "Daily Whisper ✨"
    ),
    DailyQuoteItem(
        quoteAr = "«أنا هو أنا، وأنت هو أنت — سلامي الداخلي أولاً»",
        quoteEn = "“I am me, and you are you — protecting my peace & glow.”",
        authorAr = "عقلية وونيونغ 🌸",
        authorEn = "Wonyoungism Mindset 🌸"
    ),
    DailyQuoteItem(
        quoteAr = "«بيت مرتب، وقت مبارك، وبال مرتاح»",
        quoteEn = "“An organized space, blessed time, and a peaceful mind.”",
        authorAr = "الإدارة الذكية 🍵",
        authorEn = "Mindful Living 🍵"
    ),
    DailyQuoteItem(
        quoteAr = "«الخطوات الصغيرة الهادئة تصنع أجمل التغييرات»",
        quoteEn = "“Soft, gentle consistency builds the dreamiest life.”",
        authorAr = "طاقة الأنوثة 🎀",
        authorEn = "Feminine Glow 🎀"
    )
)

data class SectionCategory(
    val key: String,
    val titleEn: String,
    val titleAr: String
) {
    fun title(isArabic: Boolean): String = if (isArabic) titleAr else titleEn
}

enum class PlannerSection(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val shortEn: String,
    val shortAr: String,
    val subtitleEn: String,
    val subtitleAr: String,
    val pastelColor: Color,
    val deepAccentColor: Color,
    val categories: List<SectionCategory>,
    val supportsPhotoUpload: Boolean = true
) {
    HOME_DASHBOARD(
        id = "home_dashboard",
        titleEn = "Home Dashboard",
        titleAr = "لوحة التحكم الرئيسية",
        shortEn = "Home",
        shortAr = "الرئيسية",
        subtitleEn = "Your automated daily overview: mood, hydration, brain dump & top 3 priorities",
        subtitleAr = "نظرة شاملة وهادئة ليومكِ: المزاج، شرب الماء، التفريغ الذهني وأهم 3 مهام",
        pastelColor = BlushPink,
        deepAccentColor = BlushPinkDeep,
        categories = listOf(
            SectionCategory("top_priority", "Top 3 Priorities", "المهام العاجلة (3 فقط)")
        ),
        supportsPhotoUpload = true
    ),
    SKINCARE_GLOW(
        id = "skincare_glow",
        titleEn = "Skincare & Glow-Up Care",
        titleAr = "العناية بالبشرة والجمال",
        shortEn = "Skincare",
        shortAr = "البشرة",
        subtitleEn = "Glass Skin AM/PM routine, K-Beauty product library, SPF 50+ & DIY smart beauty",
        subtitleAr = "روتين البشرة الزجاجية، مكتبة المنتجات الكورية، واقي الشمس، وجمال بميزانية ذكية",
        pastelColor = SageGreen,
        deepAccentColor = SageGreenDeep,
        categories = listOf(
            SectionCategory("hydration_routine", "AM/PM Glass Skin & Product Library", "روتين البشرة الزجاجية ومكتبة المنتجات"),
            SectionCategory("natural_recipes", "Face Yoga, Clean Makeup & DIY Beauty", "يوغا الوجه، المكياج الطبيعي، ووصفات منزلية"),
            SectionCategory("sun_care", "Sun Care SPF 50+ Tracker", "حماية الشمس وتجديد SPF 50+")
        ),
        supportsPhotoUpload = true
    ),
    OUTFITS_HIJAB(
        id = "outfits_hijab",
        titleEn = "Modest Elegance & Hijab",
        titleAr = "الأناقة والأزياء والحجاب",
        shortEn = "Wardrobe",
        shortAr = "الأزياء",
        subtitleEn = "Pinterest masonry grid, Capsule Wardrobe, Hijab fabrics, Color Theory & Emergency Uniform",
        subtitleAr = "شبكة Pinterest، الخزانة الكبسولية، دليل أقمشة الحجاب، نظرية الألوان، وزي الطوارئ",
        pastelColor = DustyRose,
        deepAccentColor = DustyRoseDeep,
        categories = listOf(
            SectionCategory("everyday_look", "Capsule Wardrobe & Pastel Looks", "الخزانة الكبسولية وإطلالات الباستيل"),
            SectionCategory("occasion_look", "Hijab Fabrics & Monochromatic Guide", "دليل أقمشة الحجاب والدمج الأحادي"),
            SectionCategory("emergency_prep", "Emergency Uniform & Night Prep Kit", "زي الطوارئ والتحضير الليلي وحيل الأناقة")
        ),
        supportsPhotoUpload = true
    ),
    BODY_CARE(
        id = "body_care",
        titleEn = "Body Care & Everything Shower",
        titleAr = "العناية بالجسم والاستحمام الشامل",
        shortEn = "Body Care",
        shortAr = "الجسم",
        subtitleEn = "Accordion rituals for Dry Brushing, Everything Shower, Coffee Scrub & Scalp/Nail care",
        subtitleAr = "قوائم قابلة للطي للتقشير الجاف، الاستحمام العميق، مقشر القهوة، وعناية الشعر والأظافر",
        pastelColor = LavenderMist,
        deepAccentColor = LavenderMistDeep,
        categories = listOf(
            SectionCategory("shower_routine", "Pre-Shower & Everything Shower", "التحضير وروتين الاستحمام العميق"),
            SectionCategory("body_scrubs", "Coffee Scrub, Scalp & Targeted DIY Care", "مقشر القهوة، فروة الرأس، وعناية الأظافر والشفاه")
        ),
        supportsPhotoUpload = true
    ),
    FORGET_ME_NOT(
        id = "forget_me_not",
        titleEn = "Forget-Me-Not, Budget & Space",
        titleAr = "لا تنسي، الميزانية، والمحيط",
        shortEn = "Lists",
        shortAr = "لا تنسي",
        subtitleEn = "Kanban board with Bell alerts + Smart Budget Planner, Deep Cleaning & Interior Vibes",
        subtitleAr = "بطاقات لوحية (Kanban) بجرس التنبيه + الميزانية الذكية، التنظيف العميق، والتصميم الداخلي",
        pastelColor = BlushPink,
        deepAccentColor = BlushPinkDeep,
        categories = listOf(
            SectionCategory("pharmacy", "Pharmacy", "مشتريات الصيدلية"),
            SectionCategory("supermarket", "Supermarket", "السوبرماركت"),
            SectionCategory("clothing", "Clothing", "ملابس"),
            SectionCategory("budget_space", "Smart Budget, Cleaning & Interior", "الميزانية الذكية، التنظيف، وديكور المساحة")
        ),
        supportsPhotoUpload = true
    ),
    HABIT_TRACKER(
        id = "habit_tracker",
        titleEn = "Habits, Study & Time Blocking",
        titleAr = "العادات، المذاكرة، وتقسيم الوقت",
        shortEn = "Habits",
        shortAr = "العادات",
        subtitleEn = "Interactive weekly habit table + Smart Studying, Visual Learning & Time Blocking",
        subtitleAr = "جدول العادات الأسبوعي التفاعلي + المذاكرة الذكية، التعلم البصري، وتقسيم الوقت",
        pastelColor = ButterYellow,
        deepAccentColor = ButterYellowDeep,
        categories = listOf(
            SectionCategory("daily_ritual", "Weekly Habits", "العادات الأسبوعية"),
            SectionCategory("time_study", "Time Blocking & Smart Studying", "تقسيم الوقت والمذاكرة الذكية")
        ),
        supportsPhotoUpload = false
    ),
    FUTURE_YOU(
        id = "future_you",
        titleEn = "Vision Board & Wonyoungism",
        titleAr = "لوحة الإلهام وعقلية وونيونغ",
        shortEn = "Vision",
        shortAr = "الإلهام",
        subtitleEn = "Free-form drag & drop wall canvas, Wonyoungism affirmations, Shadow Work & Gratitude",
        subtitleAr = "مساحة حرة (سحب وإفلات)، توكيدات وونيونغ، تمارين الظل، فلترة العلاقات، ويوميات الامتنان",
        pastelColor = Periwinkle,
        deepAccentColor = PeriwinkleDeep,
        categories = listOf(
            SectionCategory("vision_board", "Free-form Vision Canvas", "لوحة الإلهام الحرة"),
            SectionCategory("wonyoung_mindset", "Wonyoungism, Shadow Work & Boundaries", "عقلية وونيونغ، تمارين الظل، والحدود الاجتماعية")
        ),
        supportsPhotoUpload = true
    ),
    FITNESS_NUTRITION(
        id = "fitness_nutrition",
        titleEn = "Wellness, Nutrition & Sleep",
        titleAr = "اللياقة، التغذية، وسجل العافية",
        shortEn = "Wellness",
        shortAr = "العافية",
        subtitleEn = "Water-rich glow meals, low-sugar habits, Pilates, 7-8h Sleep log & Emergency Cycle Kit",
        subtitleAr = "الأطعمة الغنية بالماء، تقليل السكر، تمارين البيلاتس، سجل النوم 7-8 ساعات، وحقيبة الطوارئ",
        pastelColor = MintGreen,
        deepAccentColor = MintGreenDeep,
        categories = listOf(
            SectionCategory("meals", "Mindful Meals & Hydration Foods", "وجبات النضارة والأطعمة الغنية بالماء"),
            SectionCategory("workouts", "Pilates, Sleep Log & Cycle Emergency Kit", "البيلاتس، سجل النوم، وحقيبة الطوارئ الصحية")
        ),
        supportsPhotoUpload = true
    );

    fun title(isArabic: Boolean): String = if (isArabic) titleAr else titleEn
    fun shortTitle(isArabic: Boolean): String = if (isArabic) shortAr else shortEn
    fun subtitle(isArabic: Boolean): String = if (isArabic) subtitleAr else subtitleEn
}

@Entity(tableName = "planner_cards")
data class PlannerCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sectionId: String,
    val categoryKey: String,
    val titleEn: String,
    val titleAr: String,
    val subtitleEn: String = "",
    val subtitleAr: String = "",
    val badgeEn: String = "",
    val badgeAr: String = "",
    val isChecked: Boolean = false,
    val imageBase64: String? = null,
    val presetDrawableName: String? = null,
    val aspectRatio: Float = 0.8f,
    val bellEnabled: Boolean = false,
    val bellTime: String = "",
    val offsetX: Float = 0f,
    val offsetY: Float = 0f,
    val mediaType: String = "text",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun displayTitle(isArabic: Boolean): String =
        if (isArabic) titleAr.ifBlank { titleEn } else titleEn.ifBlank { titleAr }

    fun displaySubtitle(isArabic: Boolean): String =
        if (isArabic) subtitleAr.ifBlank { subtitleEn } else subtitleEn.ifBlank { subtitleAr }

    fun displayBadge(isArabic: Boolean): String =
        if (isArabic) badgeAr.ifBlank { badgeEn } else badgeEn.ifBlank { badgeAr }
}

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameEn: String,
    val nameAr: String,
    val targetEn: String = "Daily",
    val targetAr: String = "يومياً",
    val mon: Boolean = false,
    val tue: Boolean = false,
    val wed: Boolean = false,
    val thu: Boolean = false,
    val fri: Boolean = false,
    val sat: Boolean = false,
    val sun: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun displayName(isArabic: Boolean): String =
        if (isArabic) nameAr.ifBlank { nameEn } else nameEn.ifBlank { nameAr }

    fun displayTarget(isArabic: Boolean): String =
        if (isArabic) targetAr.ifBlank { targetEn } else targetEn.ifBlank { targetAr }

    fun daysList(): List<Boolean> = listOf(mon, tue, wed, thu, fri, sat, sun)

    fun completedCount(): Int = daysList().count { it }
}

@Entity(tableName = "note_blocks")
data class NoteBlockEntity(
    @PrimaryKey val noteKey: String,
    val contentEn: String,
    val contentAr: String,
    val updatedAt: Long = System.currentTimeMillis()
)

data class SimulatedNotification(
    val id: Long = System.currentTimeMillis(),
    val cardId: Int,
    val titleEn: String,
    val titleAr: String,
    val scheduledTime: String,
    val categoryEn: String,
    val categoryAr: String,
    val isDelivered: Boolean = true
) {
    fun title(isArabic: Boolean): String = if (isArabic) titleAr.ifBlank { titleEn } else titleEn.ifBlank { titleAr }
    fun category(isArabic: Boolean): String = if (isArabic) categoryAr else categoryEn
}

/**
 * Ensures numbers and ratios stay Left-to-Right (LTR) even inside Arabic RTL context.
 */
fun ltrNumber(value: String): String = "\u200E$value\u200E"
