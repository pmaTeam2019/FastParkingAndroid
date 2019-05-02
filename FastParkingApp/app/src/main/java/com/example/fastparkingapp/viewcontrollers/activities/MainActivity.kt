package com.example.fastparkingapp.viewcontrollers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.fastparkingapp.BottomNavigationViewHelper
import com.example.fastparkingapp.R
import com.example.fastparkingapp.viewcontrollers.fragments.MapFragment
import com.example.fastparkingapp.viewcontrollers.fragments.HomeFragment
import com.example.fastparkingapp.viewcontrollers.fragments.ReservationFragment
import com.example.fastparkingapp.viewcontrollers.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedFragment = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedFragment)
        navigateTo(navigation.menu.findItem(R.id.navigation_home))
        BottomNavigationViewHelper.removeShiftMode(findViewById(R.id.navigation))
    }

    fun getFragmentFor(item:MenuItem):Fragment{
        when(item.itemId){
            R.id.navigation_home ->{
                return HomeFragment()
            }
            R.id.navigation_maps ->{
                return MapFragment()
            }
            R.id.navigation_reservations ->{
                return ReservationFragment()
            }
            R.id.navigation_settings ->{
                return SettingsFragment()
            }
        }
        return HomeFragment()
    }

    fun navigateTo(item:MenuItem):Boolean{
        item.isChecked = true
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.content,getFragmentFor((item)))
            .commit() > 0
    }

}
