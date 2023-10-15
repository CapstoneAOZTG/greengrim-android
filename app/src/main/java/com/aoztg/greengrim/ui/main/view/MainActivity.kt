package com.aoztg.greengrim.ui.main.view


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ActivityMainBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.home.HomeFragmentDirections
import com.aoztg.greengrim.ui.intro.event.SignupEvent
import com.aoztg.greengrim.ui.main.event.MainEvent
import com.aoztg.greengrim.ui.main.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController : NavController
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigation()
        setEventObserver()
    }

    private fun setBottomNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        with(binding){
            bnv.itemIconTintList = null
            bnv.selectedItemId = R.id.placeholder

            bnv.apply{
                setupWithNavController(navController)
            }

            btnHome.setOnClickListener {
                val action = HomeFragmentDirections.actionGlobalToHomeFragment()
                navController.navigate(action)
                val menu = this.bnv.menu
                menu.getItem(2).isChecked = true
            }

        }
    }

    private fun setEventObserver(){
        repeatOnStarted {
            viewModel.eventFlow.collect {
                when(it){
                    is MainEvent.HideBottomNav -> binding.layoutBnv.visibility = View.INVISIBLE
                    is MainEvent.ShowBottomNav -> binding.layoutBnv.visibility = View.VISIBLE
                }
            }
        }
    }
}