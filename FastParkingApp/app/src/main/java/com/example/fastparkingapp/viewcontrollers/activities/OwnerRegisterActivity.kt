package com.example.fastparkingapp.viewcontrollers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.fastparkingapp.R
import com.example.fastparkingapp.networking.FastParkingApi
import org.json.JSONObject


class OwnerRegisterActivity : AppCompatActivity() {

    private lateinit var companyNameTextView:TextView
    private lateinit var addressTextView:TextView
    private lateinit var rucTextView:TextView
    private lateinit var slotQuantity:TextView
    private lateinit var descriptionTextView:TextView
    private lateinit var confirmButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_register)
        val intent = getIntent()
        val jsonObject = JSONObject(intent.getStringExtra("jsonObject"))
        val username:String = jsonObject.getString("email")
        val password:String = jsonObject.getString("password")
        val firstName:String = jsonObject.getString("firstName")
        val lastName:String = jsonObject.getString("lastName")
        val dni:String = jsonObject.getString("dni")
        jsonObject.put("birthday","1996-05-25")

        companyNameTextView = findViewById(R.id.companyNameEditText)
        addressTextView = findViewById(R.id.addressNameEditText)
        rucTextView = findViewById(R.id.rucEditText)
        slotQuantity = findViewById(R.id.slotsQuantityEditText)
        descriptionTextView = findViewById(R.id.descriptionEditText)
        confirmButton = findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener{
            val ownerJsonObject = JSONObject()
            ownerJsonObject.put("fullName",companyNameTextView.text)
            ownerJsonObject.put("address",addressTextView.text)
            ownerJsonObject.put("ruc",rucTextView.text)
            ownerJsonObject.put("slotsQuantity",slotQuantity.text)
            ownerJsonObject.put("description",descriptionTextView.text)
            ownerJsonObject.put("email",username)
            ownerJsonObject.put("password",password)

            Log.d("FastParking",ownerJsonObject.toString())
            AndroidNetworking.post(FastParkingApi.getOwnersEndPoint)
                .addJSONObjectBody(ownerJsonObject)
                .setPriority(Priority.MEDIUM)
                .setTag("FastParking")
                .build()
                .getAsJSONObject(object :JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d("FastParking","On response exitoso -> ${response.toString()}")
                        saveCustomer(it,jsonObject)
                        val intent = Intent(it.context,LoginActivity::class.java)
                        it.context.startActivity(intent)
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("FastParking","Error failure -> ${anError?.message.toString()}")
                    }
                })
        }
    }


    fun saveCustomer(it:View,customerJsonObject:JSONObject){
        AndroidNetworking.post(FastParkingApi.postCustomer)
            .addJSONObjectBody(customerJsonObject)
            .setPriority(Priority.MEDIUM)
            .setTag("FastParking")
            .build()
            .getAsJSONObject(object:JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    Log.d("FastParking","On response exitoso -> ${response.toString()}")
                }

                override fun onError(anError: ANError?) {
                    Log.d("FastParking","Error failure -> ${anError?.message.toString()}")
                }

            })
    }

}
