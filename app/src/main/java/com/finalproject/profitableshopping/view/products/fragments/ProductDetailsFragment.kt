package com.finalproject.profitableshopping.view.products.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.report.dialog.ComplainDialog
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

private const val ARG_PRODUCT_ID = "product_id"

class ProductDetailsFragment : Fragment(),AdapterView.OnItemSelectedListener {
    private var productId: String? = null
    private lateinit var progressBar: ProgressBar
    lateinit var productViewModel: ProductViewModel
    lateinit var productImageIv: ImageView
    lateinit var productNameTv: TextView
    lateinit var productQuantityTv: TextView
    lateinit var productReviewsTv: TextView
    lateinit var productRialPriceTv: TextView
    lateinit var productDollarPriceTv: TextView
    lateinit var productDescriptionTv: TextView
    lateinit var deleteBtn: Button
    lateinit var reportBtn: Button
    lateinit var updateBtn: Button
    lateinit var callbacks: Callbacks
    lateinit var product: Product
    var  userId=""
    private lateinit var btnsLayout: LinearLayout


    override fun onStart() {
        super.onStart()
        callbacks = (context as Callbacks)
        showUpdateDeleteBtns()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            productViewModel.loadProduct(productId!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_products_details, container, false)
        progressBar = view.findViewById(R.id.progress_circular)
        productImageIv = view.findViewById(R.id.product_image_v) as ImageView
        productNameTv = view.findViewById(R.id.product_name_Tv) as TextView
        productReviewsTv = view.findViewById(R.id.reviews_tv) as TextView
        productQuantityTv = view.findViewById(R.id.quantity_tv) as TextView
        productRialPriceTv = view.findViewById(R.id.tv_price_rial) as TextView
        productDollarPriceTv = view.findViewById(R.id.tv_price_dollar) as TextView
        deleteBtn = view.findViewById(R.id.product_delete_btn) as Button
        updateBtn = view.findViewById(R.id.product_update_btn) as Button
        reportBtn = view.findViewById(R.id.product_report_btn) as Button
        btnsLayout = view.findViewById(R.id.product_btns_layout) as LinearLayout
        productDescriptionTv =
            view.findViewById(R.id.tv_details_product) as TextView


        reportBtn.setOnClickListener {
          ComplainDialog.newInstance(productId!!,userId).apply {
              show(this@ProductDetailsFragment.getParentFragmentManager(), "report")
          }

        }
        deleteBtn.setOnClickListener {
            productViewModel.refresh()
            showProgress(true)
            productViewModel.deleteProduct(productId.toString()).observe(
                viewLifecycleOwner,
                Observer {
                    showProgress(false)
                    context?.showMessage("product deleted successfully")
                    onProductDeleted()
                }
            )
        }

        updateBtn.setOnClickListener {
            callbacks.onUpdateProductClicked(productId)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        productViewModel.productIDetailsLiveData.observe(
            viewLifecycleOwner,
            Observer { product ->
                showProgress(false)
                if(product != null){
                this.product = product
                updateUi(product)
                    userId=product.userId.toString()
                }else{
                    context?.showMessage("product not found")
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

    private fun showUpdateDeleteBtns() {
        AppSharedPreference
        if (AppSharedPreference.isActive(context!!))
            btnsLayout.visibility = View.GONE
        else
            btnsLayout.visibility = View.VISIBLE
    }

    private fun updateUi(product: Product) {
        productNameTv.text = product.name
        productRialPriceTv.text = product.rialPrice.toString()
        productDollarPriceTv.text = product.dollarPrice.toString()
        productQuantityTv.text = product.quantity.toString()
        productDescriptionTv.text = product.description
        if (product.images.isNotEmpty())
            Picasso.get().also {
                val path = product.images[0].getUrl()
                it.load(path)
                    .resize(350, 350)
                    .centerCrop()
                    .placeholder(R.drawable.shoe)
                    .into(productImageIv)
            }

    }

    private fun onProductDeleted() {
       /* activity?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(this)
            ?.commit()8*/
        callbacks.onDeleteProductClicked()
    }
    fun openOptionsDialog(pro: Product){
        var alertBuilder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.cart_item_option_dialog, null)
        val quantityEd=view.findViewById(R.id.ed_category_name) as EditText
        val colorSV=view.findViewById(R.id.spinner_product_color) as Spinner
        val sizeSV=view.findViewById(R.id.spinner_product_size) as Spinner
        val addToCartBtn=view.findViewById<Button>(R.id.btn_add_to_cart)
        val exitBtn=view.findViewById<Button>(R.id.btn_exit)
        alertBuilder.setView(view)
        var alertDialog = alertBuilder.create()
        alertDialog.show()
        val colors= listOf<String>("red","blue","yelew")
        val sizes= listOf<String>("L","Xl","XXL")
        val colorAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            colors
        )
        val sizeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            sizes
        )

        colorAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        colorSV.adapter = colorAdapter

        sizeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        sizeSV.adapter = sizeAdapter


    }

    companion object {
        @JvmStatic
        fun newInstance(productId: String) =
            ProductDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_ID, productId)
                }
            }
    }

    interface Callbacks {
        fun onUpdateProductClicked(productId: String?)
        fun onDeleteProductClicked()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.get(p2).toString()

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}