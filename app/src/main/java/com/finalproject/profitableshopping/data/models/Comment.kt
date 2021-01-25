package com.finalproject.profitableshopping.data.models

import com.google.gson.annotations.SerializedName

class Comment( var id: Int = 0,
               @SerializedName("title")
               var title: String = "",
               @SerializedName("rate")
               var rate:Int = 0,
               @SerializedName("user_id")
               var userId: String = "0",
               @SerializedName("product_id")
               var productId: Int = 0,
               @SerializedName("date")
               var date: String="21-1-2021") {}