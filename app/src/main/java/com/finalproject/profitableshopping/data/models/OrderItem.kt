package com.finalproject.profitableshopping.data.models

data class OrderItem(
    var id: Int?=null,
    var ProductId: String="",

    var orderId: Int=0,
    var quantity: Int=0,
    var color: String="",
    var size:String="",
    var totalPrice:Double=0.0
)