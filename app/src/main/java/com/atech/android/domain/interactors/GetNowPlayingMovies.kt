package com.atech.domain.interactors

import com.atech.android.domain.FlowableUseCase
import com.atech.android.domain.PostExecutionThread
import com.atech.android.domain.entities.MovieModel
import com.atech.android.domain.repositories.MovieRepository
import io.reactivex.Flowable


/**
 * Created by Abraham Lay on 2019-09-29.
 */
class GetNowPlayingMovies constructor(
    private val repository: MovieRepository,
    postExecutionThread: PostExecutionThread
) : FlowableUseCase<List<MovieModel>?, GetNowPlayingMovies.Params>(postExecutionThread) {
    override fun build(params: Params): Flowable<List<MovieModel>?> =
        repository.getNowPlayingMovies(params.apiKey)

    data class Params(val apiKey: String)
}