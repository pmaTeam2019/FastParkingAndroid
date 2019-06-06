package com.example.fastparkingapp.viewcontrollers.fragments


import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.directions.route.*

import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.example.fastparkingapp.models.OwnerResponse
import com.example.fastparkingapp.networking.FastParkingApi
import com.example.fastparkingapp.viewcontrollers.activities.MainActivity
import com.example.fastparkingapp.viewcontrollers.activities.OwnerRegisterActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MapFragment : Fragment(),OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation : Location
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var currentLatLng: LatLng
    private var owners = ArrayList<Owner>()
    private var polylines =  ArrayList<Polyline>()
    private lateinit var currentLatLong:LatLng

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        currentLatLng = LatLng(0.0,0.0)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapViewFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)
        return view
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
        getOwners()
        showMarkers(owners)
        mMap.setOnMarkerClickListener(this)
    }


    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("FastLocation",p0?.position.toString())
        showRoute(p0.position)
        return true
    }


    private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this.context!!,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            //return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener{
            if(it != null){
                lastLocation = it
                currentLatLong = LatLng(it.latitude,it.longitude)
                Log.d("FastLocation",currentLatLong.toString())
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,13f))
            }
        }
    }

    private fun getOwners(){
        AndroidNetworking.get(FastParkingApi.getOwnersEndPoint)
            .setPriority((Priority.LOW))
            .build()
            .getAsObject(OwnerResponse::class.java, object : ParsedRequestListener<OwnerResponse>{
                override fun onResponse(response: OwnerResponse?) {
                    if(response?.status == "ok"){
                        owners = response.owners
                        showMarkers(owners)
                        Log.d("FastParking",owners.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("FastParking", "Error: ${anError?.message.toString()}")
                }
            })
    }

    private fun showMarkers(owners:ArrayList<Owner>){
        for(item in owners){
            val ownerLatLong = LatLng(item.latitude,item.longitude)
            Log.d("LATLONG",ownerLatLong.toString())
            mMap.addMarker(MarkerOptions().position(ownerLatLong).title(item.fullName).icon(BitmapDescriptorFactory
                .fromResource(R.drawable.parking_marker)).zIndex(item.id.toFloat()))
        }
    }


    private fun showRoute(markerPosition:LatLng){
        val path:MutableList<List<LatLng>> = ArrayList()
        val origin:String = "origin=-12.103824,-76.9626864"
        val destination:String = "destination=${markerPosition.latitude},${markerPosition.longitude}"
        val mode: String = "Driving"
        val key:String = "AIzaSyBUZyqXsOEXWGzYxpJpICi-vVy5vVOIM3A"
        val urlDirections = "https://maps.googleapis.com/maps/api/directions/json?$origin&$destination&mode=$mode&key=$key"

        Log.d("FastParkig","URL:$urlDirections")
        AndroidNetworking.get(urlDirections)
            .setTag("FastParking")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object :JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    Log.d("FastParking","Success: ${response.toString()}")
                    val routes = response?.getJSONArray("routes")
                    val legs = routes?.getJSONObject(0)?.getJSONArray("legs")
                    val steps = legs?.getJSONObject(0)?.getJSONArray("steps")
                    for (i in 0 until steps!!.length()){
                    val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                        path.add(PolyUtil.decode(points))
                    }
                    for (i in 0 until path.size){
                        mMap.addPolyline(PolylineOptions().addAll(path[i]).color(Color.BLUE))
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.d("FastParking","Error: ${anError?.message}")
                }

            })

    }

}
