package org.wednesday.artistrecords

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.wednesday.api.ITunesClient
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.db.ArtistDao
import org.wednesday.artistrecords.db.ArtistDatabase
import org.wednesday.artistrecords.db.Records
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RoomAndApiTest {
    private lateinit var database: ArtistDatabase
    private lateinit var dao:ArtistDao
    private var iTunesClient = ITunesClient
    private lateinit var list:List<Artist>
    @Before
    fun setup(){
        database= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ArtistDatabase::class.java
        ).allowMainThreadQueries().build()
        dao=database.getArtistDao()
    }

    @After
    fun teardown(){
        database.close()
    }


    @Test
    fun insertArtistItem(){
        runBlocking {
            val artist = iTunesClient.api.getArtist("drake")
            list = artist.body()!!.results
            assertNotNull(list)
            val recordsList=list.asDomainModel()
            for(i in recordsList.indices){
                recordsList[i].term="drake"
                dao.upsertArtist(recordsList[i])
            }
            val allArtist=dao.getAllArtist("drake")
            assertNotNull(allArtist)

        }
    }

    fun List<Artist>.asDomainModel(): List<Records> {
        return map {
            Records(
                term = it.term,
                artworkUrl100 = it.artworkUrl100,
                artworkUrl30 = it.artworkUrl30,
                artworkUrl60 = it.artworkUrl60,
                trackName = it.trackName,
                collectionName= it.collectionName)
        }
    }
}