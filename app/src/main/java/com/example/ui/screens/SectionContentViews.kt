package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.OpenWith
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.model.HabitEntity
import com.example.model.NoteBlockEntity
import com.example.model.PlannerCardEntity
import com.example.model.PlannerSection
import com.example.model.ltrNumber
import com.example.ui.components.PlannerCardImage
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.LocalPastelPalette
import com.example.ui.theme.MutedCocoa
import com.example.ui.theme.WarmSlateBrown
import kotlin.math.roundToInt

/**
 * 1. SKINCARE & FACE CARE (قسم العناية بالبشرة والوجه)
 * 2-Column Grid:
 * - Right Column (in RTL) / Column 1: Chronological Product Routine (AM/PM) with [Edit/Delete] buttons
 * - Left Column (in RTL) / Column 2: Media, Routine Results & Editable Face Yoga Massage Steps
 */
@Composable
fun SkincareGlowSectionView(
    cards: List<PlannerCardEntity>,
    isArabic: Boolean,
    onToggleCard: (PlannerCardEntity) -> Unit,
    onEditCard: (PlannerCardEntity) -> Unit,
    onUpdateCardDirectly: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onAddCardForCategory: (String) -> Unit
) {
    val palette = LocalPastelPalette.current
    val routineProducts = cards.filter { it.categoryKey == "hydration_routine" }
    val sunCareCards = cards.filter { it.categoryKey == "sun_care" }
    val mediaAndYoga = cards.filter { it.categoryKey != "hydration_routine" && it.categoryKey != "sun_care" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("skincare_two_column_section"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 2-Column Grid (شبكة من عمودين: العمود الأيمن للروتين، والعمود الأيسر للوسائط والوصفات)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Column 1 (Right in RTL): Routine Products ordered chronologically (AM / PM)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .testTag("skincare_routine_column"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ColumnHeaderPill(
                    title = if (isArabic) "الروتين (صباحاً/مساءً) 🧴" else "AM/PM Routine 🧴",
                    count = routineProducts.size,
                    onAdd = { onAddCardForCategory("hydration_routine") }
                )

                routineProducts.forEach { product ->
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = palette.cardSurface,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, palette.borderTint, RoundedCornerShape(22.dp))
                            .testTag("skincare_product_${product.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (product.displayBadge(isArabic).isNotBlank()) {
                                    Surface(
                                        color = palette.primaryPastel,
                                        shape = CircleShape
                                    ) {
                                        Text(
                                            text = product.displayBadge(isArabic),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = WarmSlateBrown,
                                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (product.isChecked) palette.primaryPastel else CreamSurface)
                                        .border(1.2.dp, WarmSlateBrown, CircleShape)
                                        .clickable { onToggleCard(product) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (product.isChecked) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = WarmSlateBrown,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product.displayTitle(isArabic),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown
                            )

                            if (product.displaySubtitle(isArabic).isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = product.displaySubtitle(isArabic),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedCocoa
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Explicit [تعديل / حذف] Buttons to swap or delete the product
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    onClick = { onEditCard(product) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = palette.secondaryPastel,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 6.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            tint = WarmSlateBrown,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isArabic) "تعديل" else "Edit",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = WarmSlateBrown
                                        )
                                    }
                                }
                                Surface(
                                    onClick = { onDeleteCard(product) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = CreamBackground,
                                    modifier = Modifier.border(1.dp, palette.borderTint, RoundedCornerShape(12.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = if (isArabic) "حذف" else "Delete",
                                        tint = MutedCocoa,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 6.dp)
                                            .size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Column 2 (Left in RTL): Media, Uploaded Result Photos & Editable Face Yoga Steps
            Column(
                modifier = Modifier
                    .weight(1f)
                    .testTag("skincare_media_column"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ColumnHeaderPill(
                    title = if (isArabic) "الوسائط ويوغا الوجه ✨" else "Media & Face Yoga ✨",
                    count = mediaAndYoga.size,
                    onAdd = { onAddCardForCategory("natural_recipes") }
                )

                mediaAndYoga.forEach { item ->
                    val hasImage = !item.imageBase64.isNullOrBlank() || !item.presetDrawableName.isNullOrBlank()
                    var isEditingInline by remember { mutableStateOf(false) }
                    var inlineText by remember(item.subtitleAr, item.subtitleEn, isArabic) {
                        mutableStateOf(item.displaySubtitle(isArabic))
                    }

                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = palette.cardSurface,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, palette.borderTint, RoundedCornerShape(22.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (hasImage) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { onEditCard(item) }
                                ) {
                                    PlannerCardImage(
                                        imageBase64 = item.imageBase64,
                                        presetDrawableName = item.presetDrawableName,
                                        contentDescription = item.displayTitle(isArabic),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Text(
                                text = item.displayTitle(isArabic),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            if (isEditingInline) {
                                OutlinedTextField(
                                    value = inlineText,
                                    onValueChange = { inlineText = it },
                                    minLines = 3,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = {
                                            val updated = if (isArabic) {
                                                item.copy(subtitleAr = inlineText)
                                            } else {
                                                item.copy(subtitleEn = inlineText)
                                            }
                                            onUpdateCardDirectly(updated)
                                            isEditingInline = false
                                        }
                                    ) {
                                        Text(
                                            text = if (isArabic) "حفظ الخطوات" else "Save Steps",
                                            color = palette.deepAccent,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = item.displaySubtitle(isArabic),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedCocoa,
                                    modifier = Modifier.clickable { isEditingInline = true }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isArabic) "انقري لتعديل النص أو الصورة" else "Tap to edit text or photo",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = palette.deepAccent,
                                    modifier = Modifier.clickable { isEditingInline = !isEditingInline }
                                )
                                Row {
                                    IconButton(
                                        onClick = { onEditCard(item) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.AddPhotoAlternate,
                                            contentDescription = null,
                                            tint = WarmSlateBrown,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = { onDeleteCard(item) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.DeleteOutline,
                                            contentDescription = null,
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
        }

        // 3. Sun Care SPF 50+ Reapplication Reminder Section (حماية الشمس وتجديد SPF 50+ كل 2-3 ساعات)
        ColumnHeaderPill(
            title = if (isArabic) "حماية الشمس وتجديد الواقي (SPF 50+) ☀️" else "Sun Care SPF 50+ Reapplication ☀️",
            count = sunCareCards.size,
            onAdd = { onAddCardForCategory("sun_care") }
        )

        sunCareCards.forEach { sunCard ->
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.2.dp, palette.primaryPastel, RoundedCornerShape(22.dp))
                    .testTag("sun_care_card_${sunCard.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (sunCard.isChecked) palette.primaryPastel else CreamSurface)
                            .border(1.5.dp, WarmSlateBrown, CircleShape)
                            .clickable { onToggleCard(sunCard) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (sunCard.isChecked) {
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
                            .clickable { onEditCard(sunCard) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = sunCard.displayTitle(isArabic),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown
                            )
                            if (sunCard.displayBadge(isArabic).isNotBlank()) {
                                Surface(color = palette.primaryPastel, shape = CircleShape) {
                                    Text(
                                        text = sunCard.displayBadge(isArabic),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = WarmSlateBrown,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        if (sunCard.displaySubtitle(isArabic).isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sunCard.displaySubtitle(isArabic),
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    IconButton(onClick = { onEditCard(sunCard) }, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = MutedCocoa,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = { onDeleteCard(sunCard) }, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MutedCocoa,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 2. WARDROBE & HIJAB STYLING (قسم الأزياء وتنسيق الإطلالات)
 * Pinterest-style Masonry Grid where clicking on any photo expands an inline note editor underneath
 * (e.g., "مثالي لفصل الربيع" / "Perfect for Spring").
 */
@Composable
fun OutfitsHijabSectionView(
    cards: List<PlannerCardEntity>,
    isArabic: Boolean,
    onEditCard: (PlannerCardEntity) -> Unit,
    onUpdateCardDirectly: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onAddCardForCategory: (String) -> Unit
) {
    val palette = LocalPastelPalette.current
    var expandedNoteCardId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upload New Outfit / Hijab Styling Banner
        Surface(
            onClick = { onAddCardForCategory("everyday_look") },
            shape = RoundedCornerShape(24.dp),
            color = palette.primaryPastel.copy(alpha = 0.42f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, palette.primaryPastel, RoundedCornerShape(24.dp))
                .testTag("add_outfit_photo_banner")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(color = CreamSurface, shape = CircleShape) {
                        Icon(
                            imageVector = Icons.Outlined.AddPhotoAlternate,
                            contentDescription = null,
                            tint = WarmSlateBrown,
                            modifier = Modifier
                                .padding(10.dp)
                                .size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isArabic) "ارفعي صورة إطلالة أو تنسيق حجاب جديد" else "Upload New Outfit or Hijab Styling",
                            style = MaterialTheme.typography.titleMedium,
                            color = WarmSlateBrown
                        )
                        Text(
                            text = if (isArabic) "انقري على أي صورة لإضافة ملاحظة نصية تحتها (مثال: مثالي لفصل الربيع)" else "Tap any photo to write styling notes underneath (e.g., Perfect for Spring)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedCocoa
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    tint = WarmSlateBrown
                )
            }
        }

        // Pinterest 2-Column Masonry Grid
        val leftColumn = cards.filterIndexed { index, _ -> index % 2 == 0 }
        val rightColumn = cards.filterIndexed { index, _ -> index % 2 == 1 }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("pinterest_masonry_grid"),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                leftColumn.forEach { card ->
                    WardrobePinterestCard(
                        card = card,
                        isArabic = isArabic,
                        isNoteEditorOpen = expandedNoteCardId == card.id,
                        onPhotoTap = {
                            expandedNoteCardId = if (expandedNoteCardId == card.id) null else card.id
                        },
                        onSaveNote = { newNote ->
                            val updated = if (isArabic) {
                                card.copy(subtitleAr = newNote)
                            } else {
                                card.copy(subtitleEn = newNote)
                            }
                            onUpdateCardDirectly(updated)
                            expandedNoteCardId = null
                        },
                        onFullEdit = { onEditCard(card) },
                        onDelete = { onDeleteCard(card) }
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rightColumn.forEach { card ->
                    WardrobePinterestCard(
                        card = card,
                        isArabic = isArabic,
                        isNoteEditorOpen = expandedNoteCardId == card.id,
                        onPhotoTap = {
                            expandedNoteCardId = if (expandedNoteCardId == card.id) null else card.id
                        },
                        onSaveNote = { newNote ->
                            val updated = if (isArabic) {
                                card.copy(subtitleAr = newNote)
                            } else {
                                card.copy(subtitleEn = newNote)
                            }
                            onUpdateCardDirectly(updated)
                            expandedNoteCardId = null
                        },
                        onFullEdit = { onEditCard(card) },
                        onDelete = { onDeleteCard(card) }
                    )
                }
            }
        }
    }
}

@Composable
private fun WardrobePinterestCard(
    card: PlannerCardEntity,
    isArabic: Boolean,
    isNoteEditorOpen: Boolean,
    onPhotoTap: () -> Unit,
    onSaveNote: (String) -> Unit,
    onFullEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val palette = LocalPastelPalette.current
    val hasImage = !card.imageBase64.isNullOrBlank() || !card.presetDrawableName.isNullOrBlank()
    var noteDraft by remember(card.subtitleAr, card.subtitleEn, isArabic) {
        mutableStateOf(card.displaySubtitle(isArabic))
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = palette.cardSurface,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, palette.borderTint, RoundedCornerShape(24.dp))
            .testTag("masonry_card_${card.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (hasImage) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(card.aspectRatio.coerceIn(0.68f, 1.35f))
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .clickable { onPhotoTap() }
                ) {
                    PlannerCardImage(
                        imageBase64 = card.imageBase64,
                        presetDrawableName = card.presetDrawableName,
                        contentDescription = card.displayTitle(isArabic),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (card.displayBadge(isArabic).isNotBlank()) {
                        Surface(
                            color = CreamSurface.copy(alpha = 0.90f),
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = card.displayBadge(isArabic),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPhotoTap() }
                    .padding(14.dp)
            ) {
                Text(
                    text = card.displayTitle(isArabic),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = WarmSlateBrown
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.displaySubtitle(isArabic).ifBlank {
                        if (isArabic) "انقري لإضافة ملاحظة (مثال: مثالي لفصل الربيع)" else "Tap to add note (e.g., Perfect for Spring)"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedCocoa
                )

                // Inline Note Editor when Photo is tapped
                AnimatedVisibility(visible = isNoteEditorOpen) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        OutlinedTextField(
                            value = noteDraft,
                            onValueChange = { noteDraft = it },
                            label = {
                                Text(if (isArabic) "ملاحظة تحت الصورة" else "Note underneath photo")
                            },
                            placeholder = {
                                Text(if (isArabic) "مثالي لفصل الربيع..." else "Perfect for Spring...")
                            },
                            minLines = 2,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = onFullEdit) {
                                Text(
                                    text = if (isArabic) "تغيير الصورة" else "Change Photo",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedCocoa
                                )
                            }
                            Button(
                                onClick = { onSaveNote(noteDraft) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = palette.deepAccent,
                                    contentColor = CreamSurface
                                )
                            ) {
                                Text(
                                    text = if (isArabic) "حفظ" else "Save",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onFullEdit, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = if (isArabic) "تعديل" else "Edit",
                            tint = MutedCocoa,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = if (isArabic) "حذف" else "Delete",
                            tint = MutedCocoa,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 3. BODY CARE & EVERYTHING SHOWER (قسم العناية بالجسم)
 * Collapsible Accordion Lists (نظام القوائم القابلة للطي):
 * - Main titles like "روتين الاستحمام العميق" or "مقشر القهوة".
 * - Clicking the header expands the card to show numbered steps/ingredients + circular (+) button to add a new step or photo.
 */
@Composable
fun BodyCareSectionView(
    cards: List<PlannerCardEntity>,
    isArabic: Boolean,
    onEditCard: (PlannerCardEntity) -> Unit,
    onUpdateCardDirectly: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onAddCardForCategory: (String) -> Unit
) {
    val palette = LocalPastelPalette.current
    val expandedStateMap = remember {
        mutableStateMapOf<Int, Boolean>()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("body_care_accordion_section"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isArabic) "قوائم العناية بالجسم القابلة للطي (Accordion) 🛁" else "Collapsible Body Care Accordions 🛁",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = WarmSlateBrown
            )
            Surface(
                onClick = { onAddCardForCategory("shower_routine") },
                shape = CircleShape,
                color = palette.primaryPastel
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = WarmSlateBrown,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isArabic) "قائمة جديدة" else "New Accordion",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown
                    )
                }
            }
        }

        cards.forEachIndexed { index, card ->
            val isExpanded = expandedStateMap.getOrPut(card.id) { index == 0 }
            var showQuickAddStepInput by remember { mutableStateOf(false) }
            var newStepText by remember { mutableStateOf("") }

            val stepsList = remember(card.subtitleAr, card.subtitleEn, isArabic) {
                card.displaySubtitle(isArabic)
                    .split("\n")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
            }

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isExpanded) 1.5.dp else 1.dp,
                        color = if (isExpanded) palette.deepAccent.copy(alpha = 0.6f) else palette.borderTint,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .testTag("accordion_card_${card.id}")
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Accordion Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isExpanded) palette.primaryPastel.copy(alpha = 0.38f)
                                else Color.Transparent
                            )
                            .clickable {
                                expandedStateMap[card.id] = !isExpanded
                            }
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                            .testTag("accordion_header_${card.id}"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = card.displayTitle(isArabic),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = WarmSlateBrown
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = card.displayBadge(isArabic).ifBlank {
                                    if (isArabic) "${stepsList.size} خطوات" else "${stepsList.size} steps"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Circular (+) Button to add a step or photo
                            Surface(
                                onClick = {
                                    expandedStateMap[card.id] = true
                                    showQuickAddStepInput = !showQuickAddStepInput
                                },
                                shape = CircleShape,
                                color = palette.primaryPastel,
                                modifier = Modifier
                                    .size(34.dp)
                                    .border(1.dp, WarmSlateBrown.copy(alpha = 0.4f), CircleShape)
                                    .testTag("accordion_add_step_${card.id}")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = if (isArabic) "إضافة خطوة" else "Add step",
                                        tint = WarmSlateBrown,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = WarmSlateBrown
                            )
                        }
                    }

                    // Expandable Accordion Content
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Numbered Steps / Ingredients
                            stepsList.forEachIndexed { stepIdx, stepLine ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(palette.background)
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(palette.primaryPastel),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ltrNumber("${stepIdx + 1}"),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = WarmSlateBrown
                                        )
                                    }
                                    Text(
                                        text = stepLine.removePrefix("${stepIdx + 1}.").trim(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WarmSlateBrown,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            // Optional Result Photo
                            val hasPhoto = !card.imageBase64.isNullOrBlank() || !card.presetDrawableName.isNullOrBlank()
                            if (hasPhoto) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .border(1.dp, palette.borderTint, RoundedCornerShape(18.dp))
                                ) {
                                    PlannerCardImage(
                                        imageBase64 = card.imageBase64,
                                        presetDrawableName = card.presetDrawableName,
                                        contentDescription = card.displayTitle(isArabic),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                    )
                                }
                            }

                            // Inline (+) New Step Input Box
                            if (showQuickAddStepInput) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = newStepText,
                                        onValueChange = { newStepText = it },
                                        placeholder = {
                                            Text(if (isArabic) "اكتبي خطوة أو مقداراً جديداً..." else "Write a new step or ingredient...")
                                        },
                                        singleLine = true,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Button(
                                        onClick = {
                                            if (newStepText.isNotBlank()) {
                                                val updatedEn = if (card.subtitleEn.isBlank()) newStepText.trim()
                                                else "${card.subtitleEn}\n${newStepText.trim()}"
                                                val updatedAr = if (card.subtitleAr.isBlank()) newStepText.trim()
                                                else "${card.subtitleAr}\n${newStepText.trim()}"
                                                onUpdateCardDirectly(
                                                    card.copy(subtitleEn = updatedEn, subtitleAr = updatedAr)
                                                )
                                                newStepText = ""
                                                showQuickAddStepInput = false
                                            }
                                        },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = WarmSlateBrown,
                                            contentColor = CreamSurface
                                        )
                                    ) {
                                        Text(if (isArabic) "إضافة" else "Add")
                                    }
                                }
                            }

                            // Footer Actions: Add Step (+), Upload Result Photo, Edit, Delete
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    TextButton(onClick = { showQuickAddStepInput = !showQuickAddStepInput }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isArabic) "إضافة خطوة (+)" else "Add Step (+)",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                    TextButton(onClick = { onEditCard(card) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.AddPhotoAlternate,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isArabic) "صورة النتيجة" else "Result Photo",
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    }
                                }

                                Row {
                                    IconButton(onClick = { onEditCard(card) }, modifier = Modifier.size(32.dp)) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null,
                                            tint = MutedCocoa,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    IconButton(onClick = { onDeleteCard(card) }, modifier = Modifier.size(32.dp)) {
                                        Icon(
                                            imageVector = Icons.Outlined.DeleteOutline,
                                            contentDescription = null,
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
        }
    }
}

/**
 * 4. FORGET-ME-NOT & SMART LISTS (قسم "لا تنسي" — Kanban Board)
 * Divided columns (مشتريات الصيدلية Pharmacy, السوبرماركت Supermarket, ملابس Clothing).
 * Each card has a small Bell icon that toggles and simulates sending a time-based notification alert.
 */
@Composable
fun ForgetMeNotKanbanSectionView(
    cards: List<PlannerCardEntity>,
    isArabic: Boolean,
    onToggleCard: (PlannerCardEntity) -> Unit,
    onToggleBellAlert: (PlannerCardEntity) -> Unit,
    onEditCard: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onAddCardForCategory: (String) -> Unit
) {
    val palette = LocalPastelPalette.current
    val kanbanColumns = listOf(
        Triple("pharmacy", "Pharmacy 💊", "مشتريات الصيدلية 💊"),
        Triple("supermarket", "Supermarket 🛒", "السوبرماركت 🛒"),
        Triple("clothing", "Clothing 👗", "ملابس 👗"),
        Triple("budget_space", "Budget & Space 🛋️", "الميزانية والمحيط 🛋️")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("forget_me_not_kanban_board"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = if (isArabic) {
                "لوحة المشتريات الذكية (Kanban Board) • اضغطي على الجرس 🔔 لمحاكاة إرسال تنبيه في الوقت المحدد"
            } else {
                "Smart Shopping Kanban Board • Tap the Bell 🔔 on any card to simulate a scheduled alert"
            },
            style = MaterialTheme.typography.bodySmall,
            color = MutedCocoa
        )

        // Horizontally scrollable Kanban Columns (works great on both mobile & tablet)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            kanbanColumns.forEach { (colKey, titleEn, titleAr) ->
                val columnCards = cards.filter { it.categoryKey == colKey }
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = palette.primaryPastel.copy(alpha = 0.25f),
                    modifier = Modifier
                        .width(275.dp)
                        .border(1.dp, palette.borderTint, RoundedCornerShape(24.dp))
                        .testTag("kanban_column_$colKey")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Kanban Column Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (isArabic) titleAr else titleEn,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = WarmSlateBrown
                                )
                                Surface(
                                    color = palette.cardSurface,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = ltrNumber("${columnCards.size}"),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = WarmSlateBrown,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onAddCardForCategory(colKey) },
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .background(palette.cardSurface)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = if (isArabic) "إضافة" else "Add",
                                    tint = WarmSlateBrown,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Kanban Cards inside this Column
                        columnCards.forEach { card ->
                            val bellTint by animateColorAsState(
                                targetValue = if (card.bellEnabled) palette.deepAccent else MutedCocoa,
                                label = "bellTint"
                            )
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = palette.cardSurface,
                                shadowElevation = 2.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = if (card.bellEnabled) 1.4.dp else 1.dp,
                                        color = if (card.bellEnabled) palette.deepAccent.copy(alpha = 0.6f) else palette.borderTint,
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .testTag("kanban_card_${card.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(if (card.isChecked) palette.primaryPastel else CreamBackground)
                                                    .border(1.2.dp, WarmSlateBrown, CircleShape)
                                                    .clickable { onToggleCard(card) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (card.isChecked) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = WarmSlateBrown,
                                                        modifier = Modifier.size(13.dp)
                                                    )
                                                }
                                            }
                                            Text(
                                                text = card.displayTitle(isArabic),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    textDecoration = if (card.isChecked) TextDecoration.LineThrough else TextDecoration.None
                                                ),
                                                color = if (card.isChecked) MutedCocoa else WarmSlateBrown,
                                                modifier = Modifier.clickable { onEditCard(card) }
                                            )
                                        }

                                        // Small Bell Icon (أيقونة جرس صغيرة لمحاكاة إرسال إشعار تنبيهي)
                                        Surface(
                                            onClick = { onToggleBellAlert(card) },
                                            shape = CircleShape,
                                            color = if (card.bellEnabled) palette.primaryPastel else CreamBackground,
                                            modifier = Modifier
                                                .border(1.dp, palette.borderTint, CircleShape)
                                                .testTag("bell_button_${card.id}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (card.bellEnabled) {
                                                        Icons.Filled.NotificationsActive
                                                    } else {
                                                        Icons.Outlined.NotificationsNone
                                                    },
                                                    contentDescription = "Bell alert",
                                                    tint = bellTint,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                                if (card.bellEnabled) {
                                                    Text(
                                                        text = ltrNumber(card.bellTime.ifBlank { "18:00" }),
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontWeight = FontWeight.Bold
                                                        ),
                                                        color = WarmSlateBrown
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (card.displaySubtitle(isArabic).isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = card.displaySubtitle(isArabic),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MutedCocoa
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        IconButton(onClick = { onEditCard(card) }, modifier = Modifier.size(26.dp)) {
                                            Icon(
                                                imageVector = Icons.Outlined.Edit,
                                                contentDescription = null,
                                                tint = MutedCocoa,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        IconButton(onClick = { onDeleteCard(card) }, modifier = Modifier.size(26.dp)) {
                                            Icon(
                                                imageVector = Icons.Outlined.DeleteOutline,
                                                contentDescription = null,
                                                tint = MutedCocoa,
                                                modifier = Modifier.size(14.dp)
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
    }
}

/**
 * 5. PLANNER & HABIT TRACKER (قسم المخطط ومتتبع العادات)
 * Interactive Horizontal Table:
 * - Rows represent habits (Reading, Walking, Sleep, Hydration, Pilates)
 * - Columns represent days of the week
 * - Checking a cell puts (✓) and changes the square background to the active Theme color!
 */
@Composable
fun HabitTrackerSectionView(
    habits: List<HabitEntity>,
    timeStudyCards: List<PlannerCardEntity> = emptyList(),
    isArabic: Boolean,
    onToggleHabitDay: (HabitEntity, Int) -> Unit,
    onAddHabit: () -> Unit,
    onEditHabit: (HabitEntity) -> Unit,
    onDeleteHabit: (HabitEntity) -> Unit,
    onToggleCard: (PlannerCardEntity) -> Unit = {},
    onEditCard: (PlannerCardEntity) -> Unit = {},
    onDeleteCard: (PlannerCardEntity) -> Unit = {},
    onAddTimeStudyCard: () -> Unit = {}
) {
    val palette = LocalPastelPalette.current
    val totalSlots = (habits.size * 7).coerceAtLeast(1)
    val completedSlots = habits.sumOf { it.completedCount() }
    val completionPct = ((completedSlots.toFloat() / totalSlots.toFloat()) * 100).toInt()

    val dayHeadersEn = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val dayHeadersAr = listOf("الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت", "الأحد")
    val dayLabels = if (isArabic) dayHeadersAr else dayHeadersEn

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("habit_horizontal_table_section"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress Summary Banner
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = palette.cardSurface,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, palette.borderTint, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isArabic) "جدول متتبع العادات الأفقي التفاعلي ✨" else "Interactive Weekly Habit Table ✨",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = WarmSlateBrown
                        )
                        Text(
                            text = if (isArabic) {
                                "يتغير لون المربع إلى سمة (${palette.nameAr}) عند وضع علامة (✓)"
                            } else {
                                "Cells highlight in your active (${palette.nameEn}) theme when checked (✓)"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedCocoa
                        )
                    }
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        Surface(
                            color = palette.primaryPastel,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "$completionPct%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { (completionPct / 100f).coerceIn(0f, 1f) },
                    color = palette.deepAccent,
                    trackColor = palette.primaryPastel.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                )
            }
        }

        // Interactive Horizontal Table (جدول أفقي تفاعلي)
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = palette.cardSurface,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, palette.borderTint, RoundedCornerShape(26.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Table Header Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(palette.primaryPastel.copy(alpha = 0.45f))
                        .padding(vertical = 10.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = if (isArabic) "العادة اليومية" else "Daily Habit",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown,
                        modifier = Modifier.width(155.dp)
                    )
                    dayLabels.forEach { dayName ->
                        Text(
                            text = dayName,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = WarmSlateBrown,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(52.dp)
                        )
                    }
                    Text(
                        text = if (isArabic) "الإنجاز" else "Done",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Table Habit Rows
                habits.forEach { habit ->
                    val days = habit.daysList()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(palette.background.copy(alpha = 0.7f))
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .testTag("habit_row_${habit.id}")
                    ) {
                        // Habit Title & Edit/Delete
                        Column(
                            modifier = Modifier
                                .width(155.dp)
                                .clickable { onEditHabit(habit) }
                        ) {
                            Text(
                                text = habit.displayName(isArabic),
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = WarmSlateBrown,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = habit.displayTarget(isArabic),
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedCocoa
                            )
                        }

                        // 7 Day Columns (M T W T F S S)
                        days.forEachIndexed { dayIndex, isDone ->
                            val cellBg by animateColorAsState(
                                targetValue = if (isDone) palette.primaryPastel else CreamSurface,
                                label = "habitTableCellBg"
                            )
                            Box(
                                modifier = Modifier.width(52.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(11.dp))
                                        .background(cellBg)
                                        .border(
                                            width = if (isDone) 1.5.dp else 1.dp,
                                            color = if (isDone) palette.deepAccent else palette.borderTint,
                                            shape = RoundedCornerShape(11.dp)
                                        )
                                        .clickable { onToggleHabitDay(habit, dayIndex) }
                                        .testTag("habit_${habit.id}_day_$dayIndex"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isDone) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Done",
                                            tint = WarmSlateBrown,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Row Score + Delete
                        Box(
                            modifier = Modifier.width(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                    Text(
                                        text = "${habit.completedCount()}/7",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = palette.deepAccent
                                    )
                                }
                                IconButton(
                                    onClick = { onDeleteHabit(habit) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteOutline,
                                        contentDescription = null,
                                        tint = MutedCocoa,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Add Habit Row Button
                Surface(
                    onClick = onAddHabit,
                    shape = RoundedCornerShape(14.dp),
                    color = palette.primaryPastel.copy(alpha = 0.5f),
                    modifier = Modifier.border(1.dp, palette.primaryPastel, RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = WarmSlateBrown,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isArabic) "إضافة عادة جديدة للجدول" else "Add New Habit Row",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmSlateBrown
                        )
                    }
                }
            }
        }

        // Academic Productivity, Smart Studying & Time Blocking Hub (المذاكرة الذكية وتقسيم الوقت)
        ColumnHeaderPill(
            title = if (isArabic) "التطوير الأكاديمي، المذاكرة الذكية، وتقسيم الوقت 🎓" else "Smart Studying & Time Blocking Hub 🎓",
            count = timeStudyCards.size,
            onAdd = onAddTimeStudyCard
        )

        timeStudyCards.forEach { studyCard ->
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, palette.borderTint, RoundedCornerShape(22.dp))
                    .testTag("time_study_card_${studyCard.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(if (studyCard.isChecked) palette.primaryPastel else CreamSurface)
                            .border(1.5.dp, WarmSlateBrown, CircleShape)
                            .clickable { onToggleCard(studyCard) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (studyCard.isChecked) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = WarmSlateBrown,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onEditCard(studyCard) }
                    ) {
                        if (studyCard.displayBadge(isArabic).isNotBlank()) {
                            Surface(color = palette.primaryPastel, shape = CircleShape) {
                                Text(
                                    text = studyCard.displayBadge(isArabic),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = WarmSlateBrown,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        Text(
                            text = studyCard.displayTitle(isArabic),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                textDecoration = if (studyCard.isChecked) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = WarmSlateBrown
                        )
                        if (studyCard.displaySubtitle(isArabic).isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = studyCard.displaySubtitle(isArabic),
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    Row {
                        IconButton(onClick = { onEditCard(studyCard) }, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                tint = MutedCocoa,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        IconButton(onClick = { onDeleteCard(studyCard) }, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.DeleteOutline,
                                contentDescription = null,
                                tint = MutedCocoa,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 6. VISION BOARD & MINDSET (قسم لوحة الإلهام والمستقبل)
 * Free-form Drag & Drop Canvas (مساحة حرة) where photos and goals are arranged like a wall board
 * and can be dragged around (`detectDragGestures`), plus a bottom Gratitude Journal text box.
 */
@Composable
fun FutureYouSectionView(
    cards: List<PlannerCardEntity>,
    gratitudeNote: NoteBlockEntity?,
    isArabic: Boolean,
    onEditCard: (PlannerCardEntity) -> Unit,
    onUpdateCardDirectly: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onSaveGratitudeNote: (String, String) -> Unit,
    onAddCardForCategory: (String) -> Unit
) {
    val palette = LocalPastelPalette.current
    val visionCards = cards.filter { it.categoryKey == "vision_board" }
    val mindsetCards = cards.filter { it.categoryKey != "vision_board" }

    var gratEn by remember(gratitudeNote?.contentEn) {
        mutableStateOf(gratitudeNote?.contentEn.orEmpty())
    }
    var gratAr by remember(gratitudeNote?.contentAr) {
        mutableStateOf(gratitudeNote?.contentAr.orEmpty())
    }

    LaunchedEffect(gratitudeNote) {
        if (gratitudeNote != null) {
            gratEn = gratitudeNote.contentEn
            gratAr = gratitudeNote.contentAr
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("vision_board_section"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Canvas Toolbar Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isArabic) "لوحة الإلهام الحرة (اسحبي وأفلتي الصور بحرية) ✨" else "Free-Form Vision Canvas (Drag & Drop Cards) ✨",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = WarmSlateBrown
                )
                Text(
                    text = if (isArabic) "حرّكي بطاقات أحلامكِ بإصبعكِ لترتيب لوحتكِ الجدارية" else "Drag any polaroid card with your finger to arrange your dream wall",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedCocoa
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Reset Canvas Offsets Button
                IconButton(
                    onClick = {
                        visionCards.forEach { c ->
                            if (c.offsetX != 0f || c.offsetY != 0f) {
                                onUpdateCardDirectly(c.copy(offsetX = 0f, offsetY = 0f))
                            }
                        }
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(palette.cardSurface)
                        .border(1.dp, palette.borderTint, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.RestartAlt,
                        contentDescription = if (isArabic) "إعادة ترتيب" else "Reset layout",
                        tint = WarmSlateBrown,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Surface(
                    onClick = { onAddCardForCategory("vision_board") },
                    shape = CircleShape,
                    color = palette.primaryPastel
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = WarmSlateBrown,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (isArabic) "إضافة هدف/صورة" else "Pin Vision",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = WarmSlateBrown
                        )
                    }
                }
            }
        }

        // Free-Form Drag & Drop Wall Canvas
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = palette.primaryPastel.copy(alpha = 0.22f),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, palette.primaryPastel, RoundedCornerShape(28.dp))
                .testTag("vision_freeform_canvas")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val leftCol = visionCards.filterIndexed { idx, _ -> idx % 2 == 0 }
                val rightCol = visionCards.filterIndexed { idx, _ -> idx % 2 == 1 }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        leftCol.forEach { card ->
                            DraggableVisionPolaroidCard(
                                card = card,
                                isArabic = isArabic,
                                onDragFinished = { newX, newY ->
                                    onUpdateCardDirectly(card.copy(offsetX = newX, offsetY = newY))
                                },
                                onEdit = { onEditCard(card) },
                                onDelete = { onDeleteCard(card) }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rightCol.forEach { card ->
                            DraggableVisionPolaroidCard(
                                card = card,
                                isArabic = isArabic,
                                onDragFinished = { newX, newY ->
                                    onUpdateCardDirectly(card.copy(offsetX = newX, offsetY = newY))
                                },
                                onEdit = { onEditCard(card) },
                                onDelete = { onDeleteCard(card) }
                            )
                        }
                    }
                }
            }
        }

        // Wonyoungism Mindset, Shadow Work & Social Boundaries (عقلية وونيونغ، تمارين الظل، وفلترة العلاقات)
        ColumnHeaderPill(
            title = if (isArabic) "عقلية وونيونغ، تمارين الظل، والحدود الاجتماعية 👑" else "Wonyoungism Mindset, Shadow Work & Boundaries 👑",
            count = mindsetCards.size,
            onAdd = { onAddCardForCategory("wonyoung_mindset") }
        )

        mindsetCards.forEach { item ->
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, palette.borderTint, RoundedCornerShape(22.dp))
                    .testTag("mindset_card_${item.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditCard(item) }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.displayBadge(isArabic).isNotBlank()) {
                            Surface(color = palette.primaryPastel, shape = CircleShape) {
                                Text(
                                    text = item.displayBadge(isArabic),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = WarmSlateBrown,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }
                        }
                        Row {
                            IconButton(onClick = { onEditCard(item) }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = null,
                                    tint = MutedCocoa,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            IconButton(onClick = { onDeleteCard(item) }, modifier = Modifier.size(28.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.DeleteOutline,
                                    contentDescription = null,
                                    tint = MutedCocoa,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.displayTitle(isArabic),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown
                    )
                    if (item.displaySubtitle(isArabic).isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.displaySubtitle(isArabic),
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedCocoa
                        )
                    }
                }
            }
        }

        // Bottom Gratitude Journal Box (مربع نصي سفلي ليوميات الامتنان)
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = palette.cardSurface,
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.2.dp, palette.primaryPastel, RoundedCornerShape(26.dp))
                .testTag("gratitude_journal_box")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(palette.primaryPastel),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = WarmSlateBrown,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = if (isArabic) "يوميات الامتنان اليومية 💌" else "Daily Gratitude Journal 💌",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = WarmSlateBrown
                        )
                        Text(
                            text = if (isArabic) "ما هي 3 أشياء تُسعدكِ اليوم؟" else "What are 3 things making you smile today?",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedCocoa
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val currentGratText = if (isArabic) gratAr else gratEn
                OutlinedTextField(
                    value = currentGratText,
                    onValueChange = { updated ->
                        if (isArabic) {
                            gratAr = updated
                            onSaveGratitudeNote(gratEn, updated)
                        } else {
                            gratEn = updated
                            onSaveGratitudeNote(updated, gratAr)
                        }
                    },
                    placeholder = {
                        Text(
                            text = if (isArabic) "1. ...\n2. ...\n3. ..." else "1. ...\n2. ...\n3. ...",
                            color = MutedCocoa
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = palette.deepAccent,
                        unfocusedBorderColor = palette.borderTint,
                        focusedContainerColor = palette.background,
                        unfocusedContainerColor = palette.background.copy(alpha = 0.7f),
                        cursorColor = WarmSlateBrown
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gratitude_journal_textarea")
                )
            }
        }
    }
}

@Composable
private fun DraggableVisionPolaroidCard(
    card: PlannerCardEntity,
    isArabic: Boolean,
    onDragFinished: (Float, Float) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val palette = LocalPastelPalette.current
    var dragX by remember(card.id, card.offsetX) { mutableFloatStateOf(card.offsetX) }
    var dragY by remember(card.id, card.offsetY) { mutableFloatStateOf(card.offsetY) }
    val hasImage = !card.imageBase64.isNullOrBlank() || !card.presetDrawableName.isNullOrBlank()

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = CreamSurface,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(dragX.roundToInt(), dragY.roundToInt()) }
            .pointerInput(card.id) {
                detectDragGestures(
                    onDragEnd = {
                        onDragFinished(dragX, dragY)
                    },
                    onDragCancel = {
                        onDragFinished(dragX, dragY)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragX = (dragX + dragAmount.x).coerceIn(-120f, 120f)
                        dragY = (dragY + dragAmount.y).coerceIn(-120f, 120f)
                    }
                )
            }
            .border(1.dp, palette.borderTint, RoundedCornerShape(20.dp))
            .testTag("vision_polaroid_${card.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            if (hasImage) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(125.dp)
                        .clip(RoundedCornerShape(14.dp))
                ) {
                    PlannerCardImage(
                        imageBase64 = card.imageBase64,
                        presetDrawableName = card.presetDrawableName,
                        contentDescription = card.displayTitle(isArabic),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(125.dp)
                    )
                    Surface(
                        color = CreamSurface.copy(alpha = 0.85f),
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.OpenWith,
                            contentDescription = "Drag",
                            tint = WarmSlateBrown,
                            modifier = Modifier
                                .padding(5.dp)
                                .size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (card.displayBadge(isArabic).isNotBlank()) {
                Surface(
                    color = palette.primaryPastel,
                    shape = CircleShape
                ) {
                    Text(
                        text = card.displayBadge(isArabic),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = card.displayTitle(isArabic),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = WarmSlateBrown
            )

            if (card.displaySubtitle(isArabic).isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = card.displaySubtitle(isArabic),
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedCocoa,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit, modifier = Modifier.size(26.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        tint = MutedCocoa,
                        modifier = Modifier.size(14.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(26.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        tint = MutedCocoa,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * 7. FITNESS & NUTRITION / MEAL PLANNER (مخطط الوجبات واللياقة)
 */
@Composable
fun FitnessNutritionSectionView(
    cards: List<PlannerCardEntity>,
    isArabic: Boolean,
    onToggleCard: (PlannerCardEntity) -> Unit,
    onEditCard: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onAddCardForCategory: (String) -> Unit
) {
    val palette = LocalPastelPalette.current
    val mealCards = cards.filter { it.categoryKey == "meals" }
    val workoutCards = cards.filter { it.categoryKey != "meals" }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ColumnHeaderPill(
            title = if (isArabic) "أعمدة الوجبات الصحية (مخطط الوجبات) 🥣" else "Nourishing Meal Columns 🥣",
            count = mealCards.size,
            onAdd = { onAddCardForCategory("meals") }
        )

        val leftMeals = mealCards.filterIndexed { index, _ -> index % 2 == 0 }
        val rightMeals = mealCards.filterIndexed { index, _ -> index % 2 == 1 }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                leftMeals.forEach { meal ->
                    MealCardItem(
                        card = meal,
                        isArabic = isArabic,
                        onEdit = { onEditCard(meal) },
                        onDelete = { onDeleteCard(meal) }
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rightMeals.forEach { meal ->
                    MealCardItem(
                        card = meal,
                        isArabic = isArabic,
                        onEdit = { onEditCard(meal) },
                        onDelete = { onDeleteCard(meal) }
                    )
                }
            }
        }

        ColumnHeaderPill(
            title = if (isArabic) "قائمة التمارين اليومية 🩰" else "Mindful Workout Checklist 🩰",
            count = workoutCards.size,
            onAdd = { onAddCardForCategory("workouts") }
        )

        workoutCards.forEach { workout ->
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = palette.cardSurface,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, palette.borderTint, RoundedCornerShape(20.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(if (workout.isChecked) palette.primaryPastel else CreamSurface)
                            .border(1.5.dp, WarmSlateBrown, CircleShape)
                            .clickable { onToggleCard(workout) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (workout.isChecked) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = WarmSlateBrown,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onEditCard(workout) }
                    ) {
                        Text(
                            text = workout.displayTitle(isArabic),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                textDecoration = if (workout.isChecked) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = WarmSlateBrown
                        )
                        if (workout.displaySubtitle(isArabic).isNotBlank()) {
                            Text(
                                text = workout.displaySubtitle(isArabic),
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedCocoa
                            )
                        }
                    }
                    IconButton(onClick = { onEditCard(workout) }, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            tint = MutedCocoa,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = { onDeleteCard(workout) }, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = null,
                            tint = MutedCocoa,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MealCardItem(
    card: PlannerCardEntity,
    isArabic: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val palette = LocalPastelPalette.current
    Surface(
        onClick = onEdit,
        shape = RoundedCornerShape(22.dp),
        color = palette.cardSurface,
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, palette.borderTint, RoundedCornerShape(22.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            if (card.displayBadge(isArabic).isNotBlank()) {
                Surface(color = palette.primaryPastel, shape = CircleShape) {
                    Text(
                        text = card.displayBadge(isArabic),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            Text(
                text = card.displayTitle(isArabic),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = WarmSlateBrown
            )
            if (card.displaySubtitle(isArabic).isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.displaySubtitle(isArabic),
                    style = MaterialTheme.typography.bodySmall,
                    color = MutedCocoa
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDelete, modifier = Modifier.size(26.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = null,
                        tint = MutedCocoa,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnHeaderPill(
    title: String,
    count: Int,
    onAdd: () -> Unit
) {
    val palette = LocalPastelPalette.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = WarmSlateBrown,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Surface(color = palette.primaryPastel, shape = CircleShape) {
                Text(
                    text = ltrNumber("$count"),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = WarmSlateBrown,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
        }
        IconButton(
            onClick = onAdd,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(palette.primaryPastel)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = WarmSlateBrown,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}
