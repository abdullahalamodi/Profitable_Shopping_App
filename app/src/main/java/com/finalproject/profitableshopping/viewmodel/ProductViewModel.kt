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
import java.io.File

class ProductViewModel : ViewModel() {

    val productRepositry: ProductRepositry
    val productsListLiveData: LiveData<List<Product>>

    private val productIdLiveData = MutableLiveData<Int>()
    private val userIdLiveData = MutableLiveData<Int>()

    var productIDetailsLiveData = Transformations.switchMap(productIdLiveData) { proId ->
        getProduct(proId)
    }
    var userProductsListLiveData: LiveData<List<Product>>

    init {
        productRepositry = ProductRepositry()
        productsListLiveData = getProducts()
        this.userProductsListLiveData = Transformations.switchMap(userIdLiveData) { useId ->
            getUserProducts(useId)
        }
    }

    fun getProducts(): MutableLiveData<List<Product>> {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call = productRepositry.getProducts()
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Failed ", t.message!!)
                responseLiveData.value= emptyList()
            }
        })
        return responseLiveData
    }

    fun loadUser(useId: Int) {
        userIdLiveData.value = useId
    }

    fun getUserProducts(userId: Int): MutableLiveData<List<Product>> {
        val responseLiveData: MutableLiveData<List<Product>> = MutableLiveData()
        val call = productRepositry.getUserProducts(userId)
        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                responseLiveData.value = response.body() ?: emptyList()
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.d("Faild ", t.message!!)
                responseLiveData.value= emptyList()
            }
        })
        return responseLiveData
    }

    fun loadProduct(proId: Int) {
        productIdLiveData.value = proId
    }

    private fun getProduct(proId: Int): MutableLiveData<Product> {
        val responseLiveData: MutableLiveData<Product> = MutableLiveData()
        val call = productRepositry.getProduct(proId)
        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                responseLiveData.value= Product()
                Log.d("Faild ", t.message!!)

            }

        })
        return responseLiveData
    }

    fun addProduct(product: HashMap<String, Any>): MutableLiveData<Int> {

        val responseLiveData: MutableLiveData<Int> = MutableLiveData()

        val call = productRepositry.AddProduct(product)
            call.enqueue(object : Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                responseLiveData.value = response.body()!!
                    Log.d("success",responseLiveData.value.toString())
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Log.d("failed",t.message!!)
                responseLiveData.value = 0

            }
        })
        return responseLiveData
    }

    fun updateProduct(
        proId: Int,
        product: HashMap<String, String>
    ): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = productRepositry.updateProduct(proId, product)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value=t.message
            }
        })
        return responseLiveData

    }


    fun deleteProduct(proId: Int): MutableLiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val call = productRepositry.deleteProduct(proId)
        call.enqueue(object : Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                responseLiveData.value=t.message
            }
        })
        return responseLiveData

    }

    fun uploadImage(images: List<Uri>, productId: Int, userId: Int) {
        lateinit var imageFiles: MutableList<MultipartBody.Part>
        for (im in images) {
            val file = im.toFile()
            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)

            imageFiles.add(MultipartBody.Part.createFormData("file", file.name, requestBody))
           // val fileName = RequestBody.create(MediaType.parse("image/*"), file.name)
        }
            val call = productRepositry.uploadImage(
                imageFiles,
                productId,
                userId
            )
            call.enqueue(
                object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                       Log.d("success",response.body()!!)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("failed",t.message!!)

                    }
                }
            )
        }
    }

