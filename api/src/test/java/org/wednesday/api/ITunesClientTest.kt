package org.wednesday.api

import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ITunesClientTest {

    private val iTunesClient =ITunesClient

    @Test
    fun`GET artist by their name`(){
        runBlocking {
            val artist = iTunesClient.api.getArtist("jane+jackson")
            assertNotNull(artist.body()?.results)
        }
    }
}