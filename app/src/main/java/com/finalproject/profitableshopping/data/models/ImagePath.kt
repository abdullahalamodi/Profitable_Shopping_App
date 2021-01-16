package com.finalproject.profitableshopping.data.models

data class ImageUrl(
    val path: String
){
    fun getUrl():String{
        return path.replace("localhost","10.0.2.2:80")
    }
}