package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.data.ItunesTrack

class TrackAdapter() : RecyclerView.Adapter<TrackAdapter.TrackCardViewHolder>() {
    var tracks = ArrayList<ItunesTrack>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackCardViewHolder {
        return TrackCardViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackCardViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

    class TrackCardViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.song_card_view, parent, false)
    ) {
        private val iwSongImage: ImageView = itemView.findViewById(R.id.iwSongImage)
        private val twSongTitle: TextView = itemView.findViewById(R.id.twSongTitle)
        private val twArtistName: TextView = itemView.findViewById(R.id.twArtistName)
        private val twSongDuration: TextView = itemView.findViewById(R.id.twSongDuration)

        fun bind(model: ItunesTrack) {
            Glide.with(itemView)
                .load(model.coverUrl)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(iwSongImage)
            twSongTitle.text = model.trackName
            twArtistName.text = model.artistName
            twSongDuration.text = formatDuration(model.trackTimeMillis)
        }

        private fun formatDuration(duration: Long): String {
            val seconds = duration / 1000
            return if (seconds % 60 > 9)
                "${seconds / 60}:${seconds % 60}"
            else
                "${seconds / 60}:0${seconds % 60}"
        }
    }
}