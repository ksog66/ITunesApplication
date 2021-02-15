package org.wednesday.artistrecords.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.wednesday.artistrecords.ArtistApplication
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.db.Records
import org.wednesday.artistrecords.repository.ArtistRepository

class ArtistViewModel(
    app:Application,
    val artistRepository: ArtistRepository)
    : AndroidViewModel(app) {

        private val _data= MutableLiveData<List<Records>>()
        var data:LiveData<List<Records>> = _data


        fun search(artistName: String){
            if(hasInternetConnection()){
                searchArtist(artistName)
            }else {
                searchFromRoom(artistName)
            }
        }

    private fun searchFromRoom(artistName: String) {
        data = artistRepository.getArtist(artistName)
    }

        private fun searchArtist(artistName:String)=viewModelScope.launch(Dispatchers.IO) {
            try {
                if(hasInternetConnection()) {
                    artistRepository.searchArtist(artistName)?.let {

                        val currentData = it.asDomainModel()
                        for (i in currentData.indices) {
                            currentData[i].term = artistName
                            artistRepository.insert(currentData[i])
                        }
                        _data.postValue(currentData)

                    }
                }
            }catch (e:Exception){
                Toast.makeText(getApplication(),e.localizedMessage,Toast.LENGTH_SHORT).show()

            }

        }

        private fun hasInternetConnection():Boolean{
            val connectivityManger= getApplication<ArtistApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                val activeNetwork=connectivityManger.activeNetwork ?: return false
                val capabilities=connectivityManger.getNetworkCapabilities(activeNetwork) ?: return false

                return when{
                    capabilities.hasTransport(TRANSPORT_WIFI) ||
                            capabilities.hasTransport(TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true

                    else-> false
                }
            }else{
                connectivityManger.activeNetworkInfo?.run {
                    return when(type){
                        TYPE_WIFI,
                        TYPE_MOBILE,
                        TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
            return false
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


