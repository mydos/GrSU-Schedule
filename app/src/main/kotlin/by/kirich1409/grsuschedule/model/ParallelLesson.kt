package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by kirillrozov on 10/9/15.
 */
class ParallelLesson : DaySchedule.Item {

    val interval: TimeInterval
    val lessons: Array<Lesson>

    constructor(time: TimeInterval, items: Array<Lesson>) {
        this.interval = time
        this.lessons = items
    }

    protected constructor(source: Parcel) {
        this.interval = source.readParcelable<TimeInterval>(TimeInterval::class.java.classLoader)
        val array = source.readParcelableArray(Array<Lesson>::class.java.classLoader)
        this.lessons = Array(array.size, { array[it] as Lesson })
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(interval, 0)
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