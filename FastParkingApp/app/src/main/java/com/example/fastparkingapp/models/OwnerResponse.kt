package com.example.fastparkingapp.models

class OwnerResponse(val status:String,
                    val owners:ArrayList<Owner>,
                    val totalResults:Int,
                    val code:String)