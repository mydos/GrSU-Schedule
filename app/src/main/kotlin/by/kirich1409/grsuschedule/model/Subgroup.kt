package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/18/15.
 */
public class Subgroup : BaseItem {

    @JsonCreator
    public constructor(
            @JsonProperty("id") id: Int,
            @JsonProperty("title") title: String) : super(id, title) {
    }

    public constructor(source: Parcel) : super(source)

    companion object {

        public val CREATOR: Parcelable.Creator<Subgroup> = object : Parcelable.Creator<Subgroup> {

            override fun createFromParcel(source: Parcel): Subgroup {
                return Subgroup(source)
            }

            override fun newArray(size: Int): Array<Subgroup?> {
                return arrayOfNulls(size)
            }
        }
    }
}

public fun Subgroup?.isNull(): Boolean = this == null || (this.id <= 0 && this.title.isEmpty())
