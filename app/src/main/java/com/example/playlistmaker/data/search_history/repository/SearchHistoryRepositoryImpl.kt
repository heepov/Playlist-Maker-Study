package com.example.playlistmaker.data.search_history.repository

import android.content.SharedPreferences
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.search_history.repository.SearchHistoryRepository
import com.example.playlistmaker.utils.constants.Constants.TRACKS_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) : SearchHistoryRepository {
    override fun getTrackList(): List<Track> {
        val jsonTrackList = sharedPreferences.getString(TRACKS_KEY, null)
        return jsonTrackList?.let { createTrackListFromJson(it) } ?: emptyList()
    }

    override fun addTrack(track: Track) {
        val trackList = getTrackList().toMutableList()
        checkExistAndAddTrack(track, trackList)
        checkAndCutTrackList(trackList)
        sharedPreferences.edit().putString(TRACKS_KEY, createJsonFromTrackList(trackList)).apply()
    }

    override fun clearTrackList() {
        sharedPreferences.edit().remove(TRACKS_KEY).apply()
    }

    private fun createJsonFromTrackList(trackList: List<Track>): String {
        return Gson().toJson(trackList)
    }

    private fun createTrackListFromJson(jsonTrackList: String): List<Track> {
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(jsonTrackList, type)
    }

    private fun checkAndCutTrackList(trackList: MutableList<Track>) {
        if (trackList.size > 10) {
            trackList.subList(10, trackList.size).clear()
        }
    }

    private fun checkExistAndAddTrack(track: Track, trackList: MutableList<Track>) {
        val index = trackList.indexOf(track)
        if (index == -1) trackList.add(0, track)
        else trackList.add(0, trackList.removeAt(index))
    }
}