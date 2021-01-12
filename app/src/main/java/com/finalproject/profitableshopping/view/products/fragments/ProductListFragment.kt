package com.finalproject.profitableshopping.view.products.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.ProductViewModel

class ProductListFragment : Fragment() {
    private lateinit var productViewModel: ProdductViewModel
    private lateinit var productsRv: RecyclerView
    private var adapter: ProductAdapter = ProductAdapter(emptyList())

    // private lateinit var addFbtn: FloatingActionButton
    private lateinit var loadingProgressBar: ContentLoadingProgressBar
    var callbacks: Callbacks? = null

    override fun onStart() {
        super.onStart()
        /* addFbtn.setOnClickListener {
             //to open product fragment to  add new product
             callbacks?.onFloatButtonClicked()
         }*/

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProdductViewModel::class.java)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            //next line still need some config
            R.layout.fragment_catergory_list, container, false
        )
        productsRv = view.findViewById(R.id.rv_product)
        productsRv.layoutManager = GridLayoutManager(context, 2)
        //  addFbtn =view.findViewById()
        //    loadingProgressBar=view.findViewById()
        productsRv.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.productsListLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
                updateUI(prodcts)

            }
        )

    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(productsList: List<Product>) {
        adapter = ProductAdapter(productsList)
        productsRv.adapter = adapter
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

        }

        override fun onClick(p0: View?) {
            //to open product fragment to  desplay  product details
            callbacks?.onItemSelected(this.product.id)
        }
    }

    private inner class ProductAdapter(val productsList: List<Product>) :
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
    }

    interface Callbacks {
        fun onItemSelected(itemId: Int)
        // fun onFloatButtonClicked()
    }

}