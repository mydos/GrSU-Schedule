package by.kirich1409.grsuschedule.schedule

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.model.TimeInterval
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by kirillrozov on 9/23/15.
 */
public data class FreeTime(val interval: TimeInterval) : DaySchedule.Item {

    protected constructor(source: Parcel) :
        this(source.readParcelable<TimeInterval>(TimeInterval::class.java.classLoader))

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(interval, 0)
    }

    @JsonIgnore
    override fun getFirstLesson() = null

    companion object {
        val CREATOR = object : Parcelable.Creator<FreeTime> {
            override fun createFromParcel(source: Parcel) = FreeTime(source)
            override fun newArray(size: Int): Array<out FreeTime?> = arrayOfNulls(size)
        }
    }
}