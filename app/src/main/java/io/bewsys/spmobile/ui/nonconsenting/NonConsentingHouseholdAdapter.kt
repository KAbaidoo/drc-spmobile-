package io.bewsys.spmobile.ui.nonconsenting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.databinding.ItemNonConsentingHouseholdBinding


class NonConsentingHouseholdAdapter(private val listener: OnItemClickListener) :
    ListAdapter<NonConsentHouseholdModel, NonConsentingHouseholdAdapter.NonConsentingHouseholdViewHolder>(
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
//                        val nonConsentingHousehold = getItem(position)
//                        listener.onItemClick(nonConsentingHousehold)
                    }
                }
            }
        }

        fun bind(nonConsentingHousehold: NonConsentHouseholdModel) {
            binding.apply {
                textViewProvinceName.text = nonConsentingHousehold.province_name ?: ""
                textViewCommunityName.text = nonConsentingHousehold.community_name ?: ""
                if (!nonConsentingHousehold.status.isNullOrEmpty()) checkBoxIcon.isChecked = true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(nonConsentingHousehold: NonConsentHouseholdModel)

    }

    class DiffCallback : DiffUtil.ItemCallback<NonConsentHouseholdModel>() {
        override fun areItemsTheSame(oldItem: NonConsentHouseholdModel, newItem: NonConsentHouseholdModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: NonConsentHouseholdModel,
            newItem: NonConsentHouseholdModel
        ) = oldItem == newItem
    }
}