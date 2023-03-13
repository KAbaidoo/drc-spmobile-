package io.bewsys.spmobile.ui.login

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginDialogFragment : DialogFragment() {

    private val viewModel: LoginDialogViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.failure_dialog_message))
            .setPositiveButton(getString(R.string.failure_dialog_ok)) { _, _ ->
            }
            .setMessage(viewModel.message)
            .create()
}