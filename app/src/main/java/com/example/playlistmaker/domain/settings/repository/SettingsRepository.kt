package com.example.playlistmaker.domain.settings.repository

interface SettingsRepository {
    fun checkDarkMode(): Boolean
    fun setDarkMode(value: Boolean)
}