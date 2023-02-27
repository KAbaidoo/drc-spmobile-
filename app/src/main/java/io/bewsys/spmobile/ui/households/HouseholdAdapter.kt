package io.bewsys.spmobile.ui.households

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.model.HouseholdModel
import io.bewsys.spmobile.data.model.NonConsentHousehold
import io.bewsys.spmobile.databinding.ItemHouseholdBinding
import io.bewsys.spmobile.databinding.ItemNonConsentingHouseholdBinding
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingHouseholdAdapter
import io.ktor.server.routing.RoutingPath.Companion.root


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
//                        listener.onItemClick(nonConsentingHousehold)
                    }
                }
            }
        }

        fun bind(household: HouseholdModel) {
            binding.apply {
//                textViewProvinceName.text = household.province_name ?: ""
//                textViewCommunityName.text = household.community_name ?: ""
                if (!household.status.isNullOrEmpty()) checkBoxIcon.isChecked = true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(household: HouseholdModel)

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