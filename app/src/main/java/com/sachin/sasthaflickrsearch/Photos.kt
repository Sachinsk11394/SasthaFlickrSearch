package com.sachin.sasthaflickrsearch

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FlickrResponse(
    @Json(name = "photos")
    val photosData: Photos,

    @Json(name = "stat")
    val stat: String,
)

@JsonClass(generateAdapter = true)
data class Photos(
    @Json(name = "page")
    val page: Int,

    @Json(name = "pages")
    val pages: Int,

    @Json(name = "perpage")
    val perpage: Int,

    @Json(name = "total")
    val total: Int,

    @Json(name = "photo")
    val photoList: List<Photo>?,
)

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "id")
    val id: String,

    @Json(name = "secret")
    val secret: String,

    @Json(name = "server")
    val server: String,

    @Json(name = "title")
    val title: String,
)
