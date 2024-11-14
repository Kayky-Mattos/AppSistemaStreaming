package com.example.sistemastreaming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sistemastreaming.Models.Files

class CarouselAdapter(private val images: List<Files>) : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewCarouselItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val file = images[position]

        // Usa Glide para carregar a imagem da URL no ImageView
        Glide.with(holder.itemView.context)
            .load(file.banner) // `banner` é a URL da imagem no modelo `Files`
            .placeholder(R.drawable.placeholderimage) // Imagem enquanto carrega
            .error(R.drawable.placeholderimage) // Imagem em caso de erro
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size
}


