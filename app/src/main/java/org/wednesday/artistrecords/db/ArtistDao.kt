package org.wednesday.artistrecords.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.wednesday.api.model.Artist

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArtist(record: Records) :Long

    @Query("Select * from artist  where term=:term")
    fun getAllArtist(term:String) :LiveData<List<Records>>
}