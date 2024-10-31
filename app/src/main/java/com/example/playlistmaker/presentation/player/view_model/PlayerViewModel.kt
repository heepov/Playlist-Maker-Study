package com.example.playlistmaker.presentation.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.presentation.player.mapper.TrackInfoMapper
import com.example.playlistmaker.presentation.player.state.PlayerScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track?,
    private val tracksInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> get() = _screenState

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>()
    val mediaPlayerStateLiveData: LiveData<MediaPlayerState> get() = _mediaPlayerState

    private val _trackTime = MutableLiveData<String>()
    val trackTimeLiveData: LiveData<String> get() = _trackTime

    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    init {
        if (track == null) {
            _screenState.value = PlayerScreenState.Error("Track data is missing")
        } else {
            _screenState.value = PlayerScreenState.Loading
            loadTrackData(track)
        }
    }

    private fun loadTrackData(track: Track) {
        try {
            val trackInfo = TrackInfoMapper.map(track)
            _screenState.value = PlayerScreenState.Content(trackInfo)
            preparePlayer()
        } catch (e: Exception) {
            _screenState.value = PlayerScreenState.Error("Failed to load track data")
        }
    }

    fun preparePlayer() {
        track?.let {
            tracksInteractor.setMediaPlayerDataSource(it)
        }

        tracksInteractor.prepare(listener = object : MediaPlayerInteractor.OnStateChangeListener {
            override fun onStateChange(state: MediaPlayerState) {
                _mediaPlayerState.value = state
                when (state) {
                    MediaPlayerState.PREPARED -> {
                        resetTimer()
                    }

                    MediaPlayerState.PLAYING -> {
                        startTimer()
                    }

                    MediaPlayerState.PAUSED -> {
                        stopTimer()
                    }

                    MediaPlayerState.DEFAULT -> {
                        stopTimer()
                    }
                }
            }
        })
    }


    fun onPlayPauseClicked() {
        when (tracksInteractor.getMediaPlayerState()) {
            MediaPlayerState.PREPARED, MediaPlayerState.PAUSED -> {
                tracksInteractor.start()
            }

            MediaPlayerState.PLAYING -> {
                tracksInteractor.pause()
            }

            MediaPlayerState.DEFAULT -> {
            }
        }
    }

    private fun startTimer() {
        mainThreadHandler.postDelayed(object : Runnable {
            override fun run() {
                _trackTime.value = tracksInteractor.getMediaPlayerCurrentTime()
                mainThreadHandler.postDelayed(this, REFRESH_DELAY_MILLIS)
            }
        }, REFRESH_DELAY_MILLIS)
    }

    private fun stopTimer() {
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun resetTimer() {
        stopTimer()
        _trackTime.value = dateFormat.format(0L)

    }

    fun pausePlayback() {
        tracksInteractor.pause()
    }

    fun releaseResources() {
        tracksInteractor.release()
        stopTimer()
    }

    override fun onCleared() {
        super.onCleared()
        releaseResources()
    }

    fun getScreenStateLiveData(): LiveData<PlayerScreenState> = screenState

    companion object {
        fun getViewModelFactory(track: Track?): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        track,
                        Creator.provideMediaPlayerInteractor(),
                    ) as T
                }
            }

        private const val REFRESH_DELAY_MILLIS = 100L
    }
}