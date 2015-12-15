package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.Keep

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Department : BaseItem {

    @JsonCreator
    public constructor(
            @JsonProperty("id") id: Int,
            @JsonProperty("title") title: String) : super(id, title)

    public constructor(parcel: Parcel) : super(parcel)

    companion object {
        @Keep
        public val CREATOR = object : Parcelable.Creator<Department> {
            override fun createFromParcel(source: Parcel) = Department(source)
            override fun newArray(size: Int) = arrayOfNulls<Department>(size)
        }
    }
}
