package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.AppLanguage
import com.example.model.DailyMoodOption
import com.example.model.HabitEntity
import com.example.model.NoteBlockEntity
import com.example.model.PlannerCardEntity
import com.example.model.PlannerSection
import com.example.model.QuickAddMode
import com.example.model.SimulatedNotification
import com.example.ui.components.AddEditCardDialog
import com.example.ui.components.AddEditHabitDialog
import com.example.ui.components.NotificationsCenterDialog
import com.example.ui.components.QuickAddTypeChooserDialog
import com.example.ui.components.SectionWallpaperHeader
import com.example.ui.components.SettingsModalDialog
import com.example.ui.components.sectionOutlineIcon
import com.example.ui.screens.BodyCareSectionView
import com.example.ui.screens.FitnessNutritionSectionView
import com.example.ui.screens.ForgetMeNotKanbanSectionView
import com.example.ui.screens.FutureYouSectionView
import com.example.ui.screens.HabitTrackerSectionView
import com.example.ui.screens.HomeDashboardScreen
import com.example.ui.screens.OutfitsHijabSectionView
import com.example.ui.screens.SkincareGlowSectionView
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ItGirlTheme
import com.example.ui.theme.LocalPastelPalette
import com.example.ui.theme.MutedCocoa
import com.example.ui.theme.PastelThemePalette
import com.example.ui.theme.QuicksandFontFamily
import com.example.ui.theme.TajawalFontFamily
import com.example.ui.theme.WarmSlateBrown
import com.example.viewmodel.PlannerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val plannerViewModel: PlannerViewModel = viewModel(
                factory = PlannerViewModel.provideFactory(context)
            )
            ItGirlPlannerApp(viewModel = plannerViewModel)
        }
    }
}

