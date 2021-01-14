package com.finalproject.profitableshopping.view.products.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

private const val ARG_PRODUCT_ID = "product_id"

class ProductDetailsFragment : Fragment() {
    private var productId: String? = null
    lateinit var productViewModel: ProductViewModel
    lateinit var productImageIv: ImageView
    lateinit var productNameTv: TextView
    lateinit var productRialPriceTv: TextView
    lateinit var productDescriptionTv: TextView


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
        productImageIv = view.findViewById(R.id.product_image_v) as ImageView
        productNameTv = view.findViewById(R.id.product_name_Tv) as TextView
        productRialPriceTv = view.findViewById(R.id.tv_price_products) as TextView
        productDescriptionTv =
            view.findViewById(R.id.tv_details_product) as TextView

        return view;
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.productIDetailsLiveData.observe(
            viewLifecycleOwner,
            Observer { product ->
                updateUi(product)
            }
        )
    }

    private fun updateUi(product: Product) {
        productNameTv.text = product.name
        productRialPriceTv.text = product.rialPrice.toString()
        productDescriptionTv.text = product.description
//        Picasso.get().load(product.images[0].imagePath).into(productImageIv);
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
}