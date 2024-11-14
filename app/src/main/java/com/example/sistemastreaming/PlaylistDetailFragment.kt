package com.example.sistemastreaming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemastreaming.Models.Playlist
import com.example.sistemastreaming.Networkings.ApiServiceInterface
import com.example.sistemastreaming.Networkings.RetrofitInstance
import kotlinx.coroutines.launch

class PlaylistDetailFragment : Fragment() {

    private lateinit var playlist: Playlist
    private lateinit var recyclerViewConteudos: RecyclerView
    private lateinit var conteudosAdapter: ConteudoAdapter
    private lateinit var attTitlePlaylist: TextView
    private val apiService by lazy {
        RetrofitInstance.createService(ApiServiceInterface::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_detail, container, false)

        recyclerViewConteudos = view.findViewById(R.id.playlistsRecyclerViewDetail)
        attTitlePlaylist = view.findViewById(R.id.titlePlaylists)
        recyclerViewConteudos.layoutManager = LinearLayoutManager(context)

        // Obter a lista de conteúdos da playlist
        playlist = arguments?.getSerializable("playlist") as Playlist
        val conteudosFromPlaylist = playlist.conteudos.toMutableList()
        attTitlePlaylist.text = playlist.title

        conteudosAdapter = ConteudoAdapter(conteudosFromPlaylist,
            onConteudoClick = { conteudo ->
//                val intent = Intent(this@PlaylistDetailFragment.context, VideoPlayedActivity::class.java)
//                intent.putExtra("FILE_URL", conteudo.urlConteudo)
//                startActivity(intent)
            },
            onDeleteClick = { conteudo ->
                lifecycleScope.launch {
                    deleteConteudoFromPlaylist(conteudo.conteudoId)
                    conteudosFromPlaylist.remove(conteudo)
                    conteudosAdapter.notifyDataSetChanged()
                }
            }
        )

        recyclerViewConteudos.adapter = conteudosAdapter

        return view
    }

    private suspend fun deleteConteudoFromPlaylist(conteudoId: String) {
        try {
            apiService.deleteConteudo(conteudoId)
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao deletar conteúdo: ${e.message}")
        }
    }

    companion object {
        fun newInstance(playlist: Playlist): PlaylistDetailFragment {
            val fragment = PlaylistDetailFragment()
            val args = Bundle().apply {
                putSerializable("playlist", playlist)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
