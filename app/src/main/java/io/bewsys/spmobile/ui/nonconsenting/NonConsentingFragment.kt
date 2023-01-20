package io.bewsys.spmobile.ui.nonconsenting

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentNonConsentingBinding
import io.bewsys.spmobile.util.exhaustive


class NonConsentingFragment : Fragment(R.layout.fragment_non_consenting) {

    val viewModel: NonConsentingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
