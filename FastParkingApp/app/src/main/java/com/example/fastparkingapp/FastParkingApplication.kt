package com.example.fastparkingapp

import android.app.Application
import com.androidnetworking.AndroidNetworking

class FastParkingApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
    }
}