package org.wednesday.artistrecords.repository

import org.wednesday.api.ITunesClient
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.db.ArtistDatabase

class ArtistRepository(
    private val db: ArtistDatabase
) {
    val api=ITunesClient.api

    suspend fun searchArtist(searchQuery: String) =
        api.getArtist(searchQuery).body()?.results


    suspend fun insert(artist: Artist)= db.getArtistDao().upsertArtist(artist)

    fun getArtist(term:String)= db.getArtistDao().getAllArtist(term)
}