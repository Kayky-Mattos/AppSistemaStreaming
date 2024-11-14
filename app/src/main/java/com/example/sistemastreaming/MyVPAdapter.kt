package com.example.sistemastreaming

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyVPAdapter(fa: FragmentActivity,private val tokenJwt: String,private val userId: String): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment.newInstance(tokenJwt,userId)
            1 -> SeriesFragment.newInstance(tokenJwt)
            2 -> FilmesFragment.newInstance(tokenJwt)
            3 -> PodcastFragment.newInstance(tokenJwt)
            4 -> MusicaFragment.newInstance(tokenJwt)
            else -> HomeFragment.newInstance(tokenJwt,userId)
        }
    }
}