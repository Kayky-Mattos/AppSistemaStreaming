package com.example.sistemastreaming

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

class VideoPlayedActivity : AppCompatActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var loadingIndicator: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_played)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        playerView = findViewById(R.id.playerView)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        // Inicializar o ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        val urlVideo =  Uri.parse(intent.getStringExtra("FILE_URL"))

        // Configurar a URL do vídeo
        //val videoUri = Uri.parse("https://sistemastreaming.blob.core.windows.net/files/cd80e5ef-cf4e-4b33-b6cf-77b1d8b36292.mp4")
        val mediaItem = MediaItem.fromUri(urlVideo)
        exoPlayer.setMediaItem(mediaItem)

        // Listener para o estado do player
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsLoadingChanged(isLoading: Boolean) {
                // Mostra o indicador de carregamento enquanto o vídeo está carregando
                loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_READY) {
                    loadingIndicator.visibility = View.GONE // Esconde quando está pronto
                }
            }
        })

        // Preparar e iniciar o vídeo
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true // Inicia a reprodução automaticamente
    }

    override fun onPause() {
        super.onPause()
        exoPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        exoPlayer.release()
    }
}
