package com.sachin.sasthaflickrsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

open class SearchViewModel(private val repository: SearchActivityRepository) : ViewModel() {
    private val photosDataResponse = MutableLiveData<NetworkResponse<FlickrResponse>>()
    var photoList : ArrayList<Photo> = arrayListOf()
    var page: Int = 1
    var total: Int = 1
    private val perPage: Int = 100

    // Get images as photos
    fun getPhotos(text: String) : LiveData<NetworkResponse<FlickrResponse>> {
        viewModelScope.launch {
            photosDataResponse.postValue(repository.getPhotos(text, page, perPage))
        }
        return photosDataResponse
    }
}