package com.rolling.meadows.views.authentication

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.rolling.meadows.R
import com.rolling.meadows.base.BaseActivity
import com.rolling.meadows.utils.extensions.setStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialActivity : BaseActivity() {
    private lateinit var binding: com.rolling.meadows.databinding.ActivityInitialBinding
    private var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_initial)
        navController = findNavController(R.id.nav_host_fragment)

        when (intent.getStringExtra("page_open") ?: "splash") {
            "splash" -> {
                navController?.navigate(R.id.splashFragment)
            }
            else -> {
                navController?.navigate(R.id.loginFragment)
            }
        }
        initDestinationChangeListener()
    }

    private fun initDestinationChangeListener() {
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
//            val destArray = listOf<Int>(R.id.privacyFragment)
//            if (destArray.contains(destination.id)) {
//                supportActionBar?.show()
//                setToolbar(showLightStatusBar = false)
//            } else {
//                supportActionBar?.hide()
//                setToolbar(showLightStatusBar = true)
//            }
        }
    }

    fun setToolbar(icon: Int = 0, showLightStatusBar: Boolean = false) {
        if (showLightStatusBar) {
            setStatusBarColor(R.color.white, true)
        } else {
            setStatusBarColor()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    fun getCurrentFragment(): Int? {
        return navController?.currentDestination?.id
    }

    override fun onBackPressed() {
        val destArray = listOf<Int>(R.id.splashFragment, R.id.loginFragment)
        if (destArray.contains(getCurrentFragment())) {
            backAction()

        } else {
            navController?.popBackStack()
        }
    }
}