package com.example.fastparkingapp.viewcontrollers.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Login
import com.example.fastparkingapp.models.LoginResponse
import com.example.fastparkingapp.networking.FastParkingApi
import com.shashank.sony.fancytoastlib.FancyToast

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var login : Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        registerLink.setOnClickListener{
            val intent = Intent(it.context,RegisterActivity::class.java)
            it.context.startActivity(intent)
        }

        loginButton.setOnClickListener{
            login = Login(usernameEditText.text.toString(),passwordEditText.text.toString())
            Log.d("FastParking","username:${login.email},password:${login.password}")
            AndroidNetworking.post(FastParkingApi.authentication)
                .addBodyParameter(login)
                .setPriority(Priority.MEDIUM)
                .setTag("FastParking")
                .build()
                .getAsObject(LoginResponse::class.java,object:ParsedRequestListener<LoginResponse>{
                    override fun onResponse(response: LoginResponse?) {
                        if(response?.status == "ok"){
                            Log.d("FastParking","Login successful")
                            val intent = Intent(it.context,OnboardingActivity::class.java)
                            it.context.startActivity(intent)
                        }else{
                            Log.d("FastParking","Wrong Credentials")
                            FancyToast.makeText(it.context,"Wrong Credentials",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        Log.d("FastParking","Error:${anError?.message}")
                        FancyToast.makeText(it.context,"Wrong Credentials",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show()
                    }

                })
        }
    }
}
