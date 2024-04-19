package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.data.ItunesTrack
import java.text.SimpleDateFormat
import java.util.Locale

class TrackAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackAdapter.TrackCardViewHolder>() {
    var tracks = ArrayList<ItunesTrack>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackCardViewHolder {
        return TrackCardViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackCardViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {clickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount() = tracks.size

    fun interface TrackClickListener {
        fun onTrackClick(track: ItunesTrack)
    }

    class TrackCardViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.track_card_view, parent, false)
    ) {
        private val trackCover: ImageView = itemView.findViewById(R.id.ivTrackCover)
        private val trackTitle: TextView = itemView.findViewById(R.id.tvTrackTitle)
        private val artistName: TextView = itemView.findViewById(R.id.tvArtistName)
        private val trackDuration: TextView = itemView.findViewById(R.id.tvTrackDuration)

        fun bind(model: ItunesTrack) {
            Glide.with(itemView)
                .load(model.coverUrl)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .into(trackCover)
            trackTitle.text = model.trackName
            artistName.text = model.artistName
            trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        }
        
    }
}