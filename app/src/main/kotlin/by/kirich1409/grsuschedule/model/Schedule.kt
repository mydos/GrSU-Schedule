package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Schedule : Parcelable {

    val days: Array<Day>

    @JsonCreator
    constructor(@JsonProperty("days") days: Array<Day>) {
        this.days = days
    }

    internal constructor(source: Parcel) {
        val days = source.readParcelableArray(Day::class.java.classLoader)
        this.days = Array(days.size, { days[it] as Day })
    }

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeParcelableArray(days, 0)

    override fun describeContents() = 0

    companion object {
        public val CREATOR = object : Parcelable.Creator<Schedule> {
            override fun createFromParcel(source: Parcel) = Schedule(source)
            override fun newArray(size: Int) = arrayOfNulls<Schedule>(size)
        }
    }
}
