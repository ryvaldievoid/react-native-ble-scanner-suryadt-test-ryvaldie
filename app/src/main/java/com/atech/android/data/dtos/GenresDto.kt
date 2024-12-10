package com.atech.android.data.dtos


import com.atech.android.data.dtos.Genre
import com.google.gson.annotations.SerializedName

/**
 * Created by Abraham Lay on 2020-06-09.
 */

data class GenresDto(
    @SerializedName("genres")
    val genres: List<Genre>?
)