package com.example.playlistmaker.data.shared_preference.repository

import com.example.playlistmaker.data.shared_preference.SharedPreferencesManager
import com.example.playlistmaker.domain.shared_preference.repository.SettingsRepository
import com.example.playlistmaker.utils.constants.Constants.THEME_SWITCHER_KEY

class SettingsRepositoryImpl(private val preferencesManager: SharedPreferencesManager) :
    SettingsRepository {
    override fun checkDarkMode(): Boolean {
        return preferencesManager.getBoolean(THEME_SWITCHER_KEY)
    }

    override fun setDarkMode(enabled: Boolean) {
        preferencesManager.putBoolean(THEME_SWITCHER_KEY, enabled)
    }
}