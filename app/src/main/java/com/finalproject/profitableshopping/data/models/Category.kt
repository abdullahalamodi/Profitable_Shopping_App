package com.finalproject.profitableshopping.data.models

import com.google.gson.annotations.SerializedName


data class Category(
    var id: Int? = 0,
    var name: String = "",
    var path: String? = null,
    @SerializedName("is_active")
    var isActive: Int = 1,
    var checked: Boolean = false
) {
    fun getUrl(): String {
//        return path.replace("localhost",/*"10.0.2.2:80"*/"192.168.191.1:8080")
        return path?.replace("localhost", "10.0.2.2:80")!!
    }

    fun isActive():Boolean{
        return isActive == 1
    }

    fun changeIsActive(): Int {
        if (isActive == 1)
            return 0
        return 1
    }
}