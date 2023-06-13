package io.bewsys.spmobile.ui.households.form

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdSevenReview1Binding
import org.koin.androidx.navigation.koinNavGraphViewModel

class FormStepSevenFragment : Fragment(R.layout.fragment_add_household_seven_review1) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddHouseholdSevenReview1Binding.bind(view)

        binding.apply {
            viewModel.apply {
                tvAreaOfResidence.append(": $placeOfResidence")
                tvProvince.append(": $province")
                tvTerritory.append(": $territory")
                tvCommunity.append(": $community")
                tvGroupment.append(": $groupment")
                tvHealthArea.append(": $healthArea")
                tvHealthZone.append(": $healthZone")
                tvVillageOrQuartier.append(": $villageOrDistrict")
                tvAddress.append(": $address")
                tvCac.append(": $cac")
                tvGpsLon.append(": $lon")
                tvGpsLat.append(": $lat")

                tvHeadFirstName.append(": $headFirstName")
                tvHeadMiddleName.append(": $headMiddleName")
                tvHeadLastName.append(": $headLastName")
                tvHeadAge.append(": $headAge")
                tvHeadSex.append(": $headSex")
                tvHeadVoterId.append(": $headVoterId")
                tvHeadPhoneNumber.append(": $headPhoneNo")
                tvRespondentFirstname.append(": $respondentFirstName")
                tvRespondentMiddlename.append(": $respondentMiddleName")
                tvRespondentLastname.append(": $respondentLastName")
                tvFamilyBondToHead.append(": $respondentFamilyBondToHead")
                tvRespondentVoterId.append(": $respondentVoterId")
                tvRespondentPhoneNumber.append(": $respondentPhoneNo")
                tvFamilyBondToHead.append(": $respondentFamilyBondToHead")
                tvMigrationStatus.append(": $migrationStatus")
                val duration =
                    if (durationDisplacedReturnedRepatriatedRefugee.isBlank()) "N/A" else "$durationDisplacedReturnedRepatriatedRefugee $unitOfMigrationDuration"
                tvDurationDisplacedReturnedRepatriatedRefugee.append(": $duration")

                tvOtherOccupancyStatus.append(": $occupancyStatus")
                tvExteriorWalls.append(": $exteriorWalls")
                tvSoilMaterial.append(": $soilMaterial")
                tvNumberOfRoomsUsedForSleeping.append(": $roomsUsedForSleeping")
                tvFuelForCooking.append(": $cookingFuel")









                memberList.forEachIndexed { index, member ->
                    val tv = TextView(context)
                    tv.setPadding(8)
                    tv.text = "${index + 1}. ${member.firstname}  ${member.lastname}"
                    llListOfMembers.addView(tv)
                }


            }
            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepEightFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionIVulnerabilityFragment, bundle)

            }

        }


    }


}