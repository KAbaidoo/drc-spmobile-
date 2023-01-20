package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.view.View

import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentDevelopmentalFormBinding


class DevelopmentalFormFragment :Fragment (R.layout.fragment_developmental_form){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDevelopmentalFormBinding.bind(view)



    }
}