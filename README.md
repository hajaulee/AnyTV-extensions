# AnyTV-extensions

Phần mở rộng cho ứng dụng [AnyTV](https://github.com/hajaulee/AnyTV) cho phép tích hợp nội dung của các web xem phim vào 1 ứng dụng duy nhất.

## Vị trí module

Đặt phần mở rộng mở tại: `"/extensions/vi/{XXX}"`

## BaseExtension 

Phần mở rộng kế thừa từ [BaseExtension](https://github.com/hajaulee/AnyTV-extensions/blob/master/common/src/main/java/com/hajaulee/anytv/extension/BaseExtension.kt)

```kotlin
abstract class BaseExtension {
    open val name = "base"
    open val baseUrl = "https://something.com"
    open val thumbnailRatio = 1.5
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
    open fun onMoviePageLoadedJavascript(): String = "Android.ready()"
    ...
}

```

## Khởi nguồn

Ứng dụng dựa trên ý tưởng của [Tachiyomi](https://github.com/tachiyomiorg/tachiyomi) và [Tachiyomi-extensions](https://github.com/tachiyomiorg/tachiyomi-extensions)
