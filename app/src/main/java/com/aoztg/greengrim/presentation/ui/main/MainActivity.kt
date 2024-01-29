package com.aoztg.greengrim.presentation.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
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
import com.aoztg.greengrim.presentation.chatmanager.ChatEvent
import com.aoztg.greengrim.presentation.chatmanager.ChatManager
import com.aoztg.greengrim.presentation.customview.PhotoBottomSheet
import com.aoztg.greengrim.presentation.ui.editgrim.CompleteGrim
import com.aoztg.greengrim.presentation.ui.editgrim.EditGrimActivity
import com.aoztg.greengrim.presentation.ui.editgrim.GrimState
import com.aoztg.greengrim.presentation.ui.home.HomeFragmentDirections
import com.aoztg.greengrim.presentation.ui.intro.IntroActivity
import com.aoztg.greengrim.presentation.ui.toCreateNft
import com.aoztg.greengrim.presentation.ui.toGrimDetail
import com.aoztg.greengrim.presentation.ui.toMultiPart
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.CAMERA_PERMISSION
import com.aoztg.greengrim.presentation.util.Constants.NOTIFICATION_PERMISSION
import com.aoztg.greengrim.presentation.util.Constants.STORAGE_PERMISSION
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private val chatManager: ChatManager by viewModels()

    private lateinit var neededPermissionList: MutableList<String>
    private val storagePermissionList =
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
    private val cameraPermission = Manifest.permission.CAMERA
    private val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
    private lateinit var tempCameraUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_frag) as NavHostFragment
        navController = navHostFragment.navController

        if (intent.hasExtra("target")) {
            when (intent.getStringExtra("target")) {
                "GRIM_DETAIL" -> {
                    intent.getIntExtra("grimId", -1).let {
                        navController.toGrimDetail(it)
                    }
                }

                "MARKET" -> {
                }

                "CREATE_NFT" -> {
                    val grimId = intent.getIntExtra("grimId", -1)
                    intent.getStringExtra("grimUrl")?.let {
                        navController.toCreateNft(grimId, it) }
                }
            }
        }

        binding.vm = viewModel
        binding.chatVm = chatManager
        checkNotificationPermission()
        setBottomNavigation()
        setBottomNavigationListener()
        initEventObserver()
        initSocketObserver()
        setKeyboardListener()
    }

    override fun onStart() {
        super.onStart()
        chatManager.getMyChatIds()
    }

    override fun onStop() {
        super.onStop()
        chatManager.subscribeFcm()
        chatManager.disconnectChat()
    }

    private fun checkNotificationPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(
                    this,
                    notificationPermission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(notificationPermission), NOTIFICATION_PERMISSION)
            }
        }
    }

    private fun setBottomNavigation() {


        with(binding) {
            bnv.itemIconTintList = null
            bnv.selectedItemId = R.id.placeholder

            bnv.apply {
                setupWithNavController(navController)
                setOnItemSelectedListener { item ->
                    if (item.itemId == R.id.market_fragment && CompleteGrim.grimState != GrimState.NONE) {
                        val intent = Intent(this@MainActivity, EditGrimActivity::class.java)
                        startActivity(intent)
                    }
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
                    is MainEvent.ShowPhotoBottomSheet -> showPhotoBottomSheet()
                    is MainEvent.Logout -> logout()
                    is MainEvent.ShowToastMessage -> showCustomToast(it.msg)
                    is MainEvent.ShowSnackMessage -> showCustomSnack(binding.snackGuide, it.msg)
                    is MainEvent.CopyInClipBoard -> copyInClipBoard(it.link)
                }
            }
        }
    }

    private fun initSocketObserver() {
        repeatOnStarted {
            chatManager.event.collect {
                when (it) {
                    is ChatEvent.ShowSnackMessage -> showCustomSnack(binding.snackGuide, it.msg)
                    is ChatEvent.ShowToastMessage -> showCustomToast(it.msg)
                }
            }
        }
    }

    private fun showPhotoBottomSheet() {
        PhotoBottomSheet(
            this,
            onPhotoClickListener = ::onCheckCameraPermission,
            onGalleryClickListener = ::onCheckStoragePermissions
        ).show()
    }

    private fun onCheckCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                cameraPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(cameraPermission), CAMERA_PERMISSION)
        } else {
            openCamera()
        }
    }

    private fun onCheckStoragePermissions() {
        neededPermissionList = mutableListOf()

        storagePermissionList.forEach { permission ->
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
                STORAGE_PERMISSION
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
        if (requestCode == STORAGE_PERMISSION) {
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
        } else if (requestCode == CAMERA_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
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
                    viewModel.setImage(
                        it, it.toMultiPart(this)
                    )
                }
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageFile()?.let { uri ->
            tempCameraUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intent.also {
                cameraLauncher.launch(it)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): Uri? {
        val curDate = SimpleDateFormat("yyMMdd_HHmmss").format(Date())
        val content = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "img_$curDate.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        }
        return this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, content)
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.setImage(
                    tempCameraUri, tempCameraUri.toMultiPart(this)
                )
            }
        }

    private fun setKeyboardListener() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            binding.root.getWindowVisibleDisplayFrame(rec)
            val screenHeight = binding.root.rootView.height
            val keypadHeight = screenHeight - rec.bottom

            if (keypadHeight > screenHeight * 0.15) {
                viewModel.showKeyboard()
            } else {
                viewModel.hideKeyboard()
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

    private fun copyInClipBoard(text: String) {
        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("내 지갑 주소", text)
        clipboard.setPrimaryClip(clip)
        showCustomToast("클립보드에 복사 완료")
    }

}