package br.com.lodjinha.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import br.com.lodjinha.R
import br.com.lodjinha.databinding.ActivityMainBinding
import br.com.lodjinha.ui.components.FilterBottomSheetDialog
import br.com.lodjinha.ui.components.FilterBottomSheetDialog.Companion.openBottomSheetDialog


interface NavigationDelegate {
    fun setToolbarTitle(title: String)
    var menuFilter: MenuItem
}

class MainActivity : AppCompatActivity(), NavigationDelegate {

    private lateinit var binding: ActivityMainBinding

    private val navigator by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationView()

        setupNavigation()
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

            binding.topAppBar.menu.findItem(R.id.filter).apply {
                isVisible = false
            }

            when(destination.id) {
                R.id.mainFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                R.id.productsListFragment -> {
                    binding.topAppBar.menu.findItem(R.id.filter).apply {
                        isVisible = true
                        menuFilter = this
                    }
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

    override var menuFilter: MenuItem
        get() = binding.topAppBar.menu.findItem(R.id.filter)
        set(value) {}

}