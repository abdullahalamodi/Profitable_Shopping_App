package com.finalproject.profitableshopping.view.products.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.core.view.get
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel

class AddProductFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var productNameET: EditText
    lateinit var productdescriptionET: EditText
    lateinit var productRialPriceET: EditText
    lateinit var productDollarPriceET: EditText
    lateinit var productQuantityET: EditText
    lateinit var pickImagesBtn: Button
    lateinit var selectCategorySv: Spinner
    lateinit var addProductBtn: Button
    lateinit var productViewModel: ProductViewModel
    lateinit var categoryViewModel: CategoryViewModel
    var map = HashMap<Int, String>()
    lateinit var categoriesName: MutableList<String>
    lateinit var images: MutableList<Uri>
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

            val product = HashMap<String,String>()


//            product["name"] = productNameET.text.toString()
//            product["description"] = productdescriptionET.text.toString()
//            product["rial_price"] = productRialPriceET.text.toString().toDouble()
//            product["dollar_price"] = productDollarPriceET.text.toString().toDouble()
//            product["user_id"] = 0
//            product["quantity"] = productQuantityET.text.toString().toInt()
//            product["category_id"] = selectedCategoryId
//            val pId = productViewModel.createProduct(product)
//            productViewModel.uploadImage(images, pId, 1)


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
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view=inflater.inflate()
//        selectCategorySv=view.findViewById()
//        productNameET=view.findViewById()
//        productdescriptionET=view.findViewById()
//        productRialPriceET=view.findViewById()
//        productDollarPriceET=view.findViewById()
//        productQuantityET=view.findViewById()
//        pickImagesBtn=view.findViewById()
//        addProductBtn=view.findViewById()
//        return  view
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer {
//                for (item in it) {
//                    categoriesName.add(item.name)
//                }
                //spiner adapter
                val dataAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    categoriesName
                )

               // dataAdapter.setDropDownViewResource(
                //    android.R.layout.simple_spinner_dropdown_item
              //  )

              //  selectCategorySv.adapter = dataAdapter
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            if (data!!.clipData != null) {
                for (i in 0 until 3) {
                    // val imageUri=data.clipData!!.getItemAt(i).uri.toFile()
                    // images!!.add(imageUri)

                    val imageUri = data.data!!
                    images!!.add(imageUri!!)
                    
                }
            }
        }
    }

    fun uploadImage() {


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