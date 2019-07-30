package cz.muni.fi.pv256.hw8.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo")
@Parcelize
data class TodoItem(val text: String, val priority: Priority) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var todoId: Int = 0
}
