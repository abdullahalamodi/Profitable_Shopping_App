package com.finalproject.profitableshopping.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Complain
import com.finalproject.profitableshopping.data.models.Report
import com.finalproject.profitableshopping.data.repositories.ReportRepositry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportViewModel:ViewModel() {
    val reportRepositry:ReportRepositry
    var productIdLiveData=MutableLiveData<String>()
    var userIdLiveData=MutableLiveData<String>()
    var productIDetailsLiveData = Transformations.switchMap(productIdLiveData) { productId ->
        getProductReports(productId)
    }
    var userIDetailsLiveData = Transformations.switchMap(userIdLiveData) { userId ->
        getUserReports(userId)
    }
    init {
        reportRepositry= ReportRepositry()
    }
    fun addReport(report: Report): LiveData<String> {
        val message= MutableLiveData<String>()
        val call=reportRepositry.addReport(report)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                message.value=response.body()
                Log.d("success add report",message.value!!)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                message.value=t.message
                Log.d("faild add report",t.message!!)
            }
        })
        return  message
    }
    fun getUserReports(userId:String):MutableLiveData<List<Report>>{
        val reportsLiveData: MutableLiveData<List<Report>> = MutableLiveData()
        val call=reportRepositry.getUserReports(userId)
        call.enqueue(object :Callback<List<Report>>{
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
               reportsLiveData.value=response.body()
                Log.d("success get user report","success get user report")
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
               reportsLiveData.value= emptyList()
                Log.d("faild get user report",t.message!!)
            }

        })


        return reportsLiveData
    }
    fun getProductReports(producIdt:String):MutableLiveData<List<Report>>{
        val reportsLiveData: MutableLiveData<List<Report>> = MutableLiveData()
        val call=reportRepositry.getProductReports(producIdt)
        call.enqueue(object :Callback<List<Report>>{
            override fun onResponse(call: Call<List<Report>>, response: Response<List<Report>>) {
               reportsLiveData.value=response.body()
                Log.d("success get pro report","success get product report")
            }

            override fun onFailure(call: Call<List<Report>>, t: Throwable) {
               reportsLiveData.value= emptyList()
                Log.d("faild get pro report",t.message!!)
            }

        })


        return reportsLiveData
    }
}