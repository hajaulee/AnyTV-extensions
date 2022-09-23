package com.hajaulee.anytv.extension

import com.hajaulee.anytv.model.Episode
import com.hajaulee.anytv.model.Movie
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

abstract class BaseExtension {
    open val name = "base"
    open val baseUrl = "https://something.com"
    open val thumbnailRatio = 1.5
    open val userAgent: String? = null

    abstract fun latestMovieRequest(page: Int): String
    open fun latestMoviesParse(doc: Document): List<Movie>? = null
    abstract fun latestMovieSelector(): String
    abstract fun latestMovieFromElement(e: Element): Movie?

    abstract fun popularMovieRequest(page: Int): String
    open fun popularMoviesParse(doc: Document): List<Movie>? = null
    abstract fun popularMovieSelector(): String
    abstract fun popularMovieFromElement(e: Element): Movie?

    abstract fun searchMovieRequest(keyword: String, page: Int): String
    open fun searchMoviesParse(doc: Document): List<Movie>? = null
    abstract fun searchMovieSelector(): String
    abstract fun searchMovieFromElement(e: Element): Movie?

    abstract fun movieDetailParse(doc: Document): Movie
    abstract fun relatedMovieParse(doc: Document): List<Movie>

    abstract fun firstEpisodeUrl(doc: Document): String?
    open fun episodesParse(doc: Document): List<Episode>? = null
    abstract fun episodeSelector(): String
    abstract fun episodeFromElement(e: Element): Episode?

    open fun moviePlayerSelector(): String = "video"
    open fun moviePlayerContainerSelector(): String = moviePlayerSelector()
    open fun onMoviePageLoadedJavascript(): String = ""

    protected fun addBaseUrl(base: String, path: String): String {
        var newPath = path
        if (!newPath.contains("://")) {
            newPath = if (newPath.startsWith("/")) {
                base + newPath
            } else {
                "$base/$newPath"
            }
        }
        return newPath
    }

    fun textAsImage(s: String): String {
        return "$testAsImagePrefix$s"
    }

    companion object {
        const val viewSource = "view-source:"
        private const val testAsImagePrefix = "!text:"
    }
}