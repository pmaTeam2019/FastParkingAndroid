package com.example.fastparkingapp.networking

class FastParkingApi(){
    companion object {
        private const val baseUrl = "http://fastpark.somee.com/FastParking/v1"
        val getOwnersEndPoint = "$baseUrl/owners"
        fun getOwnerEndPoint (id:Int):String{return "$baseUrl/$id"}

        //post endpoints
        val authentication = "$baseUrl/login/authenticate"
        val postCustomer = "$baseUrl/customers"
    }
}