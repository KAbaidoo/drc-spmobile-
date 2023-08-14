package io.bewsys.spmobile.ui.dashboard.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.bewsys.spmobile.data.TerritoryEntity
import io.bewsys.spmobile.databinding.DashboardListItemBinding

class TerritoriesListAdapter :
    PagingDataAdapter<TerritoryEntity, TerritoriesListAdapter.ViewHolder>(
        COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardListItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    fun getItemNow() =
        getItem(0)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        item?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(
        private val itemBinding: DashboardListItemBinding,
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(entity: TerritoryEntity) {
            itemBinding.apply {
                tvName.text = entity.name.orEmpty()
            }


        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TerritoryEntity>() {
            override fun areItemsTheSame(oldItem: TerritoryEntity, newItem: TerritoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TerritoryEntity, newItem: TerritoryEntity): Boolean {
                return oldItem.id == newItem.id

            }
        }
    }
}
