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
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.wednesday.api.ITunesClient
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.db.ArtistDao
import org.wednesday.artistrecords.db.ArtistDatabase
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
            for(i in list.indices){
                list[i].term="drake"
                dao.upsertArtist(list[i])
            }
        }
        val allArtist=dao.getAllArtist("drake").blockingObserve()
        assertNotNull(allArtist)

    }

    private fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}