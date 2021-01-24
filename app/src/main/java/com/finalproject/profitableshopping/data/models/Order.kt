package com.finalproject.profitableshopping.data.models

data class Order(
    var id:Int?=0,
    var date: String="1-1-2021",
    var user_id: String? ="0",
    var total_price:Double=0.0,
    var is_paid:Boolean=false
)