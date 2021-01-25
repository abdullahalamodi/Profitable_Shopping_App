package com.finalproject.profitableshopping.data.models

data class Report (
    val id:Int?,
    var from_id:String="",
    var to_id:String="",
    var complain_id:Int?,
    var product_id:Int?
)