package io.bewsys.spmobile.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar


 abstract class BaseFragment<VB : ViewBinding>(private val inflateMethod: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    Fragment() {

    private var _binding: VB? = null

    // This can be accessed by the child fragments
    // Only valid between onCreateView and onDestroyView
    val binding: VB get() = _binding!!

    // Make it open, so it can be overridden in child fragments
    open fun VB.initialize() {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(inflater, container, false)

        // Calling the extension function
        binding.initialize()

        // replaced _binding!! with binding
        return binding.root
    }

    open fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    open fun navigateTo(destination: Int) {
        findNavController().navigate(destination)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}