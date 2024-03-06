package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val switcher = findViewById<Switch>(R.id.switcher_theme)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val isDarkMode = sharedPreferences.getBoolean("IS_DARK_THEME",
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> true
                Configuration.UI_MODE_NIGHT_NO -> false
                else -> false
            }
        )

        Toast.makeText(this, "Is Dark Mode: $isDarkMode", Toast.LENGTH_SHORT).show()

        switcher.isChecked = isDarkMode

        switcher.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("IS_DARK_THEME", isChecked).apply()
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }
    }
}