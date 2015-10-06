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
    val fullname: String

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
        this.fullname = fullname ?: "$surname $name $patronym"
    }

    public constructor(id: Int, post: String?, email: String?, fullName: String?)
        : this(id, null, null, null, post, email, fullName)

    constructor(source: Parcel) {
        id = source.readInt()
        post = source.readString()
        email = source.readString()
        fullname = source.readString()
    }

    override fun toString(): String {
        return fullname
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(post)
        dest.writeString(email)
        dest.writeString(fullname)
    }

    companion object {
        public val CREATOR: Parcelable.Creator<Teacher> = object : Parcelable.Creator<Teacher> {
            override fun createFromParcel(source: Parcel): Teacher = Teacher(source)
            override fun newArray(size: Int): Array<Teacher?> = arrayOfNulls(size)
        }
    }
}
