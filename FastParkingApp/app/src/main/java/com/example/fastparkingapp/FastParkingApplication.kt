package com.example.fastparkingapp

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.parse.Parse

class FastParkingApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build())
    }
}