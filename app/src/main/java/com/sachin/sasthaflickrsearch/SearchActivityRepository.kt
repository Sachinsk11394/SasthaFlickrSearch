package com.sachin.sasthaflickrsearch

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class SearchActivityRepository(private val webService: SearchWebService,
                                    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun getPhotos(text: String, page: Int, perPage: Int): NetworkResponse<FlickrResponse> {
        return withContext(ioDispatcher){
            val response = webService.getPhotos(text, page, perPage)
            if (response.isSuccessful) {
                return@withContext NetworkResponse.success(response.body()!!)
            } else {
                return@withContext NetworkResponse.failure<FlickrResponse>(Throwable("Something went wrong, Please try again later."))
            }
        }
    }
}