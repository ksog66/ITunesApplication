package org.wednesday.api.service

import org.wednesday.api.model.ArtistResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {

    @GET("/search")
    suspend fun getArtist(
        @Query("term")
        artistName: String
    ): Response<ArtistResponse>
}