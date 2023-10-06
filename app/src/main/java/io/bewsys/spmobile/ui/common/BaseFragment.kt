package io.bewsys.spmobile.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.ui.MainActivity


abstract class BaseFragment<VB : ViewBinding>(private val inflateMethod: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    Fragment() {

    private var _binding: VB? = null

    // This can be accessed by the child fragments
    // Only valid between onCreateView and onDestroyView
    val binding: VB get() = _binding!!

    // Make it open, so it can be overridden in child fragments
    abstract fun VB.initialize()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateMethod.invoke(inflater, container, false)

        // replaced _binding!! with binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Calling the extension function
        binding.initialize()
    }

    open fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    open fun navigateTo(destination: Int) {
        findNavController().navigate(destination)
    }

    open fun navigateTo(action: NavDirections) {
        findNavController().navigate(action)
    }

    open fun navigateTo(destinationId: Int, bundle: Bundle) {
        findNavController().navigate(destinationId, bundle)
    }

    open fun navigateBack() {
        findNavController().popBackStack()
    }

    open fun showProgressBar(visible: Boolean) {
        activity?.let {
            (it as MainActivity).showProgressBar(visible)
        }
    }

    open fun hideKeyboard() {
        activity?.let {
            (it as MainActivity).hideSoftKeyboard()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}