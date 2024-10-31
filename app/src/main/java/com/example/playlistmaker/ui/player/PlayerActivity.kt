package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.presentation.player.model.TrackInfo
import com.example.playlistmaker.presentation.player.state.PlayerScreenState
import com.example.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.example.playlistmaker.utils.constants.Constants.TRACKS_KEY
import com.example.playlistmaker.utils.services.vibrate
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityPlayerBinding

    private var track: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getParcelableExtra<Track>(TRACKS_KEY)
        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(track)
        ).get(PlayerViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.screenState.observe(this) { screenState ->
            when (screenState) {
                is PlayerScreenState.Loading -> {
                }

                is PlayerScreenState.Content -> {
                    inflateUI(screenState.track)
                    binding.btnPlayStop.setOnClickListener {
                        viewModel.onPlayPauseClicked()
                    }
                }

                is PlayerScreenState.Error -> {
                    binding.btnPlayStop.setBackgroundResource(R.drawable.ic_cant_play)
                }
            }
        }

        viewModel.mediaPlayerStateLiveData.observe(this) { state ->
            when (state) {
                MediaPlayerState.PREPARED -> {
                    binding.btnPlayStop.setBackgroundResource(R.drawable.ic_play)
                    binding.btnPlayStop.isEnabled = true
                    binding.btnPlayStop.alpha = 1f
                }

                MediaPlayerState.PLAYING -> {
                    binding.btnPlayStop.setBackgroundResource(R.drawable.ic_pause)
                }

                MediaPlayerState.PAUSED -> {
                    binding.btnPlayStop.setBackgroundResource(R.drawable.ic_play)
                }

                MediaPlayerState.DEFAULT -> {
                    binding.btnPlayStop.isEnabled = false
                    binding.btnPlayStop.setBackgroundResource(R.drawable.ic_play)
                }
            }
        }
        viewModel.trackTimeLiveData.observe(this) { trackTime ->
            binding.tvTrackDuration.text = trackTime
        }
    }

    private fun inflateUI(trackInfo: TrackInfo) {
        Glide.with(applicationContext)
            .load(trackInfo.trackCover.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.ivTrackCover)

        binding.tvTrackTitle.text = trackInfo.trackTitle
        binding.tvArtistName.text = trackInfo.artistName
        binding.tvTrackDurationValue.text = trackInfo.trackDuration



        binding.toolBar.setNavigationOnClickListener {
            vibrate()
            finish()
        }
        checkAndSetTrackInformationField(
            trackInfo.trackAlbum,
            binding.tvTrackAlbumText,
            binding.tvTrackAlbumValue
        )
        checkAndSetTrackInformationField(
            trackInfo.trackYear,
            binding.tvTrackYearText,
            binding.tvTrackYearValue
        )
        checkAndSetTrackInformationField(
            trackInfo.trackGenre,
            binding.tvTrackGenreText,
            binding.tvTrackGenreValue
        )
        checkAndSetTrackInformationField(
            trackInfo.trackCountry,
            binding.tvTrackCountryText,
            binding.tvTrackCountryValue
        )
    }

    private fun checkAndSetTrackInformationField(
        str: String,
        itemViewText: TextView,
        itemViewValue: TextView
    ) {
        if (str.isNotEmpty()) {
            itemViewValue.text = str
        } else {
            itemViewText.isVisible = false
            itemViewValue.isVisible = false
        }
    }


    override fun onStop() {
        super.onStop()
        viewModel.pausePlayback()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayback()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releaseResources()
    }

}