package com.example.playlistmaker.domain.shared_preference.impl

import com.example.playlistmaker.domain.shared_preference.api.SettingsInteractor
import com.example.playlistmaker.domain.shared_preference.repository.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository
): SettingsInteractor {
    override fun checkDarkMode(): Boolean {
        return repository.checkDarkMode()
    }

    override fun setDarkMode(enabled: Boolean) {
        repository.setDarkMode(enabled)
    }

}