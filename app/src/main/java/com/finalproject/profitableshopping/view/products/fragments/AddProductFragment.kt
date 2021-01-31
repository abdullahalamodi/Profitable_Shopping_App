package com.finalproject.profitableshopping.view.products.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.getFileName
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.UploadRequestBody
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

private const val ARG_PRODUCT_ID = "product_id"

class AddProductFragment : Fragment(),
    UploadRequestBody.UploadCallback {
    lateinit var productNameET: EditText
    lateinit var productdescriptionET: EditText
    lateinit var productRialPriceET: EditText
    lateinit var productDollarPriceET: EditText
    lateinit var productQuantityET: EditText
    lateinit var pickImagesV: ImageView
    lateinit var pickImagesV2: ImageView
    lateinit var pickImagesV3: ImageView
    lateinit var selectCategorySv: Spinner
    lateinit var addProductBtn: Button
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoriesName: MutableList<String>
    lateinit var categoriesList: MutableList<Category>
    lateinit var callbacks: Callbacks
    private var imagesUris: MutableList<Uri> = mutableListOf()
    var selectedCategoryId = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var btnsLayout: LinearLayout
    private var productId: String? = null
    private var userId: String? = null
    private var isUpdate = false
    private var pickedImagePosition: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = (context as Callbacks)
    }

    override fun onStart() {
        super.onStart()
        pickImagesV.setOnClickListener {
            showProgress(true)
            pickImages()
            pickedImagePosition = 0
        }
        pickImagesV2.setOnClickListener {
            showProgress(true)
            pickImages()
            pickedImagePosition = 1
        }
        pickImagesV3.setOnClickListener {
            showProgress(true)
            pickImages()
            pickedImagePosition = 2
        }
        addProductBtn.setOnClickListener {
            if (checkFields()) {
                showProgress(true)
                val product = Product(
                    name = productNameET.text.toString(),
                    quantity = productQuantityET.text.toString().toInt(),
                    description = productdescriptionET.text.toString(),
                    rialPrice = productRialPriceET.text.toString().toDouble(),
                    dollarPrice = productDollarPriceET.text.toString().toDouble(),
                    categoryId = selectedCategoryId,
                    userId = AppSharedPreference.getUserId(requireContext())!!
                )
                if (isUpdate) {
                    updateProduct(product)
                    showProgress(false)
                } else {
                    addProduct(product)
                    showProgress(false)
                }
            } else {
                Toast.makeText(context, "some fields empty", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun addProduct(product: Product) {
        productViewModel.addProduct(product).observe(
            this,
            Observer { productId ->
                if (imagesUris.isNotEmpty()) {
                    uploadImage(productId).observe(
                        viewLifecycleOwner,
                        Observer {
                            if (!it.isNullOrEmpty()) {
                                Log.d("images", it)
                                Log.d("user",userId!!)
                                productViewModel.refreshUserList(userId!!)
                                Toast.makeText(context, "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT)
                                    .show()
                                callbacks.onSuccessAddProduct()
                            }
                        }
                    )
                } else {
                    callbacks.onSuccessAddProduct()
                    productViewModel.refreshUserList(userId!!)
                    Toast.makeText(context, "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )
    }

    private fun updateProduct(product: Product) {
        product.id = Integer.valueOf(productId!!)
        productViewModel.updateProduct(product).observe(
            this,
            Observer { productId ->
                if (imagesUris.isNotEmpty()) {
                    uploadImage(productId).observe(
                        viewLifecycleOwner,
                        Observer {
                            if (!it.isNullOrEmpty()) {
                                callbacks.onSuccessAddProduct()
                                context?.showMessage("تم نعديل المنتج بنجاح")
                                productViewModel.refreshUserList(userId!!)
                            }
                        }
                    )
                } else {
                    showProgress(false)
                    callbacks.onSuccessAddProduct()
                    productViewModel.refresh()
                    context?.showMessage("تم نعديل المنتج بنجاح")
                    productViewModel.refresh()
                }
            }
        )
    }


    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }


    private fun pickImages() {
        showProgress(false)
        openImageChooser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        categoriesList = emptyList<Category>().toMutableList()
        categoriesName = emptyList<String>().toMutableList()
<<<<<<< HEAD
        userId=AppSharedPreference.getUserId(requireContext())
=======
        userId = AppSharedPreference.getUserId(requireContext())
>>>>>>> upstream/main
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            if (productId != null)
                productViewModel.loadProduct(productId!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product_add, container, false)
        selectCategorySv = view.findViewById(R.id.spinner_all_category)
        productNameET = view.findViewById(R.id.et_name_product)
        productdescriptionET = view.findViewById(R.id.et_desc_product)
        productRialPriceET = view.findViewById(R.id.et_rial_price_product)
        productDollarPriceET = view.findViewById(R.id.et_dollar_price_product)
        productQuantityET = view.findViewById(R.id.et_quantity_product)
        pickImagesV = view.findViewById(R.id.load_image_btn)
        pickImagesV2 = view.findViewById(R.id.load_image_btn2)
        pickImagesV3 = view.findViewById(R.id.load_image_btn3)
        addProductBtn = view.findViewById(R.id.btn_add_product)
        progressBar = view.findViewById(R.id.progress_circular)
        imagesUris = mutableListOf()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        loadCategories()
        if (productId != null) {
            productViewModel.productIDetailsLiveData.observe(
                viewLifecycleOwner,
                Observer { product ->
                    updateUi(product)
                }
            )
        }

    }

    private fun loadCategories() {
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer {
                showProgress(false)
                for (item in it) {
                    if(item.isActive==1) {
                        categoriesName.add(item.name)
                        categoriesList.add(item)
                    }
                }
                //spinner adapter
                val dataAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    categoriesName
                )

                dataAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )
                selectCategorySv.adapter = dataAdapter

                selectCategorySv.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            val item = categoriesList[p2]
                            selectCategorySv.prompt = item.name
                            selectedCategoryId = categoriesList[p2].id!!
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            selectCategorySv.prompt =
                                getString(R.string.spiner_nothing_selected_message)
                        }


                    }
            }
        )
    }


    private fun updateUi(product: Product) {
        isUpdate = true
        addProductBtn.text = "تعديل"
        productNameET.setText(product.name)
        productRialPriceET.setText(product.rialPrice.toString())
        productDollarPriceET.setText(product.dollarPrice.toString())
        productQuantityET.setText(product.quantity.toString())
        productdescriptionET.setText(product.description)
        selectedCategoryId = product.categoryId
        userId = product.userId
        if (product.images.isNotEmpty())
            Picasso.get().also {
                val path = product.images[0].getUrl()
                it.load(path)
                    .resize(45, 45)
                    .centerCrop()
                    .placeholder(R.drawable.shoe)
                    .into(pickImagesV)
            }
<<<<<<< HEAD
        if(product.images.size >1)
=======
        if (product.images.size > 1)
>>>>>>> upstream/main
        Picasso.get().also {
            val path = product.images[1].getUrl()
            it.load(path)
                .resize(45, 45)
                .centerCrop()
                .placeholder(R.drawable.shoe)
                .into(pickImagesV2)
        }
<<<<<<< HEAD
        if(product.images.size >2)
=======
        if (product.images.size > 2)
>>>>>>> upstream/main
        Picasso.get().also {
            val path = product.images[2].getUrl()
            it.load(path)
                .resize(45, 45)
                .centerCrop()
                .placeholder(R.drawable.shoe)
                .into(pickImagesV3)
        }


    }

    private fun checkFields(): Boolean {
        return !(
                categoriesName.isEmpty() ||
                        productNameET.text.isEmpty() ||
                        productdescriptionET.text.isEmpty() ||
                        productRialPriceET.text.isEmpty() ||
                        productDollarPriceET.text.isEmpty() ||
                        productQuantityET.text.isEmpty())
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    imagesUris.add(data?.data!!)
                    if (pickedImagePosition == 0)
                        pickImagesV.setImageURI(imagesUris[pickedImagePosition])
                    if (pickedImagePosition == 1)
                        pickImagesV2.setImageURI(imagesUris[pickedImagePosition])
                    if (pickedImagePosition == 2)
                        pickImagesV3.setImageURI(imagesUris[pickedImagePosition])
                }
            }
        }
    }

    private fun uploadImage(productId: String): MutableLiveData<String> {
        val responseLiveData = MutableLiveData<String>()
        if (imagesUris.isEmpty()) {
            context?.showMessage("Select an Images First")
            responseLiveData.value = ""
            return responseLiveData
        }
        showProgress(true)
        val body: MutableList<RequestBody> = mutableListOf()
        var file2 = File("")
        var file3 = File("")
        val parcelFileDescriptor =
            context?.contentResolver?.openFileDescriptor(imagesUris[0], "r", null)
                ?: return responseLiveData
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file1 =
            File(context?.cacheDir, context?.contentResolver?.getFileName(imagesUris[0])!!)
        val outputStream = FileOutputStream(file1)
        inputStream.copyTo(outputStream)
        progressBar.progress = 0
        if (imagesUris.size > 1) {
            val parcelFileDescriptor2 =
                context?.contentResolver?.openFileDescriptor(imagesUris[1], "r", null)
                    ?: return responseLiveData
            val inputStream2 = FileInputStream(parcelFileDescriptor2.fileDescriptor)
            file2 =
                File(context?.cacheDir, context?.contentResolver?.getFileName(imagesUris[1])!!)
            val outputStream2 = FileOutputStream(file2)
            inputStream2.copyTo(outputStream2)
            progressBar.progress = 0
        }
        if (imagesUris.size > 2) {
            val parcelFileDescriptor3 =
                context?.contentResolver?.openFileDescriptor(imagesUris[2], "r", null)
                    ?: return responseLiveData
            val inputStream3 = FileInputStream(parcelFileDescriptor3.fileDescriptor)
            file3 =
                File(context?.cacheDir, context?.contentResolver?.getFileName(imagesUris[2])!!)
            val outputStream3 = FileOutputStream(file3)
            inputStream3.copyTo(outputStream3)
            progressBar.progress = 0
        }


//        val requestBody1: RequestBody = RequestBody.create(MediaType.parse("*/*"), file)
//        val requestBody2: RequestBody = RequestBody.create(MediaType.parse("*/*"), file1)
//        val fileToUpload1 =
//            MultipartBody.Part.createFormData("file1", file.getName(), requestBody1)
//        val fileToUpload2 =
//            MultipartBody.Part.createFormData("file2", file1.getName(), requestBody2)
        body.add(UploadRequestBody(file1, "image", this))
        body.add(UploadRequestBody(file2, "image", this))
        body.add(UploadRequestBody(file3, "image", this))

        val imgBody1 = MultipartBody.Part.createFormData(
            "image1",
            file1.name,
            body[0]
        )
        val imgBody2 = MultipartBody.Part.createFormData(
            "image2",
            file2.name,
            body[1]
        )
        val imgBody3 = MultipartBody.Part.createFormData(
            "image3",
            file3.name,
            body[2]
        )

        productViewModel.uploadImages(
            imgBody1,
            imgBody2,
            imgBody3,
            RequestBody.create(MediaType.parse("multipart/form-data"), productId)
        ).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                progressBar.progress = 0
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    progressBar.progress = 100
                }
            }

        })
        responseLiveData.value = "ok"
        return responseLiveData
    }

    interface Callbacks {
        fun onSuccessAddProduct()
        // fun onFloatButtonClicked()
    }


    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
        fun newInstance(productId: String?) = AddProductFragment().apply {
            arguments = Bundle().apply {
                productId?.let {
                    putString(ARG_PRODUCT_ID, it)
                }
            }
        }
    }


}