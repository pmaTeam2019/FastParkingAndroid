package com.example.fastparkingapp.viewcontrollers.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.widget.ANImageView
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.google.android.material.card.MaterialCardView
import com.google.gson.JsonObject
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment

import kotlinx.android.synthetic.main.activity_reservation.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var confirmReserveButton:Button


    val entryOnButtonWithNeutralClickListener = object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{
        override fun onPositiveButtonClick(date: Date?) {
            entryDateTimeTextView.text =simpleDateFormat.format(date)
        }

        override fun onNeutralButtonClick(date: Date?) {
        }

        override fun onNegativeButtonClick(date: Date?) {
            entryDateTimeTextView.text = ""
        }

    }

    val exitOnButtonWithNeutralClickListener = object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{
        override fun onPositiveButtonClick(date: Date?) {
            exitDateTimeTextView.text = simpleDateFormat.format(date)
        }

        override fun onNeutralButtonClick(date: Date?) {
        }

        override fun onNegativeButtonClick(date: Date?) {
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
        statusTextView = findViewById(R.id.statusTextView)
        entryDateTimeTextView = findViewById(R.id.entryDateTime)
        exitDateTimeTextView = findViewById(R.id.exitDateTime)
        parking = Owner.from(intent.extras)
        confirmReserveButton = findViewById(R.id.ConfirmReservationButton)

        with(logoImageView){
            setImageUrl(parking.imageUrl)
            setErrorImageResId(R.drawable.ic_launcher_background)
            setDefaultImageResId(R.drawable.ic_launcher_background)
        }
        if(parking.isAvailable){
            statusTextView.text = "Available"
        }else{statusTextView.text = "Unavailable"}

        fullNameTextView.text = parking.fullName


        confirmReserveButton.setOnClickListener{



            val reservationJsonObject = JSONObject()
            reservationJsonObject.put("SlotId","1")
            reservationJsonObject.put("CustomerId","1")
            reservationJsonObject.put("OwnerId","1")
            reservationJsonObject.put("StartReservationDate","2019-06-13T12:30")
            reservationJsonObject.put("EndReservationDate","2019-03-15T11:20")
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
            entrySwitchDateTime?.setDefaultDateTime(GregorianCalendar(2019,Calendar.MAY,30,15,20).time)
            entrySwitchDateTime?.show(supportFragmentManager,TAG_DATETIME_FRAGMENT)
        }

        //contentExit OnClickListener
        contentExit.setOnClickListener{
            exitSwitchDateTime?.startAtCalendarView()
            exitSwitchDateTime?.setDefaultDateTime(GregorianCalendar(2019,Calendar.MAY,30,15,20).time)
            exitSwitchDateTime?.show(supportFragmentManager,TAG_DATETIME_FRAGMENT)
        }

        entrySwitchDateTime?.setOnButtonClickListener(entryOnButtonWithNeutralClickListener)
        exitSwitchDateTime?.setOnButtonClickListener(exitOnButtonWithNeutralClickListener)

    }


}
