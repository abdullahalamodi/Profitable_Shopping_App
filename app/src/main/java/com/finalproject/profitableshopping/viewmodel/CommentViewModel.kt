package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Comment
import com.finalproject.profitableshopping.data.repositories.CommentRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentViewModel : ViewModel() {

     var repository: CommentRepository

    init {
        repository=CommentRepository()
    }


    private fun getComment(id: Int): MutableLiveData<Comment> {
        val responseLiveData: MutableLiveData<Comment> = MutableLiveData()
        val call = repository.getComment(id)
        call.enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                responseLiveData.value = response.body() ?: Comment(id = -1)
            }

            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.d("commentFailed", t.message!!)
                responseLiveData.value = Comment(id = -1)
            }
        })
        return responseLiveData
    }
}