package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.data.models.FavoriteItem
import com.finalproject.profitableshopping.data.models.OrderItem
import com.finalproject.profitableshopping.data.repositories.CartRepositry
import com.finalproject.profitableshopping.data.repositories.FavoriteRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoriteViewModel :ViewModel(){
    val favoriteRepositry:FavoriteRepositry


    init {
        favoriteRepositry= FavoriteRepositry()
    }


    fun createFavorite(fav:Favorite): LiveData<Int> {
        val favoriteId: MutableLiveData<Int> = MutableLiveData<Int>()
        val call=favoriteRepositry.createFavorite(fav)
        call.enqueue(object :retrofit2.Callback<Int>{
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                favoriteId.value=response.body()
                Log.d("success create favorite",favoriteId.value.toString())
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d("faild creat favorite",t.message!!)
                favoriteId.value=-1
            }

        })
        return favoriteId

    }
    fun addFavoriteItem(favoriteItem: FavoriteItem): LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call=favoriteRepositry.addFavoriteItem(favoriteItem)
        call.enqueue(
            object :retrofit2.Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    message.value= response.body()
                    Log.d("success add  item",response.body()!!)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("failes add item",t.message!!)
                    message.value= t.message
                }
            }
        )
        return message

    }
    fun deleteFavoriteItem(favoriteItemId:Int): LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call=favoriteRepositry.deleteFavoriteItem(favoriteItemId)
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

    fun getFavoriteItems(favoriteId:Int):LiveData<List<FavoriteItem>>{
        val favoriteList= MutableLiveData<List<FavoriteItem>>()
        val call=favoriteRepositry.getOrders(favoriteId)
        call.enqueue(
            object : Callback<List<FavoriteItem>> {
                override fun onResponse(
                    call: Call<List<FavoriteItem>>,
                    response: Response<List<FavoriteItem>>
                ) {
                    favoriteList.value=response.body()
                    Log.d("success get favorites","success get favorites")
                }

                override fun onFailure(call: Call<List<FavoriteItem>>, t: Throwable) {
                    Log.d("faild get favorites",t.message!!)
                    favoriteList.value= emptyList()
                }
            }
        )


        return favoriteList

    }
}