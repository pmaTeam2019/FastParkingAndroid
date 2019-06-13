package com.example.fastparkingapp.viewcontrollers.fragments


import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener

import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.example.fastparkingapp.models.OwnerResponse
import com.example.fastparkingapp.networking.FastParkingApi
import com.example.fastparkingapp.viewcontrollers.adapters.ParkingsAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    private var parkings = ArrayList<Owner>()
    private lateinit var parkingsRecyclerView: RecyclerView
    private lateinit var parkingsAdapter: ParkingsAdapter
    private lateinit var parkingsLayoutManager: RecyclerView.LayoutManager


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation : Location
    private lateinit var currentLatLong:LatLng

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        currentLatLong = LatLng(0.0,0.0)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)

        Log.d("Prueba","${setUpMap().toString()}")

        parkingsAdapter = ParkingsAdapter(parkings,view.context)
        parkingsLayoutManager = LinearLayoutManager(view.context)
        parkingsRecyclerView = view.parkingsRecyclerView
        parkingsRecyclerView.adapter = parkingsAdapter
        parkingsRecyclerView.layoutManager = parkingsLayoutManager

        return view
    }


    private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this.context!!,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }

        fusedLocationClient.lastLocation.addOnSuccessListener{
            if(it != null){
                lastLocation = it
                currentLatLong = LatLng(it.latitude,it.longitude)
                Log.d("FastLocation",currentLatLong.toString())

                var getOwnersEndPoint = "${FastParkingApi.getOwnersEndPoint}?latitude=${currentLatLong.latitude}&longitude=${currentLatLong.longitude}"
                Log.d("FastParking","$getOwnersEndPoint")
                AndroidNetworking.get(getOwnersEndPoint)
                    .setPriority(Priority.LOW)
                    .setTag("FastParking")
                    .build()
                    .getAsObject(OwnerResponse::class.java, object : ParsedRequestListener<OwnerResponse> {
                        override fun onResponse(response: OwnerResponse?) {
                            parkings = response!!.owners
                            parkingsAdapter.parkins = parkings
                            parkingsAdapter.notifyDataSetChanged()
                            Log.d("FastParking",response.owners.toString())
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("FastParking", anError!!.message)
                        }
                    })


            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
