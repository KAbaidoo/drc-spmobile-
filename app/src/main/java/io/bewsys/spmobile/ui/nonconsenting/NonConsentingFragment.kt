package io.bewsys.spmobile.ui.nonconsenting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentNonConsentingBinding
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


class NonConsentingFragment : Fragment(R.layout.fragment_non_consenting) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: NonConsentingViewModel by viewModel()
        val binding = FragmentNonConsentingBinding.bind(view)

        binding.fabNonConsenting.setOnClickListener {
            viewModel.onFabClicked()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.nonConsentingEvent.collect { event ->
                when (event) {
                    is NonConsentingViewModel.NonConsentingEvent.NavigateToNonConsentingHouseholdsForm -> {

                        val action = NonConsentingFragmentDirections.actionNavNonConsentingToNonConsentingFormFragment()
                        findNavController().navigate(action)

                    }
                }
            }.exhaustive
        }
    }
}
