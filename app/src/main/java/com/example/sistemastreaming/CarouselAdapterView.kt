package com.example.sistemastreaming
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistemastreaming.Models.Files
import com.example.sistemastreaming.Models.Playlist

import kotlin.Unit;

class CarouselAdapterSeries(
    private val seriesList: kotlin.collections.List<Files>,
    val onAddToPlaylist: (Files) -> Unit,
    val onClickPlay: (Files) -> Unit
) : RecyclerView.Adapter<CarouselAdapterSeries.SeriesViewHolder>() {

    inner class SeriesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val seriesImage: ImageView = itemView.findViewById(R.id.seriesImage)
        //val seriesTitle: TextView = itemView.findViewById(R.id.seriesTitle)
        val menuIcon: ImageView = itemView.findViewById(R.id.moreOptions)
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item_series, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val series = seriesList[position]

        Glide.with(holder.seriesImage.context)
            .load(series.banner)
            .placeholder(R.drawable.placeholderimage)
            .error(R.drawable.placeholderimage)
            .into(holder.seriesImage)

        holder.seriesImage.setOnClickListener{
            onClickPlay(series)
        }

        holder.menuIcon.setOnClickListener {
            showPopupMenu(holder.menuIcon, series)
        }
    }

    private fun showPopupMenu(view: View, series: Files) {
        val popup = PopupMenu(view.context, view)
        popup.inflate(R.menu.series_menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_to_playlist -> {
                    onAddToPlaylist(series)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = seriesList.size
}

