package com.finalproject.profitableshopping.data.models


data class Category(var id: Int? = 0, var name: String = "", var path: String = "",var checked:Boolean = false) {
    fun getUrl(): String {
//        return path.replace("localhost",/*"10.0.2.2:80"*/"192.168.191.1:8080")
        return path.replace("localhost", "10.0.2.2:80")
    }
}