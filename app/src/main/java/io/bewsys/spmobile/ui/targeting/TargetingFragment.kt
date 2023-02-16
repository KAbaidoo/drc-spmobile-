package io.bewsys.spmobile.ui.targeting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentTargetingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class TargetingFragment : Fragment(R.layout.fragment_targeting) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTargetingBinding.bind(view)
        val viewModel: TargetingViewModel by viewModel()

//        binding.apply {
//            editTextQuestion.apply {
//                answer="some answer"
//                addTextChangedListener {
//                    viewModel.printAnswer(answer)
//                }
//
//            }
//        }

    }


}