package com.aoztg.greengrim.presentation.ui.global.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemAlarmListBinding
import com.aoztg.greengrim.presentation.ui.global.model.UiAlarmData
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class AlarmAdapter :
    ListAdapter<UiAlarmData, AlarmDataViewHolder>(DefaultDiffUtil<UiAlarmData>()) {

    private var listener: AlarmClickListener? = null

    fun setOnItemClickListener(listener: AlarmClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: AlarmDataViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmDataViewHolder {
        return AlarmDataViewHolder(
            ItemAlarmListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

}

class AlarmDataViewHolder(private val binding: ItemAlarmListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiAlarmData, listener: AlarmClickListener?) {
        binding.item = item
        when (item.type) {
            "POINT_CERTIFICATION", "POINT_VERIFICATION" -> {
                listener?.navigateToCertificationDetail(item.resourceId)
            }

            "CHALLENGE_SUCCESS" -> {
                listener?.navigateToChallengeDetail(item.resourceId)
            }

            "NFT_LIKE", "NFT_EXCHANGE" -> {
                listener?.navigateToNftDetail(item.resourceId)
            }

            "NEW_ISSUE" -> {
                listener?.navigateToIssueDetail(item.resourceId)
            }
        }
    }
}