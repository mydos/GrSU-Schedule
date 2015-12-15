package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by kirillrozov on 10/9/15.
 */
public class DaySchedule : Parcelable {

    val date: LocalDate
    val items: Array<Item>

    constructor(date: LocalDate, items: Array<DaySchedule.Item>) {
        this.date = date
        this.items = items
    }

    constructor(source: Parcel) {
        this.date = source.readParcelable(LocalDate::class.java.classLoader)

        val itemsArray = source.readParcelableArray(Item::class.java.classLoader)
        this.items =
                if (itemsArray.size == 0)
                    emptyArray()
                else
                    Array(itemsArray.size, { itemsArray[it] as Item })
    }

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
            override fun newArray(size: Int) = arrayOfNulls<DaySchedule>(size)
        }
    }
}