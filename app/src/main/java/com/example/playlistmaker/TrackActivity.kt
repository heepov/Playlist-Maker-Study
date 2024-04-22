package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.data.Track
import com.google.gson.Gson

class TrackActivity : AppCompatActivity() {
    private val gson = Gson()
    private lateinit var trackCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        val track = gson.fromJson(intent.getStringExtra("track"), Track::class.java)

        trackCover = findViewById(R.id.ivTrackCover)
        trackTitle = findViewById(R.id.tvTrackTitle)
        artistName = findViewById(R.id.tvArtistName)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener {
            vibrate()
            finish()
        }

        Glide.with(applicationContext)
            .load(convertToAppleMusicApiUrl(track.coverUrl, 600))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(trackCover)

        trackTitle.text = track.trackName
        artistName.text = track.artistName
    }

    // знаю что это колхоз, но зато очень по питоновски )
    // но так ка этот все я сделал для проверки работы нажатий на элементы списка, думаю что сойдет
    // потом все равно удалю
    private fun convertToAppleMusicApiUrl(url: String, size: Int): String {
        val baseUrl = url.substringBeforeLast("/")
        val filename = url.substringAfterLast("/")
        val newFilename = filename.replace("100x100bb", "${size}x${size}")
        return "$baseUrl/$newFilename"
    }
}