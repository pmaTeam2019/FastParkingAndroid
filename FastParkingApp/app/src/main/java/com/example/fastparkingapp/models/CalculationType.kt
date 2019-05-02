package com.example.fastparkingapp.models

import android.os.Bundle

data class CalculationType(val id:Int,
                           val name:String,
                           val description:String){
    companion object {
        fun from(bundle:Bundle):CalculationType{
            return CalculationType(
                bundle.getInt("id"),
                bundle.getString("name"),
                bundle.getString("description"))
        }
    }

    fun toBundle():Bundle{
        val bundle = Bundle()
        with(bundle){
            putInt("id",id)
            putString("name",name)
            putString("description",description)
        }
        return bundle
    }
}