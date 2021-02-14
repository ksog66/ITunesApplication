package org.wednesday.artistrecords.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.wednesday.api.model.Artist

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArtist(artist: Artist) :Long

    @Query("Select * from artist where term=:item limit 20")
    fun getAllArtist(item:String) : LiveData<List<Artist>>
}