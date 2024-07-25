package com.example.playlistmaker.data.shared_preference.repository

import com.example.playlistmaker.data.shared_preference.SharedPreferencesManager
import com.example.playlistmaker.domain.search.model.Track
import com.example.playlistmaker.domain.shared_preference.repository.SearchHistoryRepository
import com.example.playlistmaker.utils.constants.Constants.TRACKS_KEY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val preferencesManager: SharedPreferencesManager) : SearchHistoryRepository {
    override fun getTrackList(): List<Track> {
        val jsonTrackList = preferencesManager.getString(TRACKS_KEY)
        return jsonTrackList?.let { createTrackListFromJson(it) } ?: emptyList()
    }

    override fun addTrack(track: Track) {
        val trackList = getTrackList().toMutableList()
        checkExistAndAddTrack(track, trackList)
        checkAndCutTrackList(track, trackList)
        preferencesManager.putString(TRACKS_KEY, createJsonFromTrackList(trackList))
    }

    override fun clearTrackList() {
        preferencesManager.clear(TRACKS_KEY)
    }

    private fun createJsonFromTrackList(trackList: List<Track>): String {
        return Gson().toJson(trackList)
    }

    private fun createTrackListFromJson(jsonTrackList: String): List<Track> {
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(jsonTrackList, type)
    }

    private fun checkAndCutTrackList(track: Track, trackList: MutableList<Track>) {
        trackList.add(track)
        if (trackList.size > 10) {
            trackList.removeAt(trackList.size - 1)
        }
    }

    private fun checkExistAndAddTrack(track: Track, trackList: MutableList<Track>) {
        val index = trackList.indexOf(track)
        if (index == -1) trackList.add(0, track)
        else trackList.add(0, trackList.removeAt(index))
    }
}