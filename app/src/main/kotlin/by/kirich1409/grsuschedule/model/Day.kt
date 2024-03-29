package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.utils.LessonComparator
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Day : Iterable<Lesson>, Parcelable {

    val date: LocalDate
    val lessons: Array<Lesson>

    @JsonCreator
    public constructor(
            @JsonProperty("date") date: String,
            @JsonProperty("lessons") lessons: List<Lesson>) {
        this.date = LocalDate(date)
        this.lessons = lessons.sortedWith(LessonComparator()).toTypedArray()
    }

    constructor(source: Parcel) {
        this.date = source.readParcelable(LocalDate::class.java.classLoader)

        val lessonsArray = source.readParcelableArray(Lesson::class.java.classLoader)
        this.lessons = Array(lessonsArray.size, { lessonsArray[it] as Lesson })
    }

    override fun iterator() = lessons.iterator()

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(date, 0)
        dest.writeParcelableArray(lessons, 0)
    }

    override fun describeContents() = 0

    companion object {
        public val CREATOR: Parcelable.Creator<Day> = object : Parcelable.Creator<Day> {
            override fun createFromParcel(source: Parcel) = Day(source)
            override fun newArray(size: Int) = arrayOfNulls<Day>(size)
        }
    }
}
