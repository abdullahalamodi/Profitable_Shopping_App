package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Report
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ReportRepositry {
    private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }

    fun addReport(report: Report):Call<String>{
        return  shoppingApi.addReport(report)
    }
    fun getUserReports(userId:String):Call<List<Report>>{
        return shoppingApi.getUserReports(userId)
    }
    fun getProductReports(productId:String):Call<List<Report>>{
        return shoppingApi.getProductReports(productId)
    }
}