package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Group : BaseItem {

    val studentCount: Int
    val faculty: Faculty?
    val department: Department?
    val course: Int

    public constructor(id: Int, title: String) : this(id, title, -1, null, null, -1)

    @JsonCreator
    public constructor(
            @JsonProperty("id") id: Int,
            @JsonProperty("title") title: String,
            @JsonProperty("students") studentsCount: Int,
            @JsonProperty("faculty") faculty: Faculty?,
            @JsonProperty("department") department: Department?,
            @JsonProperty("course") course: Int) : super(id, title) {
        this.studentCount = studentsCount
        this.faculty = faculty
        this.department = department
        this.course = course
    }

    constructor(source: Parcel) : super(source) {
        studentCount = source.readInt()
        faculty = source.readParcelable<Faculty>(Faculty::class.java.classLoader)
        department = source.readParcelable<Department>(Department::class.java.classLoader)
        course = source.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeInt(studentCount)
        dest.writeParcelable(faculty, flags)
        dest.writeParcelable(department, flags)
        dest.writeInt(course)
    }

    companion object {
        public val CREATOR = object : Parcelable.Creator<Group> {
            override fun createFromParcel(source: Parcel) = Group(source)
            override fun newArray(size: Int) = arrayOfNulls<Group>(size)
        }
    }
}
