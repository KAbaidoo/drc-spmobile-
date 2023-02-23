package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneRespondentBinding
import io.bewsys.spmobile.databinding.FragmentAddHouseholdTwoHeadBinding
import java.text.SimpleDateFormat
import java.util.*

class FormPhaseTwoFragment : Fragment(R.layout.fragment_add_household_two_head) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdTwoHeadBinding.bind(view)

        val datePicker = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText("Select date of birth")
            .build()

        binding.apply {
            textViewDob.answerView.editText?.setOnClickListener {
                datePicker.show(parentFragmentManager, "DATE_PICKER")
                datePicker.addOnPositiveButtonClickListener {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = sdf.format(it)
                    textViewDob.answer = date

                }
            }
        }


    }
}