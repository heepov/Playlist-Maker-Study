package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SearchActivity : AppCompatActivity() {
    private var searchString: String = SEARCH_STRING_DEF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.etSearchField)
        if (searchString != "")
            inputEditText.setText(searchString)

// Не очень понял зачем нам переопределять onRestoreInstanceState если можно напрямую получить данные
//        if (savedInstanceState != null) {
//            inputEditText.setText(
//                savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF)
//            )
//        }

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            vibrate()
            finish()
        }
        val clearButton = findViewById<ImageView>(R.id.ivSearchFieldCloseButton)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            vibrate()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                searchString = s.toString()
            }

        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        val recyclerView = findViewById<RecyclerView>(R.id.rvSongSearchList)
        recyclerView.adapter = TrackAdapter(
            songs = List(5) {
                TrackCard(
                    imageUrl = songsImages[it],
                    title = songsTitles[it],
                    artist = songsArtists[it],
                    duration = songsDuration[it]
                )
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
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
        val songsTitles = arrayOf(
            "Smells Like Teen Spirit",
            "Billie Jean",
            "Stayin' Alive",
            "Whole Lotta Love",
            "Sweet Child O'Mine"
        )
        val songsArtists =
            arrayOf("Nirvana", "Michael Jackson", "Bee Gees", "Led Zeppelin", "Guns N' Roses")
        val songsImages = arrayOf(
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        )
        val songsDuration = arrayOf(301000L, 275000L, 250000L, 333000L, 303000L)
    }
}