package com.finalproject.profitableshopping.view.products.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.core.view.get
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel

class AddProductFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var productNameET: EditText
    lateinit var productdescriptionET: EditText
    lateinit var productRialPriceET: EditText
    lateinit var productDollarPriceET: EditText
    lateinit var productQuantityET: EditText
    lateinit var pickImagesBtn: ImageView
    lateinit var selectCategorySv: Spinner
    lateinit var addProductBtn: Button
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoriesName: MutableList<String>
    lateinit var categoriesList: MutableList<Category>
    lateinit var images: MutableList<Uri>
    lateinit var callbacks: Callbacks
    var selectedCategoryId = 0
    private lateinit var progressBar: ProgressBar

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = (context as Callbacks)
    }

    override fun onStart() {
        super.onStart()
        pickImagesBtn.setOnClickListener {
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
                    userId = 0
                )

                productViewModel.addProduct(product).observe(
                    this,
                    Observer {

//                        productViewModel.uploadImage(images, productId, 1).observe(
//                            this,
//                            Observer {
//                                showProgress(false)
//
//                            }
//                        )
                        callbacks.onSuccessAddProduct()
                        Toast.makeText(context, "تم اضافة المنتج بنجاح", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
                // productViewModel.uploadImage(images, 1, 1)
            } else {
                Toast.makeText(context, "some fields empty", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun pickImages() {
        showProgress(false)
        var intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(S)"), PICK_IMAGES_REQUEST)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        categoriesList = emptyList<Category>().toMutableList()
        categoriesName = emptyList<String>().toMutableList()
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
        pickImagesBtn = view.findViewById(R.id.btn_load_photo_product)
        addProductBtn = view.findViewById(R.id.btn_add_product)
        progressBar = view.findViewById(R.id.progress_circular)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer {
                showProgress(false)
                for (item in it) {
                    categoriesName.add(item.name)
                    categoriesList.add(item)
                }
                //spiner adapter
                val dataAdapter = ArrayAdapter<String>(
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

    fun checkFields(): Boolean {
        return !(
                categoriesName.isEmpty() ||
                        productNameET.text.isEmpty() ||
                        productdescriptionET.text.isEmpty() ||
                        productRialPriceET.text.isEmpty() ||
                        productDollarPriceET.text.isEmpty() ||
                        productQuantityET.text.isEmpty())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            if (data!!.clipData != null) {
                for (i in 0 until 3) {
                    // val imageUri=data.clipData!!.getItemAt(i).uri.toFile()
                    // images!!.add(imageUri)

                    val imageUri = data.data!!
                    pickImagesBtn.setImageURI(imageUri)
                    images!!.add(imageUri!!)


                }
            }
        }
    }

    fun uploadImage() {

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


    companion object {
        const val PICK_IMAGES_REQUEST = 0
        fun newInstance(): AddProductFragment {
            return AddProductFragment();
        }
    }


}