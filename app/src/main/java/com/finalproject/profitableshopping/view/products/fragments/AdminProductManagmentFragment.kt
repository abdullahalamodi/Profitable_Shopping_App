package com.finalproject.profitableshopping.view.products.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class AdminProductManagmentFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    lateinit var adminProductRV: RecyclerView
   private var adapter: AdminMangeAdapter = AdminMangeAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_admin_product_managment, container, false)
        adminProductRV=view.findViewById(R.id.admin_product_rv)
        adminProductRV.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.productsListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
                Toast.makeText(requireContext(),it.size.toString(),Toast.LENGTH_LONG).show()
            }

        )
    }
    private fun updateUI(products: List<Product>) {
        adapter = AdminMangeAdapter(products)
        adminProductRV.adapter = adapter
    }
    inner class AdminMangeHolder(view: View) : RecyclerView.ViewHolder(view) {

        var productImgV = view.findViewById<ImageView>(R.id.ImgV_product)
        var prodctNameTv = view.findViewById<TextView>(R.id.product_name_tv)
        var hideProductBtn = view.findViewById<Button>(R.id.hide_btn)
        var product = Product()
init {
    hideProductBtn.setOnClickListener {
        this.product.isActive = hideProductBtn.text=="إظهار"

        productViewModel.updateProductCase(this.product).observe(
            viewLifecycleOwner,
            Observer {
                Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
            }
        )
        productViewModel.productsListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
            }

        )


    }
}

        fun bind(product: Product) {
            this.product=product
/*
            productViewModel.loadProduct(this.product.id.toString())
            productViewModel.productIDetailsLiveData.observe(
                viewLifecycleOwner,
                Observer { product ->
                    // showProgress(false)
                    if (product != null) {
                        this.product = product
                        prodctNameTv.text = this.product.name
                        Log.d("pro name",product.name)
                    } else {
                        context?.showMessage("product not found")
                    }
                    prodctNameTv.text = this.product.name
                }
            )*/
            prodctNameTv.text = this.product.name

            if(this.product.isActive){
                hideProductBtn.text="إخفاء"
            }else
                hideProductBtn.text="إظهار"


            if (this.product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = this.product.images[0].getUrl()
                    it.load(path)
                        .resize(75, 75)
                        .centerCrop()
                        .placeholder(R.drawable.shoe)
                        .into(productImgV)
                }
            }
        }
    }
    inner class AdminMangeAdapter(val productList:List<Product>):RecyclerView.Adapter<AdminMangeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminMangeHolder {
           val view=layoutInflater.inflate(R.layout.admin_product_manegment_item_list,parent,false)
            return AdminMangeHolder(view)
        }

        override fun onBindViewHolder(holder: AdminMangeHolder, position: Int) {
           val product=productList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productList.size
        }
    }

    companion object {

        fun newInstance() =
            AdminProductManagmentFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}