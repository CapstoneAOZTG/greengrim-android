package com.aoztg.greengrim.ui.intro.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.databinding.ActivitySignupBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.intro.event.SignupEvent
import com.aoztg.greengrim.ui.intro.viewmodel.SignupViewModel
import com.aoztg.greengrim.ui.main.MainActivity
import com.aoztg.greengrim.util.Constants.RC_PERMISSION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate) {

    private val viewModel : SignupViewModel by viewModels()
    private lateinit var neededPermissionList : MutableList<String>
    private val requiredPermissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
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

        binding.vm = viewModel
        setNavigationObserver()
        setTextWatcher()
    }

    private fun setNavigationObserver(){
        repeatOnStarted {
            viewModel.eventFlow.collect {
                when(it){
                    is SignupEvent.NavigateToMainActivity -> startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                    is SignupEvent.NavigateToGallery -> onCheckPermissions()
                    is SignupEvent.ShowWarning -> binding.tvWarning.visibility = View.VISIBLE
                    is SignupEvent.HideWarning -> binding.tvWarning.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun onCheckPermissions(){
        neededPermissionList = mutableListOf()

        requiredPermissionList.forEach{permission->
            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED) neededPermissionList.add(permission)
        }

        if(neededPermissionList.isNotEmpty()){
            ActivityCompat.requestPermissions(this, neededPermissionList.toTypedArray(), RC_PERMISSION)
        }
    }



    private fun setTextWatcher(){
        with(binding){
            etNickname.addTextChangedListener(getTextWatcher(8, this.tvNicknameCount, true))
            etIntroduce.addTextChangedListener(getTextWatcher(200,this.tvIntroduceCount, false ))
        }
    }

    private fun getTextWatcher(
        limit : Int,
        countView : TextView,
        isNick : Boolean
    ) : TextWatcher{

        val textWatcher = object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let{
                    countView.text = "(${it.length}/$limit)"

                    if(isNick){
                        viewModel.checkNick(it.toString())
                    }

                }?:run{
                    countView.text = "(0/$limit)"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        }

        return textWatcher
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == RC_PERMISSION){
            if (ContextCompat.checkSelfPermission(this@SignupActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@SignupActivity, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        }
    }

    private fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }


    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

        if(result.resultCode == Activity.RESULT_OK){
            val uri = result.data?.data

            binding.ivProfile.setImageURI(uri)
        }
    }

}