package com.sachin.sasthaflickrsearch

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SearchWebService {
    /**
     * Get all categories from server
     */
    @GET("rest/?method=flickr.photos.search")
    suspend fun getPhotos(
        @Query("text") text: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Response<FlickrResponse>
}