package com.example.sistemastreaming

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.sistemastreaming.Models.Files
import com.example.sistemastreaming.Models.Playlist
import com.example.sistemastreaming.Models.contentReqPlaylist
import com.example.sistemastreaming.Networkings.ApiServiceInterface
import com.example.sistemastreaming.Networkings.RetrofitInstance
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val ApiService by lazy {
        RetrofitInstance.createService(ApiServiceInterface::class.java)
    }

    private lateinit var recyclerViewSeries: RecyclerView
    private lateinit var recyclerViewFilmes: RecyclerView
    private lateinit var recyclerViewPodcasts: RecyclerView
    private lateinit var recyclerViewMusicas: RecyclerView
    private lateinit var carouselAdapter: CarouselAdapterSeries
    var playlistNames: MutableList<String> = mutableListOf()
    private var tokenJwt: String? = null
    private var userId: String? = null
    private var imageFiles: List<Files> = listOf() // Inicializa a lista como vazia
    private var imageFilesSeries: List<Files> = listOf() // Inicializa a lista como vazia
    private var imageFilesFilmes: List<Files> = listOf() // Inicializa a lista como vazia
    private var imageFilesPodcasts: List<Files> = listOf() // Inicializa a lista como vazia
    private var imageFilesMusicas: List<Files> = listOf() // Inicializa a lista como vazia
    private var listaPlaylist: List<Playlist> = listOf() // Inicializa a lista como vazia
    private lateinit var viewPager: ViewPager2
    private lateinit var playlistViewModel: PlaylistViewModel
    private lateinit var dotsIndicator: WormDotsIndicator
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    private val updatePage = object : Runnable {
        override fun run() {
            if (currentPage == imageFiles.size) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(this, 3000) // Tempo de transição de 3 segundos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tokenJwt = it.getString(ARG_TOKEN)
            userId = it.getString(USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        playlistViewModel = ViewModelProvider(requireActivity()).get(PlaylistViewModel::class.java)

        playlistViewModel.playlists.observe(viewLifecycleOwner) { updatedPlaylists ->
            playlistNames = updatedPlaylists.map { it.title }.toMutableList()
        }

        recyclerViewSeries = view.findViewById(R.id.recyclerViewSeries)
        recyclerViewSeries.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewFilmes = view.findViewById(R.id.RecyclerViewFilmes)
        recyclerViewFilmes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewPodcasts = view.findViewById(R.id.recyclerViewPodcasts)
        recyclerViewPodcasts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerViewMusicas = view.findViewById(R.id.recyclerViewMusicas)
        recyclerViewMusicas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewPager = view.findViewById(R.id.viewPagerBanner)
        dotsIndicator = view.findViewById(R.id.dotsIndicator)

        lifecycleScope.launch {
            try {

                val playlist = userId?.let { fetchPlaylist(it) }
                val files = fetchFiles()

                imageFilesSeries   = files.filter { it.type == "Serie" }
                imageFilesFilmes   = files.filter { it.type == "Film" }
                imageFilesPodcasts = files.filter { it.type == "Podcast" }
                imageFilesMusicas  = files.filter { it.type == "Music" }

                imageFiles = files.take(4)

                if (imageFiles.isNotEmpty()) {
                    viewPager.adapter = CarouselAdapter(imageFiles)
                    dotsIndicator.setViewPager2(viewPager)
                    handler.postDelayed(updatePage, 3000)

                    // Cria instâncias separadas do adaptador para cada RecyclerView
                    val seriesAdapter = CarouselAdapterSeries(imageFilesSeries,
                        onAddToPlaylist = { files ->
                            showPlaylistDialog(files)
                        },
                        onClickPlay = { files ->
                            val intent = Intent(this@HomeFragment.context, VideoPlayedActivity::class.java)
                            intent.putExtra("FILE_URL", files.fileUrl)
                            startActivity(intent)
                        })
                    recyclerViewSeries.adapter = seriesAdapter

                    val filmesAdapter = CarouselAdapterSeries(imageFilesFilmes,
                        onAddToPlaylist = { files ->
                            showPlaylistDialog(files)
                        },
                        onClickPlay = { files ->
                            val intent = Intent(this@HomeFragment.context, VideoPlayedActivity::class.java)
                            intent.putExtra("FILE_URL", files.fileUrl)
                            startActivity(intent)
                        })
                    recyclerViewFilmes.adapter = filmesAdapter

                    val podcastsAdapter = CarouselAdapterSeries(imageFilesPodcasts,
                        onAddToPlaylist = { files ->
                            showPlaylistDialog(files)
                        },
                        onClickPlay = { files ->
                            val intent = Intent(this@HomeFragment.context, VideoPlayedActivity::class.java)
                            intent.putExtra("FILE_URL", files.fileUrl)
                            startActivity(intent)
                        })
                    recyclerViewPodcasts.adapter = podcastsAdapter

                    val musicasAdapter = CarouselAdapterSeries(imageFilesMusicas,
                        onAddToPlaylist = { files ->
                            showPlaylistDialog(files)
                        },
                        onClickPlay = { files ->
                            val intent = Intent(this@HomeFragment.context, VideoPlayedActivity::class.java)
                            intent.putExtra("FILE_URL", files.fileUrl)
                            startActivity(intent)
                        })
                    recyclerViewMusicas.adapter = musicasAdapter
                } else {
                    Log.e("HomeFragment", "Nenhuma imagem encontrada na API.")
                }


            } catch (e: Exception) {
                Log.e("API Error", "Erro ao obter arquivos: ${e.message}")
            }
        }

        return view
    }

    private  fun showPlaylistDialog(files: Files) {
        lifecycleScope.launch {
            atualizaDialogo(files)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updatePage)
    }

    companion object {
        private const val ARG_TOKEN = "TOKEN_JWT"
        private const val USER_ID = "USER_ID"

        fun newInstance(tokenJwt: String,userId: String) = HomeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TOKEN, tokenJwt)
                putString(USER_ID, userId)
            }
        }
    }

    private suspend fun fetchFiles(): List<Files> {
        return try {
            ApiService.getFiles()
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao obter arquivos: ${e.message}")
            emptyList()
        }
    }
    private suspend fun fetchPlaylist(userId: String): List<Playlist> {
        return try {
            ApiService.getPlaylist(userId)
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao obter arquivos: ${e.message}")
            emptyList()
        }
    }
    private suspend fun addContentToPlaylist(playListId: String, contentRequest: contentReqPlaylist) {
        try {
            val response = ApiService.postContentPlaylist(playListId, contentRequest)
            if (response.isSuccessful) {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Adicionado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Log.e("API Error", "Erro ao adicionar conteúdo à playlist: Código HTTP ${response.code()}")
                if (response.code() == 400){
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Conteúdo já consta na playlist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("API Error", "Erro ao adicionar conteúdo à playlist: ${e.message}")
        }
    }
    private suspend fun atualizaDialogo(files: Files) {
        listaPlaylist = userId?.let { fetchPlaylist(it) }!!
        playlistNames = listaPlaylist.map { it.title }.toMutableList()

        var dialogAtt = AlertDialog.Builder(requireContext())
            .setTitle("Adicionar à Playlist")
            .setItems(playlistNames.toTypedArray()) { _, which ->
                    val selectedPlaylist = listaPlaylist[which]
                    selectedPlaylist?.let {
                        val contentRequest = contentReqPlaylist(
                            title = files.title,
                            type = files.type,
                            urlconteudo = files.fileUrl
                        )
                        lifecycleScope.launch {
                            addContentToPlaylist(it.playlistId, contentRequest)
                            Log.d("ContentAdded", "Conteudo Adicionado com sucesso!")
                        }
                    }
            }
            .create()
            dialogAtt.show()
    }
}
