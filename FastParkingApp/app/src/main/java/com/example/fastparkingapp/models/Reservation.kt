package com.example.fastparkingapp.models

import android.os.Bundle

data class Reservation(
    val id:Int,
    val customerId:Int,
    val ownerId:Int,
    val startReservationDate:String,
    val endReservationDate:String,
    val isActive:Boolean,
    val rating:Double,
    val price: Double,
    val owner: Owner?
){

    companion object {
        fun from(bundle: Bundle):Reservation{
            return Reservation(
                bundle.getInt("id"),
                bundle.getInt("customerId"),
                bundle.getInt("ownerId"),
                bundle.getString("startReservationDate"),
                bundle.getString("endReservationDate"),
                bundle.getBoolean("isActive"),
                bundle.getDouble("rating"),
                bundle.getDouble("price"),
                Owner.from(bundle.getBundle("owner")))
        }
    }

    fun toBundle():Bundle{
        val bundle = Bundle()
        with(bundle){
            putInt("id",id)
            putInt("customerId",customerId)
            putInt("ownerId",ownerId)
            putString("startReservationDate",startReservationDate)
            putString("endReservationDate",endReservationDate)
            putBoolean("isActive",isActive)
            putDouble("rating",rating)
            putDouble("price",price)
            putBundle("owner",owner?.toBundle())
        }
        return bundle
    }
}