package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.findFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdThreeHeadBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.*

class FormStepThreeFragment : Fragment(R.layout.fragment_add_household_three_head) {
    private val viewModel: SharedDevelopmentalFormViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdThreeHeadBinding.bind(view)


        binding.apply {
            val til = listOf(
                tilHouseholdHeadFirstname,
                tilHouseholdHeadMiddlename,
                tilHouseholdHeadLastname,
                tilHouseholdHeadVoterIdCard,
                tilHouseholdHeadPhoneNumber,
                tilHouseholdHeadDob,
                tilHouseholdHeadAge
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
            val tils = listOf(
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


                )



            //           set radio buttons

            when (viewModel.headIsRespondent) {
                rbYesIsRespondent.text -> rgIsHeadRespondent.check(rbYesIsRespondent.id)
                rbNoIsRespondent.text -> rgIsHeadRespondent.check(rbNoIsRespondent.id)
            }
            //           set radio buttons

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
                rbMarried.text -> rgHeadMaritalStatus.check(rbMale.id)
                rbDivorced.text -> rgHeadMaritalStatus.check(rbDivorced.id)
                rbSingle.text -> rgHeadMaritalStatus.check(rbSingle.id)
                rbWidowed.text -> rgHeadMaritalStatus.check(rbWidowed.id)
            }

            //            set text fields
            viewModel.apply {
                tils.forEachIndexed { index, til ->
                    til.editText?.setText(fields[index])
                }
            }
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
                        viewModel.headPregnancyStatus = rbWidowed.text.toString()
                    }
                }
            }
            //           respond to respondent is head
            rgIsHeadRespondent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesIsRespondent.id -> {
                        viewModel.headIsRespondent = rbYesIsRespondent.text as String

                        viewModel.apply {
                            val fieldList = listOf(
                                respondentFirstName,
                                respondentMiddleName,
                                respondentLastName,
                                respondentVoterId,
                                respondentPhoneNo,
                                respondentDOB,
                                respondentAge
                            )
                            til.mapIndexed { index, textInputLayout ->
                                Pair(textInputLayout, fieldList[index])
                            }.forEach {
                                it.first.editText?.setText(it.second)
                                it.first.isEnabled = false
                            }
                            if(respondentSex == "Male" ){
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
                .setTitleText("Select date of birth")
                .build()
            tilHouseholdHeadDob.editText?.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            tilHouseholdHeadDob.editText?.setOnClickListener {
                datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(it)
                tilHouseholdHeadDob.editText!!.setText(date)
            }


            tilHouseholdHeadFirstname.editText?.addTextChangedListener {
                viewModel.headFirstName = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadLastname.editText?.addTextChangedListener {
                viewModel.headLastName = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadAge.editText?.addTextChangedListener {
                viewModel.headAge = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadDob.editText?.addTextChangedListener {
                viewModel.headDOB = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadVoterIdCard.editText?.addTextChangedListener {
                viewModel.headVoterId = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadPhoneNumber.editText?.addTextChangedListener {
                viewModel.headPhoneNo = it.toString()
                viewModel.stepThreeHasBlankFields()
            }

            tilHouseholdHeadBirthCertificate.editText?.addTextChangedListener {
                viewModel.headBirthCert = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadEducational.editText?.addTextChangedListener {
                viewModel.headEduLevel = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadSocioProfessionalCategory.editText?.addTextChangedListener {
                viewModel.headSocioProfessionalCategory = it.toString()
                viewModel.stepThreeHasBlankFields()
            }

            tilHouseholdHeadSectorOfWork.editText?.addTextChangedListener {
                viewModel.headSectorOfWork = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadSchoolAttendance.editText?.addTextChangedListener {
                viewModel.headSchoolAttendance = it.toString()
                viewModel.stepThreeHasBlankFields()
            }
            tilHouseholdHeadDisability.editText?.addTextChangedListener {
                viewModel.headDisability = it.toString()
                viewModel.stepThreeHasBlankFields()
            }





            btnNext.setOnClickListener {
                val action =
                    FormStepThreeFragmentDirections.actionFormStepThreeFragmentToFormStepFourFragment()
                findNavController().navigate(action)
            }
            btnPrevious.setOnClickListener {
                val action =
                    FormStepThreeFragmentDirections.actionFormStepThreeFragmentToFormStepTwoFragment()
                findNavController().navigate(action)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepThreeHasBlankFields.collectLatest {
                    btnNext.isEnabled =   !it
                }

            }

        }
    }
}