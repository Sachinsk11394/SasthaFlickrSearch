package com.sachin.sasthaflickrsearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModelFactory: SearchViewModelFactory
    private var isLoading = false

    private val mViewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this@SearchActivity,
            mViewModelFactory
        ).get(SearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.hide()

        DaggerCoreComponent.builder().coreModule(CoreModule(this@SearchActivity)).build()
            .injectSearchActivity(this@SearchActivity)

        setSearch()
    }

    private fun setSearch() {
        searchText.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mViewModel.page = 1
                mViewModel.getPhotos(textView.text.toString())
                true;
            }
            false;
        }

        photos.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    if ((photos.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() == mViewModel.photoList.size - 1) {
                        mViewModel.page++
                        if(mViewModel.total > mViewModel.photoList.size) {
                            mViewModel.getPhotos(searchText.text.toString())
                            progressBar.visibility = View.VISIBLE
                            isLoading = true
                        }
                    }
                }
            }
        })

        val photosList = mViewModel.getPhotos(searchText.text.toString())
        photosList.observe(this@SearchActivity, Observer { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    photos.adapter?.let { adapter ->
                        if(isLoading) {
                            mViewModel.photoList.addAll(response.data.photosData.photoList ?: arrayListOf())
                            (adapter as PhotosAdapter).addPhotoList(response.data.photosData)
                            progressBar.visibility = View.GONE
                            photos.smoothScrollToPosition((photos.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition() + 1)
                            isLoading = false
                        } else {
                            mViewModel.photoList = response.data.photosData.photoList as ArrayList<Photo>
                            photos.smoothScrollToPosition(0)
                            (adapter as PhotosAdapter).setPhotoList(response.data.photosData)
                        }
                        mViewModel.total = response.data.photosData.total
                    }?: run {
                        mViewModel.photoList = response.data.photosData.photoList as ArrayList<Photo>
                        photos.adapter = PhotosAdapter(response.data.photosData)
                        mViewModel.total = response.data.photosData.total
                    }
                }
                is NetworkResponse.Failure -> {
                    Toast.makeText(this@SearchActivity, "API error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}