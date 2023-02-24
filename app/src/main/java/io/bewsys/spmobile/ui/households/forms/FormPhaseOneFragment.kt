package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneRespondentBinding
import io.bewsys.spmobile.ui.views.CustomQuestionViews
import io.bewsys.spmobile.ui.views.EditTextQuestion
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class FormPhaseOneFragment : Fragment(R.layout.fragment_add_household_one_respondent) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel:DevelopmentalFormViewModel by activityViewModels()

        val binding = FragmentAddHouseholdOneRespondentBinding.bind(view)


        binding.apply {

            linearLayout.children.forEach {
                val v = it as CustomQuestionViews
                v.addTextChangedListener {
                    if (it != null) {
                        sharedViewModel.printResults(v.title,it)
                    }
                }

            }


        }


    }

}