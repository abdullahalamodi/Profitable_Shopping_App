package com.finalproject.profitableshopping.view.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.FavoriteDetails
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.FavoriteViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso


class FavoriteFragment : Fragment() {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var favoriteProductsRv: RecyclerView
    private var adapter:FavoriteAdapter = FavoriteAdapter(emptyList())
    private lateinit var progressBar: ProgressBar

    // private lateinit var addFbtn: FloatingActionButton
    private lateinit var loadingProgressBar: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {


        favoriteViewModel= ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            //next line still need some config
            R.layout.fragment_product_list, container, false
        )
        favoriteProductsRv = view.findViewById(R.id.rv_product)
        progressBar = view.findViewById(R.id.progress_circular)
        favoriteProductsRv.layoutManager = LinearLayoutManager(context)
        //  addFbtn =view.findViewById()
        //    loadingProgressBar=view.findViewById()
        favoriteProductsRv.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        val items= mutableListOf<FavoriteDetails>()
        items.add(FavoriteDetails(null,1,"1"))
        items.add(FavoriteDetails(null,1,"1"))
        items.add(FavoriteDetails(null,1,"1"))
        items.add(FavoriteDetails(null,1,"1"))
       favoriteViewModel.getFavoriteItems(1).observe(
            viewLifecycleOwner,
            Observer { favoriteList ->
                showProgress(false)
                //updateUI(favoriteList)
                updateUI(items)

            }
        )
    }
    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }
    private fun updateUI(productsList: List<FavoriteDetails>) {
        adapter = FavoriteAdapter(productsList)
        favoriteProductsRv.adapter = adapter
    }
    private inner class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view){
        // need to change next variable inflate to be comfortable with product item xml file
        var productImageIv = view.findViewById(R.id.ImgV_product_fav) as ImageView
        var productNameTv: TextView = view.findViewById(R.id.tv_name_product_fav) as TextView
        var productRialPriceTv: TextView = view.findViewById(R.id.tv_price_product_fav) as TextView
        var deleteImageView: ImageView = view.findViewById(R.id.img_delete_fav) as ImageView
        var favoriteItem = FavoriteDetails(null)
        init {
            deleteImageView.setOnClickListener {
               favoriteViewModel.deleteFavoriteItem(this.favoriteItem.favorite_id).observe(
                   this@FavoriteFragment,
                   Observer {

                             Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                       favoriteViewModel.getFavoriteItems(1).observe(
                           viewLifecycleOwner,
                           Observer { favoriteList ->
                               showProgress(false)
                               updateUI(favoriteList)

                           }
                       )
                   }
               )

            }
        }



        fun bind(fav: FavoriteDetails) {
            this.favoriteItem = fav
            productViewModel.loadProduct(favoriteItem.product_id)
            productViewModel.productIDetailsLiveData.observe(
                this@FavoriteFragment,
                Observer {pro ->
                    productNameTv.text = pro.name
                    productRialPriceTv.text = pro.rialPrice.toString()
                    if (pro.images.isNotEmpty()){
                        Picasso.get().also {
                            val path = pro.images[0].getUrl()
                            it.load(path)
                                .resize(150,150)
                                .centerCrop()
                                .placeholder(R.drawable.shoe)
                                .into(productImageIv)
                        }
                    }else{
                        productImageIv.setImageResource(R.drawable.shoe)
                    }
                }

            )
        }
    }

    private inner class FavoriteAdapter(val productsList: List<FavoriteDetails>) :
        RecyclerView.Adapter<FavoriteHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.favorite_item_list,
                parent, false
            )
            return FavoriteHolder(view)
        }

        override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
            val product = productsList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productsList.size
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            FavoriteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}