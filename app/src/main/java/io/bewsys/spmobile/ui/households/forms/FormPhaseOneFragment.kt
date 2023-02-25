package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneRespondentBinding
import io.bewsys.spmobile.ui.views.CustomQuestionViews

class FormPhaseOneFragment : Fragment(R.layout.fragment_add_household_one_respondent) {
    private val sharedViewModel:SharedDevelopmentalFormViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdOneRespondentBinding.bind(view)

        binding.apply {

            linearLayout.children.forEach {view ->
                val v = view as CustomQuestionViews

               v.answer = sharedViewModel.getEntry(v.title)

                v.addTextChangedListener {
                    if (it != null) {
                        sharedViewModel.saveEntry(v.title,it)
                    }
                }

            }

        }


    }

}