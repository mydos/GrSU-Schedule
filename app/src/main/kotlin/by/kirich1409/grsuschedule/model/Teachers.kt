package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
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

    constructor(source: Parcel) : this(
            source.readParcelableArray(Teacher::class.java.classLoader) as Array<Teacher>) {

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelableArray(items, 0)
    }

    private class TeacherComparator : Comparator<Teacher> {

        val mRuCollator: Collator = Collator.getInstance(Locale("be", "BY"))

        override fun compare(lhs: Teacher, rhs: Teacher): Int {
            return mRuCollator.compare(lhs.fullname, rhs.fullname)
        }
    }

    companion object {

        public val CREATOR: Parcelable.Creator<Teachers> = object : Parcelable.Creator<Teachers> {
            override fun createFromParcel(source: Parcel): Teachers {
                return Teachers(source)
            }

            override fun newArray(size: Int): Array<Teachers?> {
                return arrayOfNulls(size)
            }
        }
    }
}
