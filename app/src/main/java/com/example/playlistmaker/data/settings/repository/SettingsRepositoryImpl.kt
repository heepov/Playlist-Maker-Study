package com.example.playlistmaker.data.settings.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.settings.repository.SettingsRepository
import com.example.playlistmaker.utils.constants.Constants.THEME_SWITCHER_KEY

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {
    override fun checkDarkMode(): Boolean {
        return sharedPreferences.getBoolean(THEME_SWITCHER_KEY, false)
    }

    override fun setDarkMode(value: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_SWITCHER_KEY, value).apply()
    }
}