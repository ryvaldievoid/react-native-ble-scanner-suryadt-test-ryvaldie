package com.atech.android.data.dtos


import com.atech.android.data.dtos.ResultReview
import com.google.gson.annotations.SerializedName

data class ReviewDto(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<ResultReview>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)