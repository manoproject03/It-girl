package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.model.HabitEntity
import com.example.model.NoteBlockEntity
import com.example.model.PlannerCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannerDao {
    @Query("SELECT * FROM planner_cards ORDER BY timestamp ASC")
    fun getAllCards(): Flow<List<PlannerCardEntity>>

    @Query("SELECT COUNT(*) FROM planner_cards")
    suspend fun getCardCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: PlannerCardEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<PlannerCardEntity>)

    @Update
    suspend fun updateCard(card: PlannerCardEntity)

    @Query("DELETE FROM planner_cards WHERE id = :id")
    suspend fun deleteCardById(id: Int)

    @Query("SELECT * FROM habits ORDER BY timestamp ASC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT COUNT(*) FROM habits")
    suspend fun getHabitCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Int)

    @Query("SELECT * FROM note_blocks WHERE noteKey = :key LIMIT 1")
    fun getNoteBlock(key: String): Flow<NoteBlockEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNoteBlock(note: NoteBlockEntity)
}

@Database(
    entities = [
        PlannerCardEntity::class,
        HabitEntity::class,
        NoteBlockEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class PlannerDatabase : RoomDatabase() {
    abstract fun plannerDao(): PlannerDao

    companion object {
        @Volatile
        private var INSTANCE: PlannerDatabase? = null

        fun getDatabase(context: Context): PlannerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlannerDatabase::class.java,
                    "it_girl_offline_planner.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
