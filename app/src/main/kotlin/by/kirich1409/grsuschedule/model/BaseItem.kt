package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.CallSuper
import by.kirich1409.grsuschedule.utils.LOCALE_BY_BE
import java.text.Collator
import java.util.*

/**
 * Created by kirillrozov on 9/18/15.
 */
abstract class BaseItem : Parcelable, Comparable<BaseItem> {

    val id: Int
    val title: String

    constructor(id: Int, title: String) {
        this.id = id
        this.title = title
    }

    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString())

    override fun equals(other: Any?): Boolean {
        return when {
            other == this -> true
            other == null || javaClass != other.javaClass -> false
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

        val сollator = Collator.getInstance(LOCALE_BY_BE)

        override fun compare(lhs: E, rhs: E) = сollator.compare(lhs.title, rhs.title)
    }
}
