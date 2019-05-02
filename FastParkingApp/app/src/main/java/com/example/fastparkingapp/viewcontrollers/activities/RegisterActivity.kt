package com.example.fastparkingapp.viewcontrollers.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Customer
import com.example.fastparkingapp.models.CustomerResponse
import com.example.fastparkingapp.networking.FastParkingApi
import com.google.gson.JsonObject

import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject


class RegisterActivity : AppCompatActivity() {

    lateinit var firstNameTextView: TextView
    lateinit var lastNameTextView: TextView
    lateinit var dniTextView:TextView
    lateinit var usernameTextView: TextView
    lateinit var passwordTextView: TextView
    lateinit var ownerRegisterTextView:TextView
    lateinit var registerButton:Button
    val customerJson = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firstNameTextView = findViewById(R.id.firstNameEditText)
        lastNameTextView = findViewById(R.id.lastNameEditText)
        dniTextView = findViewById(R.id.dniEditText)
        usernameTextView = findViewById(R.id.usernameEditText)
        passwordTextView = findViewById(R.id.passwordEditText)
        ownerRegisterTextView = findViewById(R.id.ownerRegisterTextView)
        registerButton = findViewById(R.id.registerButton)

        ownerRegisterTextView.setOnClickListener{

            customerJson.put("firstName",firstNameTextView.text)
            customerJson.put("lastName",lastNameTextView.text)
            customerJson.put("dni",dniTextView.text)
            customerJson.put("email",usernameTextView.text)
            customerJson.put("password",passwordTextView.text)
            customerJson.put("fullName","${firstNameTextView.text} ${lastNameTextView.text}")

            val intent = Intent(it.context,OwnerRegisterActivity::class.java)
            it.context.startActivity(intent.putExtra("jsonObject",customerJson.toString()))
        }

        registerButton.setOnClickListener{

            customerJson.put("firstName",firstNameTextView.text)
            customerJson.put("lastName",lastNameTextView.text)
            customerJson.put("dni",dniTextView.text)
            customerJson.put("email",usernameTextView.text)
            customerJson.put("password",passwordTextView.text)
            customerJson.put("fullName","${firstNameTextView.text} ${lastNameTextView.text}")
            customerJson.put("birthday","1996-05-25")

            Log.d("FastParking",customerJson.toString())

            AndroidNetworking.post(FastParkingApi.postCustomer)
                .addJSONObjectBody(customerJson)
                .setPriority(Priority.MEDIUM)
                .setTag("FastParking")
                .build()
                .getAsJSONObject(object:JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.d("FastParking","On response exitoso -> ${response.toString()}")
                        val intent = Intent(it.context,LoginActivity::class.java)
                        it.context.startActivity(intent)
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("FastParking","Error failure -> ${anError?.message.toString()}")
                    }

                })
        }
    }

}
