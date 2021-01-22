package com.finalproject.profitableshopping.view.products.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
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

class AddProductFragment : Fragment(), AdapterView.OnItemSelectedListener,
    UploadRequestBody.UploadCallback {
    lateinit var productNameET: EditText
    lateinit var productdescriptionET: EditText
    lateinit var productRialPriceET: EditText
    lateinit var productDollarPriceET: EditText
    lateinit var productQuantityET: EditText
    lateinit var pickImagesV: ImageView
    lateinit var selectCategorySv: Spinner
    lateinit var addProductBtn: Button
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoriesName: MutableList<String>
    lateinit var categoriesList: MutableList<Category>
    lateinit var callbacks: Callbacks
    private var selectedImageUri: Uri? = null
    var selectedCategoryId = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var btnsLayout: LinearLayout
    private var productId: String? = null
    private var userId: String? = null
    private var isUpdate = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = (context as Callbacks)
    }

    override fun onStart() {
        super.onStart()
        pickImagesV.setOnClickListener {
            showProgress(true)
            pickImages()
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
                    userId = AppSharedPreference.getUserId(context!!)!!.toInt()
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
                showProgress(true)
                uploadImage(productId).observe(
                    viewLifecycleOwner,
                    Observer {
                        productViewModel.refresh()
                        callbacks.onSuccessAddProduct()
                        Toast.makeText(context, "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT)
                            .show()
                   showProgress(false) }
                )

            }
        )
    }

    private fun updateProduct(product: Product) {
        product.id = Integer.valueOf(productId!!)
        productViewModel.updateProduct(product).observe(
            this,
            Observer { productId ->
                showProgress(true)
                if(selectedImageUri != null){
                    uploadImage(productId).observe(
                        viewLifecycleOwner,
                        Observer {
                            callbacks.onSuccessAddProduct()
                            context?.showMessage("تم نعديل المنتج بنجاح")
                            productViewModel.refresh()
                            showProgress(false)
                        }
                    )

                }else{
                    showProgress(false)
                    productViewModel.refresh()
                    callbacks.onSuccessAddProduct()
                    context?.showMessage("تم نعديل المنتج بنجاح")
                    productViewModel.refresh()
                }
                callbacks.onSuccessAddProduct()
                Toast.makeText(context, "تم نعديل المنتج بنجاح", Toast.LENGTH_SHORT)
                    .show()
                productViewModel.refresh()
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
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            if(productId!=null)
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
        pickImagesV = view.findViewById(R.id.btn_load_photo_product)
        addProductBtn = view.findViewById(R.id.btn_add_product)
        progressBar = view.findViewById(R.id.progress_circular)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        loadCategories()
        if(productId !=null){
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
                    categoriesName.add(item.name)
                    categoriesList.add(item)
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
        userId = product.userId.toString()
            if (product.images.isNotEmpty())
            Picasso.get().also {
                val path = product.images[0].getUrl()
                it.load(path)
                    .resize(350, 350)
                    .centerCrop()
                    .placeholder(R.drawable.shoe)
                    .into(pickImagesV)
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
                    selectedImageUri = data?.data
                    pickImagesV.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage(productId: String):MutableLiveData<String> {
        val responseLiveData = MutableLiveData<String>()
        if (selectedImageUri == null) {
            context?.showMessage("Select an Image First")
            responseLiveData.value = ""
            return responseLiveData
        }
        showProgress(true)
        val parcelFileDescriptor =
            context?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null) ?:
        return responseLiveData
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =
            File(context?.cacheDir, context?.contentResolver?.getFileName(selectedImageUri!!)!!)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        productViewModel.uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), productId)
        ).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                context?.showMessage(t.message!!)
                progressBar.progress = 0
                responseLiveData.value=t.message
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    context?.showMessage(it)
                    progressBar.progress = 100
                    showProgress(false)
                    responseLiveData.value=it
                }
            }

        })
        responseLiveData.value = ""
        return responseLiveData
    }

    interface Callbacks {
        fun onSuccessAddProduct()
        // fun onFloatButtonClicked()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.get(p2).toString()
        selectCategorySv.prompt = item
        selectedCategoryId = categoriesList[p2].id!!
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        selectCategorySv.prompt =
            getString(R.string.spiner_nothing_selected_message)
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