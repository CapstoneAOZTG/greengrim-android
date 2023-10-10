package com.aoztg.greengrim.ui.login

import android.content.Intent
import android.os.Bundle
import com.aoztg.greengrim.databinding.ActivityLoginBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.ui.main.MainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setListener()
    }

    private fun setListener(){

        // TODO 홈화면 구현용 테스트코드
        binding.btnKakaoLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}