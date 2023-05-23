package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.os.bundleOf

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentIdentificationOfHouseholdCBinding
import io.bewsys.spmobile.databinding.FragmentLocationOfHouseholdBBinding
import io.bewsys.spmobile.util.LocationProvider
import io.bewsys.spmobile.util.swap
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.util.*

class SectionCIdentificationFragment : Fragment(R.layout.fragment_identification_of_household_c) {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    private var _binding: FragmentIdentificationOfHouseholdCBinding? = null

    private val binding get() = _binding!!


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentIdentificationOfHouseholdCBinding.bind(view)

        val dropdownLayout = R.layout.dropdown_item


        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.SectionCHasBlank.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }

            viewModel.apply {
                tilRelationship.editText?.setText(respondentFamilyBondToHead)
                tilMigrationStatus.editText?.setText(migrationStatus)
                tilDurationDisplacedReturnedRepatriatedRefugee.editText?.setText(
                    durationDisplacedReturnedRepatriatedRefugee
                )
                tilUnitOfMigrationDuration.editText?.setText(unitOfMigrationDuration)
            }


            rgHouseholdHeadSex.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMale.id -> {
                        viewModel.headSex = rbMale.text.toString()
                        viewModel.sectionCHasBlankFields()
                    }
                    else -> {
                        viewModel.headSex = rbFemale.text.toString()
                        viewModel.sectionCHasBlankFields()
                    }
                }
            }

            val tils = listOf(
                tilRespondentFirstname,
                tilRespondentMiddlename,
                tilRespondentLastname,
                tilRespondentVoterId,
                tilRespondentPhoneNumber
            )



