package io.bewsys.spmobile.ui.households.forms.pages

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdTwoHeadBinding
import io.bewsys.spmobile.ui.households.forms.SharedDevelopmentalFormViewModel
import io.bewsys.spmobile.ui.customviews.CustomQuestionViews
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepTwoFragment : Fragment(R.layout.fragment_add_household_two_head) {
    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()
    private val territories = mutableListOf<String>()
    private val groupments = mutableListOf<String>()

    private val viewModel: SharedDevelopmentalFormViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.provinces.observe(viewLifecycleOwner) {
            provinces.clear()
            provinces.addAll(it)

        }
        viewModel.territories.observe(viewLifecycleOwner) {
            territories.clear()
            territories.addAll(it)
        }

        viewModel.communities.observe(viewLifecycleOwner) {
            communities.clear()
            communities.addAll(it)
        }

        viewModel.groupments.observe(viewLifecycleOwner) {
            groupments.clear()
            groupments.addAll(it)
        }

        val dropdownLayout = R.layout.dropdown_item

        val binding = FragmentAddHouseholdTwoHeadBinding.bind(view)
        binding.apply {

            textFieldProvince.editText?.setText(viewModel.province)
            textFieldCommunity.editText?.setText(viewModel.community)
            textFieldTerritory.editText?.setText(viewModel.territory)
            textFieldGroupment.editText?.setText(viewModel.groupment)


            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, provinces).also {
                        addTextChangedListener {
                            viewModel.province = it.toString()

                        }
                    }
                )
            }

            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, communities).also {
                        addTextChangedListener {
                            viewModel.community = it.toString()


                        }
                    }
                )
            }
            (autoCompleteTextViewTerritory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, territories)
                ).also {
                    addTextChangedListener {
                        viewModel.territory = it.toString()

                    }
                }
            }
            (autoCompleteTextGroupment as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, groupments)
                ).also {
                    addTextChangedListener {
                        viewModel.groupment = it.toString()
                    }
                }
            }


            linearLayout.children.forEach { view ->
                val v = view as CustomQuestionViews

                v.answer = viewModel.getEntry(v.title)

                v.addTextChangedListener {
                    if (it != null) {
                        viewModel.saveEntry(v.title, it)
                    }
                }

            }


        }


    }
}