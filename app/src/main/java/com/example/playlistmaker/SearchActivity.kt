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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.data.ItunesApi
import com.example.playlistmaker.data.ItunesTrack
import com.example.playlistmaker.data.ItunesTracksResponse
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
    private val tracks = ArrayList<ItunesTrack>()
    private val adapter = TrackAdapter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var backButton: ImageView

    private lateinit var placeholderErrorImage: ImageView
    private lateinit var placeholderErrorMessage: TextView
    private lateinit var placeholderErrorExtraMessage: TextView
    private lateinit var placeholderErrorRefreshButton: Button
    private lateinit var placeholderErrorLayout: LinearLayout


    private var searchString: String = SEARCH_STRING_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById<RecyclerView>(R.id.rvSongSearchList)
        inputEditText = findViewById<EditText>(R.id.etSearchField)
        clearButton = findViewById<ImageView>(R.id.ivSearchFieldCloseButton)
        backButton = findViewById<ImageView>(R.id.ivBack)

        placeholderErrorImage = findViewById<ImageView>(R.id.ivPlaceholderErrorImage)
        placeholderErrorMessage = findViewById<TextView>(R.id.tvPlaceholderMessage)
        placeholderErrorExtraMessage = findViewById<TextView>(R.id.tvPlaceholderExtraMessage)
        placeholderErrorRefreshButton = findViewById<Button>(R.id.btnPlaceholderErrorRefresh)
        placeholderErrorLayout = findViewById<LinearLayout>(R.id.placeholderErrorLayout)



        if (searchString != "")
            inputEditText.setText(searchString)

        backButton.setOnClickListener {
            vibrate()
            finish()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            placeholderErrorLayout.visibility = View.GONE
            tracks.clear()
            adapter.notifyDataSetChanged()
            vibrate()
        }

        placeholderErrorRefreshButton.setOnClickListener {
            search(searchString)
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchString = s.toString()
//                search(searchString) // мне кажется такое поведение поисковой строки более привычное
            }

        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search(searchString)
            }
            false
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)


        adapter.tracks = tracks
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun search(queryInput: String) {
        if (queryInput.trim().isNotEmpty()) {
            service.search(queryInput)
                .enqueue(object : Callback<ItunesTracksResponse> {
                    override fun onResponse(
                        call: Call<ItunesTracksResponse>,
                        response: Response<ItunesTracksResponse>
                    ) {
                        when (response.code()) {
                            200 -> {
                                if (!response.body()?.results.isNullOrEmpty()) {
                                    Log.d("SearchActivity", "Size ${response.body()?.results!!.size}")
                                    tracks.clear()
                                    tracks.addAll(response.body()?.results!!)
                                    adapter.notifyDataSetChanged()
                                } else
                                    placeHolderErrorProcessing(response.code(), null)
                            }

                            else -> placeHolderErrorProcessing(response.code(), null)
                        }

                    }

                    override fun onFailure(call: Call<ItunesTracksResponse>, t: Throwable) {
                        placeHolderErrorProcessing(-1, t.message.toString())
                    }

                })
        }
    }

    private fun placeHolderErrorProcessing(
        responseCode: Int,
        errorMessage: String?
    ) {
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
            -1 -> {
                placeholderErrorMessage.setText(R.string.something_went_wrong)
                placeholderErrorImage.setImageResource(R.drawable.no_results_error)
                if (errorMessage!= null) {
                    placeholderErrorExtraMessage.visibility = View.VISIBLE
                    placeholderErrorExtraMessage.text = errorMessage
                }
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

    companion object {
        const val SEARCH_STRING_KEY = "SEARCH_STRING_KEY"
        const val SEARCH_STRING_DEF = ""
    }
}