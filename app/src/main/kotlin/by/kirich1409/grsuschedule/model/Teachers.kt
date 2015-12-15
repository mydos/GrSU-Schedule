package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import by.kirich1409.grsuschedule.utils.LOCALE_BY_BE
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.text.Collator
import java.util.*

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Teachers : ItemList<Teacher>, Parcelable {

    @JsonCreator
    constructor(@JsonProperty("items") teachers: Array<Teacher>) : super(teachers) {
        Arrays.sort(items, TeacherComparator())
    }

    constructor(source: Parcel) : super(readTeacherArray(source))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = dest.writeParcelableArray(items, 0)

    private class TeacherComparator : Comparator<Teacher> {
        val collator = Collator.getInstance(LOCALE_BY_BE)
        override fun compare(lhs: Teacher, rhs: Teacher) =
                collator.compare(lhs.fullName, rhs.fullName)
    }

    companion object {
        public val CREATOR = object : Parcelable.Creator<Teachers> {
            override fun createFromParcel(source: Parcel) = Teachers(source)
            override fun newArray(size: Int) = arrayOfNulls<Teachers>(size)
        }

        private fun readTeacherArray(source: Parcel): Array<Teacher> {
            val itemsArray = source.readParcelableArray(Teacher::class.java.classLoader)
            return Array(itemsArray.size, { itemsArray[it] as Teacher })
        }
    }
}
