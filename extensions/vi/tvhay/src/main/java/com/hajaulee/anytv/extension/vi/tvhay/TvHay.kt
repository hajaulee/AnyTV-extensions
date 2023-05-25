package com.hajaulee.anytv.extension.vi.tvhay

import com.hajaulee.anytv.extension.BaseExtension
import com.hajaulee.anytv.model.Episode
import com.hajaulee.anytv.model.Movie
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URLDecoder

class TvHay: BaseExtension() {
    override val name = "TvHay"
    override val baseUrl = "https://itvhay.org"
    override val thumbnailRatio = 0.75

    override fun latestMovieUrl(page: Int): String {
        return "$baseUrl/phim-moi/page/$page/"
    }

    override fun latestMoviesParse(doc: Document): List<Movie>? {
        return null
    }

    override fun latestMovieSelector(): String {
        return ".list-film li"
    }

    override fun latestMovieFromElement(e: Element): Movie {
        val movieUrl =  e.selectFirst("a").attr("abs:href")
        val title = e.selectFirst("a").attr("title")
        val imgUrl = extractImgUrl(e.selectFirst("img").attr("data-original"))
        val label = e.selectFirst(".status")?.text() ?: ""
        val latestEpisode = Movie.extractFirstNumber(label).ifEmpty { label }
        return Movie.buildMovieInfo(latestEpisode,
            title,  //Title
            Movie.LOAD_DESCRIPTION_MESSAGE,
            "",
            movieUrl,
            imgUrl,
            imgUrl
        )
    }

    override fun popularMovieUrl(page: Int): String {
        return "$baseUrl/phim-hai/page/$page/"
    }

    override fun popularMoviesParse(doc: Document): List<Movie>? {
        return null
    }

    override fun popularMovieSelector(): String {
        return latestMovieSelector()
    }

    override fun popularMovieFromElement(e: Element): Movie {
        return latestMovieFromElement(e)
    }

    override fun searchMovieUrl(keyword: String, page: Int): String {
        return "$baseUrl/search/${keyword.replace(" ", "+")}"
    }

    override fun searchMovieSelector(): String {
        return latestMovieSelector()
    }

    override fun searchMovieFromElement(e: Element): Movie {
        return latestMovieFromElement(e)
    }

    override fun movieDetailParse(doc: Document): Movie {
        val movie = Movie()
        movie.description = doc.selectFirst(".detail #info-film")?.text() ?: ""
        movie.genres = doc.select(".dinfo .col1 dd:eq(2) a").joinToString(", ") { it.text() } ?: ""
        return movie
    }

    override fun relatedMoviesParse(doc: Document): List<Movie> {
        return listOf()
    }

    override fun firstEpisodeUrl(doc: Document): String? {
        return doc.selectFirst("a.btn-watch").attr("href")
    }

    override fun episodesParse(doc: Document): List<Episode>? {
        return doc.select(".episodelist a")?.map {
            Episode(
                it.text(),
                it.attr("href")
            )
        }
    }

    override fun episodeSelector(): String {
        return ""
    }

    override fun episodeFromElement(e: Element): Episode {
        return Episode("", "")
    }

    override fun moviePlayerSelector(): String {
        return "video"
    }

    override fun moviePlayerContainerSelector(): String {
        return "#media iframe"
    }

    private fun extractImgUrl(url: String): String{
        var imgUrl = url;
        if (imgUrl.contains("&url")){
            imgUrl = URLDecoder.decode(imgUrl.split("&url=")[1], "UTF-8")

        }
        return imgUrl;
    }
}