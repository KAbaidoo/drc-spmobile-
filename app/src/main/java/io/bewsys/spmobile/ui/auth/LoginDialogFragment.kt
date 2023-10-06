package io.bewsys.spmobile.ui.auth

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KFunction1


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




