package io.bewsys.spmobile.ui.households.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.bewsys.spmobile.R
import io.bewsys.spmobile.ui.households.forms.developmentalform.SharedDevelopmentalFormViewModel
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class DeleteHouseholdDialogFragment : DialogFragment() {

    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.nav_household)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args: DeleteHouseholdDialogFragmentArgs by navArgs()
        var id = 0L
        if (savedInstanceState != null) {
            id = args.id
        }
       return  AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Do you really want to delete this household?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ ->
                viewModel.onConfirmDeleteHouseholdClicked()
            }
            .create()
    }

}