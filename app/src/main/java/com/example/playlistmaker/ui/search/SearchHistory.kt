package com.example.playlistmaker.ui.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.TRACKS_KEY
import com.example.playlistmaker.domain.search.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPreferences: SharedPreferences) {
    private val tackList = ArrayList<Track>()

    init {
        val tmpTrackList: MutableList<Track>? =
            createTrackListFromJson(sharedPreferences.getString(TRACKS_KEY, null))
        if (tmpTrackList != null) {
            tackList.addAll(tmpTrackList)
        }
    }

    fun addTrack(track: Track) {
        checkExistAndAddTrack(track)
        checkAndCutTrackList()
        sharedPreferences.edit {
            putString(TRACKS_KEY, createJsonFromTrackList(tackList))
        }
    }

    fun clearTrackList() {
        tackList.clear()
        sharedPreferences.edit {
            putString(TRACKS_KEY, null)
        }
    }

    fun getTrackList(): ArrayList<Track> {
        return tackList
    }

    private fun checkAndCutTrackList() {
        if (tackList.size > 10) {
            val tmpList: List<Track> = tackList.subList(0, 10).toList()
            tackList.clear()
            tackList.addAll(tmpList)
        }
    }

    private fun checkExistAndAddTrack(track: Track) {
        val index = tackList.indexOf(track)
        if (index == -1) tackList.add(0, track)
        else tackList.add(0, tackList.removeAt(index))
    }

    private fun createJsonFromTrackList(tackList: MutableList<Track>): String {
        return Gson().toJson(tackList)
    }

    private fun createTrackListFromJson(jsonTrackList: String?): MutableList<Track>? {
        if (jsonTrackList == null) return null
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(jsonTrackList, type)
    }
}