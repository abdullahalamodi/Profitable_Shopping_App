package com.finalproject.profitableshopping.data.models

import com.google.gson.annotations.SerializedName

class Product (
    var id: Int = 0,
    @SerializedName("name")
    var productName: String = "",
    @SerializedName("description")
    var productDescription: String = "",
    @SerializedName("rial_price")
    var productRialPrice: Double = 0.0,
    @SerializedName("dollar_price")
    var productDollarPrice: Double = 0.0,
    @SerializedName("category_id")
    var categoryId:Int=0,
    @SerializedName("user_id")
    var userId:Int=0,
    @SerializedName("images")
    var images:List<ImageModel> =emptyList(),
    @SerializedName("quantity")
    var quantity:Int=0

)