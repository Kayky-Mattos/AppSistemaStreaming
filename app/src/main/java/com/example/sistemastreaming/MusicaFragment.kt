package com.example.sistemastreaming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MusicaFragment : Fragment() {

    private var tokenJwt: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tokenJwt = it.getString(ARG_TOKEN)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_musica, container, false)
    }

    companion object {
        private const val ARG_TOKEN = "TOKEN_JWT"

        fun newInstance(tokenJwt: String) = MusicaFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TOKEN, tokenJwt)
            }
        }
    }
}