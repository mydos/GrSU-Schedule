package by.kirich1409.grsuschedule.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by kirillrozov on 9/13/15.
 */
public class Teacher : Parcelable {

    val id: Int
    val post: String?
    val email: String?
    val fullName: String

    @JsonCreator
    public constructor(
            @JsonProperty("id") id: Int,
            @JsonProperty("name") name: String?,
            @JsonProperty("surname") surname: String?,
            @JsonProperty("patronym") patronym: String?,
            @JsonProperty("post") post: String?,
            @JsonProperty("email") email: String?,
            @JsonProperty("fullname") fullname: String?) {
        this.id = id
        this.post = post
        this.email = email
        this.fullName = fullname ?: "$surname $name $patronym"
    }

    public constructor(id: Int, fullName: String?)
    : this(id, null, null, null, null, null, fullName)

    public constructor(id: Int, post: String?, email: String?, fullName: String?)
    : this(id, null, null, null, post, email, fullName)

    constructor(source: Parcel) {
        id = source.readInt()
        post = source.readString()
        email = source.readString()
        fullName = source.readString()
    }

    override fun toString() = fullName

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(post)
        dest.writeString(email)
        dest.writeString(fullName)
    }

    companion object {
        public val CREATOR = object : Parcelable.Creator<Teacher> {
            override fun createFromParcel(source: Parcel) = Teacher(source)
            override fun newArray(size: Int) = arrayOfNulls<Teacher>(size)
        }
    }
}
