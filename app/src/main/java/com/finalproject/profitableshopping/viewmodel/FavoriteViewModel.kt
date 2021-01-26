package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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


    fun createFavorite(fav:Favorite): LiveData<String> {
        val favoriteId: MutableLiveData<String> = MutableLiveData<String>()
        val call=favoriteRepositry.createFavorite(fav)
        call.enqueue(object :retrofit2.Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                favoriteId.value=response.body()
                Log.d("success create favorite",favoriteId.value.toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("faild creat favorite",t.message!!)
                favoriteId.value=t.message
            }

        })
        return favoriteId

    }
    fun addFavoriteItem(favoriteDetails: FavoriteDetails): LiveData<String>{
        val message: MutableLiveData<String> = MutableLiveData<String>()
        val call=favoriteRepositry.addFavoriteItem(favoriteDetails)
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

    fun getFavoriteItems(favoriteId:Int):LiveData<List<FavoriteDetails>>{
        val favoriteList= MutableLiveData<List<FavoriteDetails>>()
        val call=favoriteRepositry.getOrders(favoriteId)
        call.enqueue(
            object : Callback<List<FavoriteDetails>> {
                override fun onResponse(
                    call: Call<List<FavoriteDetails>>,
                    response: Response<List<FavoriteDetails>>
                ) {
                    favoriteList.value=response.body()
                    Log.d("success get favorites","success get favorites")
                }

                override fun onFailure(call: Call<List<FavoriteDetails>>, t: Throwable) {
                    Log.d("faild get favorites",t.message!!)
                    favoriteList.value= emptyList()
                }
            }
        )


        return favoriteList

    }
}