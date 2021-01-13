package com.finalproject.profitableshopping.view.products.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() , AdapterView.OnItemSelectedListener {
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
    private lateinit var progressBar: ProgressBar

    companion object {
        val PICK_IMAGES_REQUST = 0
    }

    override fun onStart() {
        super.onStart()
        pickImagesBtn.setOnClickListener {
            showProgress(true)
            pickImages()



        }
        addProductBtn.setOnClickListener {
                showProgress(true)
            val product = HashMap<String,Any>()

            product["name"] = productNameET.text.toString()
            product["description"] = productdescriptionET.text.toString()
            product["rial_price"] = productRialPriceET.text.toString().toDouble()
            product["dollar_price"] = productDollarPriceET.text.toString().toDouble()
            product["user_id"] = 0
            product["quantity"] = productQuantityET.text.toString().toInt()
            product["category_id"] = selectedCategoryId
            productViewModel.addProduct(product).observe(
                this,
                Observer {
                    showProgress(false)
                    productViewModel.uploadImage(images,it,1)

                }
            )
            showProgress(false)
          // productViewModel.uploadImage(images, 1, 1)


        }
    }
    private fun showProgress(show:Boolean){
        if (show)
            progressBar.visibility  = View.VISIBLE
        else
            progressBar.visibility  = View.GONE
    }
    private fun pickImages() {
        showProgress(false)
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

   // <<<<<<< HEAD
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_product_add,container,false)
        selectCategorySv=view.findViewById(R.id.spinner_all_category)
        productNameET=view.findViewById(R.id.et_name_product)
        productdescriptionET=view.findViewById(R.id.et_desc_product)
        productRialPriceET=view.findViewById(R.id.et_rial_price_product)
       productDollarPriceET=view.findViewById(R.id.et_dollar_price_product)
        productQuantityET=view.findViewById(R.id.et_quantity_product)
        pickImagesBtn=view.findViewById(R.id.btn_load_photo_product)
       addProductBtn=view.findViewById(R.id.btn_add_product)
       progressBar = view.findViewById(R.id.progress_circular)

        return return view
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
        addProductBtn.isEnabled = !(categoriesName.isEmpty()||
                productNameET.text.isEmpty()||
                productdescriptionET.text.isEmpty()||
                productRialPriceET.text.isEmpty()||
                productDollarPriceET.text.isEmpty()||
                productQuantityET.text.isEmpty()||
                images.size==0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUST && resultCode == RESULT_OK && data != null && data.data != null) {

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

        showProgress(true)
        categoryViewModel.getCategoryByName(selectCategorySv.prompt.toString()).observe(
            this,
            Observer {
                showProgress(false)
                selectedCategoryId = it.id!!
            }
        )
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        selectCategorySv.prompt =
            getString(com.finalproject.profitableshopping.R.string.spiner_nothing_selected_message)
    }


}