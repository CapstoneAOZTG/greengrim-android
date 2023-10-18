package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeHotChallengeBinding
import com.aoztg.greengrim.databinding.ItemHomeHotNftBinding
import com.aoztg.greengrim.databinding.ItemHomeMoreActivityBinding
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel

class HomeAdapter(val data : List<HomeUiModel>, private val type : vpItemType) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listener : HomeItemClickListener? = null

    fun setOnHomeItemClickListener(listener : HomeItemClickListener){
        this.listener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(type){
            vpItemType.HOT_CHALLENGE-> (holder as HotChallengeHolder).bind(data[position], listener)
            vpItemType.MORE_ACTIVITY-> (holder as MoreActivityHolder).bind(data[position], listener)
            vpItemType.HOT_NFT-> (holder as HotNftHolder).bind(data[position], listener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(type){
            vpItemType.HOT_CHALLENGE -> HotChallengeHolder(ItemHomeHotChallengeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))

            vpItemType.MORE_ACTIVITY -> MoreActivityHolder(ItemHomeMoreActivityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))

            vpItemType.HOT_NFT -> HotNftHolder(ItemHomeHotNftBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemCount(): Int = data.size

}

enum class vpItemType{
    HOT_CHALLENGE,
    MORE_ACTIVITY,
    HOT_NFT
}