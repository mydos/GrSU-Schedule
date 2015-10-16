package by.kirich1409.grsuschedule.utils

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kirillrozov on 10/9/15.
 */
@JsonSerialize(using = LocalDate.Serializer::class)
class LocalDate : Parcelable, Comparable<LocalDate> {

    val day: Int
    val month: Int
    val year: Int

    constructor(day: Int, month: Int, year: Int) {
        this.day = day
        this.month = month
        this.year = year
    }

    constructor(data: String) {
        val parts = data.split("-")
        if (parts.size() != 3) {
            throw IllegalArgumentException("Illegal data format=$data")
        }
        year = parts[0].toInt()
        month = parts[1].toInt()
        day = parts[2].toInt()
    }

    constructor(source: Parcel) {
        day = source.readInt()
        month = source.readInt()
        year = source.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(day)
        dest.writeInt(month)
        dest.writeInt(year)
    }

    override fun equals(other: Any?): Boolean {
        return when {
            other === this -> true
            other == null, other.javaClass != LocalDate::class.java -> false
            other is LocalDate -> compareTo(other) == 0
            else -> false
        }
    }

    public fun format(short: Boolean): String {
        val pattern = if (short) "EE, d MMM" else "EEEE, d MMMM"
        val dateFormat = SimpleDateFormat(pattern, Constants.LOCALE_RU)
        val dateFormatted = dateFormat.format(toDate())
        val result = "${dateFormatted.charAt(0).toUpperCase()}${dateFormatted.substring(1)}"
        return if (result.endsWith(".")) result.substring(0, result.length() - 1) else result
    }

    override fun compareTo(other: LocalDate): Int {
        var result = year.compareTo(other.year)
        return if (result == 0) {
            result = month.compareTo(other.month)
            if (result == 0) day.compareTo(other.day) else result
        } else {
            result
        }
    }

    override fun hashCode() = year + month * 12 + day * 30

    fun toDate(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.DAY_OF_MONTH, day)
        return cal.time
    }

    override fun toString() = "%02d-%02d-%04d".format(day, month, year)

    override fun describeContents() = 0

    public class Serializer : JsonSerializer<LocalDate>() {
        override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeString("%04d-%02d-%02d".format(value.year, value.month, value.day))
        }

        override fun handledType() = LocalDate::class.java
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<LocalDate> {
            override fun newArray(size: Int): Array<out LocalDate?> = arrayOfNulls<LocalDate>(size)

            override fun createFromParcel(source: Parcel) = LocalDate(source)
        }
    }
}
