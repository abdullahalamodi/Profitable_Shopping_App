package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.Complain
import com.finalproject.profitableshopping.data.repositories.ComplainRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ComplainViewModel:ViewModel() {
    var complainListLiveData: MutableLiveData<List<Complain>> = MutableLiveData()
    var complainIdLiveData=MutableLiveData<Int>()
    val complainRepositry:ComplainRepositry

    var complainIDetailsLiveData = Transformations.switchMap(complainIdLiveData) { complainId ->
        getComplain(complainId)
    }
    val comp= mutableListOf<Complain>()


    init {



        complainRepositry= ComplainRepositry()
        complainListLiveData=getComplains()
    }
    fun loadComplainr(complainId: Int) {
        complainIdLiveData.value = complainId
    }
    fun getComplains():MutableLiveData<List<Complain>>{
        val complainsLiveData: MutableLiveData<List<Complain>> = MutableLiveData()
         val call=complainRepositry.getComplains()
        call.enqueue(object :Callback<List<Complain>>{
            override fun onResponse(
                call: Call<List<Complain>>,
                response: Response<List<Complain>>
            ) {

                complainsLiveData.value=response.body()?: emptyList()
                Log.d("success get complains",complainsLiveData.value.toString())
            }

            override fun onFailure(call: Call<List<Complain>>, t: Throwable) {
               complainsLiveData.value= emptyList()
                Log.d("faild get complains",t.message!!)
            }

        })

            return  complainsLiveData
    }

    fun getComplain(complainId:Int):LiveData<Complain>{
        val complainsLiveData: MutableLiveData<Complain> = MutableLiveData()
        val call=complainRepositry.getComplain(complainId)
        call.enqueue(object :Callback<Complain>{
            override fun onResponse(
                call: Call<Complain>,
                response: Response<Complain>
            ) {

                complainsLiveData.value=response.body()
                Log.d("success get complain",complainsLiveData.value.toString())
            }

            override fun onFailure(call: Call<Complain>, t: Throwable) {
                complainsLiveData.value= Complain(null)
                Log.d("faild get complains",t.message!!)
            }

        })

        return  complainsLiveData
    }
    fun addComplain(complain: Complain):LiveData<String>{
        val message=MutableLiveData<String>()
        val call=complainRepositry.addComplain(complain)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                message.value=response.body()
                Log.d("success add complain",message.value!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                message.value=t.message
                Log.d("faild add complain",t.message!!)
            }
        })
        return  message
    }
}