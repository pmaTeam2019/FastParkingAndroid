package com.example.fastparkingapp.viewcontrollers.activities

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.example.fastparkingapp.models.Reservation
import com.example.fastparkingapp.networking.FastParkingApi
import com.example.fastparkingapp.viewcontrollers.fragments.ReservationFragment
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.ParseInstallation
import com.stripe.android.Stripe
import com.stripe.android.TokenCallback
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget

import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject
import java.lang.Exception

class PaymentActivity : AppCompatActivity() {

    val PUBLISHABLE_KEY = "pk_test_tpnQiGTuoWAUVUugJqOpbdHr00cpXgjJjG"
    private var card: Card? = null
    private var progress: ProgressDialog? = null
    private lateinit var purchase: Button
    private lateinit var mCardInputWidget :CardInputWidget
    private lateinit var reservation: Reservation
    private lateinit var sharedPref : SharedPreferences
    val PREFS_FILENAME:String = "com.example.fastparkingapp.prefs"
    private lateinit var amountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        //setSupportActionBar(toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //ParseInstallation.getCurrentInstallation().saveInBackground()
        reservation = Reservation.from(intent.extras)
        amountTextView = findViewById(R.id.amountTextView)
        amountTextView.text = reservation.price.toString()
        mCardInputWidget  = findViewById(R.id.cardDetailsInputWidget)
        card = mCardInputWidget.card
        if(mCardInputWidget.card == null){
            Log.d("Payment","Card NUll Exception")
        }
        purchase = findViewById(R.id.payButton) as Button
        sharedPref = this.getSharedPreferences(PREFS_FILENAME,0)
        progress = ProgressDialog(this)
        purchase.setOnClickListener{
            buy()
        }

    }


    fun buy(){
        card = mCardInputWidget.card
        var validation:Boolean = card!!.validateCard()
        if(card!!.validateCard()){
            startProgress("Validating Credit Card")
            Stripe(this).createToken(
                card!!,
                PUBLISHABLE_KEY,
                object: TokenCallback {
                    override fun onSuccess(result: Token) {
                        finishProgress()
                        charge(result)
                    }

                    override fun onError(e: Exception) {
                        Toast.makeText(this@PaymentActivity,
                            "Stripe -" + e.toString(),
                            Toast.LENGTH_LONG).show()
                    }

                }
            )
        }else if(!card!!.validateNumber()){
            Toast.makeText(this@PaymentActivity,
                "Stripe - The card number that you entered is invalid",
                Toast.LENGTH_LONG).show()
        }else if(!card!!.validateCVC()){
            Toast.makeText(this@PaymentActivity,
                "Stripe - The CVC code that you entered is invalid",
                Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this@PaymentActivity,
                "Stripe - The card details that you entered are invalid",
                Toast.LENGTH_LONG).show();
        }
    }fun charge(cardToken:Token){
        val userId:Int = sharedPref.getString("userId","0").toInt()
        val username:String = sharedPref.getString("username","default username").toString()
        val email:String = sharedPref.getString("email","default email").toString()
        val address:String = sharedPref.getString("address","default address").toString()

        val params = HashMap<String, Any>()
        params.put("ItemName", "test")
        params.put("cardToken", cardToken.getId())
        params.put("name",username)
        params.put("email",email)
        params.put("address",address)
        params.put("zip","27")
        params.put("city_state","LI")
        params.put("price",reservation.price)

        startProgress("Purchasing Item")
        ParseCloud.callFunctionInBackground("purchaseItem", params, FunctionCallback<Any> { response, e ->
            finishProgress()
            if (e == null) {
                Log.d("Cloud Response", "There were no exceptions! $response")

                val reservationJsonObject = JSONObject()
                reservationJsonObject.put("customerId",reservation.customerId)
                reservationJsonObject.put("ownerId",reservation.ownerId)
                reservationJsonObject.put("startReservationDate",reservation.startReservationDate)
                reservationJsonObject.put("endReservationDate",reservation.endReservationDate)
                reservationJsonObject.put("isActive",reservation.isActive)
                Toast.makeText(
                    applicationContext,
                    "Item Purchased Successfully ",
                    Toast.LENGTH_LONG
                ).show()

                AndroidNetworking.post(FastParkingApi.postReserve(userId))
                    .addJSONObjectBody(reservationJsonObject)
                    .setPriority(Priority.MEDIUM)
                    .setTag("FastParking")
                    .build()
                    .getAsJSONObject(object:JSONObjectRequestListener{
                        override fun onResponse(response: JSONObject?) {
                            if(response?.getString("status")=="ok"){
                                val intent = Intent(this@PaymentActivity,MainActivity::class.java)
                                this@PaymentActivity.startActivity(intent)
                            }else{
                                Log.d("PaymentActivity","Error to call Api")
                            }
                        }

                        override fun onError(anError: ANError?) {
                            Log.d("PaymentActivity", "Error: ${anError?.message}")
                        }

                    })


            } else {
                Log.d("Cloud Response", "Exception: " + e!!)
                Toast.makeText(
                    applicationContext,
                    e!!.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    fun startProgress(title:String){
        progress?.setTitle(title)
        progress?.setMessage("Please Wait")
        progress?.show()
    }

    fun finishProgress(){
        progress?.dismiss()
    }





}
