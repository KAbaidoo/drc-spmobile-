package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.data.local.MemberModel
import io.bewsys.spmobile.databinding.ItemMemberBinding
import io.bewsys.spmobile.databinding.ItemNonConsentingHouseholdBinding


class MembersAdapter() :
    ListAdapter<MemberModel, MembersAdapter.MemberViewHolder>(
        DiffCallback()
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemberViewHolder {
        val binding = ItemMemberBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class MemberViewHolder(private val binding: ItemMemberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val member = getItem(position)
//                        listener.onItemClick(member)
                    }
                }
            }
        }

        fun bind(memberModel: MemberModel) {
            binding.apply {
                textViewFirstName.text = memberModel.firstname ?: ""
                textViewLastName.text =  memberModel.lastname ?: ""

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(memberModel: MemberModel)

    }

    class DiffCallback : DiffUtil.ItemCallback<MemberModel>() {
        override fun areItemsTheSame(oldItem: MemberModel, newItem: MemberModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MemberModel,
            newItem: MemberModel
        ) = oldItem == newItem
    }
}