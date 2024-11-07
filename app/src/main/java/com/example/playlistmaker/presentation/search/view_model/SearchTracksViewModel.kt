package com.example.playlistmaker.presentation.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.api.SearchTracksInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search_history.api.SearchHistoryInteractor
import com.example.playlistmaker.presentation.player.state.PlayerScreenState
import com.example.playlistmaker.presentation.search.state.SearchTracksScreenState

class SearchTracksViewModel(
//    private val searchTracksInteractor: SearchTracksInteractor,
//    private val seachHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
//    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
//
//    private val _screenState = MutableLiveData<SearchTracksScreenState>()
//    val screenStateLiveData: LiveData<SearchTracksScreenState> get() = _screenState
//
//    private val _seachHistoryList = MutableLiveData<ArrayList<Track>>()
//    val searchHistoryListLiveData: LiveData<ArrayList<Track>> get() = _seachHistoryList
//
//
//    private fun updateSearchHistoryList() {
//        searchHistoryListLiveData.value.clear()
//        searchHistoryListLiveData.value.addAll(
//            seachHistoryInteractor.getTrackList()
//        )
//
//        adapterSearchHistory.notifyDataSetChanged()
//        searchHistoryVisibility()
//    }
//
//    fun getSearchHistoryList(): LiveData<ArrayList<Track>> = searchHistoryListLiveData
//    fun getScreenStateLiveData(): LiveData<SearchTracksScreenState> = screenStateLiveData
//
//    companion object {
//        fun getViewModelFactory(): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return SearchTracksViewModel(
//                        Creator.provideSearchTracksInteractor(),
//                        Creator.provideSearchHistoryInteractor(Context)
//                    ) as T
//                }
//            }
//
//        const val SEARCH_STRING_KEY = "SEARCH_STRING_KEY"
//        const val SEARCH_STRING_DEF = ""
//        private const val SEARCH_DEBOUNCE_DELAY = 2000L
//        private const val CLICK_DEBOUNCE_DELAY = 1000L
//    }

}