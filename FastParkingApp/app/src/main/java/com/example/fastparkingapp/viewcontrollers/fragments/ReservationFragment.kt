package com.example.fastparkingapp.viewcontrollers.fragments


import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener

import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Reservation
import com.example.fastparkingapp.models.ReservationResponse
import com.example.fastparkingapp.networking.FastParkingApi
import com.example.fastparkingapp.viewcontrollers.adapters.ReservationsAdapter
import kotlinx.android.synthetic.main.fragment_reservation.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ReservationFragment : Fragment() {
    private var reservations = ArrayList<Reservation>()
    private lateinit var reservationRecyclerView: RecyclerView
    private lateinit var reservationAdapter:ReservationsAdapter
    private lateinit var reservationLayoutManager: RecyclerView.LayoutManager
    private lateinit var sharePref: SharedPreferences
    val PREFS_FILENAME:String = "com.example.fastparkingapp.prefs"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  = inflater.inflate(R.layout.fragment_reservation, container, false)
        reservationAdapter = ReservationsAdapter(reservations,view.context)
        reservationLayoutManager = LinearLayoutManager(view.context)
        reservationRecyclerView = view.reservationRecyclerView
        reservationRecyclerView.adapter = reservationAdapter
        reservationRecyclerView.layoutManager = reservationLayoutManager

        sharePref = this.activity!!.getSharedPreferences(PREFS_FILENAME,0)
        val userId:Int = sharePref.getString("userId","0").toInt()
        Log.d("ReservationFragment","userId : $userId")

        AndroidNetworking.get(FastParkingApi.getReservationsEndPoint(userId))
            .setPriority(Priority.LOW)
            .setTag("FastParking")
            .build()
            .getAsObject(ReservationResponse::class.java, object:ParsedRequestListener<ReservationResponse>{
                override fun onResponse(response: ReservationResponse?) {
                    reservations = response!!.reservations
                    reservationAdapter.reservations = reservations
                    reservationAdapter.notifyDataSetChanged()
                    Log.d("ReservationFragment","Result: ${response.reservations.toString()}")
                }

                override fun onError(anError: ANError?) {
                    Log.d("ReservationFragment", anError!!.message)
                }

            })


        return view
    }


}
