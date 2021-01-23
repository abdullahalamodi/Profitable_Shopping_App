package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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

    fun getComments(): MutableLiveData<List<Comment>> {
        val responseLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
        val call = repository.getComments()
        call.enqueue(object : Callback<List<Comment>>{
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                responseLiveData.value = response.body() ?: emptyList()
                Log.d("success get","success get")
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("commentFailed", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }

    fun addComment(comment: Comment): LiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = repository.addComment(comment)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body() ?:" empty"
                Log.d("success", response.body()!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("commentFailed", t.message!!)
                responseLiveData.value = ""
            }
        })
        return responseLiveData



    }
}