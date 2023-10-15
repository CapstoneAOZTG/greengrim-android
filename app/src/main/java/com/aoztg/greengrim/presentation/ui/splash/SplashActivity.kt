package com.aoztg.greengrim.presentation.ui.splash

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AlertDialog
import com.aoztg.greengrim.databinding.ActivitySplashBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.ui.intro.view.LoginActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        Handler(Looper.getMainLooper()).postDelayed({

            // 스플래시 끝난뒤 LoadingDialog 띄우기
            showLoading(this)

            if (!isNetworkConnected(this)) {
                // 네트워크 검사 끝났으면 LoadingDialog 내리기
                dismissLoading()

                val builder = AlertDialog.Builder(this)
                builder.setTitle("")
                    .setMessage("네트워크에 연결되지 않았습니다.")
                    .setPositiveButton("다시 시도하기",
                        DialogInterface.OnClickListener { dialog, id ->
                            // 앱 처음부터 다시시작
                            val intent = Intent(applicationContext, SplashActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        })
                builder.show()
            } else {
                // 네트워크 검사 끝났으면 LoadingDialog 내리기
                dismissLoading()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 1500)
    }

    // 네트워크 연결되었는지 검사코드
    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
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