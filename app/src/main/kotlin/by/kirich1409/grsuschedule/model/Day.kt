package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.utils.Constants
import by.kirich1409.grsuschedule.utils.LessonComparator
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Day : Iterable<Lesson>, Parcelable {

    @JsonFormat(pattern = "yyyy-MM-dd")
    val date: Date
    val lessons: Array<Lesson>

    @JsonIgnore
    val formatDate by lazy {
        val date = OUT_DATE_FORMAT.format(date)
        "${date.charAt(0).toUpperCase()}${date.substring(1)}"
    }

    @JsonCreator
    public constructor(
            @JsonProperty("date") date: String,
            @JsonProperty("lessons") lessons: List<Lesson>) {
        this.date = INPUT_DATE_FORMATTER.parse(date)
        this.lessons = lessons.sortedWith(LessonComparator()).toTypedArray()
    }

    constructor(source: Parcel) {
        this.date = source.readSerializable() as Date
        this.lessons = source.readParcelableArray(Lesson::class.java.classLoader) as Array<Lesson>
    }

    override fun iterator(): Iterator<Lesson> = lessons.iterator()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(date)
        dest.writeParcelableArray(lessons, 0)
    }

    override fun describeContents(): Int {
        throw UnsupportedOperationException()
    }

    companion object {
        public val INPUT_DATE_FORMATTER = SimpleDateFormat("yyyy-MM-dd", Constants.LOCALE_RU)
        private val OUT_DATE_FORMAT = SimpleDateFormat("EEEE, d MMMM", Constants.LOCALE_RU)

        public val CREATOR: Parcelable.Creator<Day> = object : Parcelable.Creator<Day> {
            override fun createFromParcel(source: Parcel) = Day(source)
            override fun newArray(size: Int): Array<Day?> = arrayOfNulls(size)
        }
    }
}
