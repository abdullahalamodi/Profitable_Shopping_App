package com.finalproject.profitableshopping.view.products.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.MainActivity
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

private const val ARG_PRODUCT_ID = "product_id"

class ProductDetailsFragment : Fragment() {
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
    lateinit var updateBtn: Button
    lateinit var callbacks: Callbacks
    lateinit var product: Product
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
        btnsLayout = view.findViewById(R.id.product_btns_layout) as LinearLayout
        productDescriptionTv =
            view.findViewById(R.id.tv_details_product) as TextView



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
            ?.commit()
        callbacks.onDeleteProductClicked()
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
}