@Composable
fun ItGirlPlannerApp(viewModel: PlannerViewModel) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val themePalette by viewModel.themePalette.collectAsStateWithLifecycle()
    val selectedSection by viewModel.selectedSection.collectAsStateWithLifecycle()
    val currentMood by viewModel.currentMood.collectAsStateWithLifecycle()
    val waterCupsCount by viewModel.waterCupsCount.collectAsStateWithLifecycle()
    val quoteIndex by viewModel.quoteIndex.collectAsStateWithLifecycle()
    val simulatedNotifications by viewModel.simulatedNotifications.collectAsStateWithLifecycle()
    val activeBannerAlert by viewModel.activeBannerAlert.collectAsStateWithLifecycle()

    val allCards by viewModel.allCards.collectAsStateWithLifecycle()
    val allHabits by viewModel.allHabits.collectAsStateWithLifecycle()
    val brainDumpNote by viewModel.brainDumpNote.collectAsStateWithLifecycle()
    val gratitudeNote by viewModel.gratitudeNote.collectAsStateWithLifecycle()

    val isArabic = language.isArabic

    // Modals & Dialogs State
    var showQuickAddChooser by remember { mutableStateOf(false) }
    var selectedQuickAddMode by remember { mutableStateOf(QuickAddMode.ADD_TEXT) }

    var showCardDialog by remember { mutableStateOf(false) }
    var editingCard by remember { mutableStateOf<PlannerCardEntity?>(null) }
    var defaultCategoryForNewCard by remember { mutableStateOf<String?>(null) }

    var showHabitDialog by remember { mutableStateOf(false) }
    var editingHabit by remember { mutableStateOf<HabitEntity?>(null) }

    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Calculate Daily Progress Percentage for the Homepage Quick Glance Circular Progress Bar
    val dailyProgressPercent = remember(allCards, allHabits, waterCupsCount) {
        val topPriorities = allCards.filter { it.sectionId == PlannerSection.HOME_DASHBOARD.id }
        val checkedPriorities = topPriorities.count { it.isChecked }
        val totalPriorityWeight = topPriorities.size.coerceAtLeast(1)
        val habitDone = allHabits.sumOf { it.completedCount() }
        val habitTotal = (allHabits.size * 7).coerceAtLeast(1)
        val waterRatio = waterCupsCount / 8f

        val combined = (
            (checkedPriorities.toFloat() / totalPriorityWeight) * 0.45f +
                (habitDone.toFloat() / habitTotal) * 0.35f +
                (waterRatio * 0.20f)
            ) * 100f
        combined.toInt().coerceIn(0, 100)
    }

    // BackHandler returns from any internal section to the Homepage Dashboard
    BackHandler(enabled = selectedSection != PlannerSection.HOME_DASHBOARD) {
        viewModel.selectSection(PlannerSection.HOME_DASHBOARD)
    }

    ItGirlTheme(isArabic = isArabic, palette = themePalette) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(themePalette.background)
        ) {
            val isExpandedScreen = maxWidth >= 720.dp

            if (isExpandedScreen) {
                // Wide / Tablet Layout: Fixed Sidebar + Top Navigation + Main Content
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) {
                    AestheticSidebarRail(
                        selectedSection = selectedSection,
                        isArabic = isArabic,
                        onSelectSection = { viewModel.selectSection(it) },
                        modifier = Modifier
                            .width(240.dp)
                            .fillMaxHeight()
                            .padding(12.dp)
                    )

                    MainPlannerWorkspace(
                        selectedSection = selectedSection,
                        language = language,
                        themePalette = themePalette,
                        isExpandedScreen = true,
                        allCards = allCards,
                        habits = allHabits,
                        brainDumpNote = brainDumpNote,
                        gratitudeNote = gratitudeNote,
                        currentMood = currentMood,
                        waterCupsCount = waterCupsCount,
                        quoteIndex = quoteIndex,
                        dailyProgressPercent = dailyProgressPercent,
                        activeBellCount = allCards.count { it.bellEnabled } + simulatedNotifications.size,
                        activeBannerAlert = activeBannerAlert,
                        onDismissBannerAlert = { viewModel.dismissBannerAlert() },
                        onSelectThemePalette = { viewModel.selectThemePalette(it) },
                        onToggleLanguage = { viewModel.toggleLanguage() },
                        onSelectLanguage = { viewModel.setLanguage(it) },
                        onOpenNotifications = { showNotificationsDialog = true },
                        onOpenSettings = { showSettingsDialog = true },
                        onOpenQuickAddChooser = {
                            defaultCategoryForNewCard = null
                            showQuickAddChooser = true
                        },
                        onSelectSection = { viewModel.selectSection(it) },
                        onSelectMood = { viewModel.selectMood(it) },
                        onSetWaterCups = { viewModel.setWaterCups(it) },
                        onNextQuote = { viewModel.cycleDailyQuote() },
                        onAddCardForCategory = { categoryKey ->
                            if (selectedSection == PlannerSection.HABIT_TRACKER && categoryKey == "daily_ritual") {
                                editingHabit = null
                                showHabitDialog = true
                            } else {
                                editingCard = null
                                defaultCategoryForNewCard = categoryKey
                                selectedQuickAddMode = QuickAddMode.ADD_TEXT
                                showCardDialog = true
                            }
                        },
                        onEditCard = { card ->
                            editingCard = card
                            defaultCategoryForNewCard = card.categoryKey
                            showCardDialog = true
                        },
                        onUpdateCardDirectly = { card -> viewModel.saveCard(card) },
                        onToggleCard = { viewModel.toggleCardChecked(it) },
                        onToggleBellAlert = { viewModel.toggleCardBellAlert(it) },
                        onDeleteCard = { viewModel.deleteCard(it) },
                        onSaveBrainDump = { en, ar -> viewModel.saveBrainDump(en, ar) },
                        onSaveGratitudeNote = { en, ar -> viewModel.saveGratitudeNote(en, ar) },
                        onToggleHabitDay = { habit, day -> viewModel.toggleHabitDay(habit, day) },
                        onEditHabit = { habit ->
                            editingHabit = habit
                            showHabitDialog = true
                        },
                        onDeleteHabit = { viewModel.deleteHabit(it) },
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                // Phone Layout: Top Navigation + Main Content + Bottom Outline Navigation Bar
                MainPlannerWorkspace(
                    selectedSection = selectedSection,
                    language = language,
                    themePalette = themePalette,
                    isExpandedScreen = false,
                    allCards = allCards,
                    habits = allHabits,
                    brainDumpNote = brainDumpNote,
                    gratitudeNote = gratitudeNote,
                    currentMood = currentMood,
                    waterCupsCount = waterCupsCount,
                    quoteIndex = quoteIndex,
                    dailyProgressPercent = dailyProgressPercent,
                    activeBellCount = allCards.count { it.bellEnabled } + simulatedNotifications.size,
                    activeBannerAlert = activeBannerAlert,
                    onDismissBannerAlert = { viewModel.dismissBannerAlert() },
                    onSelectThemePalette = { viewModel.selectThemePalette(it) },
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onOpenNotifications = { showNotificationsDialog = true },
                    onOpenSettings = { showSettingsDialog = true },
                    onOpenQuickAddChooser = {
                        defaultCategoryForNewCard = null
                        showQuickAddChooser = true
                    },
                    onSelectSection = { viewModel.selectSection(it) },
                    onSelectMood = { viewModel.selectMood(it) },
                    onSetWaterCups = { viewModel.setWaterCups(it) },
                    onNextQuote = { viewModel.cycleDailyQuote() },
                    onAddCardForCategory = { categoryKey ->
                        if (selectedSection == PlannerSection.HABIT_TRACKER && categoryKey == "daily_ritual") {
                            editingHabit = null
                            showHabitDialog = true
                        } else {
                            editingCard = null
                            defaultCategoryForNewCard = categoryKey
                            selectedQuickAddMode = QuickAddMode.ADD_TEXT
                            showCardDialog = true
                        }
                    },
                    onEditCard = { card ->
                        editingCard = card
                        defaultCategoryForNewCard = card.categoryKey
                        showCardDialog = true
                    },
                    onUpdateCardDirectly = { card -> viewModel.saveCard(card) },
                    onToggleCard = { viewModel.toggleCardChecked(it) },
                    onToggleBellAlert = { viewModel.toggleCardBellAlert(it) },
                    onDeleteCard = { viewModel.deleteCard(it) },
                    onSaveBrainDump = { en, ar -> viewModel.saveBrainDump(en, ar) },
                    onSaveGratitudeNote = { en, ar -> viewModel.saveGratitudeNote(en, ar) },
                    onToggleHabitDay = { habit, day -> viewModel.toggleHabitDay(habit, day) },
                    onEditHabit = { habit ->
                        editingHabit = habit
                        showHabitDialog = true
                    },
                    onDeleteHabit = { viewModel.deleteHabit(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 1. Floating (+) Quick Add Mode Chooser ("إضافة نص", "إضافة قائمة", "رفع صورة/فيديو")
            if (showQuickAddChooser) {
                QuickAddTypeChooserDialog(
                    section = selectedSection,
                    isArabic = isArabic,
                    onDismiss = { showQuickAddChooser = false },
                    onSelectMode = { mode ->
                        showQuickAddChooser = false
                        selectedQuickAddMode = mode
                        editingCard = null
                        showCardDialog = true
                    },
                    onAddHabitDirectly = {
                        showQuickAddChooser = false
                        editingHabit = null
                        showHabitDialog = true
                    }
                )
            }

            // 2. Add / Edit Card Dialog
            if (showCardDialog) {
                AddEditCardDialog(
                    section = selectedSection,
                    initialCard = editingCard,
                    defaultCategoryKey = defaultCategoryForNewCard,
                    initialMode = selectedQuickAddMode,
                    isArabic = isArabic,
                    onDismiss = {
                        showCardDialog = false
                        editingCard = null
                    },
                    onSave = { savedCard ->
                        viewModel.saveCard(savedCard)
                        showCardDialog = false
                        editingCard = null
                    },
                    onDelete = { cardToDelete ->
                        viewModel.deleteCard(cardToDelete)
                        showCardDialog = false
                        editingCard = null
                    }
                )
            }

            // 3. Add / Edit Habit Dialog
            if (showHabitDialog) {
                AddEditHabitDialog(
                    initialHabit = editingHabit,
                    isArabic = isArabic,
                    onDismiss = {
                        showHabitDialog = false
                        editingHabit = null
                    },
                    onSave = { savedHabit ->
                        viewModel.saveHabit(savedHabit)
                        showHabitDialog = false
                        editingHabit = null
                    },
                    onDelete = { habitToDelete ->
                        viewModel.deleteHabit(habitToDelete)
                        showHabitDialog = false
                        editingHabit = null
                    }
                )
            }

            // 4. Top Bar Notifications Center Dialog
            if (showNotificationsDialog) {
                NotificationsCenterDialog(
                    notifications = simulatedNotifications,
                    bellCards = allCards.filter { it.bellEnabled },
                    isArabic = isArabic,
                    onTriggerSimulatedAlert = { card ->
                        viewModel.triggerSimulatedNotification(card)
                    },
                    onClearNotifications = { viewModel.clearNotifications() },
                    onDismiss = { showNotificationsDialog = false }
                )
            }

            // 5. Top Bar Settings Wheel Dialog
            if (showSettingsDialog) {
                SettingsModalDialog(
                    currentPalette = themePalette,
                    currentLanguage = language,
                    totalSavedCards = allCards.size + allHabits.size,
                    onSelectPalette = { viewModel.selectThemePalette(it) },
                    onSelectLanguage = { viewModel.setLanguage(it) },
                    onDismiss = { showSettingsDialog = false }
                )
            }
        }
    }
}

@Composable
private fun MainPlannerWorkspace(
    selectedSection: PlannerSection,
    language: AppLanguage,
    themePalette: PastelThemePalette,
    isExpandedScreen: Boolean,
    allCards: List<PlannerCardEntity>,
    habits: List<HabitEntity>,
    brainDumpNote: NoteBlockEntity?,
    gratitudeNote: NoteBlockEntity?,
    currentMood: DailyMoodOption,
    waterCupsCount: Int,
    quoteIndex: Int,
    dailyProgressPercent: Int,
    activeBellCount: Int,
    activeBannerAlert: SimulatedNotification?,
    onDismissBannerAlert: () -> Unit,
    onSelectThemePalette: (PastelThemePalette) -> Unit,
    onToggleLanguage: () -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenQuickAddChooser: () -> Unit,
    onSelectSection: (PlannerSection) -> Unit,
    onSelectMood: (DailyMoodOption) -> Unit,
    onSetWaterCups: (Int) -> Unit,
    onNextQuote: () -> Unit,
    onAddCardForCategory: (String) -> Unit,
    onEditCard: (PlannerCardEntity) -> Unit,
    onUpdateCardDirectly: (PlannerCardEntity) -> Unit,
    onToggleCard: (PlannerCardEntity) -> Unit,
    onToggleBellAlert: (PlannerCardEntity) -> Unit,
    onDeleteCard: (PlannerCardEntity) -> Unit,
    onSaveBrainDump: (String, String) -> Unit,
    onSaveGratitudeNote: (String, String) -> Unit,
    onToggleHabitDay: (HabitEntity, Int) -> Unit,
    onEditHabit: (HabitEntity) -> Unit,
    onDeleteHabit: (HabitEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isArabic = language.isArabic
    val sectionCards = remember(allCards, selectedSection) {
        allCards.filter { it.sectionId == selectedSection.id }
    }
    val itemCount = if (selectedSection == PlannerSection.HABIT_TRACKER) {
        habits.size
    } else {
        sectionCards.size
    }

    Scaffold(
        modifier = modifier,
        containerColor = themePalette.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(themePalette.background)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                // Row 1: Brand Title + Top Action Icons (Notifications, Settings, Quick Add +, EN/AR)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .clickable { onSelectSection(PlannerSection.HOME_DASHBOARD) }
                    ) {
                        Text(
                            text = "IT GIRL",
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = 2.sp,
                            color = WarmSlateBrown,
                            modifier = Modifier.testTag("brand_logo_title")
                        )
                        Text(
                            text = "مساحتك الجمالية اليومية",
                            fontFamily = TajawalFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.sp,
                            color = MutedCocoa,
                            modifier = Modifier.testTag("brand_tagline")
                        )
                    }

                    // Top Navigation Icons: Bell Notifications, Settings Gear, Quick Add (+), EN/AR
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Bell Notifications Icon (أيقونة التنبيهات)
                        Box {
                            Surface(
                                onClick = onOpenNotifications,
                                shape = CircleShape,
                                color = themePalette.cardSurface,
                                shadowElevation = 1.dp,
                                modifier = Modifier
                                    .size(38.dp)
                                    .border(1.dp, themePalette.borderTint, CircleShape)
                                    .testTag("top_notifications_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (activeBellCount > 0) {
                                            Icons.Filled.NotificationsActive
                                        } else {
                                            Icons.Outlined.NotificationsNone
                                        },
                                        contentDescription = if (isArabic) "التنبيهات" else "Notifications",
                                        tint = WarmSlateBrown,
                                        modifier = Modifier.size(19.dp)
                                    )
                                }
                            }
                            if (activeBellCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(themePalette.deepAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "\u200E$activeBellCount\u200E",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CreamSurface
                                    )
                                }
                            }
                        }

                        // Settings Wheel Icon (عجلة الإعدادات)
                        Surface(
                            onClick = onOpenSettings,
                            shape = CircleShape,
                            color = themePalette.cardSurface,
                            shadowElevation = 1.dp,
                            modifier = Modifier
                                .size(38.dp)
                                .border(1.dp, themePalette.borderTint, CircleShape)
                                .testTag("top_settings_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = if (isArabic) "الإعدادات" else "Settings",
                                    tint = WarmSlateBrown,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }

                        // Top Quick Add (+) Button (زر "إضافة سريعة" (+))
                        Surface(
                            onClick = onOpenQuickAddChooser,
                            shape = CircleShape,
                            color = themePalette.primaryPastel,
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .size(38.dp)
                                .border(1.2.dp, themePalette.deepAccent.copy(alpha = 0.5f), CircleShape)
                                .testTag("top_quick_add_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = if (isArabic) "إضافة سريعة" else "Quick Add",
                                    tint = WarmSlateBrown,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Fixed Corner EN / AR Toggle
                        LanguageSegmentedToggle(
                            currentLanguage = language,
                            accentColor = themePalette.primaryPastel,
                            onSelectLanguage = onSelectLanguage,
                            onToggle = onToggleLanguage
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Row 2: The 6 Pastel Color Palettes Slider (شريط التمرير لاختيار سمة الألوان الستة)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("theme_palette_slider"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(PastelThemePalette.entries) { paletteItem ->
                        val isSelected = paletteItem == themePalette
                        Surface(
                            onClick = { onSelectThemePalette(paletteItem) },
                            shape = CircleShape,
                            color = if (isSelected) paletteItem.primaryPastel else themePalette.cardSurface,
                            shadowElevation = if (isSelected) 2.dp else 0.dp,
                            modifier = Modifier
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) WarmSlateBrown else themePalette.borderTint,
                                    shape = CircleShape
                                )
                                .testTag("theme_chip_${paletteItem.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(13.dp)
                                        .clip(CircleShape)
                                        .background(paletteItem.deepAccent)
                                        .border(1.dp, CreamSurface, CircleShape)
                                )
                                Text(
                                    text = paletteItem.displayName(isArabic),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = WarmSlateBrown
                                )
                            }
                        }
                    }
                }

                // Simulated Bell Notification Toast Banner when triggered
                AnimatedVisibility(
                    visible = activeBannerAlert != null,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    activeBannerAlert?.let { alert ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = themePalette.primaryPastel,
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .border(1.2.dp, WarmSlateBrown, RoundedCornerShape(18.dp))
                                .testTag("simulated_notification_banner")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
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
                                        imageVector = Icons.Filled.NotificationsActive,
                                        contentDescription = null,
                                        tint = WarmSlateBrown
                                    )
                                    Column {
                                        Text(
                                            text = if (isArabic) {
                                                "🔔 إشعار تنبيهي (${alert.scheduledTime}): ${alert.title(true)}"
                                            } else {
                                                "🔔 Scheduled Reminder (${alert.scheduledTime}): ${alert.title(false)}"
                                            },
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = WarmSlateBrown
                                        )
                                        Text(
                                            text = alert.category(isArabic),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MutedCocoa
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = onDismissBannerAlert,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = null,
                                        tint = WarmSlateBrown,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (!isExpandedScreen) {
                AestheticBottomOutlineBar(
                    selectedSection = selectedSection,
                    isArabic = isArabic,
                    onSelectSection = onSelectSection
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenQuickAddChooser,
                containerColor = themePalette.primaryPastel,
                contentColor = WarmSlateBrown,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .border(1.2.dp, themePalette.deepAccent.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
                    .testTag("fab_add_item"),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = WarmSlateBrown
                    )
                },
                text = {
                    Text(
                        text = if (isArabic) "إضافة (+)" else "Add (+)",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = WarmSlateBrown
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 6.dp,
                bottom = 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (selectedSection != PlannerSection.HOME_DASHBOARD) {
                item(key = "header_${selectedSection.id}") {
                    SectionWallpaperHeader(
                        section = selectedSection,
                        isArabic = isArabic,
                        itemCount = itemCount,
                        modifier = Modifier.testTag("section_header_${selectedSection.id}")
                    )
                }
            }

            item(key = "body_${selectedSection.id}") {
                AnimatedContent(
                    targetState = selectedSection,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "sectionTransition"
                ) { section ->
                    when (section) {
                        PlannerSection.HOME_DASHBOARD -> {
                            HomeDashboardScreen(
                                topPriorities = allCards.filter {
                                    it.sectionId == PlannerSection.HOME_DASHBOARD.id
                                },
                                brainDumpNote = brainDumpNote,
                                currentMood = currentMood,
                                waterCupsCount = waterCupsCount,
                                quoteIndex = quoteIndex,
                                dailyProgressPercent = dailyProgressPercent,
                                isArabic = isArabic,
                                onSelectMood = onSelectMood,
                                onSetWaterCups = onSetWaterCups,
                                onNextQuote = onNextQuote,
                                onSaveBrainDump = onSaveBrainDump,
                                onTogglePriority = onToggleCard,
                                onEditPriority = onEditCard,
                                onNavigateToSection = onSelectSection
                            )
                        }

                        PlannerSection.SKINCARE_GLOW -> {
                            SkincareGlowSectionView(
                                cards = sectionCards,
                                isArabic = isArabic,
                                onToggleCard = onToggleCard,
                                onEditCard = onEditCard,
                                onUpdateCardDirectly = onUpdateCardDirectly,
                                onDeleteCard = onDeleteCard,
                                onAddCardForCategory = onAddCardForCategory
                            )
                        }

                        PlannerSection.OUTFITS_HIJAB -> {
                            OutfitsHijabSectionView(
                                cards = sectionCards,
                                isArabic = isArabic,
                                onEditCard = onEditCard,
                                onUpdateCardDirectly = onUpdateCardDirectly,
                                onDeleteCard = onDeleteCard,
                                onAddCardForCategory = onAddCardForCategory
                            )
                        }

                        PlannerSection.BODY_CARE -> {
                            BodyCareSectionView(
                                cards = sectionCards,
                                isArabic = isArabic,
                                onEditCard = onEditCard,
                                onUpdateCardDirectly = onUpdateCardDirectly,
                                onDeleteCard = onDeleteCard,
                                onAddCardForCategory = onAddCardForCategory
                            )
                        }

                        PlannerSection.FORGET_ME_NOT -> {
                            ForgetMeNotKanbanSectionView(
                                cards = sectionCards,
                                isArabic = isArabic,
                                onToggleCard = onToggleCard,
                                onToggleBellAlert = onToggleBellAlert,
                                onEditCard = onEditCard,
                                onDeleteCard = onDeleteCard,
                                onAddCardForCategory = onAddCardForCategory
                            )
                        }

                        PlannerSection.HABIT_TRACKER -> {
                            HabitTrackerSectionView(
                                habits = habits,
                                timeStudyCards = sectionCards,
                                isArabic = isArabic,
                                onToggleHabitDay = onToggleHabitDay,
                                onAddHabit = { onAddCardForCategory("daily_ritual") },
                                onEditHabit = onEditHabit,
                                onDeleteHabit = onDeleteHabit,
                                onToggleCard = onToggleCard,
                                onEditCard = onEditCard,
                                onDeleteCard = onDeleteCard,
                                onAddTimeStudyCard = { onAddCardForCategory("time_study") }
                            )
                        }

                        PlannerSection.FUTURE_YOU -> {
                            FutureYouSectionView(
                                cards = sectionCards,
                                gratitudeNote = gratitudeNote,
                                isArabic = isArabic,
                                onEditCard = onEditCard,
                                onUpdateCardDirectly = onUpdateCardDirectly,
                                onDeleteCard = onDeleteCard,
                                onSaveGratitudeNote = onSaveGratitudeNote,
                                onAddCardForCategory = onAddCardForCategory
                            )
                        }

                        PlannerSection.FITNESS_NUTRITION -> {
                            FitnessNutritionSectionView(
                                cards = sectionCards,
                                isArabic = isArabic,
                                onToggleCard = onToggleCard,
                                onEditCard = onEditCard,
                                onDeleteCard = onDeleteCard,
                                onAddCardForCategory = onAddCardForCategory
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Phone Bottom Navigation Bar with thin Outline Icons (أيقونات بخطوط رفيعة لكل قسم).
 * When tapped, the icon & pill change to the active Theme color (يتغير لونها إلى لون السمة المختارة).
 */
@Composable
private fun AestheticBottomOutlineBar(
    selectedSection: PlannerSection,
    isArabic: Boolean,
    onSelectSection: (PlannerSection) -> Unit
) {
    val palette = LocalPastelPalette.current

    Surface(
        color = palette.cardSurface,
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, palette.borderTint, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .testTag("bottom_outline_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlannerSection.entries.forEach { section ->
                val isSelected = section == selectedSection
                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) palette.deepAccent else MutedCocoa,
                    label = "bottomNavIconTint"
                )
                val pillBg by animateColorAsState(
                    targetValue = if (isSelected) palette.primaryPastel else Color.Transparent,
                    label = "bottomNavPillBg"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(pillBg)
                        .clickable { onSelectSection(section) }
                        .padding(horizontal = 11.dp, vertical = 6.dp)
                        .testTag("nav_chip_${section.id}")
                ) {
                    Icon(
                        imageVector = sectionOutlineIcon(section),
                        contentDescription = section.title(isArabic),
                        tint = iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = section.shortTitle(isArabic),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        ),
                        color = if (isSelected) WarmSlateBrown else MutedCocoa,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Fixed Sidebar on Tablet/Wide screens with Outline Icons that highlight in the active Theme color.
 */
@Composable
private fun AestheticSidebarRail(
    selectedSection: PlannerSection,
    isArabic: Boolean,
    onSelectSection: (PlannerSection) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalPastelPalette.current

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = palette.cardSurface,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, palette.borderTint, RoundedCornerShape(28.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)) {
                Text(
                    text = "IT GIRL",
                    fontFamily = QuicksandFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    letterSpacing = 2.sp,
                    color = WarmSlateBrown
                )
                Text(
                    text = "مساحتك الجمالية اليومية",
                    fontFamily = TajawalFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = MutedCocoa
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            PlannerSection.entries.forEach { section ->
                val isSelected = section == selectedSection
                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) palette.deepAccent else MutedCocoa,
                    label = "sidebarIconTint"
                )
                Surface(
                    onClick = { onSelectSection(section) },
                    shape = RoundedCornerShape(18.dp),
                    color = if (isSelected) palette.primaryPastel else Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSelected) 1.5.dp else 0.dp,
                            color = if (isSelected) palette.deepAccent else Color.Transparent,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .testTag("sidebar_item_${section.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = sectionOutlineIcon(section),
                            contentDescription = section.title(isArabic),
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = section.title(isArabic),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) WarmSlateBrown else MutedCocoa,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LanguageSegmentedToggle(
    currentLanguage: AppLanguage,
    accentColor: Color,
    onSelectLanguage: (AppLanguage) -> Unit,
    onToggle: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Surface(
            shape = CircleShape,
            color = CreamSurface,
            shadowElevation = 1.dp,
            modifier = Modifier
                .border(1.dp, WarmSlateBrown.copy(alpha = 0.25f), CircleShape)
                .clickable { onToggle() }
                .testTag("language_toggle_button")
        ) {
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    val isSelected = lang == currentLanguage
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) accentColor else CreamSurface)
                            .clickable { onSelectLanguage(lang) }
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                            .testTag("lang_option_${lang.code}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang.label,
                            fontFamily = QuicksandFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = WarmSlateBrown
                        )
                    }
                }
            }
        }
    }
}
