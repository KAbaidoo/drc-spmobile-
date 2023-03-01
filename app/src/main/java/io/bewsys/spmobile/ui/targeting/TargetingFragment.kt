package io.bewsys.spmobile.ui.targeting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentTargetingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


private val TAG = "TargetingFragment"
class TargetingFragment : Fragment(R.layout.fragment_targeting) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTargetingBinding.bind(view)
        val viewModel: TargetingViewModel by viewModel()

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }



    }


}