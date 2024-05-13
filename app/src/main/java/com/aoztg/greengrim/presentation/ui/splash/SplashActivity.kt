package com.aoztg.greengrim.presentation.ui.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.databinding.ActivitySplashBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.customview.PermissionDialog
import com.aoztg.greengrim.presentation.ui.intro.IntroActivity
import com.aoztg.greengrim.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()

    private lateinit var permissionDialog: PermissionDialog

    private lateinit var neededPermissionList: ArrayList<String>
    private val requiredPermissionList =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                // 안드로이드 13 이상 필요한 권한들
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.CAMERA,
            )
        } else {
            arrayOf(
                // 안드로이드 13 미만 필요한 권한들
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEventObserve()

        permissionDialog = PermissionDialog(this) {
            checkPermission()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            initCheckPermission()

        }, 1500)
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is SplashEvent.NavigateToIntroActivity -> {
                        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                        finish()
                    }

                    is SplashEvent.NavigateToMainActivity -> {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun initCheckPermission() {
        neededPermissionList = arrayListOf()

        requiredPermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        if (neededPermissionList.isNotEmpty()) {
            permissionDialog.show()
        } else {
            checkJwt()
        }
    }

    private fun checkPermission() {
        neededPermissionList = arrayListOf()

        requiredPermissionList.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) neededPermissionList.add(permission)
        }

        if (neededPermissionList.isNotEmpty()) {
            activityResultLauncher.launch(neededPermissionList.toTypedArray())
        } else {
            checkJwt()
        }
    }

    private val contract = ActivityResultContracts.RequestMultiplePermissions()

    private val activityResultLauncher = registerForActivityResult(contract) { resultMap ->
        val isAllGranted = requiredPermissionList.all { e -> resultMap[e] == true }

        // todo 분기처리 필요하면 하기
        if (isAllGranted) {

        } else {

        }

        checkJwt()

    }

    private fun checkJwt() {
        viewModel.checkLoginType()
        permissionDialog.dismiss()
    }

    // 풀스크린 적용
//    private fun setFullScreen(){
//        window.apply {
//            statusBarColor = Color.TRANSPARENT
//            decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//    }


}