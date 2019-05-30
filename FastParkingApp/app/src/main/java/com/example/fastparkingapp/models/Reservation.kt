package com.example.fastparkingapp.models

import android.os.Bundle

data class Reservation(val id:Int,
                       val slotId:Int,
                       val customerId:Int,
                       val startReservationDate:String,
                       val endReservationDate:String,
                       val isActive:Boolean,
                       val rating:Double){
    companion object {
        fun from(bundle: Bundle):Reservation{
            return Reservation(
                bundle.getInt("id"),
                bundle.getInt("slotId"),
                bundle.getInt("customerId"),
                bundle.getString("startReservationDate"),
                bundle.getString("endReservationDate"),
                bundle.getBoolean("isActive"),
                bundle.getDouble("rating")
            )
        }
    }

    fun toBundle():Bundle{
        val bundle = Bundle()
        with(bundle){
            putInt("id",id)
            putInt("slotId",slotId)
            putInt("customerId",customerId)
            putString("startReservationDate",startReservationDate)
            putString("endReservationDate",endReservationDate)
            putBoolean("isActive",isActive)
            putDouble("rating",rating)
        }
        return bundle
    }
}