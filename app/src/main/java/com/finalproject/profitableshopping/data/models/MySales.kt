package com.finalproject.profitableshopping.data.models

import com.google.gson.annotations.SerializedName

data class MySales (

    var id: Int = 0,
    @SerializedName("name")
var name: String = "",
    @SerializedName("quantity")
var quantity: Int = 0,
    @SerializedName("rial_price")
var totalPrice: Double = 0.0,

    @SerializedName("color")
var color: String = "",
    @SerializedName("size")
var size: String = "",
    @SerializedName("images")
var images: List<ImageUrl> = emptyList()

)