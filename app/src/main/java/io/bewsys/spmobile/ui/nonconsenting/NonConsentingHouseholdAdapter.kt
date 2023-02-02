package io.bewsys.spmobile.ui.nonconsenting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.model.NonConsentHousehold
import io.bewsys.spmobile.databinding.ItemNonConsentingHouseholdBinding


class NonConsentingHouseholdAdapter(private val listener: OnItemClickListener) :
    ListAdapter<NonConsentHousehold, NonConsentingHouseholdAdapter.NonConsentingHouseholdViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NonConsentingHouseholdViewHolder {
        val binding = ItemNonConsentingHouseholdBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NonConsentingHouseholdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NonConsentingHouseholdViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class NonConsentingHouseholdViewHolder(private val binding: ItemNonConsentingHouseholdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val nonConsentingHousehold = getItem(position)
                        listener.onItemClick(nonConsentingHousehold)
                    }
                }
            }
        }

        fun bind(nonConsentingHousehold: NonConsentHousehold) {
            binding.apply {
                textViewProvinceName.text = nonConsentingHousehold.province_name ?: ""
                textViewCommunityName.text = nonConsentingHousehold.community_name ?: ""
                if (!nonConsentingHousehold.status.isNullOrEmpty()) imageViewIcon.setImageResource(R.drawable.cloud_done_24)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(nonConsentingHousehold: NonConsentHousehold)

    }

    class DiffCallback : DiffUtil.ItemCallback<NonConsentHousehold>() {
        override fun areItemsTheSame(oldItem: NonConsentHousehold, newItem: NonConsentHousehold) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: NonConsentHousehold,
            newItem: NonConsentHousehold
        ) = oldItem == newItem
    }
}