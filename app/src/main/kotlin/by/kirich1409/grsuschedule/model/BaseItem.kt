package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.CallSuper
import java.text.Collator
import java.util.*

/**
 * Created by kirillrozov on 9/18/15.
 */
abstract class BaseItem(val id: Int, val title: String) : Parcelable, Comparable<BaseItem> {

    constructor(parcel: Parcel):this(parcel.readInt(), parcel.readString())

    override fun equals(other: Any?): Boolean {
        return when {
            other == this -> true
            other == null, javaClass != other.javaClass -> false
            else -> id == (other as BaseItem).id
        }
    }

    override fun compareTo(other: BaseItem): Int {
        return title.compareTo(other.title)
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return title
    }

    override fun describeContents(): Int {
        return 0
    }

    @CallSuper
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
    }

    public class TitleComparator<E : BaseItem> : Comparator<E> {

        val mCollator = Collator.getInstance(Locale("be", "BY"))

        override fun compare(lhs: E, rhs: E): Int {
            return mCollator.compare(lhs.title, rhs.title)
        }
    }
}
