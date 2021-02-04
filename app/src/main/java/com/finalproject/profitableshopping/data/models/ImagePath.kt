package com.finalproject.profitableshopping.data.models

data class ImageUrl(
    val path: String
) {
    fun getUrl(): String {
        // return path.replace("localhost","172.16.16.245")
        //return path.replace("localhost","192.168.191.1:8080")
        return path.replace("localhost", "192.168.191.1:80")
        // return path.replace("localhost","10.0.2.2:80")
    }
}