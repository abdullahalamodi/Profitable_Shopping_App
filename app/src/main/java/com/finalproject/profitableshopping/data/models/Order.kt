package com.finalproject.profitableshopping.data.models

import java.util.*

data class Order(
    var id:Int?=0,
    var date: String="1-1-2021",
    var user_id:Int=0,
    var total_price:Double=0.0,
    var is_paid:Boolean=false
)