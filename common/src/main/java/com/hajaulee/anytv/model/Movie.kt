package com.hajaulee.anytv.model

import java.io.Serializable

class Movie : Serializable {
    var title: String? = null
    var description: String? = null
    var backgroundImageUrl: String? = null
    var cardImageUrl: String? = null
    var movieUrl: String? = null
    var genres: String? = null
    var firstEpisodeLink: String? = null
    var currentEp: Episode? = null
    var detailLoaded = false
    var latestEpisode: String? = null

    // @Deprecated property
    var studio: String? = null
    var category: String? = null
    var year: String? = null
    var duration: String? = null

    private var episodeList: List<Episode>? = null

    override fun toString(): String {
        return """Movie{
	 title='$title',
     genres='$genres',
     description='$description',
	 videoUrl='$movieUrl',
	 backgroundImageUrl='${backgroundImageUrl}',
	 cardImageUrl='$cardImageUrl'}"""
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Movie) movieUrl == other.movieUrl else false
    }

    override fun hashCode(): Int {
        return try {
            movieUrl!!.length
        } catch (e: Exception) {
            0
        }
    }

    fun copy(): Movie {
        val newMovie = Movie()
        newMovie.title = title
        newMovie.description = description
        newMovie.backgroundImageUrl = backgroundImageUrl
        newMovie.cardImageUrl = cardImageUrl
        newMovie.movieUrl = movieUrl
        newMovie.studio = studio
        newMovie.category = category
        newMovie.genres = genres
        newMovie.year = year
        newMovie.duration = duration
        newMovie.firstEpisodeLink = firstEpisodeLink
        newMovie.currentEp = currentEp
        newMovie.episodeList = episodeList
        newMovie.latestEpisode = latestEpisode
        return newMovie
    }

    companion object {
        const val LOAD_DESCRIPTION_MESSAGE = "Đang tải tóm tắt nội dung"
        const val serialVersionUID = 727566175075960653L

        fun extractFirstNumber(s: String?): String{
            if (s.isNullOrEmpty()){
                return ""
            }
            val matcher = "[0-9]+".toRegex()
            val matchedResults = matcher.findAll(s).map{it.value}.toList()
            return if (matchedResults.isEmpty()) "" else matchedResults[0]
        }

        fun buildMovieInfo(latestEpisode: String?, title: String?,
                           description: String?, genres: String?,
                           videoUrl: String?, cardImageUrl: String?,
                           backgroundImageUrl: String?): Movie {
            val movie = Movie()
            movie.title = title
            movie.description = description
            movie.genres = genres
            movie.latestEpisode = latestEpisode
            movie.cardImageUrl = cardImageUrl
            movie.backgroundImageUrl = backgroundImageUrl
            movie.movieUrl = videoUrl
            return movie
        }
    }
}