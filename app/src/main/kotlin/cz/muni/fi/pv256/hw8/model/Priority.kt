package cz.muni.fi.pv256.hw8.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

val priorityValues = Priority::class.nestedClasses.map { it.objectInstance as Priority }

sealed class Priority : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Entity
    @Parcelize
    object Low : Priority()

    @Entity
    @Parcelize
    object Medium : Priority()

    @Entity
    @Parcelize
    object High : Priority()

    override fun toString(): String = javaClass.simpleName
}
