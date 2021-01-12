package com.finalproject.profitableshopping.viewmodel

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.data.repositories.ProductRepositry
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

<<<<<<< HEAD:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProductViewModel.kt
class ProductViewModel:ViewModel() {
=======
class ProdductViewModel:ViewModel() {

>>>>>>> main:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProdductViewModel.kt
    val productRepositry: ProductRepositry
    val productsListLiveData: LiveData<List<Product>>

    private val productIdLiveData = MutableLiveData<Int>()
    private val userIdLiveData = MutableLiveData<Int>()

    var productIDetailsLiveData = Transformations.switchMap(productIdLiveData){ proId ->
        getProduct(proId)
    }
    var userProductsListLiveData:LiveData<List<Product>>
    init {
        productRepositry = ProductRepositry()
        productsListLiveData= getProducts()
        this.userProductsListLiveData = Transformations.switchMap(userIdLiveData) { useId ->
            getUserProducts(useId)
        }
<<<<<<< HEAD:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProductViewModel.kt
=======

>>>>>>> main:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProdductViewModel.kt
    }

    fun getProducts(): MutableLiveData<List<Product>>{
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call=productRepositry.getProducts()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value=response.body()?: emptyList()
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Failed ",t.message!!)
            }
        })
        return responseLiveData
    }

    fun loadUser(useId:Int ) {
        userIdLiveData.value = useId
    }

    fun getUserProducts(userId: Int): MutableLiveData<List<Product>>{
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call=productRepositry.getUserProducts(userId )
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value=response.body()?: emptyList()
            }
            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Faild ",t.message!!)
            }
        })
        return responseLiveData
    }

    fun loadProduct(proId:Int ) {
        productIdLiveData.value = proId
    }

    private fun getProduct(proId:Int):MutableLiveData<Product>{
        val responseLiveData: MutableLiveData<Product> = MutableLiveData()
        val call= productRepositry.getProduct(proId)
        call.enqueue(object :Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {

            }

        })
        return responseLiveData
    }

<<<<<<< HEAD:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProductViewModel.kt
    fun createProduct(product: HashMap<String, Any>): MutableLiveData<String> {
         var resulte = MutableLiveData<String>();
        val call = productRepositry.AddProduct(product)
        call.enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
//                resulte.value = response.body()!!
=======
    fun addProduct(product: HashMap<String, Any>): String {
        var resulte = "0"
        val call = productRepositry.addProduct(product)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                resulte = response.body()!!
>>>>>>> main:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProdductViewModel.kt
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {
                resulte.value = t.message!!
            }
        })
        return  resulte
    }
<<<<<<< HEAD:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProductViewModel.kt
    fun editeProduct(proId:Int,product: HashMap<String, String>):MutableLiveData<String>{
=======
    fun updateProduct(proId:Int,product: HashMap<String, Any>):MutableLiveData<String>{
>>>>>>> main:app/src/main/java/com/finalproject/profitableshopping/viewmodel/ProdductViewModel.kt
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call= productRepositry.updateProduct(proId,product)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value=response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
        return responseLiveData


    }
    fun deleteProduct(proId:Int):MutableLiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call= productRepositry.deleteProduct(proId)
        call.enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                responseLiveData.value=response.body()
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
        return responseLiveData

    }

    fun uploadImage(images:List<Uri>,productId:Int,userId:Int) {

        for(im in images){
            val file = im.toFile()
            val requestBody= RequestBody.create(MediaType.parse("*/*"),file)
            val fileToUploaded= MultipartBody.Part.createFormData("file",file.name,requestBody)
            val fileName= RequestBody.create(MediaType.parse("image/*"),file.name
            )
          val  call= productRepositry.uploadImage(fileToUploaded,fileName,productId,userId)
            call.enqueue(
                object :Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {

                    }
                }
            )

        }
    }

}