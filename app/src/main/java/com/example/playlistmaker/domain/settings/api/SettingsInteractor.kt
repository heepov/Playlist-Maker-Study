package com.example.playlistmaker.domain.settings.api

interface SettingsInteractor {
    fun checkDarkMode(): Boolean
    fun setDarkMode(value: Boolean)
}