package com.example.fastparkingapp.viewcontrollers.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;
import com.androidnetworking.widget.ANImageView
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.example.fastparkingapp.models.Reservation
import com.google.android.material.card.MaterialCardView
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment

import kotlinx.android.synthetic.main.activity_reservation.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ReservationActivity : AppCompatActivity() {
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("d MMM yyy HH:mm", Locale.getDefault())
    val simpeDateFormatPatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())

    private var entrySwitchDateTime :SwitchDateTimeDialogFragment? = null
    private var exitSwitchDateTime: SwitchDateTimeDialogFragment? = null
    val TAG_DATETIME_FRAGMENT:String = "TAG_DATETIME_FRAGMENT"
    val TAG_DATETIME_FRAGMENT_EXIT:String = "TAG_EXITDATETIME_FRAGMENT"
    private lateinit var contentEntry: MaterialCardView
    private lateinit var contentExit:MaterialCardView
    private lateinit var entryDateTimeTextView:TextView
    private lateinit var exitDateTimeTextView:TextView
    private lateinit var parking: Owner
    private lateinit var logoImageView:ANImageView
    private lateinit var fullNameTextView:TextView
    private lateinit var statusTextView:TextView
    private lateinit var priceTextView: TextView
    private lateinit var confirmReserveButton:Button
    private var entryDate = Date()
    private var exitDate = Date()
    private lateinit var sharePref: SharedPreferences
    val PREFS_FILENAME:String = "com.example.fastparkingapp.prefs"
    var totalAmount:Double = 0.0


    val entryOnButtonWithNeutralClickListener = object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{
        override fun onPositiveButtonClick(date: Date?) {
            entryDate = date!!
            entryDateTimeTextView.text =simpleDateFormat.format(date)
        }

        override fun onNeutralButtonClick(date: Date?) {
        }

        override fun onNegativeButtonClick(date: Date?) {
            entryDate = date!!
            entryDateTimeTextView.text = ""
        }

    }

    val exitOnButtonWithNeutralClickListener = object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{
        override fun onPositiveButtonClick(date: Date?) {
            exitDate = date!!
            exitDateTimeTextView.text = simpleDateFormat.format(date)
            var diffInMillisec:Long = exitDate.time - entryDate.time
            var diffInHours:Long = TimeUnit.MILLISECONDS.toHours(diffInMillisec)
            totalAmount = diffInHours* parking.price!!
            Log.d("ReservationActivity","Price = ${park ing.price}")
            priceTextView.text = "S/. ${totalAmount}"

            Log.d("ReservationActivity","DiffDates ${diffInHours}")
        }

        override fun onNeutralButtonClick(date: Date?) {
        }

        override fun onNegativeButtonClick(date: Date?) {
            exitDate = date!!
            exitDateTimeTextView.text = ""
        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)
        setSupportActionBar(toolbar)
        contentEntry = findViewById(R.id.contentEntry)
        contentExit = findViewById(R.id.contentExit)
        logoImageView = findViewById(R.id.logoImageView)
        fullNameTextView = findViewById(R.id.fullNameTextView)
        entryDateTimeTextView = findViewById(R.id.entryDateTime)
        exitDateTimeTextView = findViewById(R.id.exitDateTime)
        parking = Owner.from(intent.extras)
        priceTextView = findViewById(R.id.priceTextView)
        confirmReserveButton = findViewById(R.id.ConfirmReservationButton)
        sharePref = this.getSharedPreferences(PREFS_FILENAME,0)
        with(logoImageView){
            setImageUrl(parking.imageUrl)
            setErrorImageResId(R.drawable.ic_launcher_background)
            setDefaultImageResId(R.drawable.ic_launcher_background)
        }



        fullNameTextView.text = parking.fullName


        confirmReserveButton.setOnClickListener{
            val userId:Int = sharePref.getString("userId","0").toInt()
            val owner = Owner(parking.id,parking.fullName,parking.address,parking.slotsQuantity,parking.isAvailable,parking.ruc,parking.birthday,parking.description,
                parking.email,parking.password,parking.latitude,parking.longitude,parking.imageUrl,parking.distance,parking.duration,parking.rating,parking.price)
            Log.d("ReservationActivity","imagen vacio? = ${owner.toString()}")

            val reservation = Reservation(0,userId,owner.id,"2019-07-27T11:30","2019-07-27T12:30",true,0.0,totalAmount,owner)
            val intent = Intent(this,PaymentActivity::class.java)
            this.startActivity(intent.putExtras(reservation.toBundle()))
        }

        //setUp entrySwitchDateTime
        if(entrySwitchDateTime == null){
            entrySwitchDateTime = SwitchDateTimeDialogFragment.newInstance(
                getString(R.string.label_datetime_dialog),
                getString(android.R.string.ok),
                getString(android.R.string.cancel)
            )
        }
        else{
            entrySwitchDateTime = (supportFragmentManager.findFragmentByTag(TAG_DATETIME_FRAGMENT) as? SwitchDateTimeDialogFragment)!!
        }

        entrySwitchDateTime?.setTimeZone(TimeZone.getDefault())
        entrySwitchDateTime?.set24HoursMode(true)
        entrySwitchDateTime?.setHighlightAMPMSelection(false)
        entrySwitchDateTime?.minimumDateTime = GregorianCalendar(2015,Calendar.JANUARY,1).time
        entrySwitchDateTime?.maximumDateTime = GregorianCalendar(2025,Calendar.DECEMBER,1).time

        try{
            entrySwitchDateTime?.simpleDateMonthAndDayFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        }catch (e:SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException){
            Log.e("FastParking",e.message)
        }

        //setUp exitSwitchDateTime
        if(exitSwitchDateTime == null){
            exitSwitchDateTime = SwitchDateTimeDialogFragment.newInstance(
                getString(R.string.label_datetime_dialog),
                getString(android.R.string.ok),
                getString(android.R.string.cancel)
            )
        }
        else{
            exitSwitchDateTime = (supportFragmentManager.findFragmentByTag(TAG_DATETIME_FRAGMENT_EXIT) as? SwitchDateTimeDialogFragment)!!
        }

        exitSwitchDateTime?.setTimeZone(TimeZone.getDefault())
        exitSwitchDateTime?.set24HoursMode(true)
        exitSwitchDateTime?.setHighlightAMPMSelection(false)
        exitSwitchDateTime?.minimumDateTime = GregorianCalendar(2015,Calendar.JANUARY,1).time
        exitSwitchDateTime?.maximumDateTime = GregorianCalendar(2025,Calendar.DECEMBER,1).time

        try{
            exitSwitchDateTime?.simpleDateMonthAndDayFormat = SimpleDateFormat("MMMM dd", Locale.getDefault())
        }catch (e:SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException){
            Log.e("FastParking",e.message)
        }

        //contentEntry OnClickListener
        contentEntry.setOnClickListener{
            entrySwitchDateTime?.startAtCalendarView()
            entrySwitchDateTime?.setDefaultDateTime(GregorianCalendar(2019,Calendar.JUNE,27,11,40).time)
            entrySwitchDateTime?.show(supportFragmentManager,TAG_DATETIME_FRAGMENT)
        }

        //contentExit OnClickListener
        contentExit.setOnClickListener{
            if(entryDateTimeTextView.text == "Choose your entry date" || entryDateTimeTextView.text === ""){
                Toast.makeText(this,"Please Choose Entry Date",Toast.LENGTH_SHORT).show()
            }else{
                exitSwitchDateTime?.startAtCalendarView()
                exitSwitchDateTime?.setDefaultDateTime(GregorianCalendar(2019,Calendar.JUNE,27,11,40).time)
                exitSwitchDateTime?.show(supportFragmentManager,TAG_DATETIME_FRAGMENT)
            }
        }

        entrySwitchDateTime?.setOnButtonClickListener(entryOnButtonWithNeutralClickListener)
        exitSwitchDateTime?.setOnButtonClickListener(exitOnButtonWithNeutralClickListener)

    }
}
