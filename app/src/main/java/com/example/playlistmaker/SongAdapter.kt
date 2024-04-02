package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class SongAdapter(private val songs: List<SongCard>) : RecyclerView.Adapter<SongAdapter.SongCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_card_view, parent, false)
        return SongCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongCardViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount() = songs.size

    data class SongCard(
        val imageUrl: String,
        val title: String,
        val artistName: String,
        val songDuration: Int,
    )

    class SongCardViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
        private val iwSongImage: ImageView
        private val twSongTitle: TextView
        private val twArtistName: TextView
        private val twSongDuration: TextView

        init {
            iwSongImage = parentView.findViewById(R.id.iwSongImage)
            twSongTitle = parentView.findViewById(R.id.twSongTitle)
            twArtistName = parentView.findViewById(R.id.twArtistName)
            twSongDuration = parentView.findViewById(R.id.twSongDuration)
        }

        fun bind(model: SongCard) {
            Glide.with(itemView)
                .load(model.imageUrl)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(iwSongImage)
            twSongTitle.text = model.title
            twArtistName.text = model.artistName
            twSongDuration.text = formatDuration(model.songDuration)
        }
        private fun formatDuration(duration: Int): String {
            return if (duration%60>9)
                "${duration/60}:${duration%60}"
            else
                "${duration/60}:0${duration%60}"
        }
    }
}