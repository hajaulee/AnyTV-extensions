package com.hajaulee.anytv.extension.vi.phimmoi

import com.hajaulee.anytv.model.Episode
import com.hajaulee.anytv.model.Movie
import com.hajaulee.anytv.extension.BaseExtension

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.net.URLDecoder

class Phimmoi: BaseExtension() {

    override val name= "Phimmoi"
    override val baseUrl = "https://phimmoichillc.net"
    override val thumbnailRatio = 1.7

    override fun latestMovieUrl(page: Int): String {
        return "$baseUrl/list/phim-bo/page-$page/"
    }

    override fun latestMoviesParse(doc: Document): List<Movie>? {
        return null
    }


    override fun latestMovieSelector(): String {
        return ".list-film .item.small"
    }

    override fun latestMovieFromElement(e: Element): Movie {
        val movieUrl = e.selectFirst("a").attr("abs:href")
        val title = e.selectFirst("h3").text()
        val imgUrl = extractImgUrl(e.selectFirst("img").attr("src"))
        val label = e.selectFirst(".label").text()
        val latestEpisode = Movie.extractFirstNumber(label)
        return Movie.buildMovieInfo(
            latestEpisode.ifEmpty { label },
            title,  //Title
            Movie.LOAD_DESCRIPTION_MESSAGE,
            "",
            movieUrl,
            imgUrl,
            imgUrl
        )
    }

    override fun popularMovieUrl(page: Int): String {
        return "$baseUrl/list/phim-hot/page-$page/"
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
        return "$baseUrl/tim-kiem/${keyword.replace(" ", "+")}/"
    }

    override fun searchMovieSelector(): String {
        return latestMovieSelector()
    }

    override fun searchMovieFromElement(e: Element): Movie {
        return latestMovieFromElement(e)
    }

    override fun movieDetailParse(doc: Document): Movie {
        val movie = Movie()
        movie.year = doc.selectFirst(".entry-meta li:eq(1) a").text()
        movie.description = doc.selectFirst(".film-content")?.text() ?: ""
        movie.genres = doc.selectFirst(".entry-meta li:eq(3)")?.select("a")?.eachText()
            ?.let { java.lang.String.join(", ", it) } ?: ""
        return movie
    }

    override fun relatedMoviesParse(doc: Document): List<Movie> {
        val elements = doc.select(".film-related .item")
        return elements.map { e ->
            Movie.buildMovieInfo("",
                e.selectFirst("p").text(),  //Title
                Movie.LOAD_DESCRIPTION_MESSAGE,
                "",
                e.selectFirst("a").attr("href"),
                extractImgUrl(e.selectFirst("img").attr("data-src")),  // Bg Image
                extractImgUrl(e.selectFirst("img").attr("data-src")) // Card image
            )
        }
    }

    override fun firstEpisodeUrl(doc: Document): String? {
        return doc.selectFirst(".film-info a").attr("href")
    }

    override fun episodesParse(doc: Document): List<Episode>? {
        return doc.select("#list_episodes a")?.map { Episode(it.text(), it.attr("href")) }
    }

    override fun episodeSelector(): String {
        return ""
    }

    override fun episodeFromElement(e: Element): Episode {
        return Episode("", "")
    }

    override fun moviePlayerSelector(): String {
        return "video.jw-video"
    }


    private fun extractImgUrl(url: String): String{
        var imgUrl = url;
        if (imgUrl.contains("&url")){
            imgUrl = URLDecoder.decode(imgUrl.split("&url=")[1], "UTF-8")

        }
        return imgUrl;
    }
}