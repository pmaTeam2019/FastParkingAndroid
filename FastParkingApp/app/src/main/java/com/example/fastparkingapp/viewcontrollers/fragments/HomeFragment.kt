package com.example.fastparkingapp.viewcontrollers.fragments


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
import com.example.fastparkingapp.models.Owner
import com.example.fastparkingapp.models.OwnerResponse
import com.example.fastparkingapp.networking.FastParkingApi
import com.example.fastparkingapp.viewcontrollers.adapters.ParkingsAdapter
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    private var parkings = ArrayList<Owner>()
    private lateinit var parkingsRecyclerView: RecyclerView
    private lateinit var parkingsAdapter: ParkingsAdapter
    private lateinit var parkingsLayoutManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        parkingsAdapter = ParkingsAdapter(parkings,view.context)
        parkingsLayoutManager = LinearLayoutManager(view.context)
        parkingsRecyclerView = view.parkingsRecyclerView
        parkingsRecyclerView.adapter = parkingsAdapter
        parkingsRecyclerView.layoutManager = parkingsLayoutManager
        val getOwnersEndPoint = FastParkingApi.getOwnersEndPoint
        AndroidNetworking.get(getOwnersEndPoint)
            .setPriority(Priority.LOW)
            .setTag("FastParking")
            .build()
            .getAsObject(OwnerResponse::class.java, object : ParsedRequestListener<OwnerResponse> {
                override fun onResponse(response: OwnerResponse?) {
                    parkings = response!!.owners
                    parkingsAdapter.parkins = parkings
                    parkingsAdapter.notifyDataSetChanged()
                    Log.d("FastParking",response.owners.toString())
                }

                override fun onError(anError: ANError?) {
                    Log.d("FastParking", anError!!.message)
                }
            })

        return view
    }
}
