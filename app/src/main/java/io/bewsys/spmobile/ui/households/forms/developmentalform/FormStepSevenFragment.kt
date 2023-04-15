package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdSevenReview1Binding
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepSevenFragment : Fragment(R.layout.fragment_add_household_seven_review1) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddHouseholdSevenReview1Binding.bind(view)

        binding.apply {
            viewModel.apply {

                tvInitialRegistrationType.append(": $initialRegistrationType")
                tvRespondentFirstname.append(": $respondentFirstName")
                tvRespondentMiddlename.append(": $respondentMiddleName")
                tvRespondentLastname.append(": $respondentLastName")
                tvFamilyBondToHead.append(": $respondentFamilyBondToHead")
                tvResponentAgeKnown.append(": $respondentAgeKnown")
                tvRespondentAge.append(": $respondentAge")
                tvRespondentDobKnown.append(": $respondentDOBKnown")
                tvRespondentDob.append(": $respondentDOB")
                tvRespondentSex.append(": $respondentSex")

                tvRespondentVoterId.append(": $respondentVoterId")
                tvRespondentPhoneNumber.append(": $respondentPhoneNo")
                tvProvince.append(": $province")
                tvTerritory.append(": $territory")
                tvCommunity.append(": $community")
                tvGroupment.append(": $groupment")
                tvHealthArea.append(": $healthArea")
                tvHealthZone.append(": $healthZone")

                tvAddress.append(": $address")
                tvVillageOrQuartier.append(": $villageOrQuartier")
                tvTerritoryOrTown.append(": $territoryOrTown")
                tvAreaOfResidence.append(": $respondentFirstName")
                tvGpsLon.append(": $lon")
                tvGpsLat.append(": $lat")
                tvIsHeadRespondent.append(": $headIsRespondent")
                tvHeadFirstName.append(": $headFirstName")
                tvHeadMiddleName.append(": $headMiddleName")
                tvHeadLastName.append(": $headLastName")
                tvHeadDobKnown.append(": $headDOBKnown")
                tvHeadDob.append(": $headDOB")
                tvHeadAgeKnown.append(": $headAgeKnown")
                tvHeadAge.append(": $headAge")
                tvHeadSex.append(": $headSex")
                tvHeadPregnancyStatus.append(": $headPregnancyStatus")
                tvMaritalStatus.append(": $headMaritalStatus")
                tvHeadVoterId.append(": $headVoterId")
                tvHeadPhoneNumber.append(": $headPhoneNo")
                tvHeadBirthCertificate.append(": $headBirthCert")
                tvHeadEducationalLevel.append(": $headEduLevel")
                tvHeadSocioProfessionalCategory.append(": $headSocioProfessionalCategory")
                tvHeadSchoolAttendence.append(": $headSchoolAttendance")
                tvHeadSectorOfWork.append(": $headSectorOfWork")
                tvHeadDisability.append(": $headDisability")
                tvIsIncomeRegular.append(": $isIncomeRegular")
                tvBankAccountOrBankCard.append(": $bankCardOrAccount")
                tvBenefitFromSocialAssistanceProgram.append(": $benefitFromSocialAssistanceProgram")
                tvSocialAssistanceProgram.append(": $nameOfSocialAssistanceProgram")
                tvMigrationStatus.append(": $migrationStatus")
                val otherMigration = if (otherMigrationStatus.isBlank()) "N/A" else otherMigrationStatus
                tvOtherMigrationStatus.append(": $otherMigration")
                val duration =if(durationDisplacedReturnedRepatriatedRefugee.isBlank() ) "N/A" else "$durationDisplacedReturnedRepatriatedRefugee $unitOfMigrationDuration"
                tvDurationDisplacedReturnedRepatriatedRefugee.append(": $duration")
                tvMonthlyIncome.append(": $householdMonthlyIncome")
                tvMonthlyIncomeNeeded.append(": $minimumMonthlyIncomeNecessaryLiveWithoutDifficulties")
                tvMobileMoneyUsername.append(": $mobileMoneyUsername")
                tvMobileMoneyNumber.append(": $mobileMoneyPhoneNumber")
            }
            val title = if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)
            btnNext.setOnClickListener {
                val action =
                    FormStepSevenFragmentDirections.actionFormStepSevenFragmentToFormStepEightFragment(
                        title = title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
            }
            btnPrevious.setOnClickListener {
                val action =
                    FormStepSevenFragmentDirections.actionFormStepSevenFragmentToFormStepSixFragment2(
                        title = title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
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