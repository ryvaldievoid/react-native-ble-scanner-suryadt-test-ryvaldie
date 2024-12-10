package com.atech.domain.interactors

import com.atech.android.domain.FlowableUseCase
import com.atech.android.domain.PostExecutionThread
import com.atech.android.domain.entities.MovieModel
import com.atech.android.domain.repositories.MovieRepository
import io.reactivex.Flowable

/**
 * Created by Abraham Lay on 13/06/20.
 */
class GetFavoriteMovies constructor(
    private val repository: MovieRepository,
    postExecutionThread: PostExecutionThread
) : FlowableUseCase<List<MovieModel>?, Void?>(postExecutionThread) {
    override fun build(params: Void?): Flowable<List<MovieModel>?> {
        return repository.getFavoriteMovies()
    }
}