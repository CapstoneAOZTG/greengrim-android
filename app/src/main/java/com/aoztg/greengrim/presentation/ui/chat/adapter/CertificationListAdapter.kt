package com.aoztg.greengrim.presentation.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemCertificationBinding
import com.aoztg.greengrim.presentation.ui.chat.model.CertificationListItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class CertificationListAdapter : ListAdapter<CertificationListItem, CertificationViewHolder>(
    DefaultDiffUtil<CertificationListItem>()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificationViewHolder {
        return CertificationViewHolder(
            ItemCertificationBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CertificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CertificationViewHolder(private val binding: ItemCertificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: CertificationListItem) {
        binding.item = item
    }

}