package io.bewsys.spmobile.ui.households.forms

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentDevelopmentalFormBinding

private const val NUM_PAGES = 5

class DevelopmentalFormFragment : Fragment(R.layout.fragment_developmental_form) {
    private lateinit var viewPager: ViewPager2


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDevelopmentalFormBinding.bind(view)




        binding.apply {

            viewPager = pager
            val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
            viewPager.adapter = pagerAdapter
            pager.setPageTransformer(ZoomOutPageTransformer())

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

    }



    private inner class ScreenSlidePagerAdapter(
        fragmentActivity: FragmentActivity
    ) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = NUM_PAGES


        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> FormPhaseOneFragment()
            1 -> FormPhaseTwoFragment()
            2 -> FormPhaseThreeFragment()
            3 -> FormPhaseFourFragment()
            else -> FormPhaseFiveFragment()
        }


    }
}
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