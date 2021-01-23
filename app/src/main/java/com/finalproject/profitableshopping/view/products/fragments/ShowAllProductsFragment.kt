package com.finalproject.profitableshopping.view.products.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso


class ShowAllProductsFragment : Fragment() {

    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var hCategoryRecyclerView: RecyclerView
    private lateinit var allProductsRecyclerView: RecyclerView
    private lateinit var categoriesList: MutableList<Category>
    private lateinit var productsList: List<Product>
    private lateinit var progressBar: ProgressBar
    private lateinit var callbacks: Callbacks
    private var adapterCategories: ShowAllProductsFragment.CategoryAdapter? =
        CategoryAdapter(emptyList())
    private var adapterProducts: ShowAllProductsFragment.ProductAdapter =
        ProductAdapter(emptyList())


    override fun onStart() {
        super.onStart()
        callbacks = (context as Callbacks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_show_all_products, container, false)
        hCategoryRecyclerView = view.findViewById(R.id.rv_all_categories)
        allProductsRecyclerView = view.findViewById(R.id.rv_all_products)
        hCategoryRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        allProductsRecyclerView.layoutManager = LinearLayoutManager(context)
        allProductsRecyclerView.adapter = adapterProducts

//        progressBar = view.findViewById(R.id.progress_circular)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        showProgress(true)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { categoriesList ->
                //showProgress(false)
                updateUICategory(categoriesList)
            }
        )

        productViewModel.categoryProductsLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
                //   showProgress(false)
                updateUIProducts(prodcts)

            }
        )
  var cat =Category()
        productViewModel.getProductsByIdOfCat(cat.id).observe(
            viewLifecycleOwner,
            Observer { products ->
                //   showProgress(false)
                updateUIProducts(products)

            }
        )
    }

    private fun updateUICategory(categoriesList: List<Category>) {
        this.categoriesList = categoriesList as MutableList<Category>
        adapterCategories = CategoryAdapter(categoriesList)
        hCategoryRecyclerView.adapter = adapterCategories
    }

    private fun updateUIProducts(productsList: List<Product>) {
        this.productsList = productsList
        adapterProducts = ProductAdapter(productsList)
        allProductsRecyclerView.adapter = adapterProducts
    }

    private fun updateUIFliter(id: Category) {
        this.productsList = productsList
        adapterProducts = ProductAdapter(productsList)
        allProductsRecyclerView.adapter = adapterProducts
    }

    interface Callbacks {
        fun onItemSelected(itemId: Int)
        // fun onFloatButtonClicked()
    }


    companion object {
        @JvmStatic
        fun newInstance() = ShowAllProductsFragment()
    }

    private inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var categoryNameTv: TextView = view.findViewById(R.id.tv_name_category) as TextView
        var categoryImageV: ImageView = view.findViewById(R.id.category_image_view) as ImageView
        var checkIcon: ImageView = view.findViewById(R.id.check_icon) as ImageView
        var category = Category()
        var selectedId = 0;
        fun bind(cat: Category) {
            category = cat;
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

        fun clearCheckIcon(){
            categoriesList.forEach {category ->
                category.checked = false
            }
        }

    }

    private inner class CategoryAdapter(val categoriesList: List<Category>) :
        RecyclerView.Adapter<ShowAllProductsFragment.CategoryHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowAllProductsFragment.CategoryHolder {

            val view: View = layoutInflater.inflate(
                R.layout.list_item_main_categories,
                parent, false
            )
            return CategoryHolder(view)
        }

        override fun onBindViewHolder(
            holder: ShowAllProductsFragment.CategoryHolder,
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

    private inner class ProductHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        // need to change next variable inflate to be comfortable with product item xml file
        var productImageIv = view.findViewById(R.id.img_product_main) as ImageView
        var productNameTv: TextView = view.findViewById(R.id.tv_name_product_main) as TextView
        var productRialPriceTv: TextView = view.findViewById(R.id.tv_price_product_main) as TextView

        /* var productDescriptionTv: TextView =
             view.findViewById(R.id.tv_description_product) as TextView*/
        var product = Product()

        init {
            view.setOnClickListener(this)
        }


        fun bind(pro: Product) {
            product = pro
            productNameTv.text = pro.name
            productRialPriceTv.text = pro.rialPrice.toString()
            // productDescriptionTv.text = pro.description

        }

        override fun onClick(p0: View?) {
            //to open product fragment to  display  product details
            callbacks.onItemSelected(product.id)
        }
    }



    private inner class ProductAdapter(val productsList: List<Product>) :
        RecyclerView.Adapter<ShowAllProductsFragment.ProductHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ShowAllProductsFragment.ProductHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.list_item_main_product,
                parent, false
            )
            return ProductHolder(view)
        }

        override fun onBindViewHolder(
            holder: ShowAllProductsFragment.ProductHolder,
            position: Int
        ) {
            val product = productsList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productsList.size
        }
    }

}