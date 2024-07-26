package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.player.model.MediaPlayerState
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.utils.constants.Constants.TRACKS_KEY
import com.example.playlistmaker.utils.services.vibrate
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val REFRESH_DELAY_MILLIS = 100L
    }
    private var track: Track? = null
    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())


    private lateinit var trackCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var trackDuration: TextView
    private lateinit var trackDurationText: TextView
    private lateinit var trackAlbumText: TextView
    private lateinit var trackYearText: TextView
    private lateinit var trackGenreText: TextView
    private lateinit var trackCountryText: TextView
    private lateinit var trackDurationValue: TextView
    private lateinit var trackAlbumValue: TextView
    private lateinit var trackYearValue: TextView
    private lateinit var trackGenreValue: TextView
    private lateinit var trackCountryValue: TextView
    private lateinit var btnPalyStop: Button


    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        track = intent.getParcelableExtra<Track>(TRACKS_KEY)

        inflateUI()

        if (track?.previewUrl != null) {
            preparePlayer()
            btnPalyStop.setOnClickListener {
                when(mediaPlayerInteractor.getMediaPlayerState()){
                    MediaPlayerState.PREPARED, MediaPlayerState.PAUSED -> {
                        mediaPlayerInteractor.start()
                    }
                    MediaPlayerState.PLAYING -> {
                        mediaPlayerInteractor.pause()
                    }
                    MediaPlayerState.DEFAULT -> {
                    }
                }
            }
        } else {
            btnPalyStop.setBackgroundResource(R.drawable.ic_cant_play)
        }
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

    private fun preparePlayer() {
        track?.let { mediaPlayerInteractor.setMediaPlayerDataSource(it) }
        mediaPlayerInteractor.prepare(
        listener = object : MediaPlayerInteractor.OnStateChangeListener{
            override fun onStateChange(state: MediaPlayerState) {
                when (state) {
                    MediaPlayerState.PREPARED -> {
                        btnPalyStop.setBackgroundResource(R.drawable.ic_play)
                        btnPalyStop.isEnabled = true
                        btnPalyStop.setAlpha(1f)
                        resetTimer()
                    }
                    MediaPlayerState.PLAYING -> {
                        btnPalyStop.setBackgroundResource(R.drawable.ic_pause)
                        startTimer()
                    }
                    MediaPlayerState.PAUSED -> {
                        btnPalyStop.setBackgroundResource(R.drawable.ic_play)
                        stopTimer()
                    }
                    MediaPlayerState.DEFAULT -> {
                        btnPalyStop.isEnabled = false
                        btnPalyStop.setBackgroundResource(R.drawable.ic_play)
                    }
                }
            }
        }
        )
    }


    private fun startTimer() {
        mainThreadHandler.postDelayed(object : Runnable {
            override fun run() {
                trackDuration.text = mediaPlayerInteractor.getMediaPlayerCurrentTime()
                mainThreadHandler.postDelayed(this, REFRESH_DELAY_MILLIS)
            }
        }, REFRESH_DELAY_MILLIS)
    }

    private fun stopTimer() {
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun resetTimer(){
        stopTimer()
        trackDuration.text = dateFormat.format(0L)
    }

    private fun inflateUI() {
        trackCover = findViewById(R.id.ivTrackCover)
        trackTitle = findViewById(R.id.tvTrackTitle)
        artistName = findViewById(R.id.tvArtistName)
        trackDuration = findViewById(R.id.tvTrackDuration)
        trackDurationText = findViewById(R.id.tvTrackDurationText)
        trackAlbumText = findViewById(R.id.tvTrackAlbumText)
        trackYearText = findViewById(R.id.tvTrackYearText)
        trackGenreText = findViewById(R.id.tvTrackGenreText)
        trackCountryText = findViewById(R.id.tvTrackCountryText)
        trackDurationValue = findViewById(R.id.tvTrackDurationValue)
        trackAlbumValue = findViewById(R.id.tvTrackAlbumValue)
        trackYearValue = findViewById(R.id.tvTrackYearValue)
        trackGenreValue = findViewById(R.id.tvTrackGenreValue)
        trackCountryValue = findViewById(R.id.tvTrackCountryValue)
        btnPalyStop = findViewById(R.id.btnPlayStop)

        findViewById<Toolbar>(R.id.toolBar).setNavigationOnClickListener {
            vibrate()
            finish()
        }


        Glide.with(applicationContext)
            .load(track?.coverUrl?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(trackCover)

        trackTitle.text = track?.trackName
        artistName.text = track?.artistName
        trackDuration.text = dateFormat.format(0L)
        trackDurationValue.text = dateFormat.format(track?.trackTimeMillis)

        checkAndSetTrackInformationField(
            track?.collectionName.toString(),
            trackAlbumText,
            trackAlbumValue
        )
        checkAndSetTrackInformationField(
            track?.releaseDate?.substring(0, 4).toString(),
            trackYearText,
            trackYearValue
        )
        checkAndSetTrackInformationField(
            track?.primaryGenreName.toString(),
            trackGenreText,
            trackGenreValue
        )
        checkAndSetTrackInformationField(
            track?.country.toString(),
            trackCountryText,
            trackCountryValue
        )
    }


    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerInteractor.pause()
    }



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerInteractor.release()
        stopTimer()
    }

}