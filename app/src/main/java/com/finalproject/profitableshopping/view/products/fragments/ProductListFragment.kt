package com.finalproject.profitableshopping.view.products.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.delete_category.view.*

class ProductListFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productsRv: RecyclerView
    private lateinit var searchEt: EditText
    private var adapter: ProductAdapter = ProductAdapter(emptyList())
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingProgressBar: ContentLoadingProgressBar
    var callbacks: Callbacks? = null

    override fun onStart() {
        super.onStart()
        searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productViewModel.productsListLiveData.observe(
                    viewLifecycleOwner,
                    Observer { prodcts ->
                        showProgress(false)
                        updateUI(prodcts)
                    }
                )
            }
        })
    }

    private fun filterList(filterItem: String) {
        var tempList: MutableList<Product> = ArrayList();
        for (d in adapter.productsList) {
            if (filterItem in d.name) {
                tempList.add(d)
            }
        }
        adapter.updateList(tempList)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        productViewModel.refresh()
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            //next line still need some config
            R.layout.fragment_product_list, container, false
        )
        productsRv = view.findViewById(R.id.rv_product)
        searchEt = view.findViewById(R.id.et_search_product)
        progressBar = view.findViewById(R.id.progress_circular)
        productsRv.layoutManager = GridLayoutManager(context, 2)
        productsRv.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        productViewModel.productsListLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
                showProgress(false)
                updateUI(prodcts)
            }
        )
    }


    private fun updateUI(productsList: List<Product>) {
        adapter = ProductAdapter(productsList)
        productsRv.adapter = adapter
    }

    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private inner class ProductHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        // need to change next variable inflate to be comfortable with product item xml file
        var productImageIv = view.findViewById(R.id.ImgV_product) as ImageView
        var productNameTv: TextView = view.findViewById(R.id.tv_name_product) as TextView
        var productRialPriceTv: TextView = view.findViewById(R.id.tv_price_product) as TextView
        var productDescriptionTv: TextView =
            view.findViewById(R.id.tv_description_product) as TextView
        var product = Product()

        init {
            view.setOnClickListener(this)
        }

        fun bind(pro: Product) {
            product = pro
            productNameTv.text = pro.name
            productRialPriceTv.text = pro.rialPrice.toString()
            productDescriptionTv.text = pro.description

            if (product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = product.images[0].getUrl()
                    it.load(path)
                        .resize(150, 150)
                        .centerCrop()
                        .placeholder(R.drawable.laptop)
                        .into(productImageIv)
                }
            } else {
                productImageIv.setImageResource(R.drawable.laptop)
            }
        }

        override fun onClick(p0: View?) {
            //to open product fragment to  display  product details
            callbacks?.onItemSelected(this.product.id)
        }
    }

    private inner class ProductAdapter(var productsList: List<Product>) :
        RecyclerView.Adapter<ProductHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.list_item_product,
                parent, false
            )
            return ProductHolder(view)
        }

        override fun onBindViewHolder(holder: ProductHolder, position: Int) {
            val product = productsList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productsList.size
        }

        fun updateList(list: List<Product>) {
            productsList = list
            notifyDataSetChanged();
        }
    }


    /////////////////////////////////////////////////////

    interface Callbacks {
        fun onItemSelected(itemId: Int)
        // fun onFloatButtonClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): ProductListFragment {
            return ProductListFragment();
        }
    }


}