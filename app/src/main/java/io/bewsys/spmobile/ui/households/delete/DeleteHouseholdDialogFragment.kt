package io.bewsys.spmobile.ui.households.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import io.bewsys.spmobile.DELETE_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class DeleteHouseholdDialogFragment : DialogFragment() {

    private val viewModel: DeleteHouseholdViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Do you really want to delete this household?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.onConfirmDeleteHouseholdClicked()

                setFragmentResult(
                    "delete_household_request",
                    bundleOf(
                        "delete_household_result" to DELETE_HOUSEHOLD_RESULT_OK,
                        "delete_household_id" to viewModel.id
                    )
                )
                findNavController().navigate(R.id.nav_household)
            }
            .create()
    }
}


