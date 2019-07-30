package cz.muni.fi.pv256.hw8.db

import android.content.Context
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import cz.muni.fi.pv256.hw8.model.Priority
import cz.muni.fi.pv256.hw8.model.TodoItem

class PriorityConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toPriority(value: String?): Priority? {
            return when (value) {
                "Low" -> Priority.Low
                "Medium" -> Priority.Medium
                "High" -> Priority.High
                else -> null
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromPriority(priority: Priority): String? {
            return priority.toString()
        }
    }
}

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
@TypeConverters(PriorityConverter::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            if (INSTANCE == null) {
                synchronized(TodoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            TodoDatabase::class.java, "todo-db.db")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}