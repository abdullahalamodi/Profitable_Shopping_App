package com.finalproject.profitableshopping.data.models

data class Favorite(
    var id: Int?,
    var user_id: String? = "0",
    var product_id:Int?=0,
    var product:Product?=Product()
)