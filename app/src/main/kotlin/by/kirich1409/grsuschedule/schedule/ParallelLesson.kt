package by.kirich1409.grsuschedule.schedule

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.model.Lesson
import by.kirich1409.grsuschedule.model.TimeInterval
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by kirillrozov on 10/9/15.
 */
class ParallelLesson : DaySchedule.Item {

    val time: TimeInterval
    val lessons: Array<Lesson>

    constructor(time: TimeInterval, items: Array<Lesson>) {
        this.time = time
        this.lessons = items
    }

    protected constructor(source: Parcel) {
        this.time = source.readParcelable<TimeInterval>(TimeInterval::class.java.classLoader)
        this.lessons =
                source.readParcelableArray(Array<Lesson>::class.java.classLoader) as Array<Lesson>
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(time, 0)
        dest.writeParcelableArray(lessons, 0)
    }

    @JsonIgnore
    override fun getFirstLesson() = lessons[0]

    companion object {
        val CREATOR = object : Parcelable.Creator<ParallelLesson> {
            override fun createFromParcel(source: Parcel) = ParallelLesson(source)
            override fun newArray(size: Int): Array<out ParallelLesson?> = arrayOfNulls(size)
        }
    }
}