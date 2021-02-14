package org.wednesday.artistrecords.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.wednesday.artistrecords.databinding.ActivityHomeBinding
import org.wednesday.artistrecords.db.ArtistDatabase
import org.wednesday.artistrecords.repository.ArtistRepository

class HomeActivity : AppCompatActivity() ,SearchView.OnQueryTextListener{
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: ArtistViewModel
    private lateinit var artistAdapter:ArtistAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val artistRepository= ArtistRepository(ArtistDatabase(this))
        val viewModelProviderFactor=ArtistViewModelProviderFactory(application,artistRepository)
        viewModel= ViewModelProvider(this,viewModelProviderFactor).get(ArtistViewModel::class.java)

        //RecyclerView
        binding.recyclerView.layoutManager=
            GridLayoutManager(this,3, GridLayoutManager.VERTICAL,false)
        artistAdapter= ArtistAdapter(this)
        binding.recyclerView.adapter=artistAdapter

        //LiveDataObserver
        viewModel.data.observe(this, {
            if(it!=null){
                Toast.makeText(this,"Live Data Changed",Toast.LENGTH_SHORT).show()
                artistAdapter.update(it)
            }
        })

        //SearchView
        binding.searchArtist.isSubmitButtonEnabled=true
        binding.searchArtist.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query!=null){
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        viewModel.search(query)
    }

    override fun onQueryTextChange(query: String?): Boolean {
        return true
    }
}