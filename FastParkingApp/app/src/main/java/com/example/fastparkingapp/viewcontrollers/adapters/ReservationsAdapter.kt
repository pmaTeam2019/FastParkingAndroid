package com.example.fastparkingapp.viewcontrollers.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.widget.ANImageView
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Reservation
import kotlinx.android.synthetic.main.item_reservation.view.*

class ReservationsAdapter(var reservations:ArrayList<Reservation>,val context: Context):RecyclerView.Adapter<ReservationsAdapter.ViewHolder>(){
    class ViewHolder(reservationView: View):RecyclerView.ViewHolder(reservationView){
        var ownerImageView: ANImageView
        var parkingCompanyTextView: TextView
        var entryDateTextView: TextView
        var exitDateTextView:TextView

        init{
            ownerImageView = reservationView.ownerImageView
            parkingCompanyTextView = reservationView.parkingCompanyTextView
            entryDateTextView = reservationView.entryDateTextView
            exitDateTextView = reservationView.exitDateTextView
        }

        fun bindTo(reservation:Reservation){
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reservation,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return reservations.size
    }

    override fun onBindViewHolder(holder: ReservationsAdapter.ViewHolder, position: Int) {
            holder.bindTo(reservations[position])
    }

}