package com.finalproject.profitableshopping.view.favorite

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Favorite
import com.finalproject.profitableshopping.viewmodel.FavoriteViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog


class FavoriteFragment : Fragment() {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var favoriteProductsRv: RecyclerView
    private lateinit var emptyFavoriteTV: TextView
    private var dialog: AlertDialog? = null
    private lateinit var progressBar: ProgressBar
    var favorites: List<Favorite> = emptyList()
    private var adapter: FavoriteAdapter = FavoriteAdapter(favorites)

    // private lateinit var addFbtn: FloatingActionButton
    private lateinit var loadingProgressBar: ContentLoadingProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        super.onCreate(savedInstanceState)
        favoriteViewModel.laodUserFavorite(AppSharedPreference.getUserId(requireContext())!!)
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
            R.layout.fragment_favorite_product_list, container, false
        )
        progressBar = view.findViewById(R.id.progress_circular)
        dialog=SpotsDialog.Builder().setContext(context!!).setCancelable(false).build()
        emptyFavoriteTV = view.findViewById(R.id.tv_empty_favorite)
        favoriteProductsRv = view.findViewById(R.id.rv_favorite_product_list)
        favoriteProductsRv.layoutManager = LinearLayoutManager(context)
        //  addFbtn =view.findViewById()
        //    loadingProgressBar=view.findViewById()
        favoriteProductsRv.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // showProgress(true)
       // dialog?.show()
//        val items = mutableListOf<FavoriteDetails>()
//        items.add(FavoriteDetails(null, 1, "1"))
//        items.add(FavoriteDetails(null, 1, "1"))
//        items.add(FavoriteDetails(null, 1, "1"))
//        items.add(FavoriteDetails(null, 1, "1"))
        favoriteViewModel.getUserFavorites(AppSharedPreference.getUserId(requireContext())!!)
            .observe(
                viewLifecycleOwner,
                Observer { favoriteList ->
                    favorites = favoriteList
                    showProgress(false)
                    dialog?.dismiss()
                    Log.d("fav",favorites.size.toString())
                    updateUI(favorites)
                }
            )
    }

    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun updateUI(favoriteItem: List<Favorite>) {
        if (favoriteItem.isNotEmpty()) {
            adapter = FavoriteAdapter(favoriteItem)
            favoriteProductsRv.adapter = adapter
            emptyFavoriteTV.visibility = View.GONE
        } else {
            emptyFavoriteTV.visibility = View.VISIBLE
        }
    }

    private inner class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {
        // need to change next variable inflate to be comfortable with product item xml file
        var productImageIv = view.findViewById(R.id.ImgV_product_fav) as ImageView
        var productNameTv: TextView = view.findViewById(R.id.tv_name_product_fav) as TextView
        var productRialPriceTv: TextView = view.findViewById(R.id.tv_price_product_fav) as TextView
        var deleteImageView: ImageView = view.findViewById(R.id.img_delete_fav) as ImageView
        var favoriteItem = Favorite(null)

        init {
            deleteImageView.setOnClickListener {
                favoriteViewModel.deleteFavoriteItem(this.favoriteItem.id!!).observe(
                    this@FavoriteFragment,
                    Observer {

                        // updateUI(favorites)

                        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.container, FavoriteFragment())
                            commit()
                        }
                        /*  *//*favoriteViewModel.getFavoriteItems(1).observe(
                           viewLifecycleOwner,
                           Observer { favoriteList ->
                               showProgress(false)
                               updateUI(favoriteList)*//*

                           }
                       )*/
                    }
                )

            }
        }


        fun bind(fav: Favorite) {
            this.favoriteItem = fav

            productNameTv.text = this.favoriteItem.product!!.name
            productRialPriceTv.text = this.favoriteItem.product?.rialPrice.toString()
            if (this.favoriteItem.product?.images!!.isNotEmpty()) {
                Picasso.get().also {
                    val path = this.favoriteItem.product!!.images[0].getUrl()
                    it.load(path)
                        .resize(150, 150)
                        .centerCrop()
                        .placeholder(R.drawable.ic_phone_android)
                        .into(productImageIv)
                }
            } else {
                productImageIv.setImageResource(R.drawable.ic_phone_android)
            }
/*            productViewModel.loadProduct(favoriteItem.product_id)
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

            )*/
        }
    }

    private inner class FavoriteAdapter(val favoriteItemsList: List<Favorite>) :
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
            val favoriteItem = favoriteItemsList[position]
            holder.bind(favoriteItem)
        }

        override fun getItemCount(): Int {
            return favoriteItemsList.size
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