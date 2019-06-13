package com.example.fastparkingapp.viewcontrollers.activities

import android.app.DownloadManager
import android.content.ComponentCallbacks
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.NetworkRequest
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.androidnetworking.widget.ANImageView
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.github.florent37.expansionpanel.ExpansionLayout
import com.google.android.gms.location.*

import kotlinx.android.synthetic.main.activity_parking.*
import kotlinx.android.synthetic.main.content_parking.*
import java.util.jar.Manifest




class ParkingActivity : AppCompatActivity() {
    private lateinit var  parking:Owner
    private lateinit var  pricesExpansionLayout:ExpansionLayout
    private lateinit var  pictureImageView: ANImageView
    private lateinit var addressSection: ConstraintLayout
    private lateinit var reserveButton: Button

    //Location
    private lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var location:Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val REQUEST_CODE = 1000

        parking = Owner.from(intent.extras)
        pictureImageView = findViewById(R.id.pictureStaticMap)
        addressSection = findViewById(R.id.contentAddress)
        reserveButton = findViewById(R.id.reservationButton)


        addressTextView.text = parking.address
        descriptionTextView.text = parking.description
        fullNameTextView.text = parking.fullName
        slotTextView.text = "Hay ${parking.slotsQuantity} estacionamientos disponibles"
        distanceTextView.text = "Del estacionamieto a tu destino son ${parking.distance}"
        scheduleTextView.text = "Lun a Dom: 8.00 a 22:00 hrs."
        with(pictureImageView){
            setImageUrl(parking.imageUrl)
            setDefaultImageResId(R.drawable.ic_launcher_background)
            setErrorImageResId(R.drawable.ic_launcher_background)
        }
        addressSection.setOnClickListener{
            val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=-12.104693,-76.963387&daddr=${parking.latitude},${parking.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        reserveButton.setOnClickListener{
            val intent = Intent(it.context,ReservationActivity::class.java)
            it.context.startActivity(intent.putExtras(parking.toBundle()))
        }
    }

    fun CheckPermission(RequestCode:Int){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),RequestCode)
        }else{
            Log.d("FastParking","Hola")
            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

            //set event
            addressSection.setOnClickListener(View.OnClickListener {

                buildLocationRequest()
                buildLocationCallBack()

                Log.d("FastParking","Hola")
                if(ActivityCompat.checkSelfPermission(this@ParkingActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this@ParkingActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    Log.d("FastParking","Hola")
                    ActivityCompat.requestPermissions(this@ParkingActivity, arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION),RequestCode)
                    return@OnClickListener
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
                Log.d("FastParking",location.longitude.toString())

                val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=-12.104693,-76.963387&daddr=-12.065298,-77.044246")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            })
        }
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                location = p0!!.locations.get(p0!!.locations.size - 1)
                Log.d("FastParking",location.toString())
            }
        }

    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }

}
