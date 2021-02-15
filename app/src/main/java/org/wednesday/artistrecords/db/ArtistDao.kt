package org.wednesday.artistrecords.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArtist(record: Records) :Long

    @Query
    ("Select * from records  where term=:term")
    suspend fun getAllArtist(term:String) :List<Records>
}