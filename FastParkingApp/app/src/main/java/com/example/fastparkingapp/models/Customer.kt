package com.example.fastparkingapp.models

import android.os.Bundle

data class Customer(var id:Int,
                    var firstName:String,
                    var lastName:String,
                    var address:String?,
                    var birthday:String?,
                    var dni:String,
                    var ruc:String?,
                    var fullName:String?,
                    var email:String,
                    var password:String){

    companion object {
        fun from(bundle:Bundle):Customer{
            return Customer(
                bundle.getInt("id"),
                bundle.getString("firstName"),
                bundle.getString("lastName"),
                bundle.getString("address"),
                bundle.getString("birthday"),
                bundle.getString("dni"),
                bundle.getString("ruc"),
                bundle.getString("fullName"),
                bundle.getString("email"),
                bundle.getString("password")
            )
        }
    }

    fun toBundle():Bundle{
        val bundle=Bundle()
        with(bundle){
            putInt("id",id)
            putString("firstName",firstName)
            putString("lastName",lastName)
            putString("address",address)
            putString("birthday",birthday)
            putString("dni",dni)
            putString("ruc",ruc)
            putString("fullName",fullName)
            putString("email",email)
            putString("password",password)
        }
        return bundle
    }
}