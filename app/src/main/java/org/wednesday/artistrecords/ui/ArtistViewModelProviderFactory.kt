package org.wednesday.artistrecords.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.wednesday.artistrecords.repository.ArtistRepository

class ArtistViewModelProviderFactory(
    val app: Application,
    val artistRepository: ArtistRepository
):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ArtistViewModel(app, artistRepository) as T
    }
}