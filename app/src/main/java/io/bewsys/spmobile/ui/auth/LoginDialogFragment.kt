package io.bewsys.spmobile.ui.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle

import androidx.fragment.app.DialogFragment


import io.bewsys.spmobile.R
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