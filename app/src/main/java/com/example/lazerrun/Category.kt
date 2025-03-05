package com.example.lazerrun

import android.os.Parcel
import android.os.Parcelable

data class Category(
    val id: String,
    val name: String,
    val initialDistance: Int,
    val lapDistance: Int,
    val lapCount: Int,
    val shootDistance: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(initialDistance)
        parcel.writeInt(lapDistance)
        parcel.writeInt(lapCount)
        parcel.writeInt(shootDistance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}