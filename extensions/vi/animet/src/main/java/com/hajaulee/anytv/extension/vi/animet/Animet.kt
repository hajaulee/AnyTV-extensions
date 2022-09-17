package com.hajaulee.anytv.extension.vi.animet

import com.hajaulee.anytv.extension.BaseExtension
import com.hajaulee.anytv.model.Episode
import com.hajaulee.anytv.model.Movie
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

open class Animet : BaseExtension() {
    override val name = "Animet"
    override val baseUrl = "https://animet.net"
    override val thumbnailRatio= 0.75


    override fun popularMovieRequest(page: Int): String {
        return "$viewSource$baseUrl/bang-xep-hang/month.html"
    }

    override fun popularMovieSelector(): String = "li.group"

    override fun popularMovieFromElement(e: Element): Movie {
        val movieUrl = addBaseUrl(baseUrl, e.selectFirst("a").attr("href"))
        val title = e.selectFirst(".title-item").text()
        val episode = e.selectFirst(".mli-eps")?.text()
        return Movie.buildMovieInfo(
            Movie.extractFirstNumber(episode).ifEmpty { episode },
            title,  //Title
            Movie.LOAD_DESCRIPTION_MESSAGE,
            "",
            movieUrl,
            e.selectFirst("img").attr("src"),  // Bg Image
            e.selectFirst("img").attr("src")) // Card image;
    }

    override fun latestMovieRequest(page: Int): String {
        return "$viewSource$baseUrl/danh-sach/phim-bo/trang-$page.html"
    }

    override fun latestMovieSelector(): String =  ".TPostMv"


    override fun latestMovieFromElement(e: Element): Movie {
        val movieUrl = addBaseUrl(baseUrl, e.selectFirst("a").attr("href"))
        val title = e.selectFirst(".Title").text()
        val description = e.selectFirst(".Description p")?.text() ?: ""
        val genres = e.select(".Genre a").joinToString(", ") { it.text() }
        val episode = e.selectFirst(".mli-eps")?.text()
        return Movie.buildMovieInfo(
            Movie.extractFirstNumber(episode).ifEmpty { episode },
            title,  //Title
            description,
            genres,
            movieUrl,
            e.selectFirst("img").attr("src"),  // Bg Image
            e.selectFirst("img").attr("src")) // Card image;
    }

    override fun searchMovieRequest(keyword: String, page: Int): String {
        return "$baseUrl/tim-kiem/$keyword/trang-$page.html"
    }


    override fun searchMovieSelector(): String = latestMovieSelector()
    override fun searchMovieFromElement(e: Element): Movie = latestMovieFromElement(e)


    override fun movieDetailParse(doc: Document): Movie {
        val movie = Movie()
        movie.description = doc.selectFirst(".Description")?.text() ?: ""
        movie.genres = doc.select(".InfoList li:eq(1) a").joinToString(", ") { it.text() }
        return movie
    }

    override fun relatedMovieParse(doc: Document): List<Movie> {
        val elements = doc.select(".MovieListRelated .TPostMv")
        val movies = elements.map { e ->
            latestMovieFromElement(e)
        }
        return movies
    }

    override fun firstEpisodeUrl(doc: Document): String? {
        return doc.selectFirst(".Image a").attr("href")
    }

    override fun episodeSelector(): String = ".list-episode a"

    override fun episodeFromElement(e: Element): Episode {
        return Episode(e.text(), e.attr("href"))
    }

    override fun moviePlayerSelector(): String {
        return "video"
    }
    override fun moviePlayerContainerSelector(): String = "#media-player iframe"

    override fun onMoviePageLoadedJavascript(): String {
        return """
            // Video element ready to play
            setTimeout(() => {
                Android.ready();
            }, 2000);
            """.trimMargin()
    }
}