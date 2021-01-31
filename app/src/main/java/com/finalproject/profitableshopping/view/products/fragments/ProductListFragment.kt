package com.finalproject.profitableshopping.view.products.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

class ProductListFragment : Fragment() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    lateinit var carttViewModel: CartViewModel
    private lateinit var hCategoryRecyclerView: RecyclerView
    private lateinit var productsRv: RecyclerView
    private lateinit var categoriesList: List<Category>
    private lateinit var searchEt: EditText
    private var adapter: ProductAdapter = ProductAdapter(emptyList())
    private var adapterCategories: ProductListFragment.CategoryAdapter? =
        CategoryAdapter(emptyList())
    private lateinit var progressBar: ProgressBar
    private var dialog: AlertDialog? = null
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
        var tempList: MutableList<Product> = ArrayList()
        for (d in adapter.productsList) {
            if (filterItem in d.name) {
                tempList.add(d)
            }
        }
        adapter.updateList(tempList)
    }

    private fun listenCartBudge(){
        carttViewModel.orderDetailsListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                callbacks?.onCartBudgeRefresh(it.size)
            }
        )
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        carttViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        if(AppSharedPreference.getUserToken(requireContext())=="user")
        carttViewModel.loadUserOrder(
            AppSharedPreference.getCartId(requireContext())?.toInt()!!,
            AppSharedPreference.getUserId(requireContext())!!
        )
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

        hCategoryRecyclerView = view.findViewById(R.id.rv_all_categories)
        productsRv = view.findViewById(R.id.rv_product)
        searchEt = view.findViewById(R.id.et_search_product)
        progressBar = view.findViewById(R.id.progress_circular)
        dialog= SpotsDialog.Builder().setContext(context!!).setCancelable(false).build()
        hCategoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        productsRv.layoutManager = GridLayoutManager(context, 2)
        productsRv.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenCartBudge()
        //showProgress(true)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { categoriesList ->
                //showProgress(false)
                var cat= mutableListOf<Category>()
                for(i in categoriesList){
                    if(i.isActive==1)
                        cat.add(i)
                }
                updateUICategory(cat)
               // updateUICategory(categoriesList)
            }
        )

        productViewModel.categoryProductsLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
                //   showProgress(false)
                updateUI(prodcts)
            }
        )
        dialog?.show()
        productViewModel.productsListLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
                showProgress(false)
                updateUI(prodcts)
                dialog?.dismiss()
                Log.d("size",prodcts.size.toString())
            }
        )
    }

    private fun updateUICategory(categoriesList: List<Category>) {
        this.categoriesList = categoriesList
        adapterCategories = CategoryAdapter(categoriesList)
        hCategoryRecyclerView.adapter = adapterCategories
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

    private inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var categoryNameTv: TextView = view.findViewById(R.id.tv_name_category) as TextView
        var categoryImageV: ImageView = view.findViewById(R.id.category_image_view) as ImageView
        var checkIcon: ImageView = view.findViewById(R.id.check_icon) as ImageView
        var category = Category()
        var selectedId = 0
        fun bind(cat: Category) {
            category = cat
            categoryNameTv.text = cat.name
            if (cat.path != "" && cat.path != null) {
                Picasso.get().also {
                    val path = cat.getUrl()
                    it.load(path)
                        .resize(60, 60)
                        .centerCrop()
                        .placeholder(R.drawable.laptop)
                        .into(categoryImageV)
                }
            } else {
                categoryImageV.setImageResource(R.drawable.laptop)
            }

            if (cat.checked) {
                checkIcon.visibility = View.VISIBLE
            } else {
                checkIcon.visibility = View.GONE
            }
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            selectedId = category.id!!
            clearCheckIcon()
            categoriesList[position].checked = true
            productViewModel.refreshCategoryList(selectedId.toString())
            adapterCategories?.notifyDataSetChanged()
        }

        fun clearCheckIcon() {
            categoriesList.forEach { category ->
                category.checked = false
            }
        }

    }

    private inner class CategoryAdapter(val categoriesList: List<Category>) :
        RecyclerView.Adapter<ProductListFragment.CategoryHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ProductListFragment.CategoryHolder {

            val view: View = layoutInflater.inflate(
                R.layout.list_item_main_categories,
                parent, false
            )
            return CategoryHolder(view)
        }

        override fun onBindViewHolder(
            holder: ProductListFragment.CategoryHolder,
            position: Int
        ) {
            val category = categoriesList[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int {
            return categoriesList.size
        }
    }

    //////////////////////////////// Adapter of products //////////////////////////////

    private inner class ProductHolder(val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        // need to change next variable inflate to be comfortable with product item xml file
        var productImageIv = view.findViewById(R.id.ImgV_product) as ImageView
        var productNameTv: TextView = view.findViewById(R.id.tv_name_product) as TextView
        var productDollarPriceTv: TextView = view.findViewById(R.id.tv_price_product) as TextView
        var productRateTv: RatingBar = view.findViewById(R.id.rate_bar) as RatingBar
        var productDescriptionTv: TextView =
            view.findViewById(R.id.tv_description_product) as TextView
        var product = Product()

        init {
            view.setOnClickListener(this)
        }

        fun bind(pro: Product) {
            product = pro
            productNameTv.text = pro.name
            productDollarPriceTv.text = pro.dollarPrice.toString()+"$"
            productDescriptionTv.text = pro.description
            productRateTv.rating = pro.rating.toFloat()

            if (product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = product.images[0].getUrl()
                    it.load(path)
                        .resize(150, 150)
                        .centerCrop()
                        .placeholder(R.drawable.ic_phone_android)
                        .into(productImageIv)
                }
            } else {
                productImageIv.setImageResource(R.drawable.ic_phone_android)
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
            notifyDataSetChanged()
        }
    }


    /////////////////////////////////////////////////////

    interface Callbacks {
        fun onItemSelected(itemId: Int)
        fun onCartBudgeRefresh(count:Int)
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
            return ProductListFragment()
        }
    }


}