package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyMoodOption
import com.example.model.DefaultDailyQuotes
import com.example.model.NoteBlockEntity
import com.example.model.PlannerCardEntity
import com.example.model.PlannerSection
import com.example.ui.components.PlannerCardImage
import com.example.ui.components.SectionStickerIcon
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.LocalPastelPalette
import com.example.ui.theme.MutedCocoa
import com.example.ui.theme.WarmSlateBrown
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeDashboardScreen(
    topPriorities: List<PlannerCardEntity>,
    brainDumpNote: NoteBlockEntity?,
    currentMood: DailyMoodOption,
    waterCupsCount: Int,
    quoteIndex: Int,
    dailyProgressPercent: Int,
    isArabic: Boolean,
    onSelectMood: (DailyMoodOption) -> Unit,
    onSetWaterCups: (Int) -> Unit,
    onNextQuote: () -> Unit,
    onSaveBrainDump: (String, String) -> Unit,
    onTogglePriority: (PlannerCardEntity) -> Unit,
    onEditPriority: (PlannerCardEntity) -> Unit,
    onNavigateToSection: (PlannerSection) -> Unit
) {
    val palette = LocalPastelPalette.current
    val currentQuote = DefaultDailyQuotes[quoteIndex % DefaultDailyQuotes.size]

    val formattedDate = remember(isArabic) {
        val locale = if (isArabic) Locale("ar") else Locale.ENGLISH
        SimpleDateFormat("EEEE, d MMMM yyyy", locale).format(Date())
    }

    var showMoodPickerRow by remember { mutableStateOf(false) }

    var dumpEn by remember(brainDumpNote?.contentEn) {
        mutableStateOf(brainDumpNote?.contentEn.orEmpty())
    }
    var dumpAr by remember(brainDumpNote?.contentAr) {
        mutableStateOf(brainDumpNote?.contentAr.orEmpty())
    }

    LaunchedEffect(brainDumpNote) {
        if (brainDumpNote != null) {
            dumpEn = brainDumpNote.contentEn
            dumpAr = brainDumpNote.contentAr
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("home_dashboard_screen"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. WELCOME HEADER & ROTATING DAILY QUOTE (الترويسة + اقتباس يومي متغير)
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = palette.cardSurface,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, palette.primaryPastel, RoundedCornerShape(28.dp))
                .testTag("dashboard_welcome_header")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(palette.primaryPastel.copy(alpha = 0.28f))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) "أهلاً بكِ يا جميلة في مساحتكِ 🌸" else "Welcome back, gorgeous 🌸",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = WarmSlateBrown
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formattedDate,
                            style = MaterialTheme.typography.labelLarge,
                            color = MutedCocoa
                        )
                    }

                    Surface(
                        color = palette.primaryPastel,
                        shape = CircleShape
                    ) {
                        Text(
                            text = palette.displayName(isArabic),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = WarmSlateBrown,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Rotating Daily Quote Box
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CreamSurface.copy(alpha = 0.9f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, palette.borderTint, RoundedCornerShape(20.dp))
                        .clickable { onNextQuote() }
                        .testTag("daily_quote_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentQuote.quote(isArabic),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = WarmSlateBrown
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentQuote.author(isArabic),
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                        IconButton(
                            onClick = onNextQuote,
                            modifier = Modifier
                                .size(34.dp)
                                .testTag("cycle_quote_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = if (isArabic) "تغيير الاقتباس" else "Next quote",
                                tint = palette.deepAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. QUICK GLANCE BAR (شريط الحالة السريع: المزاج + عداد الماء + شريط التقدم الدائري)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quick_glance_bar"),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 2A. Circular Mood Card (بطاقة دائرية صغيرة لتحديد المزاج اليومي)
            Surface(
                onClick = { showMoodPickerRow = !showMoodPickerRow },
                shape = RoundedCornerShape(24.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .border(1.dp, palette.borderTint, RoundedCornerShape(24.dp))
                    .testTag("mood_glance_card")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(palette.primaryPastel.copy(alpha = 0.65f))
                            .border(1.2.dp, palette.deepAccent.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentMood.emoji,
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentMood.label(isArabic),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown,
                        maxLines = 1
                    )
                }
            }

            // 2B. Water Intake Counter with Droplets (عداد شرب الماء - أيقونات قطرات ماء يتم تظليلها عند الضغط)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, palette.borderTint, RoundedCornerShape(24.dp))
                    .testTag("water_counter_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "عداد شرب الماء 💧" else "Hydration 💧",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmSlateBrown
                        )
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = "$waterCupsCount/8",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = palette.deepAccent
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // 8 Interactive Water Droplets
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (cupIndex in 1..8) {
                                val isShaded = cupIndex <= waterCupsCount
                                val tint by animateColorAsState(
                                    targetValue = if (isShaded) palette.deepAccent else MutedCocoa.copy(alpha = 0.35f),
                                    label = "waterDropTint"
                                )
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isShaded) palette.primaryPastel.copy(alpha = 0.7f)
                                            else CreamSurface
                                        )
                                        .clickable {
                                            val newCount = if (waterCupsCount == cupIndex) cupIndex - 1 else cupIndex
                                            onSetWaterCups(newCount)
                                        }
                                        .testTag("water_drop_$cupIndex"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isShaded) Icons.Filled.WaterDrop else Icons.Outlined.WaterDrop,
                                        contentDescription = "Water cup $cupIndex",
                                        tint = tint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2C. Circular Progress Bar (شريط تقدم دائري يوضح نسبة إنجاز مهام اليوم)
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .border(1.dp, palette.borderTint, RoundedCornerShape(24.dp))
                    .testTag("daily_progress_card")
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(46.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { (dailyProgressPercent / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier.size(46.dp),
                            color = palette.deepAccent,
                            trackColor = palette.primaryPastel.copy(alpha = 0.45f),
                            strokeWidth = 4.5.dp,
                            strokeCap = StrokeCap.Round
                        )
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = "$dailyProgressPercent%",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = WarmSlateBrown
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isArabic) "إنجاز اليوم" else "Today",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown
                    )
                }
            }
        }

        // Expandable Emoji Mood Selector Row when mood circle is tapped
        if (showMoodPickerRow) {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, palette.primaryPastel, RoundedCornerShape(22.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DailyMoodOption.entries.forEach { mood ->
                        val isSelected = mood == currentMood
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) palette.primaryPastel else CreamSurface)
                                .clickable {
                                    onSelectMood(mood)
                                    showMoodPickerRow = false
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = mood.emoji, fontSize = 20.sp)
                            Text(
                                text = mood.label(isArabic),
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmSlateBrown
                            )
                        }
                    }
                }
            }
        }

        // 3. BRAIN DUMP WIDGET (صندوق "التفريغ الذهني" الشفاف في المنتصف)
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = palette.cardSurface.copy(alpha = 0.88f),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, palette.primaryPastel, RoundedCornerShape(26.dp))
                .testTag("home_brain_dump_widget")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(palette.primaryPastel),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                tint = WarmSlateBrown,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isArabic) "صندوق التفريغ الذهني (Brain Dump)" else "Brain Dump Sanctuary",
                                style = MaterialTheme.typography.titleMedium,
                                color = WarmSlateBrown
                            )
                            Text(
                                text = if (isArabic) "اكتبي بحرية.. يُحفظ تلقائياً في التخزين المحلي" else "Write freely — auto-saved to local storage",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val currentDumpText = if (isArabic) dumpAr else dumpEn
                OutlinedTextField(
                    value = currentDumpText,
                    onValueChange = { newText ->
                        if (isArabic) {
                            dumpAr = newText
                            onSaveBrainDump(dumpEn, newText)
                        } else {
                            dumpEn = newText
                            onSaveBrainDump(newText, dumpAr)
                        }
                    },
                    placeholder = {
                        Text(
                            text = if (isArabic) "أفرغي أفكاركِ العشوائية وملاحظاتكِ السريعة هنا..." else "Jot down unstructured thoughts, ideas & reminders...",
                            color = MutedCocoa
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = palette.deepAccent,
                        unfocusedBorderColor = palette.borderTint,
                        focusedContainerColor = palette.background.copy(alpha = 0.65f),
                        unfocusedContainerColor = palette.background.copy(alpha = 0.45f),
                        cursorColor = WarmSlateBrown
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_brain_dump_input")
                )
            }
        }

        // 4. TOP 3 PRIORITIES (المهام العاجلة - 3 مهام فقط بتصميم Checkbox)
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = palette.cardSurface,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, palette.borderTint, RoundedCornerShape(26.dp))
                .testTag("top_priorities_section")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isArabic) "المهام العاجلة اليوم (3 مهام فقط) 🎯" else "Top 3 Daily Priorities 🎯",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = WarmSlateBrown
                        )
                        Text(
                            text = if (isArabic) "ركّزي على 3 أولويات أساسية ليوم هادئ ومنجز" else "Focus on 3 core tasks for a calm, productive day",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedCocoa
                        )
                    }
                    Surface(
                        color = palette.primaryPastel,
                        shape = CircleShape
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                text = "${topPriorities.take(3).count { it.isChecked }}/3",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    topPriorities.take(3).forEachIndexed { idx, priority ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (priority.isChecked) {
                                palette.primaryPastel.copy(alpha = 0.35f)
                            } else {
                                palette.background
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, palette.borderTint, RoundedCornerShape(18.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(if (priority.isChecked) palette.primaryPastel else CreamSurface)
                                        .border(
                                            width = 1.5.dp,
                                            color = if (priority.isChecked) WarmSlateBrown else palette.deepAccent,
                                            shape = CircleShape
                                        )
                                        .clickable { onTogglePriority(priority) }
                                        .testTag("toggle_priority_${idx + 1}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (priority.isChecked) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = WarmSlateBrown,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onEditPriority(priority) }
                                ) {
                                    Text(
                                        text = priority.displayTitle(isArabic),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            textDecoration = if (priority.isChecked) TextDecoration.LineThrough else TextDecoration.None
                                        ),
                                        color = if (priority.isChecked) MutedCocoa else WarmSlateBrown
                                    )
                                    if (priority.displaySubtitle(isArabic).isNotBlank()) {
                                        Text(
                                            text = priority.displaySubtitle(isArabic),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MutedCocoa,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { onEditPriority(priority) },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = if (isArabic) "تعديل" else "Edit",
                                        tint = MutedCocoa,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. VISUAL QUICK LINKS (الاختصارات البصرية - بطاقات مستطيلة تحمل صوراً مصغرة Thumbnails)
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = if (isArabic) "الاختصارات البصرية السريعة (Quick Links) ✨" else "Visual Quick Links ✨",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = WarmSlateBrown
            )

            val quickLinks = listOf(
                Triple(
                    PlannerSection.SKINCARE_GLOW,
                    "img_vision_morning_ritual",
                    if (isArabic) "روتين العناية بالبشرة" else "Skincare Routine"
                ),
                Triple(
                    PlannerSection.FITNESS_NUTRITION,
                    "img_outfit_sage_cardigan",
                    if (isArabic) "مخطط الوجبات واللياقة" else "Meal & Fitness Planner"
                ),
                Triple(
                    PlannerSection.OUTFITS_HIJAB,
                    "img_outfit_yellow_dress",
                    if (isArabic) "تنسيق الأزياء والحجاب" else "Wardrobe & Hijab"
                ),
                Triple(
                    PlannerSection.FUTURE_YOU,
                    "img_vision_cozy_sanctuary",
                    if (isArabic) "لوحة الإلهام والمستقبل" else "Vision Board Canvas"
                )
            )

            // 2x2 Rectangular Thumbnail Grid
            for (rowIndex in 0 until 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (colIndex in 0 until 2) {
                        val itemIndex = rowIndex * 2 + colIndex
                        val (targetSection, thumbPreset, linkLabel) = quickLinks[itemIndex]
                        Surface(
                            onClick = { onNavigateToSection(targetSection) },
                            shape = RoundedCornerShape(22.dp),
                            color = palette.cardSurface,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, palette.borderTint, RoundedCornerShape(22.dp))
                                .testTag("quick_link_${targetSection.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .border(1.dp, palette.primaryPastel, RoundedCornerShape(16.dp))
                                ) {
                                    PlannerCardImage(
                                        imageBase64 = null,
                                        presetDrawableName = thumbPreset,
                                        contentDescription = linkLabel,
                                        modifier = Modifier.size(56.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = linkLabel,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = WarmSlateBrown,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isArabic) "انتقال سريع ←" else "Open →",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = palette.deepAccent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
