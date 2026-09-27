package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.ImageBase64Helper
import com.example.model.AppLanguage
import com.example.model.HabitEntity
import com.example.model.PlannerCardEntity
import com.example.model.PlannerSection
import com.example.model.QuickAddMode
import com.example.model.SimulatedNotification
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.LocalPastelPalette
import com.example.ui.theme.MutedCocoa
import com.example.ui.theme.PastelThemePalette
import com.example.ui.theme.WarmBorder
import com.example.ui.theme.WarmSlateBrown
import kotlinx.coroutines.launch

/**
 * Floating (+) Media & Content Chooser Modal:
 * Asks the user to choose: "إضافة نص" (Add Text), "إضافة قائمة" (Add List), or "رفع صورة/فيديو" (Upload Photo/Video).
 */
@Composable
fun QuickAddTypeChooserDialog(
    section: PlannerSection,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSelectMode: (QuickAddMode) -> Unit,
    onAddHabitDirectly: () -> Unit
) {
    val palette = LocalPastelPalette.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.5.dp, palette.primaryPastel, RoundedCornerShape(28.dp))
                .testTag("quick_add_chooser_dialog"),
            shape = RoundedCornerShape(28.dp),
            color = palette.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionStickerIcon(section = section, size = 38.dp)
                        Column {
                            Text(
                                text = if (isArabic) "إضافة جديدة (+)" else "Quick Add (+)",
                                style = MaterialTheme.typography.titleLarge,
                                color = WarmSlateBrown
                            )
                            Text(
                                text = section.title(isArabic),
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = if (isArabic) "إغلاق" else "Close",
                            tint = WarmSlateBrown
                        )
                    }
                }

                // 3 Core Options: Add Text, Add List, Upload Photo/Video
                QuickAddMode.entries.forEach { mode ->
                    val icon = when (mode) {
                        QuickAddMode.ADD_TEXT -> Icons.Outlined.TextFields
                        QuickAddMode.ADD_LIST -> Icons.Outlined.FormatListBulleted
                        QuickAddMode.UPLOAD_MEDIA -> Icons.Outlined.AddPhotoAlternate
                    }
                    Surface(
                        onClick = {
                            if (section == PlannerSection.HABIT_TRACKER && mode != QuickAddMode.UPLOAD_MEDIA) {
                                onAddHabitDirectly()
                            } else {
                                onSelectMode(mode)
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = palette.cardSurface,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, palette.primaryPastel, RoundedCornerShape(20.dp))
                            .testTag("quick_add_mode_${mode.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(palette.primaryPastel.copy(alpha = 0.65f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = WarmSlateBrown,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.title(isArabic),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = WarmSlateBrown
                                )
                                Text(
                                    text = mode.desc(isArabic),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedCocoa
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditCardDialog(
    section: PlannerSection,
    initialCard: PlannerCardEntity?,
    defaultCategoryKey: String? = null,
    initialMode: QuickAddMode = QuickAddMode.ADD_TEXT,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSave: (PlannerCardEntity) -> Unit,
    onDelete: ((PlannerCardEntity) -> Unit)? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val palette = LocalPastelPalette.current

    val isEditing = initialCard != null
    var selectedCategory by remember {
        mutableStateOf(
            initialCard?.categoryKey
                ?: defaultCategoryKey
                ?: section.categories.firstOrNull()?.key.orEmpty()
        )
    }

    var titleEn by remember { mutableStateOf(initialCard?.titleEn.orEmpty()) }
    var titleAr by remember { mutableStateOf(initialCard?.titleAr.orEmpty()) }
    var subtitleEn by remember { mutableStateOf(initialCard?.subtitleEn.orEmpty()) }
    var subtitleAr by remember { mutableStateOf(initialCard?.subtitleAr.orEmpty()) }
    var badgeEn by remember { mutableStateOf(initialCard?.badgeEn.orEmpty()) }
    var badgeAr by remember { mutableStateOf(initialCard?.badgeAr.orEmpty()) }

    var bellEnabled by remember { mutableStateOf(initialCard?.bellEnabled ?: false) }
    var bellTime by remember { mutableStateOf(initialCard?.bellTime?.ifBlank { "18:00" } ?: "18:00") }

    var showBothLanguages by remember { mutableStateOf(true) }
    var imageBase64 by remember { mutableStateOf(initialCard?.imageBase64) }
    var presetDrawableName by remember { mutableStateOf(initialCard?.presetDrawableName) }
    var aspectRatio by remember { mutableFloatStateOf(initialCard?.aspectRatio ?: 0.8f) }

    val mediaType = remember(initialCard, initialMode, imageBase64, presetDrawableName) {
        when {
            !imageBase64.isNullOrBlank() || !presetDrawableName.isNullOrBlank() -> "media"
            initialMode == QuickAddMode.ADD_LIST -> "list"
            else -> initialCard?.mediaType ?: "text"
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                val result = ImageBase64Helper.uriToBase64AndAspect(context, uri)
                if (result != null) {
                    imageBase64 = result.first
                    aspectRatio = result.second
                    presetDrawableName = null
                }
            }
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = palette.deepAccent,
        unfocusedBorderColor = palette.borderTint,
        focusedLabelColor = WarmSlateBrown,
        unfocusedLabelColor = MutedCocoa,
        cursorColor = WarmSlateBrown,
        focusedContainerColor = palette.cardSurface,
        unfocusedContainerColor = palette.cardSurface
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(28.dp))
                .border(1.5.dp, palette.primaryPastel, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = palette.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionStickerIcon(section = section, size = 38.dp)
                        Column {
                            Text(
                                text = if (isEditing) {
                                    if (isArabic) "تعديل البطاقة" else "Edit Card"
                                } else {
                                    initialMode.title(isArabic) + " • " + section.shortTitle(isArabic)
                                },
                                style = MaterialTheme.typography.titleLarge,
                                color = WarmSlateBrown
                            )
                            Text(
                                text = if (isArabic) "تحويل الصور إلى Base64 وحفظ محلي فوري" else "Instant offline Base64 & local storage",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_card_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = if (isArabic) "إغلاق" else "Close",
                            tint = WarmSlateBrown
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Category Selector
                if (section.categories.size > 1) {
                    Text(
                        text = if (isArabic) "القسم الفرعي / العمود" else "Column / Category",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmSlateBrown
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        section.categories.forEach { cat ->
                            val selected = selectedCategory == cat.key
                            FilterChip(
                                selected = selected,
                                onClick = { selectedCategory = cat.key },
                                label = {
                                    Text(
                                        text = cat.title(isArabic),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                shape = CircleShape,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = palette.primaryPastel,
                                    selectedLabelColor = WarmSlateBrown,
                                    containerColor = palette.cardSurface,
                                    labelColor = MutedCocoa
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Bilingual input toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.primaryPastel.copy(alpha = 0.35f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Translate,
                            contentDescription = null,
                            tint = WarmSlateBrown,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isArabic) "إدخال ثنائي اللغة (EN + AR)" else "Bilingual Fields (EN + AR)",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmSlateBrown
                        )
                    }
                    Switch(
                        checked = showBothLanguages,
                        onCheckedChange = { showBothLanguages = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = WarmSlateBrown,
                            checkedTrackColor = palette.primaryPastel
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title Inputs
                if (showBothLanguages || !isArabic) {
                    OutlinedTextField(
                        value = titleEn,
                        onValueChange = { titleEn = it },
                        label = { Text(if (isArabic) "العنوان بالإنجليزية (EN)" else "Title (English)") },
                        placeholder = { Text("e.g., Pastel Yellow Dress / Dr. Althea 345") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_title_en_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (showBothLanguages || isArabic) {
                    OutlinedTextField(
                        value = titleAr,
                        onValueChange = { titleAr = it },
                        label = { Text(if (isArabic) "العنوان بالعربية (AR)" else "Title (Arabic / العربية)") },
                        placeholder = { Text("مثال: تنسيق فستان أصفر باستيل / مقشر القهوة") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_title_ar_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Subtitle / Steps / Styling Notes Inputs
                val detailsLabelEn = if (initialMode == QuickAddMode.ADD_LIST) {
                    "List Steps (One step per line - EN)"
                } else {
                    "Notes / Details (e.g., Perfect for Spring - EN)"
                }
                val detailsLabelAr = if (initialMode == QuickAddMode.ADD_LIST) {
                    "عناصر القائمة أو الخطوات (كل خطوة في سطر - AR)"
                } else {
                    "الملاحظات أو الوصف (مثال: مثالي لفصل الربيع - AR)"
                }

                if (showBothLanguages || !isArabic) {
                    OutlinedTextField(
                        value = subtitleEn,
                        onValueChange = { subtitleEn = it },
                        label = { Text(detailsLabelEn) },
                        placeholder = { Text("Step 1...\nStep 2... or styling note underneath photo") },
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_subtitle_en_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (showBothLanguages || isArabic) {
                    OutlinedTextField(
                        value = subtitleAr,
                        onValueChange = { subtitleAr = it },
                        label = { Text(detailsLabelAr) },
                        placeholder = { Text("الخطوة الأولى...\nالخطوة الثانية... أو ملاحظة تحت الصورة") },
                        minLines = 3,
                        maxLines = 6,
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("card_subtitle_ar_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Badge / Time Slot Inputs (e.g., AM / PM)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (showBothLanguages || !isArabic) {
                        OutlinedTextField(
                            value = badgeEn,
                            onValueChange = { badgeEn = it },
                            label = { Text(if (isArabic) "الوقت/الوسم (EN)" else "Time/Tag (EN)") },
                            placeholder = { Text("AM / PM / Spring") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = textFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (showBothLanguages || isArabic) {
                        OutlinedTextField(
                            value = badgeAr,
                            onValueChange = { badgeAr = it },
                            label = { Text(if (isArabic) "الوقت/الوسم (AR)" else "Time/Tag (AR)") },
                            placeholder = { Text("صباحاً / مساءً / ربيع") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = textFieldColors,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Bell Notification Alert Setting (Especially for Kanban Shopping Cards)
                if (section == PlannerSection.FORGET_ME_NOT) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(palette.primaryPastel.copy(alpha = 0.4f))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (bellEnabled) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsNone,
                                contentDescription = null,
                                tint = WarmSlateBrown
                            )
                            Column {
                                Text(
                                    text = if (isArabic) "تفعيل جرس التذكير 🔔" else "Enable Bell Reminder 🔔",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = WarmSlateBrown
                                )
                                Text(
                                    text = if (isArabic) "الوقت المحدد: \u200E$bellTime\u200E" else "Scheduled time: $bellTime",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedCocoa
                                )
                            }
                        }
                        Switch(
                            checked = bellEnabled,
                            onCheckedChange = { bellEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = WarmSlateBrown,
                                checkedTrackColor = palette.primaryPastel
                            )
                        )
                    }
                    if (bellEnabled) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("10:00", "14:00", "17:00", "20:00").forEach { presetTime ->
                                val isTimeSelected = bellTime == presetTime
                                Surface(
                                    onClick = { bellTime = presetTime },
                                    shape = CircleShape,
                                    color = if (isTimeSelected) palette.primaryPastel else palette.cardSurface,
                                    modifier = Modifier.border(
                                        1.dp,
                                        if (isTimeSelected) WarmSlateBrown else palette.borderTint,
                                        CircleShape
                                    )
                                ) {
                                    Text(
                                        text = "\u200E$presetTime\u200E",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = WarmSlateBrown,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Base64 Local Photo Upload Section
                if (section.supportsPhotoUpload) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (isArabic) "الوسائط والصور (تُحفظ محلياً بصيغة Base64 للعمل دون اتصال)" else "Media & Photos (Encoded as Base64 for offline mode)",
                        style = MaterialTheme.typography.labelLarge,
                        color = WarmSlateBrown
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!imageBase64.isNullOrBlank() || !presetDrawableName.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(165.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .border(1.dp, palette.borderTint, RoundedCornerShape(18.dp))
                        ) {
                            PlannerCardImage(
                                imageBase64 = imageBase64,
                                presetDrawableName = presetDrawableName,
                                contentDescription = "Selected inspiration photo",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(165.dp)
                            )
                            Surface(
                                onClick = {
                                    imageBase64 = null
                                    presetDrawableName = null
                                },
                                shape = CircleShape,
                                color = CreamSurface.copy(alpha = 0.92f),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = if (isArabic) "إزالة الصورة" else "Remove photo",
                                    tint = WarmSlateBrown,
                                    modifier = Modifier
                                        .padding(6.dp)
                                        .size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("upload_local_photo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddPhotoAlternate,
                            contentDescription = null,
                            tint = WarmSlateBrown
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isArabic) "رفع صورة من الهاتف (Base64)" else "Upload Photo from Device (Base64)",
                            color = WarmSlateBrown,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isArabic) "أو اختاري من صور الاستوديو المحلية الجاهزة:" else "Or choose a bundled offline studio photo:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedCocoa
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val presets = listOf(
                        Triple("img_outfit_yellow_dress", "Pastel Yellow Dress", "فستان أصفر باستيل"),
                        Triple("img_outfit_mauve_hijab", "Mauve Hijab + Dress", "حجاب موف وفستان"),
                        Triple("img_outfit_sage_cardigan", "Sage Cardigan", "كارديغان ميرمية"),
                        Triple("img_vision_morning_ritual", "Matcha & Face Yoga", "ماتشا ويوغا الوجه"),
                        Triple("img_vision_cozy_sanctuary", "Cozy Sanctuary", "ركن القراءة")
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presets.forEach { (presetKey, labelEn, labelAr) ->
                            val isSelectedPreset = presetDrawableName == presetKey && imageBase64 == null
                            Surface(
                                onClick = {
                                    presetDrawableName = presetKey
                                    imageBase64 = null
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelectedPreset) palette.primaryPastel else palette.cardSurface,
                                modifier = Modifier.border(
                                    1.dp,
                                    if (isSelectedPreset) WarmSlateBrown else palette.borderTint,
                                    RoundedCornerShape(12.dp)
                                )
                            ) {
                                Text(
                                    text = if (isArabic) labelAr else labelEn,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmSlateBrown,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (isEditing && onDelete != null && initialCard != null) {
                        TextButton(
                            onClick = { onDelete(initialCard) },
                            modifier = Modifier.testTag("delete_card_button")
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = null,
                                tint = WarmSlateBrown
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isArabic) "حذف" else "Delete",
                                color = WarmSlateBrown
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = if (isArabic) "إلغاء" else "Cancel",
                                color = MutedCocoa
                            )
                        }
                        Button(
                            onClick = {
                                val finalTitleEn = titleEn.trim().ifBlank { titleAr.trim() }
                                val finalTitleAr = titleAr.trim().ifBlank { titleEn.trim() }
                                if (finalTitleEn.isNotBlank() || finalTitleAr.isNotBlank()) {
                                    val finalSubEn = subtitleEn.trim().ifBlank { subtitleAr.trim() }
                                    val finalSubAr = subtitleAr.trim().ifBlank { subtitleEn.trim() }
                                    val finalBadgeEn = badgeEn.trim().ifBlank { badgeAr.trim() }
                                    val finalBadgeAr = badgeAr.trim().ifBlank { badgeEn.trim() }

                                    onSave(
                                        PlannerCardEntity(
                                            id = initialCard?.id ?: 0,
                                            sectionId = section.id,
                                            categoryKey = selectedCategory,
                                            titleEn = finalTitleEn,
                                            titleAr = finalTitleAr,
                                            subtitleEn = finalSubEn,
                                            subtitleAr = finalSubAr,
                                            badgeEn = finalBadgeEn,
                                            badgeAr = finalBadgeAr,
                                            isChecked = initialCard?.isChecked ?: false,
                                            imageBase64 = imageBase64,
                                            presetDrawableName = presetDrawableName,
                                            aspectRatio = aspectRatio,
                                            bellEnabled = bellEnabled,
                                            bellTime = bellTime,
                                            offsetX = initialCard?.offsetX ?: 0f,
                                            offsetY = initialCard?.offsetY ?: 0f,
                                            mediaType = mediaType,
                                            timestamp = initialCard?.timestamp ?: System.currentTimeMillis()
                                        )
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WarmSlateBrown,
                                contentColor = CreamSurface
                            ),
                            modifier = Modifier.testTag("save_card_button")
                        ) {
                            Text(
                                text = if (isArabic) "حفظ" else "Save",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddEditHabitDialog(
    initialHabit: HabitEntity?,
    isArabic: Boolean,
    onDismiss: () -> Unit,
    onSave: (HabitEntity) -> Unit,
    onDelete: ((HabitEntity) -> Unit)? = null
) {
    val palette = LocalPastelPalette.current
    val isEditing = initialHabit != null
    var nameEn by remember { mutableStateOf(initialHabit?.nameEn.orEmpty()) }
    var nameAr by remember { mutableStateOf(initialHabit?.nameAr.orEmpty()) }
    var targetEn by remember { mutableStateOf(initialHabit?.targetEn ?: "Daily") }
    var targetAr by remember { mutableStateOf(initialHabit?.targetAr ?: "يومياً") }

    val section = PlannerSection.HABIT_TRACKER
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = palette.deepAccent,
        unfocusedBorderColor = palette.borderTint,
        focusedLabelColor = WarmSlateBrown,
        unfocusedLabelColor = MutedCocoa,
        cursorColor = WarmSlateBrown,
        focusedContainerColor = palette.cardSurface,
        unfocusedContainerColor = palette.cardSurface
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.5.dp, palette.primaryPastel, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = palette.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionStickerIcon(section = section, size = 38.dp)
                    Text(
                        text = if (isEditing) {
                            if (isArabic) "تعديل العادة" else "Edit Habit"
                        } else {
                            if (isArabic) "إضافة عادة جديدة" else "New Weekly Habit"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = WarmSlateBrown
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(if (isArabic) "اسم العادة (EN)" else "Habit Name (English)") },
                    placeholder = { Text("e.g., Reading / Walking / Sleep") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("habit_name_en_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = nameAr,
                    onValueChange = { nameAr = it },
                    label = { Text(if (isArabic) "اسم العادة (AR)" else "Habit Name (Arabic / العربية)") },
                    placeholder = { Text("مثال: قراءة / مشي / نوم") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = textFieldColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("habit_name_ar_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = targetEn,
                        onValueChange = { targetEn = it },
                        label = { Text("Goal (EN)") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = targetAr,
                        onValueChange = { targetAr = it },
                        label = { Text("الهدف (AR)") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isEditing && initialHabit != null && onDelete != null) {
                        TextButton(onClick = { onDelete(initialHabit) }) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = null,
                                tint = WarmSlateBrown
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isArabic) "حذف" else "Delete", color = WarmSlateBrown)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onDismiss) {
                            Text(if (isArabic) "إلغاء" else "Cancel", color = MutedCocoa)
                        }
                        Button(
                            onClick = {
                                val finalEn = nameEn.trim().ifBlank { nameAr.trim() }
                                val finalAr = nameAr.trim().ifBlank { nameEn.trim() }
                                if (finalEn.isNotBlank() || finalAr.isNotBlank()) {
                                    onSave(
                                        HabitEntity(
                                            id = initialHabit?.id ?: 0,
                                            nameEn = finalEn,
                                            nameAr = finalAr,
                                            targetEn = targetEn.trim().ifBlank { "Daily" },
                                            targetAr = targetAr.trim().ifBlank { "يومياً" },
                                            mon = initialHabit?.mon ?: false,
                                            tue = initialHabit?.tue ?: false,
                                            wed = initialHabit?.wed ?: false,
                                            thu = initialHabit?.thu ?: false,
                                            fri = initialHabit?.fri ?: false,
                                            sat = initialHabit?.sat ?: false,
                                            sun = initialHabit?.sun ?: false,
                                            timestamp = initialHabit?.timestamp ?: System.currentTimeMillis()
                                        )
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WarmSlateBrown,
                                contentColor = CreamSurface
                            ),
                            modifier = Modifier.testTag("save_habit_button")
                        ) {
                            Text(if (isArabic) "حفظ" else "Save", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Top Bar Notifications Center Modal (أيقونة التنبيهات) showing active Bell Reminders from Kanban cards.
 */
@Composable
fun NotificationsCenterDialog(
    notifications: List<SimulatedNotification>,
    bellCards: List<PlannerCardEntity>,
    isArabic: Boolean,
    onTriggerSimulatedAlert: (PlannerCardEntity) -> Unit,
    onClearNotifications: () -> Unit,
    onDismiss: () -> Unit
) {
    val palette = LocalPastelPalette.current

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.5.dp, palette.primaryPastel, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = palette.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(palette.primaryPastel),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.NotificationsActive,
                                contentDescription = null,
                                tint = WarmSlateBrown
                            )
                        }
                        Column {
                            Text(
                                text = if (isArabic) "مركز التنبيهات الذكية 🔔" else "Smart Bell Reminders 🔔",
                                style = MaterialTheme.typography.titleLarge,
                                color = WarmSlateBrown
                            )
                            Text(
                                text = if (isArabic) "إشعارات المشتريات والمهام المفعلة" else "Active shopping & daily bell alerts",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = WarmSlateBrown
                        )
                    }
                }

                if (notifications.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isArabic) "التنبيهات المرسلة مؤخراً" else "Recent Simulated Alerts",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmSlateBrown
                        )
                        TextButton(onClick = onClearNotifications) {
                            Text(
                                text = if (isArabic) "مسح الكل" else "Clear All",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    notifications.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = palette.primaryPastel.copy(alpha = 0.45f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, palette.primaryPastel, RoundedCornerShape(18.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.NotificationsActive,
                                    contentDescription = null,
                                    tint = WarmSlateBrown,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.title(isArabic),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = WarmSlateBrown
                                    )
                                    Text(
                                        text = "${item.category(isArabic)} • \u200E${item.scheduledTime}\u200E",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MutedCocoa
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = if (isArabic) "بطاقات المشتريات المفعّل بها الجرس" else "Scheduled Kanban Bell Cards",
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmSlateBrown
                )

                if (bellCards.isEmpty()) {
                    Text(
                        text = if (isArabic) "لا توجد بطاقات مفعّل بها الجرس حالياً. اضغطي على أيقونة الجرس في قسم (لا تنسي)." else "No active bell reminders yet. Tap the bell icon on any card in Forget-Me-Not.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedCocoa
                    )
                } else {
                    bellCards.forEach { card ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = palette.cardSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, palette.borderTint, RoundedCornerShape(18.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = card.displayTitle(isArabic),
                                        style = MaterialTheme.typography.titleSmall,
                                        color = WarmSlateBrown
                                    )
                                    Text(
                                        text = if (isArabic) "موعد التنبيه: \u200E${card.bellTime.ifBlank { "18:00" }}\u200E" else "Alert at: ${card.bellTime.ifBlank { "18:00" }}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MutedCocoa
                                    )
                                }
                                TextButton(onClick = { onTriggerSimulatedAlert(card) }) {
                                    Text(
                                        text = if (isArabic) "تجربة التنبيه الآن" else "Simulate Now",
                                        style = MaterialTheme.typography.labelLarge,
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

/**
 * Top Bar Settings Wheel Modal (عجلة الإعدادات):
 * Allows switching between the 6 Pastel Color Palettes, toggling language (EN/AR), and viewing Offline PWA Vault status.
 */
@Composable
fun SettingsModalDialog(
    currentPalette: PastelThemePalette,
    currentLanguage: AppLanguage,
    totalSavedCards: Int,
    onSelectPalette: (PastelThemePalette) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val isArabic = currentLanguage.isArabic

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .border(1.5.dp, currentPalette.primaryPastel, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            color = currentPalette.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
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
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(currentPalette.primaryPastel),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Palette,
                                contentDescription = null,
                                tint = WarmSlateBrown
                            )
                        }
                        Column {
                            Text(
                                text = if (isArabic) "إعدادات التطبيق والسمات ⚙️" else "Settings & Pastel Themes ⚙️",
                                style = MaterialTheme.typography.titleLarge,
                                color = WarmSlateBrown
                            )
                            Text(
                                text = if (isArabic) "تخصيص الألوان واللغة والعمل دون اتصال" else "Customize 6 pastel palettes & language",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = WarmSlateBrown
                        )
                    }
                }

                // Language Selection
                Text(
                    text = if (isArabic) "لغة الواجهة والاتجاه (RTL / LTR)" else "Language & Layout Direction",
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmSlateBrown
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AppLanguage.entries.forEach { lang ->
                        val selected = lang == currentLanguage
                        Surface(
                            onClick = { onSelectLanguage(lang) },
                            shape = RoundedCornerShape(16.dp),
                            color = if (selected) currentPalette.primaryPastel else currentPalette.cardSurface,
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    width = if (selected) 1.5.dp else 1.dp,
                                    color = if (selected) WarmSlateBrown else currentPalette.borderTint,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (lang == AppLanguage.AR) "العربية (AR)" else "English (EN)",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = WarmSlateBrown
                                )
                                Text(
                                    text = if (lang == AppLanguage.AR) "اتجاه من اليمين لليسار" else "Left-to-Right",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedCocoa
                                )
                            }
                        }
                    }
                }

                // The 6 Pastel Color Palettes
                Text(
                    text = if (isArabic) "لوحة ألوان السمات الستة (6 Pastel Palettes)" else "The 6 Pastel Color Palettes",
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmSlateBrown
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PastelThemePalette.entries.forEach { themePalette ->
                        val isSelected = themePalette == currentPalette
                        Surface(
                            onClick = { onSelectPalette(themePalette) },
                            shape = RoundedCornerShape(18.dp),
                            color = themePalette.cardSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) WarmSlateBrown else themePalette.borderTint,
                                    shape = RoundedCornerShape(18.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Swatch preview circles
                                Row(horizontalArrangement = Arrangement.spacedBy((-6).dp)) {
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(themePalette.primaryPastel)
                                            .border(1.dp, WarmSlateBrown.copy(alpha = 0.4f), CircleShape)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(themePalette.secondaryPastel)
                                            .border(1.dp, WarmSlateBrown.copy(alpha = 0.4f), CircleShape)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(themePalette.deepAccent)
                                            .border(1.dp, WarmSlateBrown.copy(alpha = 0.4f), CircleShape)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${themePalette.nameEn} • ${themePalette.nameAr}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = WarmSlateBrown
                                    )
                                    Text(
                                        text = themePalette.displayDesc(isArabic),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MutedCocoa
                                    )
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = WarmSlateBrown,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Offline Storage Info
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = currentPalette.primaryPastel.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudDone,
                            contentDescription = null,
                            tint = WarmSlateBrown
                        )
                        Column {
                            Text(
                                text = if (isArabic) "التخزين المحلي دون اتصال (Offline Vault)" else "Offline-First Local Storage",
                                style = MaterialTheme.typography.titleSmall,
                                color = WarmSlateBrown
                            )
                            Text(
                                text = if (isArabic) {
                                    "عدد البطاقات والصور المحفوظة بصيغة Base64: \u200E$totalSavedCards\u200E (يعمل في وضع الطيران)"
                                } else {
                                    "Saved Base64 cards & items: $totalSavedCards (Works in Airplane Mode)"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }
                    }
                }
            }
        }
    }
}
