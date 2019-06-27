package com.example.fastparkingapp.viewcontrollers.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.fastparkingapp.R
import com.example.fastparkingapp.networking.FastParkingApi
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.json.JSONObject

class CustomInfoWindowAdapter: GoogleMap.InfoWindowAdapter{

    private lateinit var mWindow:View
    private lateinit var mContext:Context

    constructor(context:Context){
        mContext = context
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_layout,null)
    }

    private fun rendowWindowText(marker:Marker,view:View){
        var priceTextView:TextView = view.findViewById(R.id.priceTextView)
        var ratingRating:RatingBar = view.findViewById(R.id.calificationRatingBar)
        Log.d("CustomInfo", "OwnerId: ${marker.zIndex}")
        AndroidNetworking.get(FastParkingApi.getOwnerEndPoint(marker.zIndex.toInt()))
            .setPriority(Priority.LOW)
            .setTag("FastParking")
            .build()
            .getAsJSONObject(object :JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    priceTextView.text = response?.getJSONObject("owner")?.getString("fullName")
                    Log.d("CustomInfo","FullName: ${priceTextView.text}")
                    ratingRating.rating = response?.getJSONObject("owner")?.getString("rating")!!.toFloat()
                    Log.d("CustomInfo","response: ${response.toString()}")
                }

                override fun onError(anError: ANError?) {
                    Log.d("CustomInfo","Error: ${anError?.message}")
                }
            })

        ///var priceTextView:TextView = view.findViewById(R.id.priceTextView)
        ///var ratingRating:RatingBar = view.findViewById(R.id.calificationRatingBar)
        ///ratingRating.rating = 2.2F
    }
    override fun getInfoContents(p0: Marker?): View? {
        rendowWindowText(p0!!,mWindow)
        return mWindow
    }

    override fun getInfoWindow(p0: Marker?): View? {
        rendowWindowText(p0!!,mWindow)
        return mWindow
    }

}