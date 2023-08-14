package io.bewsys.spmobile.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentGroupmentsDetailBinding
import io.bewsys.spmobile.databinding.FragmentMembersDetailBinding
import io.bewsys.spmobile.ui.dashboard.adaptors.CommunityListAdapter
import io.bewsys.spmobile.ui.dashboard.adaptors.MemberListAdapter

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MembersDetailFragment : Fragment(R.layout.fragment_members_detail)
   {
    val viewModel: DashboardDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentMembersDetailBinding.bind(view)
        var mAdapter = MemberListAdapter()





        binding.apply {
            progressBar.isVisible = true

            recyclerView.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            progressBar.isVisible = true


            lifecycleScope.launch{
                viewModel.members().collect{pagingData->
//                    val filteredData = pagingData.filter { it.name != "prefManager.dummyFarmerName "}
                    mAdapter.submitData(pagingData)

                }
            }

            progressBar.isVisible = false


        }



    }




    companion object {
        const val TAG = "CommunityList"

    }


}