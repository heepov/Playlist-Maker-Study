package com.example.playlistmaker.presentation.settings.state

sealed class SettingsScreenState {
        object Loading : SettingsScreenState()
        class Content() : SettingsScreenState()
        data class Error(val message: String) : SettingsScreenState()
}