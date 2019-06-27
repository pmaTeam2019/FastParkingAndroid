package com.example.fastparkingapp.viewcontrollers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.fastparkingapp.R

class OnboardingAdapter: PagerAdapter {

    lateinit var context : Context
    lateinit var layoutInflater:LayoutInflater
    val slideImages : IntArray = intArrayOf(
        R.drawable.garage,
        R.drawable.shield,
        R.drawable.payment
    )
    val slideHeading: Array<String> = arrayOf(
        "SEARCH FAST AND SIMPLE",
        "SECURITY BEFORE EVERYTHING",
        "PAY FAST AND EASY"
    )
    val slideDescription: Array<String> = arrayOf(
        " Find your parking space at any time in a quick and easy way",
        "FastParking has security standards so you can search and publish parking spaces with total confidence",
        "Pay online with any available credit card. FastParking will make the payment to the owners of parking quickly and safely."
    )

    constructor(context:Context){
        this.context = context
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return slideHeading.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pos = position

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.item_onboarding,container,false) as ViewGroup
        val iconImageView =layout.findViewById<ImageView>(R.id.iconImageView)
        val headerTextView = layout.findViewById<TextView>(R.id.headerCard)
        val descriptionTextView = layout.findViewById<TextView>(R.id.descriptionCard)
        //val sliceButton = layout.findViewById<Button>(R.id.sliceButton)
        headerTextView.text =slideHeading[pos]
        descriptionTextView.text = slideDescription[pos]
        iconImageView.setImageResource(slideImages[pos])
        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}