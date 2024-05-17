package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.ItunesApi
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.data.TracksList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ItunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter {
        addTrackToHistory(it)
    }

    private lateinit var recyclerViewSearch: RecyclerView
    private lateinit var searchField: EditText
    private lateinit var clearButton: ImageView

    private lateinit var placeholderErrorImage: ImageView
    private lateinit var placeholderErrorMessage: TextView
    private lateinit var placeholderErrorExtraMessage: TextView
    private lateinit var placeholderErrorRefreshButton: Button
    private lateinit var placeholderErrorLayout: LinearLayout

    private val searchHistoryList = ArrayList<Track>()
    private val searchHistoryAdapter = TrackAdapter {
//        removeTrackFromHistory(it)
    }
    private lateinit var searchHistoryLayout: LinearLayout
    private lateinit var searchHistoryClearButton: Button
    private lateinit var searchHistoryRecyclerView: RecyclerView


    private var searchString: String = SEARCH_STRING_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

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
        searchHistoryList.addAll(
            SearchHistory(
                getSharedPreferences(
                    HISTORY_PREFERENCE,
                    MODE_PRIVATE
                )
            ).getTrackList()
        )
        searchHistoryVisibility()

        searchHistoryAdapter.tracks = searchHistoryList
        searchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryRecyclerView.adapter = searchHistoryAdapter

        if (searchString.isNotEmpty())
            searchField.setText(searchString)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            vibrate()
            finish()
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            manager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            tracks.clear()
            adapter.notifyDataSetChanged()
            placeHolderErrorProcessing(null)
            vibrate()
        }

        placeholderErrorRefreshButton.setOnClickListener {
            manager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            search(searchString)
            vibrate()
        }

        searchHistoryClearButton.setOnClickListener {
            SearchHistory(getSharedPreferences(HISTORY_PREFERENCE, MODE_PRIVATE)).clearTrackList()
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
                // добавил вот такое условие, чтобы при удалении запроса поиска НЕ через кнопку очистить (крестик)
                // и заново не начав писать запрос поиска, чтобы до нажатия применить старый список не появлялся на экране
                if (s?.isEmpty() == true)
                    tracks.clear()
            }

            override fun afterTextChanged(s: Editable?) {
                searchString = s.toString()
//                search(searchString) // мне кажется такое поведение поисковой строки более привычное
            }

        }
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchString)
            }
            false
        }
        searchField.addTextChangedListener(simpleTextWatcher)


        adapter.tracks = tracks
        recyclerViewSearch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewSearch.adapter = adapter
    }

    private fun search(queryInput: String) {
        if (queryInput.trim().isNotEmpty()) {
            service.search(queryInput)
                .enqueue(object : Callback<TracksList> {
                    override fun onResponse(
                        call: Call<TracksList>,
                        response: Response<TracksList>
                    ) {
                        when (response.code()) {
                            200 -> {
                                if (!response.body()?.results.isNullOrEmpty()) {
                                    tracks.clear()
                                    tracks.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                    placeHolderErrorProcessing(null)
                                } else
                                    placeHolderErrorProcessing(response.code())
                            }

                            else -> {
                                placeHolderErrorProcessing(response.code())
                                Log.d("SearchActivity", "Response code: ${response.code()}")
                            }
                        }

                    }

                    override fun onFailure(call: Call<TracksList>, t: Throwable) {
                        placeHolderErrorProcessing(-1)
                        Log.d("SearchActivity", "onFailure: ${t.message.toString()}")
                    }

                })
        }
    }

    private fun placeHolderErrorProcessing(
        responseCode: Int?,
    ) {
        if (responseCode == null) {
            placeholderErrorLayout.visibility = View.GONE
            return
        }
        tracks.clear()
        adapter.notifyDataSetChanged()

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
        SearchHistory(getSharedPreferences(HISTORY_PREFERENCE, MODE_PRIVATE)).addTrack(track)
        updateSearchHistoryList()
        vibrate()
    }

//    private fun removeTrackFromHistory(track: Track) {
//        SearchHistory(getSharedPreferences(HISTORY_PREFERENCE, MODE_PRIVATE)).deleteTrack(track)
//        updateSearchHistoryList()
//        vibrate()
//    }

    private fun updateSearchHistoryList() {
        searchHistoryList.clear()
        searchHistoryList.addAll(
            SearchHistory(
                getSharedPreferences(
                    HISTORY_PREFERENCE,
                    MODE_PRIVATE
                )
            ).getTrackList()
        )
        searchHistoryAdapter.notifyDataSetChanged()
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
    private fun searchHistoryVisibility(condition: Boolean = true){
        searchHistoryLayout.isVisible = condition && searchHistoryList.isNotEmpty() && searchField.text.isEmpty()
    }

    companion object {
        const val SEARCH_STRING_KEY = "SEARCH_STRING_KEY"
        const val SEARCH_STRING_DEF = ""
    }
}