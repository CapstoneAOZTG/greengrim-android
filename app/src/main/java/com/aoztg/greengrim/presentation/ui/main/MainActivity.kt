package com.aoztg.greengrim.presentation.ui.main


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.ActivityMainBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.ui.home.HomeFragmentDirections
import com.aoztg.greengrim.presentation.ui.intro.IntroActivity
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.toMultiPart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private lateinit var neededPermissionList: MutableList<String>
    private val requiredPermissionList =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(  // 안드로이드 13 이상 필요한 권한들
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(  // 안드로이드 13 미만 필요한 권한들
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBottomNavigation()
        setBottomNavigationListener()
        initEventObserver()
    }

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frag) as NavHostFragment
        navController = navHostFragment.navController

        with(binding) {
            bnv.itemIconTintList = null
            bnv.selectedItemId = R.id.placeholder

            bnv.apply {
                setupWithNavController(navController)
                setOnItemSelectedListener { item ->
                    NavigationUI.onNavDestinationSelected(item, navController)
                    navController.popBackStack(item.itemId, inclusive = false)
                    true
                }
            }

            btnHome.setOnClickListener {
                val action = HomeFragmentDirections.actionGlobalToHomeFragment()
                navController.navigate(action)
            }

        }
    }

    private fun setBottomNavigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.home_fragment) {
                val menu = binding.bnv.menu
                menu.getItem(2).isChecked = true
            }
        }
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MainEvent.HideBottomNav -> binding.layoutBnv.visibility = View.INVISIBLE
                    is MainEvent.ShowBottomNav -> binding.layoutBnv.visibility = View.VISIBLE
                    is MainEvent.GoToGallery -> onCheckPermissions()
                    is MainEvent.Logout -> logout()
                }
            }
        }
    }

    private fun onCheckPermissions() {
        neededPermissionList = mutableListOf()

        requiredPermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        if (neededPermissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                neededPermissionList.toTypedArray(),
                Constants.RC_PERMISSION
            )
        } else {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.RC_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }


    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data

                uri?.let {
                    viewModel.imageToUrl(it.toMultiPart(this))
                }
            }
        }

    private fun logout() {
        App.sharedPreferences.edit()
            .remove(Constants.X_ACCESS_TOKEN)
            .remove(Constants.X_REFRESH_TOKEN)
            .apply()
        val intent = Intent(applicationContext, IntroActivity::class.java)
        intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            finishAffinity()
            startActivity(this)
        }
    }
}