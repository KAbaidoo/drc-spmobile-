package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.*

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentDevelopmentalFormBinding
import io.bewsys.spmobile.ui.households.forms.pages.*
import org.koin.androidx.viewmodel.ext.android.activityViewModel

private const val NUM_PAGES = 5

class DevelopmentalFormFragment : Fragment(R.layout.fragment_developmental_form) {
    private lateinit var viewPager: ViewPager2

    private val sharedViewModel: SharedDevelopmentalFormViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDevelopmentalFormBinding.bind(view)

//TODO add menu to clear form

        binding.apply {

            viewPager = pager
            val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
            viewPager.adapter = pagerAdapter

            TabLayoutMediator(tabLayout,viewPager){
                tab, position ->
            }.attach()



            requireActivity().onBackPressedDispatcher.addCallback(object :
                OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (pager.currentItem == 0) {
                        remove()
                        findNavController().popBackStack()

                    } else {
                        pager.currentItem = pager.currentItem - 1
                    }
                }
            })
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.addDevelopmentalHouseholdEvent.collect { event ->
                when (event) {
                    is SharedDevelopmentalFormViewModel.AddDevelopmentalHouseholdEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is SharedDevelopmentalFormViewModel.AddDevelopmentalHouseholdEvent.NavigateBackWithResults -> {

                        setFragmentResult(
                            "add_household_request",
                            bundleOf("add_household_result" to event.results)
                        )
                        sharedViewModel.clearEntries()
                        findNavController().popBackStack()
                    }
                }

            }

        }

    }



    private inner class ScreenSlidePagerAdapter(
        fragmentActivity: FragmentActivity
    ) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = NUM_PAGES


        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> FormStepOneFragment()
            1 -> FormStepTwoFragment()
            2 -> FormStepThreeFragment()
            3 -> FormStepFourFragment()
            else -> FormStepFiveFragment()
        }


    }
}
/*
private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f


class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        view.apply {
            val pageWidth = width
            val pageHeight = height
            when {
                position < -1 -> { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    alpha = 0f
                }
                position <= 1 -> { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                    val vertMargin = pageHeight * (1 - scaleFactor) / 2
                    val horzMargin = pageWidth * (1 - scaleFactor) / 2
                    translationX = if (position < 0) {
                        horzMargin - vertMargin / 2
                    } else {
                        horzMargin + vertMargin / 2
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade the page relative to its size.
                    alpha = (MIN_ALPHA +
                            (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                }
                else -> { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    alpha = 0f
                }
            }
        }
    }
}
 */