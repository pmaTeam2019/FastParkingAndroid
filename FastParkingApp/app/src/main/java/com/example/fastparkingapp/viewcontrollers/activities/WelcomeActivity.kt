package com.example.fastparkingapp.viewcontrollers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.example.fastparkingapp.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        loginButton.setOnClickListener{
            val intent = Intent(it.context, LoginActivity::class.java)
            it.context.startActivity(intent)
        }
        signInButton.setOnClickListener{
            val intent = Intent(it.context, RegisterActivity::class.java)
            it.context.startActivity(intent)
        }
    }
}
