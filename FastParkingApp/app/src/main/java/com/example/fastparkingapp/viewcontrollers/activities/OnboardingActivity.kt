package com.example.fastparkingapp.viewcontrollers.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.example.fastparkingapp.R
import com.example.fastparkingapp.ViewPagerListener
import com.example.fastparkingapp.viewcontrollers.adapters.OnboardingAdapter
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnboardingActivity : AppCompatActivity() {

    lateinit var onboardingViewPager: ViewPager
    lateinit var linearLayout: LinearLayout
    lateinit var onboardingAdapter: OnboardingAdapter
    var indicators: MutableList<TextView> = mutableListOf<TextView>()
    lateinit var NextButton : Button
    var currentPage : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        NextButton = findViewById(R.id.NextButton)
        onboardingViewPager =findViewById(R.id.onboardingViewPager)
        onboardingAdapter = OnboardingAdapter(this)
        onboardingViewPager.adapter = onboardingAdapter
        linearLayout = findViewById(R.id.contentNavigation)
        val view:View = LayoutInflater.from(applicationContext).inflate(R.layout.item_onboarding,onboardingLayoutActivity,false)
        //sliceButton = view.findViewById<Button>(R.id.sliceButton)
        //Log.d("sda",sliceButton.text.toString())
        addIndicator(0)
        onboardingViewPager.addOnPageChangeListener(ViewPagerListener(this::addIndicator))
        NextButton.setOnClickListener{
            if(NextButton.text == "GET STARTED"){
                val intent = Intent(it.context,MainActivity::class.java)
                it.context.startActivity(intent)
            }
            onboardingViewPager.setCurrentItem(currentPage + 1)
        }
    }

    fun addIndicator(position: Int){

        linearLayout.removeAllViews()
        indicators.clear()
        currentPage = position
        for (i in 0..2) {

            indicators.add(TextView(this))
            indicators[i].text = "•"
            indicators[i].textSize = 30.00f
            indicators[i].setTextColor(Color.parseColor("#cccccc"))
            linearLayout.addView(indicators[i])
        }

        currentPage = position
        if(currentPage == (indicators.size - 1)){
            NextButton.text = "GET STARTED"
        }else{
            NextButton.text = "NEXT"
        }
    }
}
