package com.example.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.R
import com.example.model.HabitEntity
import com.example.model.NoteBlockEntity
import com.example.model.PlannerCardEntity
import com.example.model.PlannerSection
import java.io.ByteArrayOutputStream
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlannerRepository(private val dao: PlannerDao) {

    val allCards: Flow<List<PlannerCardEntity>> = dao.getAllCards()
    val allHabits: Flow<List<HabitEntity>> = dao.getAllHabits()
    val brainDumpNote: Flow<NoteBlockEntity?> = dao.getNoteBlock(BRAIN_DUMP_KEY)
    val gratitudeNote: Flow<NoteBlockEntity?> = dao.getNoteBlock(GRATITUDE_NOTE_KEY)

    suspend fun insertCard(card: PlannerCardEntity) = dao.insertCard(card)
    suspend fun updateCard(card: PlannerCardEntity) = dao.updateCard(card)
    suspend fun deleteCardById(id: Int) = dao.deleteCardById(id)

    suspend fun insertHabit(habit: HabitEntity) = dao.insertHabit(habit)
    suspend fun updateHabit(habit: HabitEntity) = dao.updateHabit(habit)
    suspend fun deleteHabitById(id: Int) = dao.deleteHabitById(id)

    suspend fun saveBrainDump(contentEn: String, contentAr: String) {
        dao.upsertNoteBlock(
            NoteBlockEntity(
                noteKey = BRAIN_DUMP_KEY,
                contentEn = contentEn,
                contentAr = contentAr,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun saveGratitudeNote(contentEn: String, contentAr: String) {
        dao.upsertNoteBlock(
            NoteBlockEntity(
                noteKey = GRATITUDE_NOTE_KEY,
                contentEn = contentEn,
                contentAr = contentAr,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun ensureSeedDataPopulated() {
        val cardCount = dao.getCardCount()
        if (cardCount == 0) {
            dao.insertCards(buildInitialSeedCards())
            dao.upsertNoteBlock(
                NoteBlockEntity(
                    noteKey = BRAIN_DUMP_KEY,
                    contentEn = "✨ Wonyoungism affirmation: “I am me, and you are you”\n🎀 Review today's lectures right after returning home (Visual Learning)\n🌸 Prepare emergency uniform outfit & hijab tape for tomorrow\n🕯️ Sunday Reset: clean room, oil scalp & track weekly budget",
                    contentAr = "✨ توكيد اليوم: «أنا هو أنا، وأنت هو أنت» — سلامي الداخلي أولاً\n🎀 مراجعة الدروس فور العودة للمنزل بطريقة التعلم البصري\n🌸 تجهيز زي الطوارئ وشريط الحجاب وحقيبة الجامعة ليلاً\n🕯️ روتين يوم الأحد (Sunday Reset): ترتيب الغرفة، تزييت الشعر، ومراجعة الميزانية"
                )
            )
            dao.upsertNoteBlock(
                NoteBlockEntity(
                    noteKey = GRATITUDE_NOTE_KEY,
                    contentEn = "1. Waking up early with a clear mind and zero social media in the first hour ☀️\n2. A calm, organized room — making my bed immediately upon waking 🛏️\n3. Peaceful boundaries and supportive people in my circle 💕",
                    contentAr = "1. الاستيقاظ المبكر بصفاء ذهني دون تصفح السوشيال ميديا في الساعة الأولى ☀️\n2. بيت مرتب، وقت مبارك، وبال مرتاح — ترتيب السرير فور الاستيقاظ 🛏️\n3. دائرة اجتماعية إيجابية وحدود صحية تحمي طاقتي 💕"
                )
            )
        }
        val habitCount = dao.getHabitCount()
        if (habitCount == 0) {
            dao.insertHabits(buildInitialSeedHabits())
        }
    }

    private fun buildInitialSeedCards(): List<PlannerCardEntity> {
        var t = 1000L
        return listOf(
            // 0. HOME DASHBOARD — TOP 3 PRIORITIES (المهام العاجلة - 3 مهام فقط)
            PlannerCardEntity(
                sectionId = PlannerSection.HOME_DASHBOARD.id,
                categoryKey = "top_priority",
                titleEn = "Glass Skin AM Routine & SPF 50+ Protection",
                titleAr = "روتين البشرة الزجاجية الصباحي وتجديد واقي الشمس SPF 50+",
                subtitleEn = "Dokdo Toner + Anua Rice 70+ + Beta-Glucan Serum + SPF",
                subtitleAr = "تونر دوكدو + تونر الأرز أنوا + سيروم بيتا جلوكان + واقي شمس",
                badgeEn = "Priority 01",
                badgeAr = "الأولوية 01",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.HOME_DASHBOARD.id,
                categoryKey = "top_priority",
                titleEn = "Smart Study Visual Review & 25-Min Mat Pilates",
                titleAr = "المذاكرة الذكية فور العودة للمنزل + 25 دقيقة بيلاتس",
                subtitleEn = "Visual mind-maps for lectures + spine posture Pilates flow",
                subtitleAr = "مراجعة الدروس بالتعلم البصري لمنع تراكمها + تمارين استقامة القوام",
                badgeEn = "Priority 02",
                badgeAr = "الأولوية 02",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.HOME_DASHBOARD.id,
                categoryKey = "top_priority",
                titleEn = "Night Prep: Steam Outfit, Pack Emergency Kit & 8h Sleep",
                titleAr = "التحضير الليلي: كي الملابس، تجهيز الحقيبة، ونوم 8 ساعات",
                subtitleEn = "Prepare tomorrow's uniform, hijab tape & sleep before 11 PM",
                subtitleAr = "تجهيز إطلالة الغد وحقيبة الطوارئ لتفادي التوتر الصباحي",
                badgeEn = "Priority 03",
                badgeAr = "الأولوية 03",
                isChecked = false,
                timestamp = t++
            ),

            // 1. SKINCARE & GLOW-UP CARE (قسم العناية بالبشرة والجمال)
            // Right Column: Chronological Routine & Product Library (صباحاً/مساءً + مكتبة المنتجات)
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "hydration_routine",
                titleEn = "Double Cleansing (Oil + Gentle Foam)",
                titleAr = "التنظيف المزدوج (غسول زيتي + غسول مائي لطيف)",
                subtitleEn = "Melt away SPF & sebum without stripping the skin barrier for true Glass Skin.",
                subtitleAr = "إذابة واقي الشمس والشوائب بلطف للحفاظ على حاجز البشرة والحصول على بشرة زجاجية.",
                badgeEn = "PM • Step 01",
                badgeAr = "مساءً • خطوة 01",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "hydration_routine",
                titleEn = "Round Lab 1025 Dokdo Toner",
                titleAr = "تونر دوكدو (Round Lab 1025 Dokdo Toner)",
                subtitleEn = "Deep sea mineral water toner — pat 2 layers to gently exfoliate & hydrate.",
                subtitleAr = "تونر بمياه البحر العميقة — ربتي طبقتين لترطيب عميق وتقشير يومي لطيف جداً.",
                badgeEn = "AM • 08:00",
                badgeAr = "صباحاً • 08:00",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "hydration_routine",
                titleEn = "Anua Rice 70+ Glow Milky Toner",
                titleAr = "تونر الأرز الحليبي (Anua Rice 70+ Glow Milky Toner)",
                subtitleEn = "70% Rice Bran Water + Ceramides for luminous, milky Glass Skin barrier support.",
                subtitleAr = "خلاصة ماء الأرز 70% والسيراميد لتفتيح البشرة ومنحها توهجاً حليبياً زجاجياً.",
                badgeEn = "AM / PM",
                badgeAr = "صباحاً ومساءً",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "hydration_routine",
                titleEn = "iUNIK Beta-Glucan Power Moisture Serum",
                titleAr = "سيروم بيتا جلوكان (iUNIK Beta-Glucan Power Moisture)",
                subtitleEn = "98% pure Beta-Glucan retains 20% more moisture than hyaluronic acid.",
                subtitleAr = "يرطب البشرة بعمق يفوق حمض الهيالورونيك بـ 20% لامتلاء فوري وتقوية الحاجز.",
                badgeEn = "AM / PM",
                badgeAr = "صباحاً ومساءً",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "hydration_routine",
                titleEn = "Dr. Althea 345 Relief Cream",
                titleAr = "كريم دكتور ألثيا (Dr. Althea 345 Relief Cream)",
                subtitleEn = "Niacinamide + Panthenol + Opuntia Cactus to calm blemishes & repair skin barrier.",
                subtitleAr = "نياسيناميد وبانثينول لتهدئة الاحمرار، ترميم حاجز البشرة، وترطيبها دون ثقل.",
                badgeEn = "PM • 21:30",
                badgeAr = "مساءً • 21:30",
                isChecked = false,
                timestamp = t++
            ),

            // Left Column: Face Yoga, DIY Smart Beauty & Clean Makeup (الوسائط، يوغا الوجه، والمكياج الطبيعي)
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "natural_recipes",
                titleEn = "Face Yoga Massage Steps",
                titleAr = "خطوات مساج اليوغا للوجه (Face Yoga)",
                subtitleEn = "1. Warm 3 drops of facial oil between palms\n2. Glide knuckles along jawline from chin to ears (10x)\n3. Sweep under-eye area gently outward with ring fingers\n4. Massage temples & drain down sides of neck",
                subtitleAr = "1. دفئي 3 قطرات من زيت الوجه بين راحتي يديكِ\n2. مرري المفاصل بلطف على خط الفك من الذقن نحو الأذن (10 مرات)\n3. طبطبة رقيقة تحت العينين باتجاه الخارج\n4. تدليك جانبي الرقبة لتنشيط التصريف اللمفاوي",
                badgeEn = "5 Mins Daily",
                badgeAr = "5 دقائق يومياً",
                presetDrawableName = "img_vision_morning_ritual",
                mediaType = "list",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "natural_recipes",
                titleEn = "Rice Water & Green Tea Ice Cubes (DIY Smart Beauty)",
                titleAr = "مكعبات ثلج ماء الأرز والشاي الأخضر لتقليل الانتفاخ",
                subtitleEn = "Wrap a frozen rice-water or green tea cube in soft gauze and glide for 2 mins each morning to depuff & boost glow on a smart budget.",
                subtitleAr = "وصفة منزلية بميزانية ذكية: مرري مكعب ثلج ماء الأرز أو الشاي الأخضر ملفوفاً بشاش ناعم صباحاً لتقليل انتفاخ الوجه وتحفيز الإشراقة.",
                badgeEn = "DIY Budget Glow 🧊",
                badgeAr = "جمال بميزانية ذكية 🧊",
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "natural_recipes",
                titleEn = "Clean Makeup Tutorial (Dewy Peach Glow)",
                titleAr = "درس المكياج الطبيعي السريع (Clean Makeup)",
                subtitleEn = "1. Prep with hydrating toner & dewy sunscreen base\n2. Dab a soft touch of peach blush high on cheekbones\n3. Brush brows upward & finish with tinted berry/honey lip balm",
                subtitleAr = "1. تهيئة البشرة بمرطب خفيف وواقي شمس يمنح إشراقة ندية\n2. وضع لمسة خفيفة من أحمر الخدود الخوخي (Peach Blush) على الوجنتين\n3. تمشيط الحواجب وتطبيق مرطب شفاه لامع لمظهر طبيعي ناعم",
                badgeEn = "3-Min Look 🌸",
                badgeAr = "مظهر طبيعي 🌸",
                mediaType = "list",
                timestamp = t++
            ),

            // Sun Care SPF 50+ Reminder Cards (حماية الشمس SPF 50+)
            PlannerCardEntity(
                sectionId = PlannerSection.SKINCARE_GLOW.id,
                categoryKey = "sun_care",
                titleEn = "SPF 50+ Reapplication Tracker (Every 2–3 Hours)",
                titleAr = "تذكير تجديد الواقي الشمسي (SPF 50+) كل ساعتين إلى 3 ساعات",
                subtitleEn = "• 08:30 AM — Base 2-finger SPF 50+ application\n• 11:30 AM — Midday sun stick touch-up\n• 02:30 PM — Afternoon UV mist/cushion reapplication",
                subtitleAr = "• 08:30 صباحاً — تطبيق طبقة أساسية (قاعدة الإصبعين SPF 50+)\n• 11:30 ظهراً — تجديد الحماية باستخدام ستيك واقي الشمس\n• 02:30 عصراً — تجديد الحماية قبل الخروج من الجامعة أو العمل",
                badgeEn = "Every 2-3h ☀️",
                badgeAr = "كل 2-3 ساعات ☀️",
                bellEnabled = true,
                bellTime = "11:30",
                isChecked = true,
                timestamp = t++
            ),

            // 2. MODEST ELEGANCE, WARDROBE & HIJAB STYLING (قسم الأناقة والأزياء المحتشمة)
            PlannerCardEntity(
                sectionId = PlannerSection.OUTFITS_HIJAB.id,
                categoryKey = "everyday_look",
                titleEn = "Pastel Yellow Dress & Matching Heels",
                titleAr = "تنسيق فستان أصفر باستيل مع حذاء كعب مطابق",
                subtitleEn = "Perfect for Spring 🌼 — Pastel yellow maxi dress, ivory chiffon hijab & matching neutral heels.",
                subtitleAr = "مثالي لفصل الربيع 🌼 — فستان أصفر باستيل انسيابي مع حجاب شيفون عاجي وحذاء متناسق.",
                badgeEn = "Pastel Capsule",
                badgeAr = "باستيل الربيع",
                presetDrawableName = "img_outfit_yellow_dress",
                aspectRatio = 0.74f,
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.OUTFITS_HIJAB.id,
                categoryKey = "everyday_look",
                titleEn = "Mauve Foulard/Jersey Hijab + Off-White Dress",
                titleAr = "تنسيق حجاب جيرسي وفولار موف مع فستان أوف وايت",
                subtitleEn = "Effortless modest harmony ✨ — Soft mauve jersey wrap paired with off-white dress & pearl clip.",
                subtitleAr = "مظهر متناسق ومريح ✨ — سكارف جيرسي بدرجات الموف مع فستان أوف وايت ومشبك لؤلؤ.",
                badgeEn = "Hijab Inspo",
                badgeAr = "لوحة إلهام الحجاب",
                presetDrawableName = "img_outfit_mauve_hijab",
                aspectRatio = 0.82f,
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.OUTFITS_HIJAB.id,
                categoryKey = "everyday_look",
                titleEn = "Capsule Wardrobe: Neutrals & Structured Coat",
                titleAr = "الخزانة الكبسولية: معطف كاميل مهيكل وبنطلون واسع",
                subtitleEn = "Timeless neutrals (Camel, Beige, Charcoal) + wide-leg trousers & matching sets to save morning time.",
                subtitleAr = "القطع الأساسية بالألوان المحايدة (الكاميل، البيج، الرمادي الداكن) مع السراويل الواسعة لتوفير الوقت صباحاً.",
                badgeEn = "Capsule Core",
                badgeAr = "خزانة كبسولية",
                presetDrawableName = "img_outfit_sage_cardigan",
                aspectRatio = 0.78f,
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.OUTFITS_HIJAB.id,
                categoryKey = "occasion_look",
                titleEn = "Monochromatic Old Money & Hijab Fabric Guide",
                titleAr = "نظرية الألوان (Old Money) ودليل أقمشة الحجاب",
                subtitleEn = "• Monochromatic: Wear tonal shades of one color for visual elongation & luxury\n• Chiffon: Airy & formal for spring/events\n• Crepe: Structured drape for work/study\n• Jersey: Breathable & cozy for long days",
                subtitleAr = "• الدمج الأحادي: ارتداء درجات متقاربة لخلق امتداد بصري ومظهر باهظ (Old Money)\n• الشيفون: خفيف وراقي للمناسبات والربيع\n• الكريب: ثبات وأناقة للجامعة والعمل\n• الجيرسي: مريح وعملي للأيام الطويلة",
                badgeEn = "Color & Fabrics 🎨",
                badgeAr = "دليل الأقمشة والألوان 🎨",
                presetDrawableName = "img_vision_cozy_sanctuary",
                aspectRatio = 0.88f,
                mediaType = "list",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.OUTFITS_HIJAB.id,
                categoryKey = "emergency_prep",
                titleEn = "The Emergency Uniform & Quick Style Hacks",
                titleAr = "زي الطوارئ (The Uniform) وحيل الأناقة السريعة",
                subtitleEn = "• Saved Matching Set: Monochrome beige knit set ready for busy mornings\n• Hijab Tape Hack: Secure wrap all day without slipping\n• 1-Feature Rule: Highlight either eyes OR lips when short on time",
                subtitleAr = "• زي الطوارئ: طقم متطابق (Matching Set) جاهز للأيام المزدحمة التي تفتقر للوقت\n• شريط الحجاب (Hijab Tape): لضمان ثبات الحجاب طوال اليوم\n• حيلة الصباح: التركيز على إبراز ميزة جمالية واحدة (العيون أو الشفاه) عند ضيق الوقت",
                badgeEn = "Emergency Prep ⚡",
                badgeAr = "التجهيز والطوارئ ⚡",
                presetDrawableName = "img_vision_morning_ritual",
                aspectRatio = 0.84f,
                mediaType = "list",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.OUTFITS_HIJAB.id,
                categoryKey = "emergency_prep",
                titleEn = "Night Prep Checklist (Zero Morning Stress)",
                titleAr = "قائمة التحضير الليلي (بدء اليوم بهدوء)",
                subtitleEn = "1. Steam & hang tomorrow's outfit and hijab at night\n2. Pack university/work bag, charger & emergency pouch\n3. Lay out matching shoes, watch & magnetic pins",
                subtitleAr = "1. كي وتعليق ملابس الغد والحجاب ليلاً لتفادي التوتر الصباحي\n2. تجهيز الحقيبة، الشاحن، وحقيبة الطوارئ الصحية\n3. تحضير الحذاء، الإكسسوارات، ودبابيس الحجاب المغناطيسية",
                badgeEn = "Night Checklist 🌙",
                badgeAr = "تحضير ليلي 🌙",
                aspectRatio = 0.80f,
                mediaType = "list",
                timestamp = t++
            ),

            // 3. BODY CARE, EVERYTHING SHOWER & SMART DIY BEAUTY (قسم العناية بالجسم — Accordion Lists)
            PlannerCardEntity(
                sectionId = PlannerSection.BODY_CARE.id,
                categoryKey = "shower_routine",
                titleEn = "Pre-Shower Ritual, Scalp Massage & Silk Pillowcase",
                titleAr = "التحضير قبل الاستحمام، تدليك الفروة، ووسادة الحرير",
                subtitleEn = "Dry brushing for 5 mins upward toward the heart to boost circulation\nWarm rosemary & argan oil scalp massage (10 mins) to stimulate hair growth\nCoat hair ends with protective oil before washing\nSleep on a silk or satin pillowcase to prevent hair breakage & frizz",
                subtitleAr = "التقشير الجاف (Dry Brushing) لمدة 5 دقائق لتحفيز الدورة الدموية\nجلسة تدليك فروة الرأس (Scalp Massage) وتزييت الشعر قبل الاستحمام لنمو صحي\nتوزيع زيت خفيف على أطراف الشعر لحمايتها أثناء الغسل\nاستخدام غطاء وسادة من الحرير أو الساتان لحماية الشعر من التكسر أثناء النوم",
                badgeEn = "4 Steps • Pre-Shower",
                badgeAr = "4 خطوات • التحضير والشعر",
                presetDrawableName = "img_vision_morning_ritual",
                mediaType = "list",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.BODY_CARE.id,
                categoryKey = "shower_routine",
                titleEn = "Deep Everything Shower & Moisture Lock",
                titleAr = "روتين الاستحمام العميق وحبس الرطوبة (Everything Shower)",
                subtitleEn = "Double cleanse hair with sulfate-free shampoo & apply deep mask\nWash body with hydrating camellia & oat cleanser\nExfoliate gently with body scrub on elbows, knees & heels\nApply body oil + ceramide lotion onto damp skin immediately after shower to lock in moisture",
                subtitleAr = "غسل الشعر بالشامبو اللطيف وتطبيق ماسك الترطيب العميق\nتنظيف الجسم بغسول الشوفان والكاميليا المرطب\nتقشير المناطق الجافة بلطف لتجديد خلايا البشرة\nتطبيق زيوت الجسم واللوشن على بشرة رطبة فوراً بعد الاستحمام لحبس الرطوبة",
                badgeEn = "4 Steps • Everything Shower",
                badgeAr = "4 خطوات • الاستحمام الشامل",
                presetDrawableName = "img_vision_cozy_sanctuary",
                mediaType = "list",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.BODY_CARE.id,
                categoryKey = "body_scrubs",
                titleEn = "Coffee & Sugar Scrub (Circulation & Anti-Cellulite)",
                titleAr = "مقشر القهوة والسكر لتعزيز الدورة الدموية ومكافحة السيلوليت",
                subtitleEn = "½ cup finely ground espresso coffee\n½ cup brown sugar + ¼ cup sweet almond or olive oil\n1 tsp honey & vanilla extract\nMassage in circular motions for 3 mins to stimulate blood flow & smooth skin texture",
                subtitleAr = "نصف كوب قهوة مطحونة ناعماً\nنصف كوب سكر بني + ربع كوب زيت اللوز أو زيت الزيتون\nملعقة صغيرة عسل وفانيليا\nيدلك بحركات دائرية لمدة 3 دقائق لتعزيز الدورة الدموية، النعومة، ومكافحة السيلوليت",
                badgeEn = "4 Ingredients • DIY",
                badgeAr = "4 مقادير • مقشر القهوة",
                mediaType = "list",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.BODY_CARE.id,
                categoryKey = "body_scrubs",
                titleEn = "Targeted DIY Smart Beauty (Nails, Teeth & Lips)",
                titleAr = "عناية مخصصة بميزانية ذكية (الأظافر، الأسنان، والشفاه)",
                subtitleEn = "Nail Strengthening: Soak nails in warm olive oil infused with crushed garlic for 10 mins weekly\nNatural Teeth Whitening: Gently rub inner banana peel on teeth for 1 min before brushing\nSoft Lips: Exfoliate lips with raw honey & sugar scrub, then seal with balm to prevent chapping",
                subtitleAr = "تقوية الأظافر: نقع الأظافر في زيت الزيتون الدافئ مع الثوم لمدة 10 دقائق أسبوعياً\nتبييض الأسنان طبيعياً: فرك الأسنان بالجزء الداخلي لقشر الموز لمدة دقيقة قبل التفريش\nترطيب الشفاه: تقشير الشفاه بمقشر العسل والسكر لتجنب التشققات ومنحها نعومة فورية",
                badgeEn = "3 DIY Rituals • Smart Beauty",
                badgeAr = "3 وصفات • عناية مخصصة",
                mediaType = "list",
                isChecked = false,
                timestamp = t++
            ),

            // 4. FORGET-ME-NOT KANBAN, SMART BUDGET & SPACE (قسم "لا تنسي"، الميزانية، والمحيط)
            // Column 1: Pharmacy (مشتريات الصيدلية)
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "pharmacy",
                titleEn = "Buy Skincare (Dr. Althea 345, Anua Rice & Cotton)",
                titleAr = "شراء منتجات العناية (كريم 345، تونر الأرز، وأقراص قطنية)",
                subtitleEn = "Restock barrier cream, Dokdo toner & SPF 50+ stick",
                subtitleAr = "تجديد كريم دكتور ألثيا 345، تونر دوكدو، وواقي الشمس",
                badgeEn = "Pharmacy",
                badgeAr = "الصيدلية",
                bellEnabled = true,
                bellTime = "17:00",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "pharmacy",
                titleEn = "Drink Water 💧 & Pack University Emergency Kit",
                titleAr = "اشربي الماء 💧 وتجهيز حقيبة الطوارئ الصحية للجامعة",
                subtitleEn = "Sanitary pads, pain reliever, mini deodorant, wipes & lip balm",
                subtitleAr = "فوط صحية، مسكن، معطر صغير، مناديل مبللة، ومرطب شفاه",
                badgeEn = "Daily Reminder",
                badgeAr = "تذكير يومي",
                bellEnabled = true,
                bellTime = "09:00",
                isChecked = true,
                timestamp = t++
            ),
            // Column 2: Supermarket (السوبرماركت)
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "supermarket",
                titleEn = "Restock Matcha, Oat Milk & Green Tea",
                titleAr = "إعادة تعبئة الماتشا، حليب الشوفان، والشاي الأخضر",
                subtitleEn = "Ceremonial matcha tin, barista oat milk & green tea for ice cubes",
                subtitleAr = "علبة ماتشا فاخرة، حليب الشوفان، وشاي أخضر لمكعبات ثلج الوجه",
                badgeEn = "Supermarket",
                badgeAr = "السوبرماركت",
                bellEnabled = true,
                bellTime = "18:30",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "supermarket",
                titleEn = "Water-Rich Fruits (Watermelon & Strawberries)",
                titleAr = "فواكه غنية بالماء (البطيخ والفراولة) لترطيب البشرة",
                subtitleEn = "Internal skin hydration + zero added-sugar snacks to prevent breakouts",
                subtitleAr = "لترطيب البشرة من الداخل وبدائل صحية بدون سكر مضاف لتجنب الحبوب",
                badgeEn = "Supermarket",
                badgeAr = "السوبرماركت",
                bellEnabled = false,
                bellTime = "12:00",
                isChecked = false,
                timestamp = t++
            ),
            // Column 3: Clothing (ملابس)
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "clothing",
                titleEn = "Mauve Jersey Hijab, Hijab Tape & Magnetic Pins",
                titleAr = "حجاب جيرسي موف، شريط الحجاب، ودبابيس مغناطيسية",
                subtitleEn = "Double-sided hijab tape for all-day hold + snag-free magnets",
                subtitleAr = "شريط تثبيت الحجاب (Hijab Tape) لثبات طوال اليوم مع دبابيس اللؤلؤ",
                badgeEn = "Clothing",
                badgeAr = "ملابس",
                bellEnabled = true,
                bellTime = "20:00",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "clothing",
                titleEn = "Silk Pillowcase & Neutral Matching Set",
                titleAr = "غطاء وسادة حريري وطقم متطابق (Matching Set) للطوارئ",
                subtitleEn = "Mulberry silk pillowcase for hair + beige capsule uniform set",
                subtitleAr = "وسادة حرير لحماية الشعر من التكسر + طقم بيج أنيق للأيام المزدحمة",
                badgeEn = "Clothing",
                badgeAr = "ملابس",
                bellEnabled = false,
                bellTime = "16:00",
                isChecked = true,
                timestamp = t++
            ),
            // Column 4: Smart Budget, Deep Cleaning & Interior Space (الميزانية الذكية والمحيط)
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "budget_space",
                titleEn = "Smart Budget Planner & Savings Challenge",
                titleAr = "الميزانية الذكية (Budget Planner) وتحدي التوفير",
                subtitleEn = "Track weekly expenses, mindful beauty purchases & save 20% in envelope challenge",
                subtitleAr = "إدارة النفقات بوعي، تتبع المشتريات، وتحدي التوفير الأسبوعي لبناء استقلال مالي",
                badgeEn = "Finance 💰",
                badgeAr = "إدارة مالية 💰",
                bellEnabled = true,
                bellTime = "21:00",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "budget_space",
                titleEn = "Deep Cleaning Routine (Daily, Weekly & Monthly)",
                titleAr = "جداول التنظيف العميق وترتيب الغرفة (يومي/أسبوعي/شهري)",
                subtitleEn = "• Daily: Make bed immediately upon waking & clear desk\n• Weekly: Change silk sheets, vacuum & sanitize vanity\n• Monthly: Declutter wardrobe & deep clean windows",
                subtitleAr = "• يومياً: ترتيب السرير فور الاستيقاظ وإخلاء المكتب من الفوضى\n• أسبوعياً: تغيير أغطية السرير، مسح الأسطح، وتنظيم تسريحة العناية\n• شهرياً: فرز الخزانة والتنظيف العميق للغرفة",
                badgeEn = "Clean Space 🧹",
                badgeAr = "ترتيب المساحة 🧹",
                bellEnabled = false,
                bellTime = "08:00",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FORGET_ME_NOT.id,
                categoryKey = "budget_space",
                titleEn = "Interior Vibes: Modern Algerian Salon & Taupe Chenille",
                titleAr = "التصميم الداخلي: الصالون الجزائري العصري وأرائك التوب (Taupe)",
                subtitleEn = "Blend traditional Algerian salon warmth with minimalist taupe chenille sofas, warm oak wood tables & sleek wall-mounted TV unit.",
                subtitleAr = "دمج عناصر الصالون الجزائري التقليدي مع لمسات عصرية بسيطة: أرائك الشنيل بلون التوب (Taupe)، الطاولات الخشبية، ووحدات التلفاز الجدارية لبيئة هادئة.",
                badgeEn = "Interior Vibes 🛋️",
                badgeAr = "ديكور المساحة 🛋️",
                presetDrawableName = "img_vision_cozy_sanctuary",
                bellEnabled = false,
                bellTime = "19:00",
                isChecked = false,
                timestamp = t++
            ),

            // 5. HABIT TRACKER, TIME BLOCKING & SMART STUDYING (قسم التطوير الأكاديمي وتقسيم الوقت)
            PlannerCardEntity(
                sectionId = PlannerSection.HABIT_TRACKER.id,
                categoryKey = "time_study",
                titleEn = "Smart Studying & Visual Learning Strategy",
                titleAr = "المذاكرة الذكية والتعلم البصري (Smart Studying)",
                subtitleEn = "• Review lectures immediately after returning home to prevent backlog\n• Use color-coded visual mind maps & diagrams for faster exam recall\n• 45-min deep focus blocks with zero phone distractions",
                subtitleAr = "• استراتيجية المراجعة اليومية للدروس فور العودة إلى المنزل لمنع تراكمها\n• اعتماد أساليب التعلم البصري (الخرائط الذهنية والألوان) لزيادة التركيز وتسهيل التذكر في الامتحانات",
                badgeEn = "Academic Glow 🎓",
                badgeAr = "تطوير أكاديمي 🎓",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.HABIT_TRACKER.id,
                categoryKey = "time_study",
                titleEn = "Daily Time Blocking Schedule (Blessed Time)",
                titleAr = "جدول تقسيم الوقت اليومي (Time Blocking)",
                subtitleEn = "• 06:30–08:00 AM: Make bed, no social media, write 3 goals & AM skincare\n• 08:30–02:30 PM: University / Work deep focus\n• 04:00–06:00 PM: Immediate lecture review & visual notes\n• 06:30–08:00 PM: Pilates, podcast & Everything Shower",
                subtitleAr = "• 06:30–08:00 ص: ترتيب السرير، عدم تصفح السوشيال ميديا، وتدوين 3 أهداف\n• 08:30–02:30 م: العمل أو المحاضرات بتركيز عالٍ\n• 04:00–06:00 م: مراجعة الدروس اليومية فور العودة\n• 06:30–08:00 م: تمارين البيلاتس، بودكاست تطوير الذات، والروتين المسائي",
                badgeEn = "Time Blocking ⏰",
                badgeAr = "تقسيم الوقت ⏰",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.HABIT_TRACKER.id,
                categoryKey = "time_study",
                titleEn = "Sunday Reset Protocol (Recharge & Organize)",
                titleAr = "روتين يوم الأحد (Sunday Reset) لإعادة شحن الطاقة",
                subtitleEn = "1. Deep clean room & fresh bed linens\n2. Review weekly study notes & plan calendar blocks\n3. Everything Shower, scalp massage & weekly meal prep",
                subtitleAr = "1. ترتيب الغرفة وتجديد أغطية السرير لراحة نفسية\n2. مراجعة ملخصات الأسبوع وتخطيط جدول الأسبوع الجديد\n3. حمام العناية الشاملة، تدليك الفروة، وتحضير وجبات الأسبوع",
                badgeEn = "Sunday Reset 🕯️",
                badgeAr = "روتين الأحد 🕯️",
                isChecked = false,
                timestamp = t++
            ),

            // 6. VISION BOARD, WONYOUNGISM MINDSET & SOCIAL BOUNDARIES (قسم لوحة الإلهام وعقلية وونيونغ)
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "vision_board",
                titleEn = "6:30 AM Wonyoungism Morning Sanctuary",
                titleAr = "صباحات وونيونغ الهادئة الساعة 6:30 صباحاً",
                subtitleEn = "Wake up early without social media, drink 2L water, Pilates & matcha",
                subtitleAr = "استيقاظ مبكر بصفاء ذهني دون هاتف، شرب الماء، بيلاتس وماتشا",
                badgeEn = "Wonyoung Era ✨",
                badgeAr = "عهد التألق ✨",
                presetDrawableName = "img_vision_morning_ritual",
                offsetX = 0f,
                offsetY = 0f,
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "vision_board",
                titleEn = "Peaceful Taupe & Algerian-Modern Sanctuary",
                titleAr = "بيتي جنتي بذكاء لا بجهد 🌸",
                subtitleEn = "Taupe chenille sofa, warm wood accents, peonies & clutter-free calm",
                subtitleAr = "أريكة شنيل بلون التوب، طاولات خشبية دافئة، ومساحة منظمة تبعث على السكينة",
                badgeEn = "Dream Space 🕯️",
                badgeAr = "مساحة الأحلام 🕯️",
                presetDrawableName = "img_vision_cozy_sanctuary",
                offsetX = 0f,
                offsetY = 0f,
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "vision_board",
                titleEn = "Modest Old-Money Capsule Wardrobe",
                titleAr = "خزانة كبسولية أنيقة ومحتشمة (Old Money)",
                subtitleEn = "Monochromatic neutrals, pastel dresses & effortless hijab styling",
                subtitleAr = "دمج أحادي راقٍ، فساتين باستيل، وإطلالات محتشمة جاهزة دائماً",
                badgeEn = "Style Goal 👗",
                badgeAr = "هدف الأناقة 👗",
                presetDrawableName = "img_outfit_yellow_dress",
                offsetX = 0f,
                offsetY = 0f,
                mediaType = "media",
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "vision_board",
                titleEn = "Top Academic Honors & Smart Financial Freedom",
                titleAr = "تفوق أكاديمي واستقلالية مالية وذهنية 📖",
                subtitleEn = "Daily visual study habits, reading 24 books & smart savings",
                subtitleAr = "مذاكرة ذكية يومية، قراءة مستمرة، وإدارة مالية واعية",
                badgeEn = "Future You 🎓",
                badgeAr = "نسختكِ القادمة 🎓",
                presetDrawableName = "img_outfit_sage_cardigan",
                offsetX = 0f,
                offsetY = 0f,
                mediaType = "media",
                timestamp = t++
            ),

            // Wonyoungism Mindset, Shadow Work & Social Boundaries Cards
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "wonyoung_mindset",
                titleEn = "Wonyoungism Principle: “I Am Me, and You Are You”",
                titleAr = "مبدأ وونيونغ والاستقلالية: «أنا هو أنا، وأنت هو أنت»",
                subtitleEn = "Detach from negative opinions and external judgment. Focus 100% on your own growth, peace, and daily rituals without comparing your journey on social media.",
                subtitleAr = "تبني عقلية الاستقلالية وتجاهل الآراء السلبية أو المقارنات على السوشيال ميديا. ركّزي طاقتكِ بالكامل على تطوير ذاتكِ وسلامكِ الداخلي.",
                badgeEn = "Wonyoungism 👑",
                badgeAr = "عقلية وونيونغ 👑",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "wonyoung_mindset",
                titleEn = "Shadow Work Daily Reflection Prompts",
                titleAr = "تمارين الظل (Shadow Work) لتعزيز الثقة بالنفس",
                subtitleEn = "• What personal strength made me proud this week?\n• When do I feel tempted to compare myself online, and how can I redirect that energy into my real life?\n• What boundary do I need to honor today?",
                subtitleAr = "• ما هي نقطة القوة التي جعلتني فخورة بنفسي هذا الأسبوع؟\n• كيف أحوّل وقت تصفح السوشيال ميديا إلى خطوة حقيقية في أهدافي؟\n• ما هي الحدود الصحية التي سألتزم بها اليوم لحماية طاقتي؟",
                badgeEn = "Shadow Work 🪞",
                badgeAr = "تمارين الظل 🪞",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FUTURE_YOU.id,
                categoryKey = "wonyoung_mindset",
                titleEn = "Social Circle Filtering & Healthy Boundaries",
                titleAr = "فلترة العلاقات والحدود الاجتماعية الصحية",
                subtitleEn = "Protect your mental energy: gracefully distance yourself from gossip, chronic complaining, and energy drainers. Surround yourself with kind, ambitious souls.",
                subtitleAr = "تقييم الدائرة الاجتماعية والابتعاد بهدوء عن الأشخاص السلبيين أو من يستنزفون الطاقة وينقلون الشائعات، وإحاطة نفسكِ بصحبة راقية وداعمة.",
                badgeEn = "Boundaries 🛡️",
                badgeAr = "حدود اجتماعية 🛡️",
                isChecked = true,
                timestamp = t++
            ),

            // 7. WELLNESS, MINDFUL NUTRITION, SLEEP & CYCLE EMERGENCY KIT (قسم اللياقة والتغذية الحيوية)
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "meals",
                titleEn = "Berry Vanilla Overnight Oats",
                titleAr = "شوفان بارد بالحليب والتوت",
                subtitleEn = "Rolled oats, chia seeds, almond milk, Greek yogurt, raw honey & fresh berries.",
                subtitleAr = "شوفان كامل، بذور الشيا، حليب اللوز، زبادي يوناني وتوت طازج لطاقة مستدامة.",
                badgeEn = "Breakfast • 22g Protein",
                badgeAr = "الفطور • 22غ بروتين",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "meals",
                titleEn = "Emerald Glow Green Smoothie",
                titleAr = "سموذي أخضر للنضارة والإشراق",
                subtitleEn = "Spinach, mango, ½ avocado, coconut water & fresh lime juice.",
                subtitleAr = "أوراق سبانخ، مانجو، نصف أفوكادو، وماء جوز الهند لنضارة البشرة.",
                badgeEn = "Glow Drink 🥬",
                badgeAr = "مشروب النضارة 🥬",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "meals",
                titleEn = "Water-Rich Foods & Low-Sugar Clear Skin Guide",
                titleAr = "الأطعمة الغنية بالماء وتقليل السكر لصفاء البشرة",
                subtitleEn = "Snack on watermelon, strawberries & cucumber to hydrate skin from within, while cutting added sugars to prevent sudden breakouts.",
                subtitleAr = "التركيز على الأطعمة الغنية بالماء (كالبطيخ، الفراولة، والخيار) لترطيب البشرة من الداخل، وتقليل السكريات المضافة لتجنب ظهور الحبوب المفاجئة.",
                badgeEn = "Hydration Foods 🍉",
                badgeAr = "ترطيب داخلي 🍉",
                isChecked = false,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "workouts",
                titleEn = "25-Min Mat Pilates (Spine Posture & Flexibility)",
                titleAr = "25 دقيقة تمارين البيلاتس لتحسين وضعية العمود الفقري والمرونة",
                subtitleEn = "Gentle core strengthening, spine articulation & posture alignment without harsh joint stress.",
                subtitleAr = "حركة لطيفة لتقوية المركز، تحسين استقامة القوام، وزيادة المرونة بعيداً عن التمارين القاسية.",
                badgeEn = "25 mins 🩰",
                badgeAr = "25 دقيقة 🩰",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "workouts",
                titleEn = "Mindful Morning Walk & Face Yoga",
                titleAr = "المشي الصباحي لتصفية الذهن + مساج يوغا الوجه",
                subtitleEn = "30 mins outdoor morning walk in fresh air + 10 mins lymphatic Face Yoga.",
                subtitleAr = "30 دقيقة مشي صباحي لتصفية الذهن وتنشيط الجسم + 10 دقائق يوغا الوجه.",
                badgeEn = "40 mins 👟",
                badgeAr = "40 دقيقة 👟",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "workouts",
                titleEn = "7–8 Hours Deep Sleep Log (Zero Dark Circles)",
                titleAr = "سجل النوم العميق (7 إلى 8 ساعات) لتجنب الإرهاق والهالات",
                subtitleEn = "In bed by 10:30 PM with chamomile tea & silk eye mask for cellular skin repair and bright eyes.",
                subtitleAr = "النوم المبكر لمدة 7–8 ساعات كاملة لتجنب الإرهاق والهالات السوداء وتجديد خلايا البشرة ليلاً.",
                badgeEn = "8h Sleep 🌙",
                badgeAr = "8 ساعات نوم 🌙",
                isChecked = true,
                timestamp = t++
            ),
            PlannerCardEntity(
                sectionId = PlannerSection.FITNESS_NUTRITION.id,
                categoryKey = "workouts",
                titleEn = "Cycle Wellness Tracker & University Emergency Kit",
                titleAr = "متتبع الدورة الشهرية وتجهيز حقيبة الطوارئ للمدرسة/الجامعة",
                subtitleEn = "Track cycle phase (Follicular/Luteal) + keep emergency pouch stocked with pads, heat patch, dark chocolate & pain relief.",
                subtitleAr = "متابعة مراحل الدورة الشهرية وتذكير بتجهيز حقيبة الطوارئ الصحية في حقيبة الجامعة أو المدرسة لراحة وأمان تام.",
                badgeEn = "Cycle & Kit 🎀",
                badgeAr = "متتبع وحقيبة الطوارئ 🎀",
                isChecked = false,
                timestamp = t++
            )
        )
    }

    private fun buildInitialSeedHabits(): List<HabitEntity> {
        var t = 2000L
        return listOf(
            HabitEntity(
                nameEn = "Make Bed Upon Waking & Zero Social Media 1st Hour 🌅",
                nameAr = "ترتيب السرير فوراً وعدم تصفح السوشيال ميديا أول ساعة 🌅",
                targetEn = "6:30 AM",
                targetAr = "6:30 صباحاً",
                mon = true, tue = true, wed = true, thu = true, fri = true, sat = true, sun = true,
                timestamp = t++
            ),
            HabitEntity(
                nameEn = "Write 3 Main Goals & Smart Daily Lesson Review 📖",
                nameAr = "تدوين 3 أهداف صباحية ومراجعة الدروس فور العودة 📖",
                targetEn = "Daily",
                targetAr = "يومياً",
                mon = true, tue = true, wed = false, thu = true, fri = true, sat = true, sun = false,
                timestamp = t++
            ),
            HabitEntity(
                nameEn = "Hydration (2L Water + Water-Rich Fruits) 💧",
                nameAr = "شرب لترين من الماء وفواكه غنية بالماء 💧",
                targetEn = "2 Liters",
                targetAr = "2 لتر",
                mon = true, tue = true, wed = true, thu = true, fri = true, sat = true, sun = false,
                timestamp = t++
            ),
            HabitEntity(
                nameEn = "Gentle Movement: Pilates & Morning Walk 🩰",
                nameAr = "تمارين البيلاتس والمشي الصباحي لتصفية الذهن 🩰",
                targetEn = "30 mins",
                targetAr = "30 دقيقة",
                mon = true, tue = true, wed = true, thu = false, fri = true, sat = true, sun = true,
                timestamp = t++
            ),
            HabitEntity(
                nameEn = "Deep Beauty Sleep (7–8h to Prevent Dark Circles) 🌙",
                nameAr = "نوم عميق (7-8 ساعات لتجنب الإرهاق والهالات) 🌙",
                targetEn = "8 hrs",
                targetAr = "8 ساعات",
                mon = true, tue = true, wed = true, thu = true, fri = false, sat = true, sun = true,
                timestamp = t++
            ),
            HabitEntity(
                nameEn = "Read 15 Pages & Educational Podcast 🎧",
                nameAr = "قراءة 15 صفحة والاستماع لبودكاست تطوير الذات 🎧",
                targetEn = "15 pages",
                targetAr = "15 صفحة",
                mon = true, tue = false, wed = true, thu = true, fri = true, sat = true, sun = true,
                timestamp = t++
            )
        )
    }

    companion object {
        const val BRAIN_DUMP_KEY = "forget_me_not_brain_dump"
        const val GRATITUDE_NOTE_KEY = "future_you_gratitude_note"
    }
}

