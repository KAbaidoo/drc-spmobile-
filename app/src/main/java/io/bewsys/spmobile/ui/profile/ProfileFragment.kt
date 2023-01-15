package io.bewsys.spmobile.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R

import io.bewsys.spmobile.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
    }
}