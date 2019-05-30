package com.example.fastparkingapp.models

class ReservationResponse(val status:String,
                          val reservations:ArrayList<Reservation>,
                          val totalResults:Int,
                          val code:String,
                          val message:String?)