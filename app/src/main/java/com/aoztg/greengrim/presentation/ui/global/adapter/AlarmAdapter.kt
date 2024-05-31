package com.aoztg.greengrim.presentation.ui.global.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemAlarmListBinding
import com.aoztg.greengrim.presentation.ui.global.model.UiAlarmData
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class AlarmAdapter :
    ListAdapter<UiAlarmData, AlarmDataViewHolder>(DefaultDiffUtil<UiAlarmData>()) {

    override fun onBindViewHolder(holder: AlarmDataViewHolder, position: Int) {
        holder.bind(getItem(position))
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

    fun bind(item: UiAlarmData) {
        binding.item = item
    }
}