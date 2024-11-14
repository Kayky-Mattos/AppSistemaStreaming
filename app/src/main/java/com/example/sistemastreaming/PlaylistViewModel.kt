package com.example.sistemastreaming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sistemastreaming.Models.Playlist

class PlaylistViewModel : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    // Função para atualizar as playlists
    fun updatePlaylists(newPlaylists: List<Playlist>) {
        _playlists.value = newPlaylists
    }

    // Função para remover uma playlist específica
    fun removePlaylist(playlistId: String) {
        _playlists.value = _playlists.value?.filter { it.playlistId != playlistId }
    }
}
