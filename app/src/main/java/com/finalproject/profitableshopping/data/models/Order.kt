package com.finalproject.profitableshopping.data.models

import java.util.*

data class Order(
    var id:Int?=null,
    var date: String="1-1-2021",
    var userId:Int=0,
    var totalPrice:Double=0.0,
    var isBought:Boolean=false
)