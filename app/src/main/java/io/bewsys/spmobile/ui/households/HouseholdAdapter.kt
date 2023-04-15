package io.bewsys.spmobile.ui.households

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.databinding.ItemHouseholdBinding


class HouseholdAdapter(private val listener: OnItemClickListener) :
    ListAdapter<HouseholdModel, HouseholdAdapter.HouseholdViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HouseholdAdapter.HouseholdViewHolder {
        val binding = ItemHouseholdBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HouseholdViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HouseholdAdapter.HouseholdViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class HouseholdViewHolder(private val binding: ItemHouseholdBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val household = getItem(position)

                        listener.onItemClick(household)
                    }
                }
            }
        }

        fun bind(householdModel: HouseholdModel) {
            binding.apply {
                textViewProvinceName.text = householdModel.province_name ?: ""
                textViewCommunityName.text = householdModel.community_name ?: ""
                if (!householdModel.status.isNullOrEmpty()) checkBoxIcon.isChecked = true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(householdModel: HouseholdModel)

    }

    class DiffCallback : DiffUtil.ItemCallback<HouseholdModel>() {
        override fun areItemsTheSame(oldItem: HouseholdModel, newItem: HouseholdModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HouseholdModel,
            newItem: HouseholdModel
        ) = oldItem == newItem
    }
}