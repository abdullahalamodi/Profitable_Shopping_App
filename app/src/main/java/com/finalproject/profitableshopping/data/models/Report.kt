package com.finalproject.profitableshopping.data.models

data class Report (
    val id:Int?,
    var fromId:String="",
    var to_id:String="",
    var complainId:Int?,
    var date:String="",
    var productId:String=""
)