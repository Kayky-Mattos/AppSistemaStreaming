package com.example.sistemastreaming

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sistemastreaming.Models.Conteudos
import com.example.sistemastreaming.Models.Playlist
import com.example.sistemastreaming.Models.contentReqPlaylist
import com.example.sistemastreaming.Networkings.ApiServiceInterface
import com.example.sistemastreaming.Networkings.RetrofitInstance
import kotlinx.coroutines.launch
import com.example.sistemastreaming.Models.PostPlaylist

class PlaylistFragment : Fragment() {

    private val apiService by lazy {
        RetrofitInstance.createService(ApiServiceInterface::class.java)
    }
    private lateinit var recyclerViewPlaylists: RecyclerView
    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var CriarNovaPlaylist: TextView
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistViewModel = ViewModelProvider(requireActivity()).get(PlaylistViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_playlists, container, false)
        recyclerViewPlaylists = view.findViewById(R.id.playlistsRecyclerView)
        recyclerViewPlaylists.layoutManager = LinearLayoutManager(context)


        CriarNovaPlaylist = view.findViewById(R.id.labelCreatePlaylist)

            CriarNovaPlaylist.setOnClickListener {
                val inflater = LayoutInflater.from(requireContext())
                val popupView = inflater.inflate(R.layout.popup_create_playlist, null)

                val dialog = AlertDialog.Builder(requireContext())
                    .setView(popupView)
                    .setCancelable(false)
                    .create()

                val playlistTitleEditText = popupView.findViewById<EditText>(R.id.playlistTitleEditText)
                val btnExit = popupView.findViewById<Button>(R.id.btnExit)
                val btnCreate = popupView.findViewById<Button>(R.id.btnCreate)

                btnExit.setOnClickListener {
                    dialog.dismiss()
                }

                btnCreate.setOnClickListener {
                    val title = playlistTitleEditText.text.toString().trim()
                    if (title.isNotEmpty()) {
                        lifecycleScope.launch {
                            val contentRequestPlaylist = PostPlaylist(
                                title = title
                            )

                            userId?.let { it1 -> postfunPlaylist(it1,contentRequestPlaylist) }
                            userId?.let { it1 -> fetchPlaylists(it1) }
                            Toast.makeText(
                                requireContext(),
                                "Playlist '$title' criada!",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Digite um título para a playlist.", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.show()
            }

        lifecycleScope.launch {
            userId?.let {
                fetchPlaylists(it)
            }
        }

        return view
    }

    private suspend fun fetchPlaylists(userId: String) {
        try {
            var playlistsfetch = apiService.getPlaylist(userId).toMutableList()

            playlistAdapter = PlaylistAdapter(playlistsfetch,
                onPlaylistClick = { clickedPlaylist ->
                    // Filtra a playlist correspondente a `clickedPlaylist.playlistId`

                    lifecycleScope.launch {
                        var UpdateContent = apiService.getPlaylist(userId).toMutableList()

                        val selectedPlaylist =
                            UpdateContent.find { it.playlistId == clickedPlaylist.playlistId }

                        if (selectedPlaylist != null) {
//                        conteudosViewModel.updateConteudo(selectedPlaylist.conteudos) // Atualiza conteúdos para essa playlist

                            // Passa a playlist filtrada para o fragmento
                            val fragment = PlaylistDetailFragment.newInstance(selectedPlaylist)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .commit()
                        }
                        else {
                            Log.e(
                                "Fetch Error",
                                "Playlist com o ID ${clickedPlaylist.playlistId} não encontrada"
                            )
                        }
                    }
                },
                onDeleteClick = { playlist ->
                    lifecycleScope.launch {
                        deletePlaylist(playlist.playlistId)
                        playlistsfetch.remove(playlist)
//                        conteudosViewModel.updateConteudo(playlistsfetch.flatMap { it.conteudos }) // Atualiza conteúdos no ViewModel
                        playlistAdapter.notifyDataSetChanged()
                    }
                }
            )

            recyclerViewPlaylists.adapter = playlistAdapter
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao obter playlists: ${e.message}")
            Toast.makeText(requireContext(), "Erro ao carregar playlists", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val USER_ID = "USER_ID"

        fun newInstance(userId: String) = PlaylistFragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId)
            }
        }
    }

    private suspend fun deletePlaylist(playlistId: String) {
        try {
            apiService.deletePlaylist(playlistId)
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao excluir playlist: ${e.message}")
            Toast.makeText(requireContext(), "Erro ao excluir playlist", Toast.LENGTH_SHORT).show()
        }
    }
    private suspend fun postfunPlaylist(userId: String, contentRequest: PostPlaylist ) {
        try {
            apiService.postPlaylist(userId,contentRequest)
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao criar playlist: ${e.message}")
        }
    }
}
