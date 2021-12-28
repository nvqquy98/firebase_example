package com.example.firebaseexample.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageData(
    @Expose
    @SerializedName("title")
    val title: String?,
    @Expose
    @SerializedName("description")
    val description: String?,
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String?
) : Parcelable