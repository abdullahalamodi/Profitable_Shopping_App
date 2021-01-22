package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.Complain
import com.finalproject.profitableshopping.data.api.ShoppingApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ComplainRepositry {
    private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }


    fun addComplain(complain: Complain):Call<String>{
        return shoppingApi.addComplain(complain)
    }
    fun getComplains():Call<List<Complain>>{
        return  shoppingApi.getComplains()
    }
    fun getComplain(complainId: Int):Call<Complain>{
        return  shoppingApi.getComplain(complainId)
    }
}