//            head
            tilHouseholdHeadFirstname.editText?.addTextChangedListener {
                viewModel.headFirstName = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilHouseholdHeadFirstname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.headFirstName.isBlank()) {
                    tilHouseholdHeadFirstname.error = getString(R.string.field_cannot_be_empty)
                } else tilHouseholdHeadFirstname.error = null
            }

            tilHouseholdHeadMiddlename.editText?.addTextChangedListener {
                viewModel.headMiddleName = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilHouseholdHeadLastname.editText?.addTextChangedListener {
                viewModel.headLastName = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilHouseholdHeadLastname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.headLastName.isBlank()) {
                    tilHouseholdHeadLastname.error = getString(R.string.field_cannot_be_empty)
                } else tilHouseholdHeadLastname.error = null
            }
            tilHouseholdHeadAge.editText?.addTextChangedListener {
                viewModel.headAge = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilHouseholdHeadVoterIdCard.editText?.addTextChangedListener {
                viewModel.headVoterId = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilHouseholdHeadPhoneNumber.editText?.addTextChangedListener {
                viewModel.headPhoneNo = it.toString()
                viewModel.sectionCHasBlankFields()
            }

//            respondent
            tilRespondentFirstname.editText?.addTextChangedListener {
                viewModel.respondentFirstName = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilRespondentFirstname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.respondentFirstName.isBlank()) {
                    tilRespondentFirstname.error = getString(R.string.field_cannot_be_empty)
                } else tilRespondentFirstname.error = null
            }
            tilRespondentMiddlename.editText?.addTextChangedListener {
                viewModel.respondentMiddleName = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilRespondentLastname.editText?.addTextChangedListener {
                viewModel.respondentLastName = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilRespondentLastname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.respondentLastName.isBlank()) {
                    tilRespondentLastname.error = getString(R.string.field_cannot_be_empty)
                } else tilRespondentLastname.error = null
            }

            tilRespondentVoterId.editText?.addTextChangedListener {
                viewModel.respondentVoterId = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilRespondentPhoneNumber.editText?.addTextChangedListener {
                viewModel.respondentPhoneNo = it.toString()
                viewModel.sectionCHasBlankFields()
            }

            tilOtherMigrationStatus.editText?.addTextChangedListener {
                viewModel.otherMigrationStatus = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilUnitOfMigrationDuration.editText?.addTextChangedListener {
                viewModel.unitOfMigrationDuration = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilDurationDisplacedReturnedRepatriatedRefugee.editText?.addTextChangedListener{
                viewModel.durationDisplacedReturnedRepatriatedRefugee = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            tilMigrationStatus.editText?.addTextChangedListener {
                viewModel.migrationStatus = it.toString()
                viewModel.sectionCHasBlankFields()
            }
            rgIsHeadRespondent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesIsRespondent.id -> {
                        viewModel.headIsRespondent = rbYesIsRespondent.text.toString()
                        viewModel.apply {
                            respondentFirstName = headFirstName
                            respondentMiddleName = headMiddleName
                            respondentLastName = headLastName
                            respondentPhoneNo = headPhoneNo
                            respondentVoterId = headVoterId

                            tilRespondentFirstname.editText?.setText(headFirstName)
                            tilRespondentMiddlename.editText?.setText(headMiddleName)
                            tilRespondentLastname.editText?.setText(headLastName)
                            tilRespondentVoterId.editText?.setText(headVoterId)
                            tilRespondentPhoneNumber.editText?.setText(headPhoneNo)
                        }

                        viewModel.sectionCHasBlankFields()
                    }
                    else -> {
                        viewModel.headIsRespondent = rbNoIsRespondent.text.toString()
                        viewModel.apply {
                            respondentFirstName = ""
                            respondentMiddleName = ""
                            respondentLastName = ""
                            respondentPhoneNo = ""
                            respondentVoterId = ""
                        }
                        tils.forEach {
                            it.isEnabled = true
                            it.editText?.text?.clear()
                        }

                        viewModel.sectionCHasBlankFields()
                    }
                }

            }

//          family relationship
            val familyRelationship = resources.getStringArray(R.array.relationship)
            (actRelationship as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, familyRelationship).also {
                        addTextChangedListener {

                            viewModel.respondentFamilyBondToHead = it.toString()

                        }
                    }
                )
            }
            val migrationStatuses = resources.getStringArray(R.array.migration_status)
            (actvMigrationStatus as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, migrationStatuses).also {
                        addTextChangedListener {

                            addTextChangedListener {
                                val status = it.toString()
                                if (status == getString(R.string.other)) {
                                    viewModel.migrationStatus = status
                                    tilOtherMigrationStatus.isEnabled = true
                                    tvOtherMigrationStatus.isEnabled = true
                                    tvDurationDisplacedReturnedRepatriatedRefugee.isEnabled = false
                                    tilDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                        false
                                } else {
                                    tilOtherMigrationStatus.editText?.text?.clear()
                                    tilOtherMigrationStatus.isEnabled = false
                                    tvOtherMigrationStatus.isEnabled = false
                                    actvUnitOfMigrationDuration.isEnabled = false
                                }

                                tvDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                    status != getString(R.string.resident)
                                tilDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                    status != getString(R.string.resident)
                                tilUnitOfMigrationDuration.isEnabled =
                                    status != getString(R.string.resident)
                                actvUnitOfMigrationDuration.isEnabled =
                                    status != getString(R.string.resident)
                                if (status != getString(R.string.resident)) {
                                    tilDurationDisplacedReturnedRepatriatedRefugee.editText?.text?.clear()
                                    tilUnitOfMigrationDuration.editText?.text?.clear()
                                }
                                viewModel.sectionCHasBlankFields()

                            }


                        }
                    }
                )
            }
            val unitMigrationStatus = resources.getStringArray(R.array.migration_duration_unit)
            (actvUnitOfMigrationDuration as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, unitMigrationStatus).also {
                        addTextChangedListener {

                            viewModel.unitOfMigrationDuration = it.toString()

                        }
                    }
                )
            }


            viewModel.apply {
                tilHouseholdHeadFirstname.editText?.setText(headFirstName)
                tilHouseholdHeadMiddlename.editText?.setText(headMiddleName)
                tilHouseholdHeadLastname.editText?.setText(headLastName)
                tilHouseholdHeadAge.editText?.setText(headAge)
                tilRespondentVoterId.editText?.setText(headVoterId)
                tilHouseholdHeadPhoneNumber.editText?.setText(headPhoneNo)

                tilRespondentFirstname.editText?.setText(respondentFirstName)
                tilRespondentMiddlename.editText?.setText(respondentMiddleName)
                tilRespondentLastname.editText?.setText(respondentLastName)
                tilRespondentVoterId.editText?.setText(respondentVoterId)
                tilRespondentPhoneNumber.editText?.setText(respondentPhoneNo)
                tilOtherMigrationStatus.editText?.setText(otherMigrationStatus)



            }
            when (viewModel.headSex) {
                rbMale.text -> rgHouseholdHeadSex.check(rbMale.id)
                rbFemale.text -> rgHouseholdHeadSex.check(rbFemale.id)
            }
            when (viewModel.headIsRespondent) {
                rbYesIsRespondent.text -> rgIsHeadRespondent.check(rbYesIsRespondent.id)
                rbNoIsRespondent.text -> rgIsHeadRespondent.check(rbNoIsRespondent.id)

            }

            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionDCompositionOfHousholdFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionBLocationFragment, bundle)

            }


        } //end of apply block

        viewModel.sectionCHasBlankFields()
    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}