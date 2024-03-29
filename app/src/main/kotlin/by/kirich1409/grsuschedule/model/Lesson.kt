package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Lesson : Parcelable, DaySchedule.Item {

    @JsonIgnore
    val interval: TimeInterval
    val subgroup: Subgroup?
    val teacher: Teacher?
    val type: String?
    val title: String
    val address: String?
    val room: String?
    val groups: Array<Group>?

    @JsonIgnore
    val fullAddress: String?
        get() {
            return if (address.isNullOrEmpty()) {
                null
            } else if (room.isNullOrEmpty()) {
                address!!
            } else {
                "${address!!} - ${room!!}"
            }
        }

    val mapAddress: String?
        get() {
            return if (address.isNullOrEmpty()) {
                null;
            } else {
                "Беларусь, Гродно, ${address!!}"
            }
        }


    @JsonCreator
    constructor(
            @JsonProperty("timeStart") startTime: String?,
            @JsonProperty("timeEnd") endTime: String?,
            @JsonProperty("teacher") teacher: Teacher?,
            @JsonProperty("type") type: String?,
            @JsonProperty("title") title: String,
            @JsonProperty("address") address: String?,
            @JsonProperty("room") room: String?,
            @JsonProperty("subgroup") subgroup: Subgroup?,
            @JsonProperty("groups") groups: Array<Group>?,
            @JsonProperty("interval") interval: TimeInterval?) {

        this.subgroup = if (subgroup.isNull()) null else subgroup
        this.teacher = teacher
        this.type = convertType(type)
        this.title = title
        this.address = address
        this.room = if (room.isNullOrEmpty() || room == "-") null else room
        this.groups = groups
        this.interval = interval ?: TimeInterval(startTime!!, endTime!!)
    }

    constructor(source: Parcel) {
        this.interval = source.readParcelable<TimeInterval>(TimeInterval::class.java.classLoader)
        this.teacher = source.readParcelable(Teacher::class.java.classLoader)
        this.type = source.readString()
        this.title = source.readString()
        this.address = source.readString()
        this.room = source.readString()
        this.subgroup = source.readParcelable<Subgroup>(Subgroup::class.java.classLoader)

        val groupsArray = source.readParcelableArray(Group::class.java.classLoader)
        if (groupsArray != null) {
            this.groups = Array(groupsArray.size, { groupsArray[it] as Group })
        } else {
            this.groups = null
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(interval, 0)
        dest.writeParcelable(teacher, 0)
        dest.writeString(type)
        dest.writeString(title)
        dest.writeString(address)
        dest.writeString(room)
        dest.writeParcelable(subgroup, 0)
        dest.writeParcelableArray(groups, 0)
    }

    @JsonIgnore
    override fun getFirstLesson() = this

    override fun describeContents() = 0

    override fun toString() = "'$title', '$type',  $interval"

    companion object {
        /**
         * Default lesson duration
         */
        public val DURATION = 80;

        public val CREATOR = object : Parcelable.Creator<Lesson> {
            override fun createFromParcel(source: Parcel) = Lesson(source)
            override fun newArray(size: Int) = arrayOfNulls<Lesson>(size)
        }

        private fun convertType(type: String?): String? {
            return when (type) {
                "лек." -> "Лекция"
                "практ.зан." -> "Практическое занятие"
                "лаб." -> "Лабораторная"
                else -> type
            }
        }

        private fun Subgroup?.isNull() = this == null || (this.id <= 0 && this.title.isEmpty())
    }
}
