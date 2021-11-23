package br.com.lodjinha.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.lodjinha.R
import br.com.lodjinha.databinding.ActivityMainBinding
import android.widget.ListView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView


interface NavigationDelegate {
    fun setToolbarTitle(title: String)
}

class MainActivity : AppCompatActivity(), NavigationDelegate {

    private lateinit var binding: ActivityMainBinding

    private val navigator by lazy { findNavController(R.id.nav_host_fragment) }

    var searchView: SearchView? = null
    var listView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationView()

        setupNavigation()

        searchView = findViewById(R.id.action_search)

        listView = findViewById(R.id.lv1);

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false;
            }
        })
    }

    private fun setupNavigation() {
        navigator.addOnDestinationChangedListener { _, destination, _ ->

            // Set navigation view lock
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


            // Add font to toolbar title
            for (i in 0 until binding.topAppBar.childCount) {
                val view = binding.topAppBar.getChildAt(i)
                if (view is android.widget.TextView && view.text == destination.label) {
                    val typeface = androidx.core.content.res.ResourcesCompat.getFont(this, R.font.pacificoregular)
                    view.typeface = typeface
                    break
                }
            }

            when(destination.id) {
                R.id.mainFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                else -> {
                }
            }

        }
    }

    private fun setupNavigationView() {
        binding.topAppBar.setupWithNavController(navigator, binding.drawerLayout)
        binding.navigationView.setupWithNavController(navigator)
    }

    override fun setToolbarTitle(title: String) {
        binding.topAppBar.title = title
    }
}