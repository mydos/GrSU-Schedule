package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.utils.MINUTES_IN_HOUR
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/18/15.
 */
public class TimeInterval : Comparable<TimeInterval>, Parcelable {

    val startTime: Time
    val endTime: Time

    @JsonIgnore
    val durationInMinutes: Int

    @JsonCreator
    constructor(@JsonProperty("startTime") startTime: Time, @JsonProperty("endTime") endTime: Time) {
        this.startTime = startTime
        this.endTime = endTime
        durationInMinutes = (endTime.hours - startTime.hours) * MINUTES_IN_HOUR +
                (endTime.minutes - startTime.minutes)
    }

    public constructor(startTime: String, endTime: String) : this(Time(startTime), Time(endTime))

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other == null || other.javaClass != TimeInterval::class.java -> false
            other is TimeInterval -> compareTo(other) == 0
            else -> false
        }
    }

    override fun hashCode() = startTime.hashCode() * 10000 + endTime.hashCode()

    constructor(source: Parcel) : this(
            source.readParcelable<Time>(Time::class.java.classLoader),
            source.readParcelable<Time>(Time::class.java.classLoader))

    override fun toString(): String = "$startTime - $endTime"

    override fun compareTo(other: TimeInterval): Int {
        val startTimeCompareTo = startTime.compareTo(other.startTime)
        return if (startTimeCompareTo == 0) endTime.compareTo(other.endTime) else startTimeCompareTo
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(startTime, 0)
        dest.writeParcelable(endTime, 0)
    }

    override fun describeContents() = 0

    companion object {
        public val CREATOR = object : Parcelable.Creator<TimeInterval> {
            override fun createFromParcel(source: Parcel) = TimeInterval(source)
            override fun newArray(size: Int) = arrayOfNulls<TimeInterval>(size)
        }
    }
}
