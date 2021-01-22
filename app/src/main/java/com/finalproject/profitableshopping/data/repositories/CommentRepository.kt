package com.finalproject.profitableshopping.data.repositories

import com.finalproject.profitableshopping.data.api.ShoppingApi
import com.finalproject.profitableshopping.data.models.Comment
import com.finalproject.profitableshopping.data.models.Product
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CommentRepository {

    private var shoppingApi: ShoppingApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(CategoryRepository.BASE_URL)
            .build()
        shoppingApi = retrofit.create(ShoppingApi::class.java)
    }

    fun addComment(comment: Comment): Call<String> {
        return shoppingApi.addComment(comment)
    }

    fun getComment(commentId:Int):Call<Comment>{
        return  shoppingApi.getComment(commentId)
    }

    fun getComments():Call<List<Comment>>{
        return shoppingApi.getComments()
    }

    fun getUserComments(userId:String): Call<List<Comment>> {
        return shoppingApi.getUserComments(userId)
    }

    fun updateComment(comment: Comment):Call<String>{
        return  shoppingApi.updateComment(comment.id.toString(),comment)
    }

    fun deleteComment(commentId:String):Call<String>{
        return shoppingApi.deleteComment(commentId)
    }

}