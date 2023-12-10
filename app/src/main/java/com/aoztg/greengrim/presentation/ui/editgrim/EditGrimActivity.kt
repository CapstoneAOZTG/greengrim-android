package com.aoztg.greengrim.presentation.ui.editgrim

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.aoztg.greengrim.databinding.ActivityEditGrimBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditGrimActivity : BaseActivity<ActivityEditGrimBinding>(ActivityEditGrimBinding::inflate) {

    private val viewModel: EditGrimViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initEventObserver()
        initStateObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is EditGrimEvents.ShowSnackMessage -> showCustomSnack(
                        binding.tvAnnounce,
                        it.msg
                    )

                    is EditGrimEvents.GoToGrimDetail -> {
                        val intent = Intent(this@EditGrimActivity, MainActivity::class.java)
                            .putExtra("target","GRIM_DETAIL")
                            .putExtra("grimId",it.grimId)
                        startActivity(intent)
                    }

                    is EditGrimEvents.GoToMainActivity -> {
                        val intent = Intent(this@EditGrimActivity, MainActivity::class.java)
                            .putExtra("target","MARKET")
                        startActivity(intent)
                    }

                    is EditGrimEvents.GoToCreateNft -> {
                        val intent = Intent(this@EditGrimActivity, MainActivity::class.java)
                            .putExtra("target","CREATE_NFT")
                            .putExtra("grimId",it.grimId)
                            .putExtra("grimUrl", it.grimUrl)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.grimState) {
                    GrimState.GRIM_DRAWED -> {
                        binding.layoutAfterEdit.visibility = View.INVISIBLE
                        binding.layoutBeforeEdit.visibility = View.VISIBLE
                        binding.tvAnnounce2.text = "그림의 제목을 지어주세요"
                    }

                    GrimState.GRIM_TITLED -> {
                        viewModel.getGrimDetail()
                        binding.layoutBeforeEdit.visibility = View.INVISIBLE
                        binding.layoutAfterEdit.visibility = View.VISIBLE
                        binding.tvAnnounce2.text = "그림을 저장하거나 NFT로 생성해보세요"
                    }

                    else -> {}
                }
            }
        }
    }

}