package com.example.fastparkingapp

import androidx.viewpager.widget.ViewPager

class ViewPagerListener(private val closure: (Int)-> Unit) : ViewPager.OnPageChangeListener{
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) = closure(position)

}