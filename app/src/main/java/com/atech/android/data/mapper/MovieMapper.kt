package com.atech.android.data.mapper

import com.atech.android.data.dtos.MovieDto
import com.atech.android.domain.entities.MovieModel

/**
 * Created by Abraham Lay on 14/06/20.
 */

class MovieMapper : Mapper<MovieDto, List<MovieModel>?>() {
    override fun apply(from: MovieDto): List<MovieModel>? {
        return from?.results?.map { movie ->
            MovieModel(
                movie.voteCount,
                movie.id,
                movie.video,
                movie.voteAverage,
                movie.originalTitle,
                movie.popularity,
                movie.posterPath,
                movie.originalLanguage,
                movie.originalTitle,
                movie.genreIds,
                movie.backdropPath,
                movie.adult,
                movie.overview,
                movie.releaseDate

            )
        }
    }
}