package org.wednesday.artistrecords.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.wednesday.api.model.Artist

@Database(
    entities = [Artist::class],
    version= 1
)
abstract class ArtistDatabase : RoomDatabase(){

    abstract fun getArtistDao(): ArtistDao
    companion object{
        @Volatile
        private var instance: ArtistDatabase?=null
        private val LOCK= Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also{ instance =it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArtistDatabase::class.java,
                "artistDatabase.db"
            ).build()
    }
}