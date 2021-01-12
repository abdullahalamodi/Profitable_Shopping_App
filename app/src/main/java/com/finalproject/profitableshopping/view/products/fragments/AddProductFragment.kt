package com.finalproject.profitableshopping.view.products.fragments

import android.app.Activity
import android.content.Intent
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
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProdductViewModel
import java.io.File


class AddProductFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var productNameET: EditText
    lateinit var productdescriptionET: EditText
    lateinit var productRialPriceET: EditText
    lateinit var productDollarPriceET: EditText
    lateinit var productQuantityET: EditText
    lateinit var pickImagesBtn: Button
    lateinit var selectCategorySv: Spinner
    lateinit var addProductBtn: Button
    lateinit var prodductViewModel: ProdductViewModel
    lateinit var categoryViewModel: CategoryViewModel

    var map = HashMap<Int, String>()
    lateinit var categoriesName: MutableList<String>
    lateinit var images: MutableList<File>
    var selectedCategoryId = 0

    companion object {
        val PICK_IMAGES_REQUST = 0
    }

    override fun onStart() {
        super.onStart()

        pickImagesBtn.setOnClickListener {
            pickImages()
        }

        addProductBtn.setOnClickListener {

            val product = HashMap<String, Any>()

            product["name"] = productNameET.text.toString()
            product["description"] = productdescriptionET.text.toString()
            product["rial_price"] = productRialPriceET.text.toString().toDouble()
            product["dollar_price"] = productDollarPriceET.text.toString().toDouble()
            product["user_id"] = 0
            product["quantity"] = productQuantityET.text.toString().toInt()
            product["category_id"] = selectedCategoryId

        }
    }

    private fun pickImages() {
        var intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image(S)"), PICK_IMAGES_REQUST)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prodductViewModel = ViewModelProviders.of(this).get(ProdductViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product_add, container, false)
        selectCategorySv = view.findViewById(R.id.spinner_all_category)
        productNameET = view.findViewById(R.id.et_name_product)
        productdescriptionET = view.findViewById(R.id.et_desc_product)
        productRialPriceET = view.findViewById(R.id.et_dollar_price_product)
        productDollarPriceET = view.findViewById(R.id.et_rial_price_product)
        productQuantityET = view.findViewById(R.id.et_quantity_product)
        pickImagesBtn = view.findViewById(R.id.btn_load_photo_product)
        addProductBtn = view.findViewById(R.id.btn_add_product)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer {

                for (item in it) {
                    categoriesName.add(item.name)

                }
                //spiner adapter
                val dataAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    categoriesName
                )

                dataAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )

                selectCategorySv.adapter = dataAdapter
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddProductFragment.PICK_IMAGES_REQUST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            if (data!!.clipData != null) {
                for (i in 0 until 3) {
                    // val imageUri=data.clipData!!.getItemAt(i).uri.toFile()
                    // images!!.add(imageUri)
                    val imageUri = data.data
                }
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.get(p2).toString()
        selectCategorySv.prompt = item
        categoryViewModel.getCategoryByName(selectCategorySv.prompt.toString()).observe(
            this,
            Observer {
                selectedCategoryId = it.id
            }
        )
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        selectCategorySv.prompt =
            getString(com.finalproject.profitableshopping.R.string.spiner_nothing_selected_message)
    }


}