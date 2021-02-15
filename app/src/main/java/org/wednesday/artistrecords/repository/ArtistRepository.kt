package org.wednesday.artistrecords.repository

import android.util.Log
import androidx.lifecycle.LiveData
import org.wednesday.api.ITunesClient
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.db.ArtistDatabase
import org.wednesday.artistrecords.db.Records

class ArtistRepository(
    private val db: ArtistDatabase
) {
    private val api=ITunesClient.api

    suspend fun searchArtist(searchQuery: String) =
        api.getArtist(searchQuery).body()?.results


    suspend fun insert(record: Records)= db.getArtistDao().upsertArtist(record)

    fun getArtist(term: String) :LiveData<List<Records>>{
        Log.d("RoomStrace","Repository")
        return db.getArtistDao().getAllArtist()
    }

}