package com.example.fastparkingapp.viewcontrollers.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.widget.ANImageView
import com.example.fastparkingapp.R
import com.example.fastparkingapp.models.Owner
import com.example.fastparkingapp.viewcontrollers.activities.ParkingActivity
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.item_parking.view.*

class ParkingsAdapter(var parkins: ArrayList<Owner>,val context:Context): RecyclerView.Adapter<ParkingsAdapter.ViewHolder>(){
    class ViewHolder(parkingView: View):RecyclerView.ViewHolder(parkingView) {
        var pictureCardView : ANImageView
        var fullnameTextView:TextView
        var addressTextView:TextView
        var priceTextView:TextView
        var distanceTextView:TextView
        var statusImageView:ImageView
        var statusTextView: TextView
        var content: CardView

        init {
            pictureCardView = parkingView.pictureCardView
            fullnameTextView = parkingView.fullNameTextView
            addressTextView = parkingView.addressTextView
            priceTextView = parkingView.priceTextView
            distanceTextView = parkingView.distanceTextView
            statusImageView = parkingView.statusIcon
            statusTextView = parkingView.statusTextView
            content = parkingView.content
        }

        fun bindTo(parking:Owner){
            fullnameTextView.text = parking.fullName
            addressTextView.text = parking.address
            priceTextView.text = "1,50 $/h"
            distanceTextView.text = "1,2 km"
            if(parking.isAvailable){
                statusImageView.setImageResource(R.drawable.status_available_24dp)
                statusTextView.text = "Available"
            }else{
                statusImageView.setImageResource(R.drawable.status_unavailable_24dp)
                statusTextView.text = "Unavailable"
            }
            with(pictureCardView){
                setImageUrl("https://www.dianellaplaza.com.au/media/4977/parkinggeneric.jpg.ashx?width=800&height=520&preset=default")
                setDefaultImageResId(R.drawable.ic_launcher_background)
                setErrorImageResId(R.drawable.ic_launcher_background)
            }

            //event click cardView
            content.setOnClickListener{
                val intent = Intent(it.context, ParkingActivity::class.java)
                it.context.startActivity(intent.putExtras(parking.toBundle()))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingsAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_parking,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return parkins.size
    }

    override fun onBindViewHolder(holder: ParkingsAdapter.ViewHolder, position: Int) {
        holder.bindTo(parkins[position])
    }
}