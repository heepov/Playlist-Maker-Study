package com.example.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.api.SearchTracksInteractor
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.domain.search.model.Resource
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search_history.api.SearchHistoryInteractor
import com.example.playlistmaker.utils.services.vibrate

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_STRING_KEY = "SEARCH_STRING_KEY"
        const val SEARCH_STRING_DEF = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val listSearchQueryTracks = ArrayList<Track>()
    private val adapterSearchQuery: TrackAdapter by lazy {
        TrackAdapter {
            addTrackToHistory(it)
            showTrackView(it)
        }
    }
    private val adapterSearchHistory: TrackAdapter by lazy {
        TrackAdapter {
            showTrackView(it)
        }
    }
    private val searchTracksInteractor = Creator.provideTracksInteractor()
    private val handler = Handler(Looper.getMainLooper())
    private var detailsRunnable: Runnable? = null
    private lateinit var searchHistoryInteractor: SearchHistoryInteractor

    private var listSearchHistoryTracks = ArrayList<Track>()


    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeholderErrorImage: ImageView
    private lateinit var placeholderErrorMessage: TextView
    private lateinit var placeholderErrorExtraMessage: TextView
    private lateinit var placeholderErrorRefreshButton: Button
    private lateinit var placeholderErrorLayout: LinearLayout

    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchHistoryClearButton: Button
    private lateinit var searchHistoryRecyclerView: RecyclerView

    private var isClickAllowed = true
    private val searchRunnable = Runnable { search(searchString) }
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private var progressBar: ProgressBar? = null


    private var searchString: String = SEARCH_STRING_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchHistoryInteractor = Creator.provideSearchHistoryInteractor(this)

        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        progressBar = findViewById<ProgressBar>(R.id.progressBar)

        recyclerViewSearch = findViewById<RecyclerView>(R.id.rvSongSearchList)
        searchField = findViewById<EditText>(R.id.etSearchField)
        clearButton = findViewById<ImageView>(R.id.ivSearchFieldCloseButton)

        placeholderErrorImage = findViewById<ImageView>(R.id.ivPlaceholderErrorImage)
        placeholderErrorMessage = findViewById<TextView>(R.id.tvPlaceholderMessage)
        placeholderErrorExtraMessage = findViewById<TextView>(R.id.tvPlaceholderExtraMessage)
        placeholderErrorRefreshButton = findViewById<Button>(R.id.btnPlaceholderErrorRefresh)
        placeholderErrorLayout = findViewById<LinearLayout>(R.id.placeholderErrorLayout)


        searchHistoryLayout = findViewById<LinearLayout>(R.id.searchHistoryLayout)
        searchHistoryClearButton = findViewById<Button>(R.id.btnClearSearchHistory)
        searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.rvSearchHistoryList)

        listSearchQueryTracks.addAll(
            searchHistoryInteractor.getTrackList()
        )

        updateSearchHistoryList()
        adapterSearchHistory.tracks = listSearchHistoryTracks
        searchHistoryVisibility()

        searchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryRecyclerView.adapter = adapterSearchHistory

        if (searchString.isNotEmpty())
            searchField.setText(searchString)

        findViewById<Toolbar>(R.id.toolBar).setNavigationOnClickListener {
            vibrate()
            finish()
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            progressBar?.visibility = View.GONE
            manager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            listSearchQueryTracks.clear()
            adapterSearchQuery.notifyDataSetChanged()
            placeHolderErrorProcessing(null)
            vibrate()
        }

        placeholderErrorRefreshButton.setOnClickListener {
            manager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            search(searchString)
            vibrate()
        }

        searchHistoryClearButton.setOnClickListener {
            searchHistoryInteractor.clearTrackList()
            updateSearchHistoryList()
            vibrate()
        }

        searchField.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryVisibility(hasFocus)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchHistoryVisibility(searchField.hasFocus() && s?.isEmpty() == true)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchHistoryVisibility(searchField.hasFocus() && s?.isEmpty() == true)
                searchDebounce(s.toString())
                if (s?.isEmpty() == true)
                    listSearchQueryTracks.clear()
            }

            override fun afterTextChanged(s: Editable?) {
                searchString = s.toString()
            }

        }
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchString)
            }
            false
        }
        searchField.addTextChangedListener(simpleTextWatcher)


        adapterSearchQuery.tracks = listSearchQueryTracks
        recyclerViewSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewSearch.adapter = adapterSearchQuery
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce(query: String) {
        searchString = query
        mainThreadHandler.removeCallbacks(searchRunnable)
        mainThreadHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun search(queryInput: String) {
        recyclerViewSearch.visibility = View.GONE
        placeholderErrorLayout.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
        searchHistoryVisibility()
        if (queryInput.trim().isNotEmpty()) {
            searchTracksInteractor.searchTracks(
                expression = queryInput,
                consumer = object : SearchTracksInteractor.TracksConsumer {
                    override fun consume(data: Resource<List<Track>>) {
                        val currentRunnable = detailsRunnable
                        if (currentRunnable != null) {
                            handler.removeCallbacks(currentRunnable)
                        }
                        val newDetailsRunnable = Runnable {
                            when (data) {
                                is Resource.Error -> {
                                    progressBar?.visibility = View.GONE
                                    placeHolderErrorProcessing(data.message.toInt())
                                }
                                is Resource.Success -> {
                                    progressBar?.visibility = View.GONE
                                    recyclerViewSearch.visibility = View.VISIBLE
                                    listSearchQueryTracks.clear()
                                    listSearchQueryTracks.addAll(data.data)
                                    adapterSearchQuery.notifyDataSetChanged()
                                    placeHolderErrorProcessing(null)
                                }
                            }
                        }
                        detailsRunnable = newDetailsRunnable
                        handler.post(newDetailsRunnable)
                    }
                }
            )
        } else {
            progressBar?.visibility = View.GONE
        }
    }

    private fun placeHolderErrorProcessing(
        responseCode: Int?,
    ) {
        if (responseCode == null) {
            placeholderErrorLayout.visibility = View.GONE
            return
        }
        listSearchQueryTracks.clear()
        adapterSearchQuery.notifyDataSetChanged()

        placeholderErrorLayout.visibility = View.VISIBLE
        when (responseCode) {
            200 -> {
                placeholderErrorExtraMessage.visibility = View.GONE
                placeholderErrorRefreshButton.visibility = View.GONE

                placeholderErrorMessage.setText(R.string.noFoundResults)
                placeholderErrorImage.setImageResource(R.drawable.no_results_error)
            }

            else -> {
                placeholderErrorExtraMessage.visibility = View.VISIBLE
                placeholderErrorRefreshButton.visibility = View.VISIBLE
                placeholderErrorMessage.setText(R.string.connectionProblem)
                placeholderErrorExtraMessage.setText(R.string.uploadFailed)
                placeholderErrorImage.setImageResource(R.drawable.server_error)
            }
        }
    }


    private fun addTrackToHistory(track: Track) {
        searchHistoryInteractor.addTrack(track)
        updateSearchHistoryList()
        vibrate()
    }

    private fun showTrackView(track: Track) {
        if (clickDebounce()) {
            val intent = Intent(this, PlayerActivity::class.java).putExtra("track", track)
            startActivity(intent)
        }
    }

    private fun updateSearchHistoryList() {
        listSearchHistoryTracks.clear()
        listSearchHistoryTracks.addAll(
            searchHistoryInteractor.getTrackList()
        )
        adapterSearchHistory.notifyDataSetChanged()
        searchHistoryVisibility()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchString = savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_STRING_KEY, searchString)
        super.onSaveInstanceState(outState)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchHistoryVisibility(condition: Boolean = true) {
        searchHistoryLayout.isVisible =
            condition && listSearchHistoryTracks.isNotEmpty() && searchField.text.isEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacksAndMessages(null)
    }
}