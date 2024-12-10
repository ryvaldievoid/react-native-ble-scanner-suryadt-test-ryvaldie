package com.atech.android.data.repositoriesimpl

import com.atech.android.data.db.MovieDao
import com.atech.android.data.mapper.DetailMovieMapper
import com.atech.android.data.mapper.MovieMapper
import com.atech.android.data.mapper.ReviewMapper
import com.atech.android.data.mapper.VideoMapper
import com.atech.android.data.remote.MovieApi
import com.atech.android.domain.entities.DetailMovieModel
import com.atech.android.domain.entities.MovieModel
import com.atech.android.domain.entities.ReviewModel
import com.atech.android.domain.entities.VideoModel
import com.atech.android.domain.repositories.MovieRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * Created by Abraham Lay on 2020-06-09.
 */


class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val movieDao: MovieDao,
    private val movieMapper: MovieMapper,
    private val reviewMapper: ReviewMapper,
    private val videoMapper: VideoMapper,
    private val detailMovieMapper: DetailMovieMapper
) :
    MovieRepository {

    override fun getPopularMovies(apiKey: String): Flowable<List<MovieModel>?> =
        api.getPopularMovies(apiKey).map(movieMapper)

    override fun getTopRatedMovies(apiKey: String): Flowable<List<MovieModel>?> =
        api.getTopRatedMovies(apiKey).map(movieMapper)

    override fun getNowPlayingMovies(apiKey: String): Flowable<List<MovieModel>?> =
        api.getNowPlayingMovies(apiKey).map(movieMapper)

    override fun getReviews(apiKey: String, movieId: Int): Flowable<List<ReviewModel>> =
        api.getReviews(movieId, apiKey).map(reviewMapper)

    override fun getVideo(apiKey: String, movieId: Int): Flowable<List<VideoModel>> =
        api.getVideo(movieId, apiKey).map(videoMapper)

    override fun getMovieDetails(apiKey: String, movieId: Int): Flowable<DetailMovieModel> =
        api.getMovieDetails(movieId, apiKey).map(detailMovieMapper)

    override fun getFavoriteMovies(): Flowable<List<MovieModel>?> =
        movieDao.selectFavoriteMovie()

    override fun getFavoriteMovie(movieId: Int): Flowable<MovieModel?> =
        movieDao.select(movieId)

    override fun insertFavoriteMovie(movieModel: MovieModel) =
        movieDao.insert(movieModel)

    override fun deleteFavoriteMovie(movieModel: MovieModel): Int =
        movieDao.delete(movieModel)
}