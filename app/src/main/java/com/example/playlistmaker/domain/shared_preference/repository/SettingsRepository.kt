package com.example.playlistmaker.domain.shared_preference.repository

interface SettingsRepository {
    fun checkDarkMode(): Boolean
    fun setDarkMode(enabled: Boolean)
}