package com.hajaulee.anytv.model

import java.io.Serializable
import java.util.*

class Episode(var title: String, var url: String) : Serializable {

    var currentPosition = 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val episode = other as Episode
        return title == episode.title && url == episode.url
    }

    override fun hashCode(): Int {
        return Objects.hash(title, url)
    }
}