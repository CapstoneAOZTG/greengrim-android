package com.aoztg.greengrim.presentation.ui.main.view


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ActivityMainBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.ui.home.HomeFragmentDirections
import com.aoztg.greengrim.presentation.ui.main.event.MainEvent
import com.aoztg.greengrim.presentation.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController : NavController
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigation()
        setEventObserver()
    }

    private fun setBottomNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_frag) as NavHostFragment
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