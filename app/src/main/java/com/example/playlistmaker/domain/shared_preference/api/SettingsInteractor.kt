package com.example.playlistmaker.domain.shared_preference.api

interface SettingsInteractor {
    fun checkDarkMode(): Boolean
    fun setDarkMode(enabled: Boolean)
}