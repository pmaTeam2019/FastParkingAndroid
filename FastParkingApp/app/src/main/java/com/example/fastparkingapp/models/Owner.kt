package com.example.fastparkingapp.models

import android.os.Bundle
import java.util.*

data class Owner( val id:Int,
                  val fullName:String?,
                  val address:String?,
                  val slotsQuantity:Int,
                  val isAvailable:Boolean,
                  val ruc:String?,
                  val birthday:String?,
                  val description:String?,
                  val email:String?,
                  val password:String?,
                  val latitude:Double,
                  val longitude:Double,
                  var imageUrl:String?,
                  val distance:String?,
                  val duration:String?,
                  val rating:Double?,
                  val price:Double?){
    companion object {
        fun from(bundle:Bundle): Owner{
            return Owner(
                bundle.getInt("id"),
                bundle.getString("fullName"),
                bundle.getString("address"),
                bundle.getInt("slotsQuantity"),
                bundle.getBoolean("isAvailable"),
                bundle.getString("ruc"),
                bundle.getString("birthday"),
                bundle.getString("description"),
                bundle.getString("email"),
                bundle.getString("password"),
                bundle.getDouble("latitude"),
                bundle.getDouble("longitude"),
                bundle.getString("imageUrl"),
                bundle.getString("distance"),
                bundle.getString("duration"),
                bundle.getDouble("rating"),
                bundle.getDouble("price")
            )
        }
    }

    fun toBundle(): Bundle{
        val bundle = Bundle()
        with(bundle){
            bundle.putInt("id",id)
            bundle.putString("fullName",fullName)
            bundle.putString("address",address)
            bundle.putInt("slotsQuantity",slotsQuantity)
            bundle.putBoolean("isAvailable",isAvailable)
            bundle.putString("ruc",ruc)
            bundle.putString("birthday",birthday)
            bundle.putString("description",description)
            bundle.putString("email",email)
            bundle.putString("password",password)
            bundle.putDouble("latitude",latitude)
            bundle.putDouble("longitude",longitude)
            bundle.putString("imageUrl",imageUrl)
            bundle.putString("distance",distance)
            bundle.putString("duration",duration)
            bundle.putDouble("rating",rating!!)
            bundle.putDouble("price",price!!)
        }
        return bundle
    }
}