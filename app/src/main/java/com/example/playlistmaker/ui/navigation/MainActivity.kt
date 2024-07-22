package com.example.playlistmaker.ui.navigation

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity
import com.example.playlistmaker.vibrate

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSearch).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            vibrate()
        }

        findViewById<Button>(R.id.btnLibrary).setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
            vibrate()
        }

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            vibrate()
        }

    }
}
