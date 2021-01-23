package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Comment
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.data.repositories.CommentRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommentViewModel : ViewModel() {

    var commentRepository: CommentRepository

    init {
        commentRepository = CommentRepository()
    }


    fun addComment(comment: Comment): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = commentRepository.addComment(comment)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
                Log.d("added success", responseLiveData.value.toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("failed", t.message!!)
                responseLiveData.value = "-1"
            }
        })
        return responseLiveData
    }

    private fun getComment(id: Int): MutableLiveData<Comment> {
        val responseLiveData: MutableLiveData<Comment> = MutableLiveData()
        val call = commentRepository.getComment(id)
        call.enqueue(object : Callback<Comment> {
            override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                responseLiveData.value = response.body() ?: Comment(id = -1)
            }
            override fun onFailure(call: Call<Comment>, t: Throwable) {
                Log.d(" Get comment failed", t.message!!)
                responseLiveData.value = Comment(id = -1)
            }
        })
        return responseLiveData
    }

     fun getComments(): MutableLiveData<List<Comment>> {
        val responseLiveData: MutableLiveData<List<Comment>> = MutableLiveData()
        val call = commentRepository.getComments()
        call.enqueue(object : Callback<List<Comment>> {
            override fun onResponse(
                call: Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("Get Comments Failed ", t.message!!)
                responseLiveData.value = emptyList()
            }
        })
        return responseLiveData
    }

    fun updateComment(comment: Comment): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = commentRepository.updateComment(comment)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message!!
            }
        })
        return responseLiveData

    }

    fun deleteComment(catId: Int): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = commentRepository.deleteComment(catId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value = response.body()!!
                Log.d("delete comment", response.body()!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value = t.message!!
                Log.d("Delete failed try again", t.message!!)
            }
        })
        return responseLiveData
    }
}