package by.kirich1409.grsuschedule.utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.format.DateFormat
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import kotlin.text.Regex

/**
 * Created by kirillrozov on 9/27/15.
 */
public class Time : Parcelable, Comparable<Time> {
    val hours: Int
    val minutes: Int

    @JsonCreator
    public constructor(@JsonProperty("hours") hours: Int, @JsonProperty("minutes") minutes: Int) {
        this.hours = hours
        this.minutes = minutes
    }

    public constructor(time: String) {
        if (time.matches(timeRegex)) {
            val groups = time.split(":")
            hours = groups[0].toInt()
            minutes = groups[1].toInt()
        } else {
            throw RuntimeException("Illegal time format '$time'")
        }
    }

    constructor(source: Parcel) {
        hours = source.readInt()
        minutes = source.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(hours)
        dest.writeInt(minutes)
    }

    override fun describeContents(): Int = 0

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other == null, other.javaClass != Time::class.java -> false
            other is Time -> compareTo(other) == 0
            else -> false
        }
    }

    override fun hashCode() = hours * Constants.MINUTES_IN_HOUR + minutes

    override fun compareTo(other: Time): Int {
        val compareHours = hours.compareTo(other.hours)
        return if (compareHours != 0) compareHours else minutes.compareTo(other.minutes)
    }

    override fun toString() = "%2d:%02d".format(hours, minutes)

    fun toString(context: Context): String {
        val hours = if (DateFormat.is24HourFormat(context) || hours <= 12) {
            this.hours
        } else {
            this.hours % 12
        }
        return "%2d:%02d".format(hours, minutes)
    }

    companion object {
        val timeRegex = Regex("\\d{1,2}:\\d{2}")

        public val CREATOR = object : Parcelable.Creator<Time> {
            override fun newArray(size: Int): Array<out Time?> = arrayOfNulls(size)
            override fun createFromParcel(source: Parcel): Time = Time(source)
        }
    }
}