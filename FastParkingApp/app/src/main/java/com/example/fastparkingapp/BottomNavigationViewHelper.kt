package com.example.fastparkingapp

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.view.menu.MenuView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationViewHelper{
    @SuppressLint("RestrictedApi")
    companion object {

        fun removeShiftMode(view: BottomNavigationView) {
            val menuView = view.getChildAt(0) as BottomNavigationMenuView
            try {
                val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
                shiftingMode.isAccessible = true
                shiftingMode.setBoolean(menuView, false)
                shiftingMode.isAccessible = false
                for (i in 0 until menuView.childCount) {
                    val item = menuView.getChildAt(i) as BottomNavigationItemView
                    item.setShifting(false)
                    item.setChecked(item.itemData.isChecked)
                }
            } catch (e: NoSuchFieldException) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift filed")
            } catch (e: IllegalAccessException) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value shift mode")
            }
        }
    }
}