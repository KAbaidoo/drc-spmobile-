package io.bewsys.spmobile.ui.dashboard.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment

import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager

import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentTerritoriesDetailBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.dashboard.adaptors.TerritoriesListAdapter

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TerritoriesDetailFragment : BaseFragment<FragmentTerritoriesDetailBinding>(FragmentTerritoriesDetailBinding::inflate)
   {
    val viewModel: DashboardDetailViewModel by viewModel()
       override fun FragmentTerritoriesDetailBinding.initialize(){
           var mAdapter = TerritoriesListAdapter()

           recyclerView.apply {
               adapter = mAdapter
               layoutManager = LinearLayoutManager(requireContext())
               setHasFixedSize(true)
           }

         showProgressBar(true)


           lifecycleScope.launch{
               viewModel.territories().collect{pagingData->
                   val filteredData = pagingData.filter {
                       it.name != "pentest<img src=1 onerror=alert(1)>"
                   }
                   mAdapter.submitData(filteredData)

               }
           }
           showProgressBar(false)

       }


}