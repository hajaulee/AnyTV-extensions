package com.hajaulee.anytv.extension.vi.hentaiz

import com.hajaulee.anytv.extension.BaseExtension
import com.hajaulee.anytv.model.Episode
import com.hajaulee.anytv.model.Movie
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class HentaiZ : BaseExtension() {

    override val baseUrl = "https://hentaiz.cc"
    override val name = "HentaiZ"
    override val thumbnailRatio = 0.69
    override val userAgent = "Mozilla/5.0 (Linux; Android 10; SM-A205U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.5195.136 Mobile Safari/537.36"

    override fun latestMovieRequest(page: Int): String {
        return "$baseUrl/hentai-vietsub/page/$page/"
    }

    override fun latestMovieSelector(): String {
        return ".videos .col"
    }

    override fun latestMovieFromElement(e: Element): Movie {
        var url = e.selectFirst("a").attr("href")
        if ("http" in url) {
            url = url.substring(url.lastIndexOf("http"))
        }
        return Movie.buildMovieInfo(
            "",
            e.selectFirst("h3").text(),
            Movie.LOAD_DESCRIPTION_MESSAGE,
            e.selectFirst(".text-truncate").text(),
            url,
            e.selectFirst("img").attr("abs:src"),
            e.selectFirst("img").attr("abs:src")
        )
    }

    override fun popularMovieRequest(page: Int): String {
        return "$baseUrl/page/$page/?s&sort=meta_value_num%3Adesc%3Ax_post_views_count"
    }
    override fun popularMovieSelector() = latestMovieSelector()
    override fun popularMovieFromElement(e: Element) = latestMovieFromElement(e)

    override fun searchMovieRequest(keyword: String, page: Int): String {
        return "$baseUrl/?sort=&s=$keyword"
    }
    override fun searchMovieSelector() = latestMovieSelector()
    override fun searchMovieFromElement(e: Element) = latestMovieFromElement(e)

    override fun movieDetailParse(doc: Document): Movie {
        val movie = Movie()
        movie.description = doc.selectFirst("#collapse-details .rounded p").text()
        movie.genres = doc.select("#collapse-details .list-inline li").joinToString(", "){
            it.text()
        }
        return movie

    }

    override fun relatedMovieParse(doc: Document): List<Movie> {
        return doc.select(latestMovieSelector()).map { latestMovieFromElement(it) }
    }

    override fun firstEpisodeUrl(doc: Document): String? = null

    override fun episodesParse(doc: Document): List<Episode> {
        return listOf(Episode("1", doc.baseUri()))
    }

    override fun episodeSelector(): String = ""
    override fun episodeFromElement(e: Element): Episode? = null

    override fun moviePlayerContainerSelector() = "iframe"
}