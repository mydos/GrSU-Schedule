package by.kirich1409.grsuschedule.schedule

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.model.TimeInterval
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by kirillrozov on 9/13/15.
 */
public class AlinementLessons : DaySchedule.Item {

    val interval: TimeInterval
    val title: String
    val lessons: Array<Lesson>

    constructor(interval: TimeInterval, title: String, lessons: Array<Lesson>) {
        this.interval = interval
        this.title = title
        this.lessons = lessons
    }

    protected constructor(source: Parcel) {
        this.interval = source.readParcelable(TimeInterval::class.java.classLoader)
        this.title = source.readString()

        val lessonsClassLoader = Lesson::class.java.classLoader
        val array = source.readParcelableArray(lessonsClassLoader)
        this.lessons = Array(array.size(), { array[it] as Lesson })
    }

    override fun toString(): String {
        return "'$title', $interval"
    }

    @JsonIgnore
    override fun getFirstLesson() = lessons[0]

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(interval, 0)
        dest.writeString(title)
        dest.writeParcelableArray(lessons, 0)
    }

    companion object {
        val CREATOR = object : Parcelable.Creator<AlinementLessons> {
            override fun newArray(size: Int) = arrayOfNulls<AlinementLessons>(size)
            override fun createFromParcel(source: Parcel) = AlinementLessons(source)
        }
    }
}
