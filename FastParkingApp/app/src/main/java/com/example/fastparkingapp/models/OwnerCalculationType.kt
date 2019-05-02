package com.example.fastparkingapp.models

import android.os.Bundle

data class OwnerCalculationType(val id:Int,
                                val ownerId:Int,
                                val calculationTypeId:Int,
                                val price:Float){
    companion object {
        fun from(bundle: Bundle):OwnerCalculationType{
            return OwnerCalculationType(
                bundle.getInt("id"),
                bundle.getInt("ownerId"),
                bundle.getInt("calculationTypeId"),
                bundle.getFloat("price")
            )
        }
    }

    fun toBundle():Bundle{
       val bundle = Bundle()
        with(bundle){
            putInt("id",id)
            putInt("ownerId",ownerId)
            putInt("calculattionTypeId",ownerId)
            putFloat("price",price)
        }
        return bundle
    }

}