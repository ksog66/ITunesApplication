package org.wednesday.artistrecords.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "records"
)
class Records(
    var term:String?,
    val artworkUrl100: String?,
    val artworkUrl30: String?,
    val artworkUrl60: String?,
    val trackName: String?,
    val collectionName: String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}