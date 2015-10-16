package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.CallSuper
import by.kirich1409.grsuschedule.utils.Constants
import java.text.Collator
import java.util.*

/**
 * Created by kirillrozov on 9/18/15.
 */
abstract class BaseItem(val id: Int, val title: String) : Parcelable, Comparable<BaseItem> {

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString())

    override fun equals(other: Any?): Boolean {
        return when {
            other == this -> true
            other == null, javaClass != other.javaClass -> false
            else -> id == (other as BaseItem).id
        }
    }

    override fun compareTo(other: BaseItem) = title.compareTo(other.title)

    override fun hashCode() = id

    override fun toString() = title

    override fun describeContents() = 0

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
    }

    public class TitleComparator<E : BaseItem> : Comparator<E> {

        val сollator = Collator.getInstance(Constants.LOCALE_BY_BE)

        override fun compare(lhs: E, rhs: E) = сollator.compare(lhs.title, rhs.title)
    }
}
