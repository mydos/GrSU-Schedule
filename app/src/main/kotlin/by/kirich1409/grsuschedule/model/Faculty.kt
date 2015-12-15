package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Faculty : BaseItem {

    @JsonCreator
    public constructor(
            @JsonProperty("id") id: Int,
            @JsonProperty("title") title: String) : super(id, title)

    constructor(source: Parcel) : super(source)

    companion object {
        @Keep
        public val CREATOR = object : Parcelable.Creator<Faculty> {
            override fun createFromParcel(source: Parcel) = Faculty(source)
            override fun newArray(size: Int) = arrayOfNulls<Faculty>(size)
        }
    }
}
