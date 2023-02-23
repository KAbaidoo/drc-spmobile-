package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneRespondentBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class FormPhaseOneFragment : Fragment(R.layout.fragment_add_household_one_respondent) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdOneRespondentBinding.bind(view)

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