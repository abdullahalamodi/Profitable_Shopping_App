package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.FavoritResponse
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.data.models.FavoriteDetails
//import com.finalproject.profitableshopping.data.models.OrderItem
import com.finalproject.profitableshopping.data.repositories.FavoriteRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel :ViewModel(){
    val favoriteRepositry:FavoriteRepositry


    init {
        favoriteRepositry= FavoriteRepositry()
    }



    fun addFavoriteItem(favorite: Favorite): LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call=favoriteRepositry.addFavoriteItem(favorite)
        call.enqueue(
            object :retrofit2.Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    message.value = response.body()
                    Log.d("success add item",response.body()!!)

                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("failes add item",t.message!!)
                    message.value= t.message
                }
            }
        )
        return message

    }
    fun deleteFavoriteItem(favoriteId:Int): LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call=favoriteRepositry.deleteFavoriteItem(favoriteId)
        call.enqueue(
            object :retrofit2.Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    message.value= response.body()
                    Log.d("success delete item",response.body()!!)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("faild delet item",t.message!!)
                    message.value= t.message
                }
            }
        )
        return message
    }

    fun getUserFavorites(userId:String):LiveData<List<Favorite>>{
        val favoriteList= MutableLiveData<List<Favorite>>()
        val call=favoriteRepositry.getUserFavorite(userId)
        call.enqueue(
            object : Callback<List<Favorite>> {
                override fun onResponse(
                    call: Call<List<Favorite>>,
                    response: Response<List<Favorite>>
                ) {
                    favoriteList.value=response.body()
                    Log.d("success get favorites","success get favorites")
                }

                override fun onFailure(call: Call<List<Favorite>>, t: Throwable) {
                    Log.d("faild get favorites",t.message!!)
                    favoriteList.value= emptyList()
                }
            }
        )


        return favoriteList

    }
}