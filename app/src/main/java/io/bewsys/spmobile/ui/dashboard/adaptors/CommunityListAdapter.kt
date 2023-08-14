package io.bewsys.spmobile.ui.dashboard.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.data.CommunityEntity
import io.bewsys.spmobile.databinding.DashboardListItemBinding

class CommunityListAdapter :
    PagingDataAdapter<CommunityEntity, CommunityListAdapter.CommunityViewHolder>(
        COMMUNITY_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardListItemBinding.inflate(inflater, parent, false)

        return CommunityViewHolder(binding)
    }

    fun getItemNow() =
        getItem(0)

    override fun onBindViewHolder(holder: CommunityViewHolder, position: Int) {
        val community = getItem(position)

        community?.let {
            holder.bind(community)
        }
    }

    inner class CommunityViewHolder(
        private val itemBinding: DashboardListItemBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(community: CommunityEntity) {
            itemBinding.apply {
                tvName.text = community.name.orEmpty()
            }


        }
    }

    companion object {
        private val COMMUNITY_COMPARATOR = object : DiffUtil.ItemCallback<CommunityEntity>() {
            override fun areItemsTheSame(oldItem: CommunityEntity, newItem: CommunityEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CommunityEntity, newItem: CommunityEntity): Boolean {
                return oldItem.id == newItem.id

            }
        }
    }
}
