package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.domain.settings.repository.SettingsRepository

class SettingsInteractorImpl(
    private val repository: SettingsRepository
): SettingsInteractor {
    override fun checkDarkMode(): Boolean {
        return repository.checkDarkMode()
    }

    override fun setDarkMode(value: Boolean) {
        repository.setDarkMode(value)
    }

}