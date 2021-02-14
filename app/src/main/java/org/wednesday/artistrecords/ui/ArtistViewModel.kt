package org.wednesday.artistrecords.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.widget.Toast
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
        var data:LiveData<List<Artist>> = _data


        fun search(artistName: String){
            searchArtist(artistName)
        }

    private fun searchFromRoom(artistName: String){
        data= artistRepository.getArtist(artistName)
    }

    private fun searchArtist(artistName:String)=viewModelScope.launch {
        try {
            if(hasInternetConnection()) {
                artistRepository.searchArtist(artistName)?.let {

                    val currentData = it
                    for (i in currentData.indices) {
                        currentData[i].term = artistName
                        artistRepository.insert(currentData[i])
                    }
                    _data.postValue(currentData)

                }
            }else{
                searchFromRoom(artistName)
            }
        }catch (e:Exception){
            Toast.makeText(getApplication(),"Internet Problem",Toast.LENGTH_SHORT).show()

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


