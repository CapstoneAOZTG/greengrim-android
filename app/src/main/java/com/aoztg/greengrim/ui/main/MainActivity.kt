package com.aoztg.greengrim.ui.main


import android.os.Bundle
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ActivityMainBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.home.HomeFragment
import com.aoztg.greengrim.ui.home.HomeFragmentDirections

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigation()
    }

    private fun setBottomNavigation(){
        binding.bnv.itemIconTintList = null
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.bnv.apply{
            setupWithNavController(navController)
        }
        binding.btnHome.setOnClickListener {
            val action = HomeFragmentDirections.actionGlobalToHomeFragment()
            navController.navigate(action)
        }
    }
}