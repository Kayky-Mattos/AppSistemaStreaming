package com.example.sistemastreaming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemastreaming.Models.Playlist

class PlaylistAdapter(
    private val playlists: MutableList<Playlist>, // Alterado para MutableList para remover dinamicamente
    private val onPlaylistClick: (Playlist) -> Unit, // Clique para visualizar detalhes
    private val onDeleteClick: (Playlist) -> Unit // Função de exclusão da playlist
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.playlistTitle)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        fun bind(playlist: Playlist) {
            titleTextView.text = playlist.title
            itemView.setOnClickListener { onPlaylistClick(playlist) }

            // Configura o listener para o botão de excluir
            deleteButton.setOnClickListener {
                onDeleteClick(playlist) // Exclui a playlist quando o botão é pressionado
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    // Função para remover a playlist da lista e atualizar o RecyclerView
    fun removePlaylist(playlist: Playlist) {
        playlists.remove(playlist)
        notifyDataSetChanged() // Atualiza a lista após exclusão
    }
}
