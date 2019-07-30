package cz.muni.fi.pv256.hw8.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import cz.muni.fi.pv256.hw8.model.TodoItem
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun getAll(): Flowable<MutableList<TodoItem>>

    @Insert()
    fun insert(todoItem: TodoItem): Completable

    @Update
    fun update(todoItem: TodoItem): Completable

    @Delete
    fun delete(todoItem: TodoItem): Completable
}