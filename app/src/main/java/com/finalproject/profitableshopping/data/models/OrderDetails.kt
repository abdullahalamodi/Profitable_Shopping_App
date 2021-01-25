package com.finalproject.profitableshopping.data.models

data class OrderDetails(
    var id: Int? = 0,
    var product_id: Int=0,
    var product:Product = Product(),
    var order_id: Int=0,
    var quantity: Int=0,
    var color: String="",
    var size:String="",
    var price:Double=0.0,
    var total_price:Double=0.0
)