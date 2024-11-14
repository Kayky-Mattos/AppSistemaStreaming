package com.example.sistemastreaming

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator

class IndexActivity : AppCompatActivity() {

    var fullname: String? = null
    var useremail: String? = null
    var userId: String? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.index)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.indexMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getStringExtra("USER_ID")
        val tokenJwt = intent.getStringExtra("TOKEN_JWT").toString()

        val mviewPager = findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.viewPager)
        val mtabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayout)

        mviewPager.adapter = MyVPAdapter(this, tokenJwt, userId ?: "")
        TabLayoutMediator(mtabLayout, mviewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Series"
                2 -> tab.text = "Filmes"
                3 -> tab.text = "Podcast"
                4 -> tab.text = "Música"
            }
        }.attach()

        val firstname = intent.getStringExtra("USER_NAME")?.split(" ")?.get(0) ?: "Usuário"
        fullname = intent.getStringExtra("USER_NAME") ?: "Usuário"
        useremail = intent.getStringExtra("USER_EMAIL")

        val toolbarMenu: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        toolbarMenu.setTitle("Para, $firstname")
        setSupportActionBar(toolbarMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.index_menu, menu)
        menuInflater.inflate(R.menu.bottom_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.userIcon -> {
                val fragment = ProfileFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.indexMain, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                true
            }
            R.id.playlists -> {
                val fragment = PlaylistFragment.newInstance(userId ?: "")
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.indexMain, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