object ImageBase64Helper {

    fun presetNameToDrawableRes(name: String?): Int? {
        return when (name) {
            "img_outfit_yellow_dress" -> R.drawable.img_outfit_yellow_dress_1790543282154
            "img_outfit_mauve_hijab" -> R.drawable.img_outfit_mauve_hijab_1790541170795
            "img_outfit_sage_cardigan" -> R.drawable.img_outfit_sage_cardigan_1790541180911
            "img_vision_morning_ritual" -> R.drawable.img_vision_morning_ritual_1790541192628
            "img_vision_cozy_sanctuary" -> R.drawable.img_vision_cozy_sanctuary_1790541203357
            "img_app_icon" -> R.drawable.img_app_icon_1790541161059
            else -> null
        }
    }

    suspend fun uriToBase64AndAspect(context: Context, uri: Uri): Pair<String, Float>? =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext null
                val original = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                if (original == null) return@withContext null

                val maxDim = 720
                val ratio = min(
                    maxDim.toFloat() / original.width.toFloat(),
                    maxDim.toFloat() / original.height.toFloat()
                ).coerceAtMost(1f)

                val width = (original.width * ratio).toInt().coerceAtLeast(1)
                val height = (original.height * ratio).toInt().coerceAtLeast(1)
                val scaled = Bitmap.createScaledBitmap(original, width, height, true)
                val aspectRatio = (width.toFloat() / height.toFloat()).coerceIn(0.6f, 1.5f)

                val output = ByteArrayOutputStream()
                scaled.compress(Bitmap.CompressFormat.JPEG, 82, output)
                val bytes = output.toByteArray()
                val encoded = Base64.encodeToString(bytes, Base64.NO_WRAP)
                Pair(encoded, aspectRatio)
            } catch (e: Exception) {
                null
            }
        }

    fun decodeBase64ToBitmap(base64Str: String?): Bitmap? {
        if (base64Str.isNullOrBlank()) return null
        return try {
            val bytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }
}
