package io.bewsys.spmobile.ui.households.forms.pages

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdTwoHeadBinding
import io.bewsys.spmobile.ui.households.forms.SharedDevelopmentalFormViewModel
import io.bewsys.spmobile.ui.views.CustomQuestionViews
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormPhaseTwoFragment : Fragment(R.layout.fragment_add_household_two_head) {

    private val sharedViewModel: SharedDevelopmentalFormViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdTwoHeadBinding.bind(view)
        binding.apply {

            linearLayout.children.forEach { view ->
                val v = view as CustomQuestionViews

                v.answer = sharedViewModel.getEntry(v.title)

                v.addTextChangedListener {
                    if (it != null) {
                        sharedViewModel.saveEntry(v.title, it)
                    }
                }

            }


        }


    }
}