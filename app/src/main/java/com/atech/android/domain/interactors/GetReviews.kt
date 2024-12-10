package com.atech.domain.interactors

import com.atech.android.domain.FlowableUseCase
import com.atech.android.domain.PostExecutionThread
import com.atech.android.domain.entities.ReviewModel
import com.atech.android.domain.repositories.MovieRepository
import io.reactivex.Flowable

/**
 * Created by Abraham Lay on 10/06/20.
 */
class GetReviews constructor(
    private val repository: MovieRepository,
    postExecutionThread: PostExecutionThread
) : FlowableUseCase<List<ReviewModel>, GetReviews.Params>(postExecutionThread) {
    override fun build(params: Params): Flowable<List<ReviewModel>> {
        return repository.getReviews(params.apiKey, params.movieId)
    }

    data class Params(val apiKey: String, val movieId: Int)
}