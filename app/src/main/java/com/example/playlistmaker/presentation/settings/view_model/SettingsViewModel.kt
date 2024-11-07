package com.example.playlistmaker.presentation.settings.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.creator.Creator.provideSettingsInteractor
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.settings.api.SettingsInteractor
import com.example.playlistmaker.presentation.player.state.PlayerScreenState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.App
import com.example.playlistmaker.presentation.search.state.SearchTracksScreenState
import com.example.playlistmaker.presentation.settings.state.SettingsScreenState


class SettingsViewModel(
    private val application: Application,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<SettingsScreenState>()
    val screenStateLiveData: LiveData<SettingsScreenState> get() = _screenState

    private val _themeState = MutableLiveData<Boolean>()
    val themeStateLiveData: LiveData<Boolean> get() = _themeState

    init {
        _screenState.value = SettingsScreenState.Loading
        loadSettings()
    }

    private fun loadSettings() {
        _themeState.value = settingsInteractor.checkDarkMode()
        _screenState.value = SettingsScreenState.Content()
    }

    fun setThemeState(checked: Boolean) {
        Log.d("SettingsViewModel", "setThemeState: $checked")
        settingsInteractor.setDarkMode(checked)
        _themeState.value = checked
        (application.applicationContext as App).switchTheme(checked)
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(application, Creator.provideSettingsInteractor(application)) as T
                }
            }
    }
}