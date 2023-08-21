package io.bewsys.spmobile.ui.dashboard.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment

import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager

import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentProvincesDetailBinding
import io.bewsys.spmobile.ui.dashboard.adaptors.ProvinceListAdapter

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProvincesDetailFragment : Fragment(R.layout.fragment_provinces_detail)
   {
    val viewModel: DashboardDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentProvincesDetailBinding.bind(view)
        var mAdapter = ProvinceListAdapter()





        binding.apply {
            progressBar.isVisible = true

            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            progressBar.isVisible = true


            lifecycleScope.launch{
                viewModel.provinces().collect{pagingData->
                    val filteredData = pagingData.filter {
                        it.name != "pentest<img src=1 onerror=alert(1)>"
                    }
                    mAdapter.submitData(filteredData)

                }
            }




        }



    }




    companion object {
        const val TAG = "ProvincesList"

    }


}