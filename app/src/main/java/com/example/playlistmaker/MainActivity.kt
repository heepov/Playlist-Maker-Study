package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : AppCompatActivity() {
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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

    private fun vibrate() {
        if (vibrator.hasVibrator()) {
            // set vibration on 50 milliseconds
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    override fun onDestroy() {
        // destroy vibrator
        vibrator.cancel()
        super.onDestroy()
    }
}
