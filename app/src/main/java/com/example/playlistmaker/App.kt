package com.example.playlistmaker

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

const val SETTINGS_PREFERENCE = "settings_preference"
const val THEME_SWITCHER_KEY = "theme_switcher_key"

class App : Application() {
    private var dakTheme = false
    override fun onCreate() {
        super.onCreate()
        val preferences = getSharedPreferences(SETTINGS_PREFERENCE, MODE_PRIVATE)
        switchTheme(preferences.getBoolean(THEME_SWITCHER_KEY, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        dakTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
