package com.aoztg.greengrim.presentation.ui.intro

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.databinding.ActivityIntroBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.ui.main.MainActivity
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.getPhotoSheet
import com.aoztg.greengrim.presentation.ui.toMultiPart
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {

    private val viewModel: IntroViewModel by viewModels()

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

    private lateinit var tempCameraUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is IntroEvent.GoToMainActivity -> startActivity(
                        Intent(
                            this@IntroActivity,
                            MainActivity::class.java
                        )
                    )

                    is IntroEvent.ShowPhotoBottomSheet -> showPhotoBottomSheet()
                }
            }
        }
    }

    private fun showPhotoBottomSheet(){
        getPhotoSheet(
            this,
            onPhotoClickListener = ::onCheckCameraPermission,
            onGalleryClickListener = ::onCheckStoragePermissions
        ).show()
    }

    private fun onCheckCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, cameraPermission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(cameraPermission),
                Constants.CAMERA_PERMISSION
            )
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
                Constants.STORAGE_PERMISSION
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
        if (requestCode == Constants.STORAGE_PERMISSION) {
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
        } else if(requestCode == Constants.CAMERA_PERMISSION){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                    viewModel.imageToUrl(it.toMultiPart(this))
                }
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageFile()?.let { uri ->
            tempCameraUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            intent.also{
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
            if(result.resultCode == RESULT_OK){
                viewModel.imageToUrl(tempCameraUri.toMultiPart(this))
            }
        }

}