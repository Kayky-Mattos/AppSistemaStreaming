package com.example.sistemastreaming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemastreaming.Models.Conteudos
import com.example.sistemastreaming.Models.Playlist

class ConteudoAdapter(
    private val conteudos: List<Conteudos>,
    private val onConteudoClick: (Conteudos) -> Unit,
    private val onDeleteClick: (Conteudos) -> Unit
) : RecyclerView.Adapter<ConteudoAdapter.ConteudoViewHolder>() {

    inner class ConteudoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.titlePlaylists)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConteudoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conteudo, parent, false)
        return ConteudoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConteudoViewHolder, position: Int) {
        val conteudo = conteudos[position]
        val deleteButton: ImageButton = holder.itemView.findViewById(R.id.deleteButtonContent)
        holder.title.text = conteudo.title

        // Ação ao clicar no conteúdo
        holder.itemView.setOnClickListener {
            onConteudoClick(conteudo)
        }
        deleteButton.setOnClickListener {
            onDeleteClick(conteudo) // Exclui a playlist quando o botão é pressionado
        }
    }

    override fun getItemCount() = conteudos.size
}

