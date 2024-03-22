package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    private lateinit var vibrator: Vibrator
    private lateinit var searchString:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            vibrate()
            finish()
        }
        val inputEditText = findViewById<EditText>(R.id.etSearchField)
        val clearButton = findViewById<ImageView>(R.id.ivSearchFieldCloseButton)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            vibrate()
        }

        if (savedInstanceState != null) {
            inputEditText.setText(savedInstanceState.getString(SEARCH_STRING_KEY, SEARCH_STRING_DEF))
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

    private fun vibrate() {
        if (vibrator.hasVibrator()) {
            // set vibration on 50 milliseconds with default vibrate
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    override fun onDestroy() {
        // destroy vibrator
        vibrator.cancel()
        super.onDestroy()
    }
    companion object {
        const val SEARCH_STRING_KEY = "SEARCH_STRING_KEY"
        const val SEARCH_STRING_DEF = ""
    }
}