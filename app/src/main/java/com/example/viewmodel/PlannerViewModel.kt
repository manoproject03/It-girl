package com.example.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.PlannerDatabase
import com.example.data.PlannerRepository
import com.example.model.AppLanguage
import com.example.model.DailyMoodOption
import com.example.model.DefaultDailyQuotes
import com.example.model.HabitEntity
import com.example.model.NoteBlockEntity
import com.example.model.PlannerCardEntity
import com.example.model.PlannerSection
import com.example.model.SimulatedNotification
import com.example.ui.theme.PastelThemePalette
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlannerViewModel(
    private val repository: PlannerRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _language = MutableStateFlow(loadInitialLanguage())
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _themePalette = MutableStateFlow(loadInitialThemePalette())
    val themePalette: StateFlow<PastelThemePalette> = _themePalette.asStateFlow()

    private val _selectedSection = MutableStateFlow(PlannerSection.HOME_DASHBOARD)
    val selectedSection: StateFlow<PlannerSection> = _selectedSection.asStateFlow()

    private val _currentMood = MutableStateFlow(loadInitialMood())
    val currentMood: StateFlow<DailyMoodOption> = _currentMood.asStateFlow()

    private val _waterCupsCount = MutableStateFlow(prefs.getInt(KEY_WATER_CUPS, 5))
    val waterCupsCount: StateFlow<Int> = _waterCupsCount.asStateFlow()

    private val _quoteIndex = MutableStateFlow(prefs.getInt(KEY_QUOTE_INDEX, 0))
    val quoteIndex: StateFlow<Int> = _quoteIndex.asStateFlow()

    private val _simulatedNotifications = MutableStateFlow<List<SimulatedNotification>>(emptyList())
    val simulatedNotifications: StateFlow<List<SimulatedNotification>> = _simulatedNotifications.asStateFlow()

    private val _activeBannerAlert = MutableStateFlow<SimulatedNotification?>(null)
    val activeBannerAlert: StateFlow<SimulatedNotification?> = _activeBannerAlert.asStateFlow()

    val allCards: StateFlow<List<PlannerCardEntity>> = repository.allCards
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allHabits: StateFlow<List<HabitEntity>> = repository.allHabits
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val brainDumpNote: StateFlow<NoteBlockEntity?> = repository.brainDumpNote
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val gratitudeNote: StateFlow<NoteBlockEntity?> = repository.gratitudeNote
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            repository.ensureSeedDataPopulated()
        }
    }

    private fun loadInitialLanguage(): AppLanguage {
        val savedCode = prefs.getString(KEY_LANGUAGE, AppLanguage.AR.code)
        return if (savedCode == AppLanguage.EN.code) AppLanguage.EN else AppLanguage.AR
    }

    private fun loadInitialThemePalette(): PastelThemePalette {
        val savedId = prefs.getString(KEY_THEME_PALETTE, PastelThemePalette.ROSE_WATER.id)
        return PastelThemePalette.fromId(savedId)
    }

    private fun loadInitialMood(): DailyMoodOption {
        val savedId = prefs.getString(KEY_DAILY_MOOD, DailyMoodOption.RADIANT.id)
        return DailyMoodOption.fromId(savedId)
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
        prefs.edit().putString(KEY_LANGUAGE, lang.code).apply()
    }

    fun toggleLanguage() {
        val next = if (_language.value == AppLanguage.EN) AppLanguage.AR else AppLanguage.EN
        setLanguage(next)
    }

    fun selectThemePalette(palette: PastelThemePalette) {
        _themePalette.value = palette
        prefs.edit().putString(KEY_THEME_PALETTE, palette.id).apply()
    }

    fun selectSection(section: PlannerSection) {
        _selectedSection.value = section
    }

    fun selectMood(mood: DailyMoodOption) {
        _currentMood.value = mood
        prefs.edit().putString(KEY_DAILY_MOOD, mood.id).apply()
    }

    fun setWaterCups(count: Int) {
        val clamped = count.coerceIn(0, 8)
        _waterCupsCount.value = clamped
        prefs.edit().putInt(KEY_WATER_CUPS, clamped).apply()
    }

    fun cycleDailyQuote() {
        val next = (_quoteIndex.value + 1) % DefaultDailyQuotes.size
        _quoteIndex.value = next
        prefs.edit().putInt(KEY_QUOTE_INDEX, next).apply()
    }

    fun saveCard(card: PlannerCardEntity) {
        viewModelScope.launch {
            if (card.id == 0) {
                repository.insertCard(card)
            } else {
                repository.updateCard(card)
            }
            if (card.bellEnabled) {
                triggerSimulatedNotification(card)
            }
        }
    }

    fun toggleCardChecked(card: PlannerCardEntity) {
        viewModelScope.launch {
            repository.updateCard(card.copy(isChecked = !card.isChecked))
        }
    }

    fun toggleCardBellAlert(card: PlannerCardEntity) {
        viewModelScope.launch {
            val nextEnabled = !card.bellEnabled
            val updated = card.copy(
                bellEnabled = nextEnabled,
                bellTime = card.bellTime.ifBlank { "18:00" }
            )
            repository.updateCard(updated)
            if (nextEnabled) {
                triggerSimulatedNotification(updated)
            }
        }
    }

    fun triggerSimulatedNotification(card: PlannerCardEntity) {
        val categoryEn = when (card.categoryKey) {
            "pharmacy" -> "Pharmacy Alert"
            "supermarket" -> "Supermarket Alert"
            "clothing" -> "Clothing Alert"
            else -> "Smart Reminder"
        }
        val categoryAr = when (card.categoryKey) {
            "pharmacy" -> "تنبيه مشتريات الصيدلية"
            "supermarket" -> "تنبيه السوبرماركت"
            "clothing" -> "تنبيه الملابس"
            else -> "تذكير ذكي"
        }
        val alert = SimulatedNotification(
            cardId = card.id,
            titleEn = card.titleEn,
            titleAr = card.titleAr,
            scheduledTime = card.bellTime.ifBlank { "18:00" },
            categoryEn = categoryEn,
            categoryAr = categoryAr
        )
        _simulatedNotifications.value = listOf(alert) + _simulatedNotifications.value.take(14)
        _activeBannerAlert.value = alert

        viewModelScope.launch {
            delay(4200)
            if (_activeBannerAlert.value?.id == alert.id) {
                _activeBannerAlert.value = null
            }
        }
    }

    fun dismissBannerAlert() {
        _activeBannerAlert.value = null
    }

    fun clearNotifications() {
        _simulatedNotifications.value = emptyList()
    }

    fun deleteCard(card: PlannerCardEntity) {
        viewModelScope.launch {
            repository.deleteCardById(card.id)
        }
    }

    fun saveHabit(habit: HabitEntity) {
        viewModelScope.launch {
            if (habit.id == 0) {
                repository.insertHabit(habit)
            } else {
                repository.updateHabit(habit)
            }
        }
    }

    fun toggleHabitDay(habit: HabitEntity, dayIndex: Int) {
        viewModelScope.launch {
            val updated = when (dayIndex) {
                0 -> habit.copy(mon = !habit.mon)
                1 -> habit.copy(tue = !habit.tue)
                2 -> habit.copy(wed = !habit.wed)
                3 -> habit.copy(thu = !habit.thu)
                4 -> habit.copy(fri = !habit.fri)
                5 -> habit.copy(sat = !habit.sat)
                6 -> habit.copy(sun = !habit.sun)
                else -> habit
            }
            repository.updateHabit(updated)
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            repository.deleteHabitById(habit.id)
        }
    }

    fun saveBrainDump(contentEn: String, contentAr: String) {
        viewModelScope.launch {
            repository.saveBrainDump(contentEn, contentAr)
        }
    }

    fun saveGratitudeNote(contentEn: String, contentAr: String) {
        viewModelScope.launch {
            repository.saveGratitudeNote(contentEn, contentAr)
        }
    }

    companion object {
        private const val PREFS_NAME = "it_girl_planner_prefs"
        private const val KEY_LANGUAGE = "active_language_code"
        private const val KEY_THEME_PALETTE = "active_pastel_theme_palette"
        private const val KEY_DAILY_MOOD = "active_daily_mood"
        private const val KEY_WATER_CUPS = "active_water_cups_count"
        private const val KEY_QUOTE_INDEX = "active_daily_quote_index"

        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val appContext = context.applicationContext
                    val db = PlannerDatabase.getDatabase(appContext)
                    val repo = PlannerRepository(db.plannerDao())
                    val prefs = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    return PlannerViewModel(repo, prefs) as T
                }
            }
        }
    }
}
