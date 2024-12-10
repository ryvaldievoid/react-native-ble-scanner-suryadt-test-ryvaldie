package com.atech.domain.interactors

import com.atech.android.domain.FlowableUseCase
import com.atech.android.domain.PostExecutionThread
import com.atech.android.domain.entities.VideoModel
import com.atech.android.domain.repositories.MovieRepository
import io.reactivex.Flowable

/**
 * Created by Abraham Lay on 10/06/20.
 */

class GetVideos constructor(
    private val repository: MovieRepository,
    postExecutionThread: PostExecutionThread
) : FlowableUseCase<List<VideoModel>, GetVideos.Params>(postExecutionThread) {
    override fun build(params: Params): Flowable<List<VideoModel>> {
        return repository.getVideo(params.apiKey, params.movieId)
    }

    data class Params(val apiKey: String, val movieId: Int)
}