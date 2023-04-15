package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdThreeHeadBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.*

class FormStepThreeFragment : Fragment(R.layout.fragment_add_household_three_head) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdThreeHeadBinding.bind(view)


        binding.apply {

            val til = listOf(
                tilHouseholdHeadFirstname,
                tilHouseholdHeadMiddlename,
                tilHouseholdHeadLastname,
                tilHouseholdHeadAge,
                tilHouseholdHeadDob,
                tilHouseholdHeadVoterIdCard,
                tilHouseholdHeadPhoneNumber,
            )
            val tvs = listOf(
                tvHeadAge,
                tvHeadAgeKnown,
                tvHeadDob,
                tvHeadDobKnown,
                tvHeadSex,
                tvHeadFirstName,
                tvHeadMiddleName,
                tvHeadLastName,
                tvHeadVoterId,
                tvHeadPhoneNumber
            )
            val rbs = listOf(rbFemale, rbMale, rbYesAge, rbNoAge, rbYesDob, rbYesDob, rbNoDob)
            val allTils = listOf(
                tilHouseholdHeadFirstname,
                tilHouseholdHeadMiddlename,
                tilHouseholdHeadLastname,
                tilHouseholdHeadAge,
                tilHouseholdHeadDob,
                tilHouseholdHeadVoterIdCard,
                tilHouseholdHeadPhoneNumber,
                tilHouseholdHeadBirthCertificate,
                tilHouseholdHeadEducational,
                tilHouseholdHeadSocioProfessionalCategory,
                tilHouseholdHeadSchoolAttendance,
                tilHouseholdHeadSectorOfWork,
                tilHouseholdHeadDisability
            )
            //           set radio buttons
            when (viewModel.headIsRespondent) {
                rbYesIsRespondent.text -> rgIsHeadRespondent.check(rbYesIsRespondent.id)
                rbNoIsRespondent.text -> rgIsHeadRespondent.check(rbNoIsRespondent.id)
            }
            when (viewModel.headAgeKnown) {
                rbYesAge.text -> rgHeadAgeKnown.check(rbYesAge.id)
                rbNoAge.text -> rgHeadAgeKnown.check(rbNoAge.id)
            }
            when (viewModel.headDOBKnown) {
                rbYesDob.text -> rgHeadDobKnown.check(rbYesDob.id)
                rbNoDob.text -> rgHeadDobKnown.check(rbNoDob.id)
            }
            when (viewModel.headSex) {
                rbMale.text -> rgHouseholdHeadSex.check(rbMale.id)
                rbFemale.text -> rgHouseholdHeadSex.check(rbFemale.id)
            }
            if (rgHeadPregnancyStatus.isEnabled) {
                when (viewModel.headPregnancyStatus) {
                    rbYesPregnant.text -> rgHeadPregnancyStatus.check(rbYesPregnant.id)
                    rbNoPregnant.text -> rgHeadPregnancyStatus.check(rbNoPregnant.id)
                }
            }
            when (viewModel.headMaritalStatus) {
                rbMarried.text -> rgHeadMaritalStatus.check(rbMarried.id)
                rbDivorced.text -> rgHeadMaritalStatus.check(rbDivorced.id)
                rbSingle.text -> rgHeadMaritalStatus.check(rbSingle.id)
                rbWidowed.text -> rgHeadMaritalStatus.check(rbWidowed.id)
            }

            //            set text fields
            viewModel.apply {
                allTils.forEachIndexed { index, til ->
                    til.editText?.setText(stepThreeFields[index])
                }
            }
            tilHouseholdHeadDob.editText?.setText(viewModel.headDOB)

            /*      Set up listeners        */
//             radio buttons setOnCheckChangelistener
            rgHeadAgeKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAge.id -> {
                        viewModel.headAgeKnown = rbYesAge.text.toString()
                        tvHeadAge.isEnabled = true
                        tilHouseholdHeadAge.isEnabled = true
                    }
                    else -> {
                        viewModel.headAgeKnown = rbNoAge.text.toString()
                        tvHeadAge.isEnabled = false
                        tilHouseholdHeadAge.isEnabled = false
                        tilHouseholdHeadAge.editText?.text?.clear()
                    }
                }
            }
            rgHeadDobKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesDob.id -> {
                        viewModel.headDOBKnown = rbYesDob.text.toString()
                        tvHeadDob.isEnabled = true
                        tilHouseholdHeadDob.isEnabled = true
                    }
                    else -> {
                        viewModel.headDOBKnown = rbNoDob.text.toString()
                        tvHeadDob.isEnabled = false
                        tilHouseholdHeadDob.isEnabled = false
                        tilHouseholdHeadDob.editText?.text?.clear()
                    }
                }

            }
            rgHouseholdHeadSex.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMale.id -> {
                        viewModel.headSex = rbMale.text.toString()
                        viewModel.headPregnancyStatus = ""
                        tvHeadPregnancyStatus.isEnabled = false
                        rgHeadPregnancyStatus.isEnabled = false
                        rbNoPregnant.isEnabled = false
                        rbYesPregnant.isEnabled = false
                    }
                    else -> {
                        viewModel.headSex = rbFemale.text.toString()
                        tvHeadPregnancyStatus.isEnabled = true
                        rgHeadPregnancyStatus.isEnabled = true
                        rbNoPregnant.isEnabled = true
                        rbYesPregnant.isEnabled = true
                    }
                }
            }
            rgHeadPregnancyStatus.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesPregnant.id -> {
                        viewModel.headPregnancyStatus = rbYesPregnant.text.toString()
                    }
                    else -> {
                        viewModel.headPregnancyStatus = rbNoPregnant.text.toString()
                    }
                }
            }
            rgHeadMaritalStatus.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMarried.id -> {
                        viewModel.headMaritalStatus = rbMarried.text.toString()
                    }
                    rbSingle.id -> {
                        viewModel.headMaritalStatus = rbSingle.text.toString()
                    }
                    rbDivorced.id -> {
                        viewModel.headMaritalStatus = rbDivorced.text.toString()
                    }
                    else -> {
                        viewModel.headMaritalStatus = rbWidowed.text.toString()
                    }
                }
            }
            //           respond to respondent is head
            rgIsHeadRespondent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesIsRespondent.id -> {
                        viewModel.headIsRespondent = rbYesIsRespondent.text.toString()

                        viewModel.apply {

                            stepThreeFields[0] = respondentFirstName
                            stepThreeFields[1] = respondentMiddleName
                            stepThreeFields[2] = respondentLastName
                            stepThreeFields[3] = respondentAge
                            stepThreeFields[4] = respondentVoterId
                            stepThreeFields[5] = respondentPhoneNo

                            headDOB = respondentDOB
                            headDOBKnown = respondentDOBKnown
                            headAgeKnown = respondentAgeKnown
                            headSex = respondentSex

                            when (viewModel.headSex) {
                                rbMale.text -> rgHouseholdHeadSex.check(rbMale.id)
                                rbFemale.text -> rgHouseholdHeadSex.check(rbFemale.id)
                            }
                            when (viewModel.headAgeKnown) {
                                rbYesAge.text -> rgHeadAgeKnown.check(rbYesAge.id)
                                rbNoAge.text -> rgHeadAgeKnown.check(rbNoAge.id)
                            }
                            when (viewModel.headDOBKnown) {
                                rbYesDob.text -> rgHeadDobKnown.check(rbYesDob.id)
                                rbNoDob.text -> rgHeadDobKnown.check(rbNoDob.id)
                            }


                            tilHouseholdHeadFirstname.editText?.apply {
                                setText(respondentFirstName)
                                isEnabled = false
                            }
                            tilHouseholdHeadMiddlename.editText?.apply {
                                setText(respondentMiddleName)
                                isEnabled = false
                            }
                            tilHouseholdHeadLastname.editText?.apply {
                                setText(respondentLastName)
                                isEnabled = false
                            }
                            tilHouseholdHeadAge.editText?.apply {
                                setText(respondentAge)
                                isEnabled = false
                            }
                            tilHouseholdHeadDob.editText?.apply {
                                setText(respondentDOB)
                                isEnabled = false
                            }
                            tilHouseholdHeadVoterIdCard.editText?.apply {
                                setText(respondentVoterId)
                                isEnabled = false
                            }
                            tilHouseholdHeadPhoneNumber.editText?.apply {
                                setText(respondentPhoneNo)
                                isEnabled = false
                            }

                            if (respondentSex == "Male") {
                                tvHeadPregnancyStatus.isEnabled = false
                                rgHeadPregnancyStatus.isEnabled = false
                                rbNoPregnant.isEnabled = false
                                rbYesPregnant.isEnabled = false
                            } else {
                                tvHeadPregnancyStatus.isEnabled = true
                                rgHeadPregnancyStatus.isEnabled = true
                                rbNoPregnant.isEnabled = true
                                rbYesPregnant.isEnabled = true
                            }
                        }

                        tvs.forEach { tv ->
                            tv.isEnabled = false
                        }
                        rbs.forEach { rb ->
                            rb.isEnabled = false
                        }
                    }
                    else -> {
                        viewModel.headIsRespondent = rbNoIsRespondent.text as String

                        viewModel.apply {
                            stepThreeFields[0] = ""
                            stepThreeFields[1] = ""
                            stepThreeFields[2] = ""
                            stepThreeFields[3] = ""
                            stepThreeFields[4] = ""
                            stepThreeFields[5] = ""

                            headDOB = ""
                            headDOBKnown = ""
                            headAgeKnown = ""
                            headSex = ""
                        }

                        til.forEach { til ->
                            til.editText?.text?.clear()
                            til.isEnabled = true
                        }
                        tvs.forEach { tv ->
                            tv.isEnabled = true
                        }
                        rbs.forEach { rb ->
                            rb.isEnabled = true
                        }
                    }
                }
            }

            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(getString(R.string.select_dob))
                .build()
            tilHouseholdHeadDob.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            tilHouseholdHeadDob.editText?.setOnClickListener {
                datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(it)
                tilHouseholdHeadDob.editText!!.setText(date)
            }

            allTils.forEachIndexed { index, til ->
                til.editText?.addTextChangedListener {
                    viewModel.setStepThreeFields(index, it)
                    viewModel.stepThreeHasBlankFields()
                }
            }

            val title = if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)
            btnNext.setOnClickListener {

                val action =
                    FormStepThreeFragmentDirections.actionFormStepThreeFragmentToFormStepFourFragment(
                       title =title,
                       household = viewModel.household
                    )
                findNavController().navigate(action)
            }
            btnPrevious.setOnClickListener {
                val action =
                    FormStepThreeFragmentDirections.actionFormStepThreeFragmentToFormStepTwoFragment(
                        title =title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepThreeHasBlankFields.collectLatest {
                    btnNext.isEnabled = it.not()

                }
            }

        }
        // set up menu
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_households_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_download_households -> {
                        val bundle = bundleOf("id" to viewModel.id)
                        findNavController().navigate(R.id.deleteHouseholdDialogFragment, bundle)

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}