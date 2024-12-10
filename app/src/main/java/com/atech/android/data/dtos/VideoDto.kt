package com.atech.android.data.dtos


import com.atech.android.data.dtos.ResultVideo
import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("results")
    val results: List<ResultVideo>?
)