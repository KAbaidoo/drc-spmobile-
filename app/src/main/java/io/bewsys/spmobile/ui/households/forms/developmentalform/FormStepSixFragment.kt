package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdSixPropertyBinding
import io.bewsys.spmobile.util.LocationProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepSixFragment : Fragment(R.layout.fragment_add_household_six_property) {
    private val viewModel: SharedDevelopmentalFormViewModel by activityViewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddHouseholdSixPropertyBinding.bind(view)

        binding.apply {

            rgHasLivestock.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesHasLivestock.id -> {
                        viewModel.hasLiveStock = rbYesHasLivestock.text.toString()
                    }
                    else -> {
                        viewModel.hasLiveStock = rbNoHasLivestock.text.toString()
                    }
                }
            }
            rgHasHouseholdGoods.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesHasHouseholdGoods.id -> {
                        viewModel.hasHouseholdGoods = rbYesHasHouseholdGoods.text.toString()
                    }
                    else -> {
                        viewModel.hasHouseholdGoods = rbNoHasHouseholdGoods.text.toString()
                    }
                }
            }
            rgAccessToCultivableLand.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAccessToCultivableLand.id -> {
                        viewModel.accessToCultivableLand =
                            rbYesAccessToCultivableLand.text.toString()
                    }
                    else -> {
                        viewModel.accessToCultivableLand =
                            rbNoAccessToCultivableLand.text.toString()
                    }
                }
            }
/*

            when (viewModel.headMaritalStatus) {
                rbMarried.text -> rgHeadMaritalStatus.check(rbMale.id)
                rbDivorced.text -> rgHeadMaritalStatus.check(rbDivorced.id)
                rbSingle.text -> rgHeadMaritalStatus.check(rbSingle.id)
                rbWidowed.text -> rgHeadMaritalStatus.check(rbWidowed.id)
            }
*/


        }
    }


}