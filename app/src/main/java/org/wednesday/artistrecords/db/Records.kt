package org.wednesday.artistrecords.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "artist"
)
data class Records(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    var term:String?,
    val artworkUrl100: String?,
    val artworkUrl30: String?,
    val artworkUrl60: String?,
    val trackName: String?,
    val collectionName: String?
)