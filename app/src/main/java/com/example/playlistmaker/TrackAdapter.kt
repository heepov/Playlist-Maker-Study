package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(private val songs: List<TrackCard>) :
    RecyclerView.Adapter<TrackAdapter.TrackCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.song_card_view, parent, false)
        return TrackCardViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackCardViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    override fun getItemCount() = songs.size

    class TrackCardViewHolder(parentView: View) : RecyclerView.ViewHolder(parentView) {
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

        fun bind(model: TrackCard) {
            Glide.with(itemView)
                .load(model.imageUrl)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
//                .transform(RoundedCorners(4)) // я знаю как скруглить улы через glide (но я не хочу этого делать так как это скругление использует px а не dp)
                .into(iwSongImage)
            twSongTitle.text = model.title
            twArtistName.text = model.artist
            twSongDuration.text = formatDuration(model.duration)
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