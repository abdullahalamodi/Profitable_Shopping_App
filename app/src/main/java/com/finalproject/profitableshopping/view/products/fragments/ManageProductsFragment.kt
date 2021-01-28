package com.finalproject.profitableshopping.view.products.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

private const val ARG_PRODUCT_ID = "product_id"

class ManageProductsFragment : Fragment() {
    private lateinit var userId: String
    private lateinit var productViewModel: ProductViewModel
    private lateinit var manageProductsRv: RecyclerView
    private lateinit var productSearchEt: EditText
    private lateinit var addProductBtn: Button
    private var openMoreOptions = true
    private var adapterManageProduct: ManageProductsFragment.ManageProductAdapter =
        ManageProductAdapter(emptyList())
    private lateinit var progressBar: ProgressBar
    var callbacks: Callbacks? = null


    override fun onStart() {
        super.onStart()
        addProductBtn.setOnClickListener {
            callbacks?.onAddProductClicked()
        }
        productSearchEt.addTextChangedListener(object : TextWatcher {
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
        var tempList: MutableList<Product> = ArrayList()
        for (d in adapterManageProduct.productsList) {
            if (filterItem in d.name) {
                tempList.add(d)
            }
        }
        adapterManageProduct.updateList(tempList)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        userId = AppSharedPreference.getUserId(context!!)!!
        productViewModel.refreshUserList(userId)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_manage_products, container, false)
        manageProductsRv = view.findViewById(R.id.rv_manage_product)
        productSearchEt = view.findViewById(R.id.et_search_manage_product)
        addProductBtn = view.findViewById(R.id.btn_add_manage_product)
        progressBar = view.findViewById(R.id.progress_circular_manage_product)
        manageProductsRv.layoutManager = LinearLayoutManager(context)
        manageProductsRv.adapter = adapterManageProduct
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        productViewModel.userProductsLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
                showProgress(false)
                updateUI(prodcts)
            })
    }


    private fun updateUI(productsList: List<Product>) {
        adapterManageProduct = ManageProductAdapter(productsList)
        manageProductsRv.adapter = adapterManageProduct
    }

    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }


    private fun onProductDeleted() {
        productViewModel.refreshUserList(userId)
    }

    private inner class ManageProductHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var manageProductImageIv = view.findViewById(R.id.img_manage_product) as ImageView
        var manageProductNameTv: TextView =
            view.findViewById(R.id.tv_name_manage_product) as TextView
        var manageProductRialPriceTv: TextView =
            view.findViewById(R.id.tv_price_manage_product) as TextView
        var manageProductMoreOptionIV: ImageView =
            view.findViewById(R.id.img_more_options_product) as ImageView
        var manageProductUpdateOptionTV: TextView =
            view.findViewById(R.id.tv_update_manage_product) as TextView
        var manageProductDeleteOptionTV: TextView =
            view.findViewById(R.id.tv_delete_manage_product) as TextView
        var productUpdateDeleteLy: LinearLayout =
            view.findViewById(R.id.ly_update_delete_product) as LinearLayout

        var product = Product()

        init {
            view.setOnClickListener(this)
        }


        fun bind(pro: Product) {
            product = pro
            manageProductNameTv.text = pro.name
            manageProductRialPriceTv.text = "RY:" + pro.rialPrice.toString()

            manageProductMoreOptionIV.setOnClickListener {
                if (openMoreOptions) {
                    openMoreOptions = false
                    productUpdateDeleteLy.visibility = View.VISIBLE
                } else {
                    openMoreOptions = true
                    productUpdateDeleteLy.visibility = View.GONE
                }

                manageProductUpdateOptionTV.setOnClickListener {
                    callbacks?.onUpdateProductClicked(pro.id.toString())
                }

                manageProductDeleteOptionTV.setOnClickListener {
                    productViewModel.refresh()
                    showProgress(true)
                    productViewModel.deleteProduct(pro.id.toString()).observe(
                        viewLifecycleOwner,
                        Observer {
                            showProgress(false)
                            context?.showMessage("product deleted successfully")
                            onProductDeleted()
                        }
                    )
                }
            }

            if (product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = product.images[0].getUrl()
                    it.load(path)
                        .resize(45, 45)
                        .centerCrop()
                        .placeholder(R.drawable.ic_phone_android)
                        .into(manageProductImageIv)
                }
            } else {
                manageProductImageIv.setImageResource(R.drawable.ic_phone_android)
            }
        }

        override fun onClick(p0: View?) {
//            callbacks?.onProductClicked(product.id)
        }
    }

    private inner class ManageProductAdapter(var productsList: List<Product>) :
        RecyclerView.Adapter<ManageProductHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProductHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.list_item_manage_product,
                parent, false
            )
            return ManageProductHolder(view)
        }

        override fun onBindViewHolder(holder: ManageProductHolder, position: Int) {
            val product = productsList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productsList.size
        }

        fun updateList(list: List<Product>) {
            productsList = list
            notifyDataSetChanged()
        }
    }

    interface Callbacks {
        fun onProductClicked(itemId: Int)
        fun onUpdateProductClicked(productId: String?)
        fun onDeleteProductClicked()
        fun onAddProductClicked()
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
        fun newInstance() = ManageProductsFragment()
    }
}