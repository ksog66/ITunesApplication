package org.wednesday.artistrecords.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.wednesday.artistrecords.ArtistApplication
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.repository.ArtistRepository

class ArtistViewModel(
    app:Application,
    val artistRepository: ArtistRepository)
    : AndroidViewModel(app) {

        private val _data= MutableLiveData<List<Artist>>()
        val data:LiveData<List<Artist>> = _data


        fun search(artistName: String){
            if(hasInternetConnection()){
                searchArtist(artistName)
            }else{
                searchFromRoom(artistName)
            }
        }

    private fun searchFromRoom(artistName: String) =viewModelScope.launch {
        artistRepository.getArtist(artistName)?.let {
            val localData=it.value
            _data.postValue(localData!!)
        }
    }

    private fun searchArtist(artistName:String)=viewModelScope.launch {
        artistRepository.searchArtist(artistName)?.let {

            val currentData=it
            for (i in 0..currentData.size-1){
                currentData[i].term= artistName
                artistRepository.insert(currentData[i])
            }
            _data.postValue(currentData)

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
}


