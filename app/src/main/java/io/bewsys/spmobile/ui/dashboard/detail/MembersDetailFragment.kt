package io.bewsys.spmobile.ui.dashboard.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentMembersDetailBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.dashboard.adaptors.MemberListAdapter

import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MembersDetailFragment :
    BaseFragment<FragmentMembersDetailBinding>(FragmentMembersDetailBinding::inflate) {
    val viewModel: DashboardDetailViewModel by viewModel()
    override fun FragmentMembersDetailBinding.initialize() {
        var mAdapter = MemberListAdapter()

        recyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        showProgressBar(true)


        lifecycleScope.launch {
            viewModel.members().collect { pagingData ->
                mAdapter.submitData(pagingData)

            }
        }

        showProgressBar(false)


    }



}