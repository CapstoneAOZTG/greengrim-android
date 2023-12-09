package com.aoztg.greengrim.presentation.ui.market.create.createpaint

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreatePaintBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import kotlinx.coroutines.delay

class CreatePaintFragment :
    BaseFragment<FragmentCreatePaintBinding>(R.layout.fragment_create_paint) {

    private val viewModel: CreatePaintViewModel by viewModels()

    private var curNounChip: TextView? = null
    private var curVerbChip: TextView? = null
    private var curStyleChip: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.getKeywords()
        initUiStateObserver()
    }

    private fun initUiStateObserver(){
        repeatOnStarted {
            viewModel.uiState.collect{
                if(it.nounKeywords.isNotEmpty()){
                    delay(200)
                    setChipListener()
                }
            }
        }
    }

    private fun setChipListener() {
        binding.cgNoun.children.forEach { view ->
            view.setOnClickListener {
                if (curNounChip != null) {
                    curNounChip?.setBackgroundResource(R.drawable.shape_grey7fill_nostroke_15radius)
                    curNounChip?.setTextAppearance(R.style.TextGgSmallBold)
                }
                curNounChip = view as TextView
                curNounChip?.setBackgroundResource(R.drawable.shape_greenfill_nostroke_15radius)
                curNounChip?.setTextAppearance(R.style.TextGgSmallBlackBold)
                viewModel.selectKeyword(KeywordType.NOUN, curNounChip?.text.toString())
            }
        }

        binding.cgVerb.children.forEach { view ->
            view.setOnClickListener {
                if (curVerbChip != null) {
                    curVerbChip?.setBackgroundResource(R.drawable.shape_grey7fill_nostroke_15radius)
                    curVerbChip?.setTextAppearance(R.style.TextGgSmallBold)
                }
                curVerbChip = view as TextView
                curVerbChip?.setBackgroundResource(R.drawable.shape_greenfill_nostroke_15radius)
                curVerbChip?.setTextAppearance(R.style.TextGgSmallBlackBold)
                viewModel.selectKeyword(KeywordType.VERB, curVerbChip?.text.toString())
            }
        }

        binding.cgStyle.children.forEach { view ->
            view.setOnClickListener {
                if (curStyleChip != null) {
                    curStyleChip?.setBackgroundResource(R.drawable.shape_grey7fill_nostroke_15radius)
                    curStyleChip?.setTextAppearance(R.style.TextGgSmallBold)
                }
                curStyleChip = view as TextView
                curStyleChip?.setBackgroundResource(R.drawable.shape_greenfill_nostroke_15radius)
                curStyleChip?.setTextAppearance(R.style.TextGgSmallBlackBold)
                viewModel.selectKeyword(KeywordType.STYLE, curStyleChip?.text.toString())
            }
        }
    }
}