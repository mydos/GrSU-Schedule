package by.kirich1409.grsuschedule.schedule

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.utils.LocalDate
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by kirillrozov on 10/9/15.
 */
class DaySchedule(val date: LocalDate, val items: Array<DaySchedule.Item>) : Parcelable {

    constructor(source: Parcel) : this(
            source.readParcelable(LocalDate::class.java.classLoader),
            source.readParcelableArray(DaySchedule.Item::class.java.classLoader) as Array<DaySchedule.Item>)

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(date, 0)
        dest.writeParcelableArray(items, 0);
    }

    override fun describeContents() = 0

    interface Item : Parcelable {
        override fun describeContents() = 0
        override fun writeToParcel(dest: Parcel, flags: Int)

        @JsonIgnore
        fun getFirstLesson(): Lesson?
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<DaySchedule> {
            override fun createFromParcel(source: Parcel) = DaySchedule(source)
            override fun newArray(size: Int): Array<out DaySchedule?> = arrayOfNulls(size)
        }
    }
}