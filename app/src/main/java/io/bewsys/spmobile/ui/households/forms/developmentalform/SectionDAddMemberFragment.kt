package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddMembersDBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import java.text.SimpleDateFormat
import java.util.*

class SectionDAddMemberFragment : Fragment(R.layout.fragment_add_members_d) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddMembersDBinding.bind(view)
        val dropdownLayout = R.layout.dropdown_item

        binding.apply {

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.memberHasBlankFields.collectLatest {
                    btnFinish.isEnabled = it.not()
                }
            }

            val tils = listOf(
                tilMemberFirstname,
                tilMemberMiddleName,
                tilMemberLastname,
                tilMemberAge
            )


            rgIsHeadOrRespondent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbHead.id -> {
                        viewModel.apply {
                            isMemberHead= getString(R.string.yes)
                            isMemberRespondent = getString(R.string.no)

                            memberFirstname = headFirstName?: ""
                            memberMiddleName = headMiddleName?: ""
                            memberLastname = headLastName?: ""
                            memberSex =headSex?: ""
                            memberAge =headAge ?: ""

                            tilMemberFirstname.editText?.setText(headFirstName)
                            tilMemberMiddleName.editText?.setText(headMiddleName)
                            tilMemberLastname.editText?.setText(headLastName)
                            tilMemberAge.editText?.setText(headAge?: "")

                            tils.forEach {
                                it.isEnabled = false
                            }

                            when (viewModel.memberSex) {
                                rbMale.text -> rgMemberSex.check(rbMale.id)
                                rbFemale.text -> rgMemberSex.check(rbFemale.id)
                            }


                        }
                    }
                    rbRespondent.id -> {
                        viewModel.apply {
                            isMemberRespondent  = getString(R.string.yes)
                          isMemberHead = getString(R.string.no)

                            memberFirstname = respondentFirstName
                            memberMiddleName = respondentMiddleName
                            memberLastname = respondentLastName
                            memberSex =respondentSex
                            memberAge =respondentAge

                            when (viewModel.memberSex) {
                                rbMale.text -> rgMemberSex.check(rbMale.id)
                                rbFemale.text -> rgMemberSex.check(rbFemale.id)
                            }

                            tilMemberFirstname.editText?.setText(respondentFirstName)
                            tilMemberMiddleName.editText?.setText(respondentMiddleName)
                            tilMemberLastname.editText?.setText(respondentLastName)
                            tilMemberAge.editText?.setText(respondentAge)

                            tils.forEach {
                                it.isEnabled = false
                            }

                        }

                    }
                    else -> {
                        viewModel.apply {
                            memberFirstname = ""
                            memberMiddleName = ""
                            memberLastname = ""
                            memberSex =""
                            memberAge =""

                            tils.forEach {
                                it.isEnabled = true
                                it.editText?.text?.clear()
                            }
                            rgMemberSex.clearCheck()

                        }

                    }
                }
            }


            rgMemberAgeKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAge.id -> {
                        viewModel.memberAgeKnown = rbYesAge.text as String
                        tvMemberAge.isEnabled = true
                        tilMemberAge.isEnabled = true
                    }
                    else -> {
                        viewModel.memberAgeKnown = rbNoAge.text as String
                        tilMemberAge.editText?.text?.clear()
                        tvMemberAge.isEnabled = false
                        tilMemberAge.isEnabled = false

                    }
                }
            }
            rgMemberDobKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesDob.id -> {
                        viewModel.memberDOBKnown = rbYesDob.text as String
                        tvMemberDob.isEnabled = true
                        tilMemberDob.isEnabled = true
                    }
                    else -> {
                        viewModel.memberDOBKnown = rbNoDob.text as String
                        tilMemberDob.editText?.text?.clear()
                        tvMemberDob.isEnabled = false
                        tilMemberDob.isEnabled = false

                    }
                }
            }
            rgMemberSex.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMale.id -> {
                        viewModel.memberSex = rbMale.text as String
                        viewModel.memberPregnancyStatus = ""
                        rgMemberPregnancyStatus.clearCheck()
                        tvMemberPregnancyStatus.isVisible = false
                        rgMemberPregnancyStatus.isVisible = false
                        rbYesPregnant.isVisible = false
                        rbNoPregnant.isVisible = false
                        viewModel.memberHasBlankFields()
                    }
                    else -> {
                        viewModel.memberSex = rbFemale.text as String
                        tvMemberPregnancyStatus.isVisible = true
                        rgMemberPregnancyStatus.isVisible = true
                        rbNoPregnant.isVisible = true
                        rbYesPregnant.isVisible = true
                        viewModel.memberHasBlankFields()
                    }
                }
            }
            rgMemberPregnancyStatus.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesPregnant.id -> {
                        viewModel.memberPregnancyStatus = rbYesPregnant.text as String
                    }
                    else -> {
                        viewModel.memberPregnancyStatus = rbNoPregnant.text as String
                    }
                }
            }
            rgBirthCertificate.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesBirthCert.id -> {
                        viewModel.memberBirthCertificate = rbYesBirthCert.text as String
                    }
                    else -> {
                        viewModel.memberBirthCertificate = rbNoBirthCert.text as String
                    }
                }
            }



            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(getString(R.string.select_dob))
                .build()
            tilMemberDob.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            tilMemberDob.editText?.setOnClickListener {
                datePicker.show(parentFragmentManager, "DATE_PICKER")
            }

            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = sdf.format(it)
                tilMemberDob.editText!!.setText(date)
                viewModel.memberDob = date
            }


            tilMemberFirstname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.memberFirstname.isBlank()) {
                    tilMemberFirstname.error = getString(R.string.field_cannot_be_empty)
                } else tilMemberFirstname.error = null
            }
            tilMemberLastname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.memberLastname.isBlank()) {
                    tilMemberLastname.error = getString(R.string.field_cannot_be_empty)
                } else tilMemberLastname.error = null
            }



            tilMemberFirstname.editText?.addTextChangedListener {
                viewModel.memberFirstname = it.toString()
                viewModel.memberHasBlankFields()
            }
            tilMemberMiddleName.editText?.addTextChangedListener {
                viewModel.memberMiddleName = it.toString()
                viewModel.memberHasBlankFields()
            }
            tilMemberLastname.editText?.addTextChangedListener {
                viewModel.memberLastname = it.toString()
                viewModel.memberHasBlankFields()
            }
            tilMemberAge.editText?.addTextChangedListener {
                viewModel.memberAge = it.toString()
                viewModel.memberHasBlankFields()
            }

            val relationship = resources.getStringArray(R.array.relationship)
            (actRelationship as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, relationship).also {
                        addTextChangedListener {

                            viewModel.memberRelationship = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val maritalStatus = resources.getStringArray(R.array.marital_status)
            (actMaritalStatus as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, maritalStatus).also {
                        addTextChangedListener {

                            viewModel.memberMaritalStatus = it.toString()

                        }
                    }
                )
            }
            val eduLevel = resources.getStringArray(R.array.level_of_education)
            (actLevelOfEducation as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, eduLevel).also {
                        addTextChangedListener {

                            viewModel.memberEducational = it.toString()

                        }
                    }
                )
            }
            val memberSchoolAttendance = resources.getStringArray(R.array.school_attendance)
            (actMemberSchoolAttendance as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, memberSchoolAttendance).also {
                        addTextChangedListener {

                            viewModel.memberSchoolAttendance = it.toString()
                            viewModel.memberHasBlankFields()
                        }
                    }
                )
            }
            val memberDisability = resources.getStringArray(R.array.disability)
            (actMemberDisability as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, memberDisability).also {
                        addTextChangedListener {

                            viewModel.memberDisability = it.toString()

                        }
                    }
                )
            }
            val socioProfessionalCategory = resources.getStringArray(R.array.socio_professional_category)
            (actSocioProfessionalCategory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, socioProfessionalCategory).also {
                        addTextChangedListener {
                            viewModel.memberSocioProfessionalCategory = it.toString()

                        }
                    }
                )
            }
            val occupation = resources.getStringArray(R.array.occupation)
            (actOccupation as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, occupation).also {
                        addTextChangedListener {
                            viewModel.memberOccupation = it.toString()

                        }
                    }
                )
            }




            rgMemberAgeKnown.check(rbYesAge.id)
            rgMemberDobKnown.check(rbYesDob.id)


            /* when (viewModel.isMemberRespondent) {
                  rbYesIsRespondent.text -> rgIsMemberRespondent.check(rbYesIsRespondent.id)
                  rbNoIsRespondent.text -> rgIsMemberRespondent.check(rbNoIsRespondent.id)
              }
              when (viewModel.isMemberHead) {
                  rbYesIsHead.text -> rgIsMemberHead.check(rbYesIsHead.id)
                  rbNoIsHead.text -> rgIsMemberHead.check(rbNoIsHead.id)
              }*/




            btnFinish.setOnClickListener {
                viewModel.createMember()
                viewModel.clearMemberFields()
                findNavController().popBackStack()
            }


        }
        // set up menu

        viewModel.memberHasBlankFields()
    }
}