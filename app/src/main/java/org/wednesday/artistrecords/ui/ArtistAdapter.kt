package org.wednesday.artistrecords.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.wednesday.api.model.Artist
import org.wednesday.artistrecords.R
import org.wednesday.artistrecords.db.Records

class ArtistAdapter(private val context: Context): RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    private val allArtistItem=ArrayList<Records>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view=LayoutInflater.from(context)
            .inflate(R.layout.artist_item,parent,false)

        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist=allArtistItem[position]
        Glide.with(context).load(artist.artworkUrl100).into(holder.imageView)
        holder.songName.text=artist.trackName.toString()
        holder.album.text=artist.collectionName.toString()
    }

    override fun getItemCount(): Int = allArtistItem.size

    inner class ArtistViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageView=itemView.findViewById<ImageView>(R.id.songPoster)
        val songName=itemView.findViewById<TextView>(R.id.songNameTv)
        val album=itemView.findViewById<TextView>(R.id.albumTv)
    }

    fun update(artistItem:List<Records>){
        allArtistItem.clear()
        allArtistItem.addAll(artistItem)
        notifyDataSetChanged()
    }
}