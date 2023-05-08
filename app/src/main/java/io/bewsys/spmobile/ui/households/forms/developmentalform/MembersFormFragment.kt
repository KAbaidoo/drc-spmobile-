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
import io.bewsys.spmobile.databinding.FragmentAddHouseholdThreebMembersBinding
import io.bewsys.spmobile.databinding.FragmentMembersBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.*

class MembersFormFragment : Fragment(R.layout.fragment_members) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMembersBinding.bind(view)


        binding.apply {
            val tils = listOf(
                tilMemberFirstname,
                tilMemberMiddleName,
                tilMemberLastname,
                tilMemberAge,
                tilMemberDob,
                tilMemberVoterIdCard,
                tilMemberPhoneNumber,
                tilMemberBirthCertificate,
                tilMemberEducational,
                tilMemberSocioProfessionalCategory,
                tilMemberSchoolAttendance,
                tilMemberSectorOfWork,
                tilMemberDisability

            )

            rgIsMemberRespondent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesIsRespondent.id -> {
                        viewModel.isMemberRespondent = rbYesIsRespondent.text as String
                    }
                    else -> {
                        viewModel.isMemberRespondent = rbNoIsRespondent.text as String
                    }
                }
            }
            rgIsMemberHead.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesIsHead.id -> {
                        viewModel.isMemberHead = rbYesIsHead.text as String
                    }
                    else -> {
                        viewModel.isMemberHead = rbNoIsHead.text as String
                    }
                }
            }
            rgMemberAgeKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAge.id -> {
                        viewModel.memberAgeKnown = rbYesAge.text as String
                    }
                    else -> {
                        viewModel.memberAgeKnown = rbNoAge.text as String
                    }
                }
            }
            rgMemberDobKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesDob.id -> {
                        viewModel.memberDOBKnown = rbYesDob.text as String
                    }
                    else -> {
                        viewModel.memberDOBKnown = rbNoDob.text as String
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
            rgMemberSex.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMale.id -> {
                        viewModel.memberSex = rbMale.text as String
                    }
                    else -> {
                        viewModel.memberSex = rbFemale.text as String
                    }
                }
            }
            rgMemberMaritalStatus.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbSingle.id -> {
                        viewModel.memberMaritalStatus = rbSingle.text as String
                    }
                    rbMarried.id -> {
                        viewModel.memberMaritalStatus = rbMarried.text as String
                    }
                    rbDivorced.id -> {
                        viewModel.memberMaritalStatus = rbDivorced.text as String
                    }
                    else -> {
                        viewModel.memberMaritalStatus = rbWidowed.text as String
                    }
                }
            }
            tils.forEachIndexed { index, til ->
                til.editText?.addTextChangedListener {
                    viewModel.setMemberFields(index, it)
                    viewModel.memberHasBlankFields()
                }
            }

            /*  when (viewModel.isMemberRespondent) {
                  rbYesIsRespondent.text -> rgIsMemberRespondent.check(rbYesIsRespondent.id)
                  rbNoIsRespondent.text -> rgIsMemberRespondent.check(rbNoIsRespondent.id)
              }
              when (viewModel.isMemberHead) {
                  rbYesIsHead.text -> rgIsMemberHead.check(rbYesIsHead.id)
                  rbNoIsHead.text -> rgIsMemberHead.check(rbNoIsHead.id)
              }
              when (viewModel.memberAgeKnown) {
                  rbYesAge.text -> rgMemberAgeKnown.check(rbYesAge.id)
                  rbNoAge.text -> rgMemberAgeKnown.check(rbNoAge.id)
              }
              when (viewModel.memberDOBKnown) {
                  rbYesDob.text -> rgMemberDobKnown.check(rbYesDob.id)
                  rbNoDob.text -> rgMemberDobKnown.check(rbNoDob.id)
              }
              when (viewModel.memberPregnancyStatus) {
                  rbYesPregnant.text -> rgMemberPregnancyStatus.check(rbYesPregnant.id)
                  rbNoPregnant.text -> rgMemberPregnancyStatus.check(rbNoPregnant.id)
              }
              when (viewModel.memberSex) {
                  rbMale.text -> rgMemberSex.check(rbMale.id)
                  rbFemale.text -> rgMemberSex.check(rbFemale.id)
              }
              when (viewModel.memberMaritalStatus) {
                  rbMarried.text -> rgMemberMaritalStatus.check(rbMarried.id)
                  rbSingle.text -> rgMemberMaritalStatus.check(rbSingle.id)
                  rbDivorced.text -> rgMemberMaritalStatus.check(rbDivorced.id)
                  rbWidowed.text -> rgMemberMaritalStatus.check(rbWidowed.id)
              }*/

            //            set text fields
            /*     viewModel.apply {
                     tils.forEachIndexed { index, til ->
                         til.editText?.setText(memberFields[index])
                     }
                 }
      */


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnFinish.setOnClickListener {
                viewModel.createMember()
                findNavController().popBackStack()
            }
        }
        // set up menu

    }
}