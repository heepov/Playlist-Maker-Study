package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmaker.data.Track
import com.example.playlistmaker.data.getCountryName
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class TrackActivity : AppCompatActivity() {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val REFRESH_DELAY_MILLIS = 100L
    }


    private val gson = Gson()

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
    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler = Handler(Looper.getMainLooper())
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var track:Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        track = intent.getParcelableExtra<Track>("track")
        // For debug purposes
//        val track = Track(
//            1,
//            "Name",
//            "Artist Name",
//            1000,
//            "404",
//            "", //НЕТУ АЛЬБОМА ТАКИЕ ДЕЛА
//            "1999",
//            "Rock",
//            country = "US")
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

        checkAndSetTrackInformationField(track?.collectionName.toString(), trackAlbumText, trackAlbumValue)
        checkAndSetTrackInformationField(
            track?.releaseDate?.substring(0, 4).toString(),
            trackYearText,
            trackYearValue
        )
        checkAndSetTrackInformationField(track?.primaryGenreName.toString(), trackGenreText, trackGenreValue)
        checkAndSetTrackInformationField(
            getCountryName(track?.country.toString()),
            trackCountryText,
            trackCountryValue
        )
        if (track?.previewUrl != null) {
            preparePlayer(track?.previewUrl.toString())
            btnPalyStop.setOnClickListener {
                playbackControl()
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

    private fun preparePlayer(trackUrl: String) {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            btnPalyStop.isEnabled = true
            btnPalyStop.setAlpha(1f)
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            btnPalyStop.setBackgroundResource(R.drawable.ic_play)
            playerState = STATE_PREPARED
            stopTimer()
            trackDuration.text = dateFormat.format(0L)
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        btnPalyStop.setBackgroundResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        btnPalyStop.setBackgroundResource(R.drawable.ic_play)
        playerState = STATE_PAUSED
        stopTimer()
    }

    private fun startTimer() {
        mainThreadHandler.postDelayed(object : Runnable {
            override fun run() {
                trackDuration.text = dateFormat.format(mediaPlayer.currentPosition)
                mainThreadHandler.postDelayed(this, REFRESH_DELAY_MILLIS)
            }
        }, REFRESH_DELAY_MILLIS)
    }

    private fun stopTimer() {
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        stopTimer()
    }

}