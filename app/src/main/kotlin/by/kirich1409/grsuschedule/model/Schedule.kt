package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Schedule @JsonCreator constructor(@JsonProperty("days") val days: Array<Day>) : Parcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelableArray(days, 0)
    }

    constructor(source: Parcel) :
        this(source.readParcelableArray(Day::class.java.classLoader) as Array<Day>)

    override fun describeContents(): Int = 0

    companion object {
        public val CREATOR: Parcelable.Creator<Schedule> = object : Parcelable.Creator<Schedule> {
            override fun createFromParcel(source: Parcel) = Schedule(source)
            override fun newArray(size: Int): Array<Schedule?> = arrayOfNulls(size)
        }
    }
}
