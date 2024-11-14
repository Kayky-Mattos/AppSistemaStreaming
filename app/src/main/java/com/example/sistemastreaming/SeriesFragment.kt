package com.example.sistemastreaming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SeriesFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_tab_series, container, false)
    }

    companion object {
        private const val ARG_TOKEN = "TOKEN_JWT"

        fun newInstance(tokenJwt: String) = SeriesFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_TOKEN, tokenJwt)
            }
        }
    }
}