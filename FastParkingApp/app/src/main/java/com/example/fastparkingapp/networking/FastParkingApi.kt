package com.example.fastparkingapp.networking

class FastParkingApi(){
    companion object {
        //private const val baseUrl = "http://fastpark.somee.com/FastParking/v1"
        private const val baseUrl = "http://fastparking.gearhostpreview.com/FastParking/v1"
        val getOwnersEndPoint = "$baseUrl/owners"
        fun getOwnerEndPoint (id:Int):String{return "$baseUrl/owners/$id"}
        fun getReservationsEndPoint(id:Int):String{return "$baseUrl/customers/$id/reservations"}

        //post endpoints
        val authentication = "$baseUrl/login/authenticate"
        val postCustomer = "$baseUrl/customers"
        fun postReserve(customerId:Int):String{return "$baseUrl/customers/${customerId}/reservations"}
    }
}