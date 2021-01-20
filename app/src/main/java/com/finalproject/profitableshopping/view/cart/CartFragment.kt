package com.finalproject.profitableshopping.view.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.OrderItem
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class CartFragment : Fragment() {
    lateinit var carttViewModel: CartViewModel
    private lateinit var productViewModel: ProductViewModel
    lateinit var cartRV:RecyclerView
    lateinit var buyBtn:Button
    lateinit var clearCartBtn:Button
    private var adapter: CartAdapter = CartAdapter(emptyList())


    override fun onStart() {
        super.onStart()
        buyBtn.setOnClickListener {
            carttViewModel.changeOrderState(AppSharedPreference.getCartId(requireContext()),true).observe(
                this,
                Observer {
                    AppSharedPreference.setOrderState(requireContext(),true)
                }
            )
        }
        clearCartBtn.setOnClickListener {

            carttViewModel.deleteOrder(AppSharedPreference.getCartId(requireContext())).observe(
                this,
                Observer {
                   // carttViewModel.deleteOrderItems()
                    AppSharedPreference.setCartId(requireContext(),null)
                    AppSharedPreference.setCartState(requireContext(),false)

                }
            )
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carttViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
       // carttViewModel.loadUser(AppSharedPreference.getUserId(requireContext()))
       // carttViewModel.loadOrder(AppSharedPreference.getCartId(requireContext()))

        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_cart2, container, false)
        cartRV=view.findViewById(R.id.cart_resuclerview)
        buyBtn=view.findViewById(R.id.buy_btn)
        clearCartBtn=view.findViewById(R.id.clear_cart_btn)
        cartRV.layoutManager=LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI(carttViewModel.pro)
       /* carttViewModel.orderListLiveData.observe(
            viewLifecycleOwner,
            Observer {
               // updateUI(it)
                updateUI(carttViewModel.pro)
            }
        )*/
    }
    private fun updateUI(orders: List<OrderItem>) {
        adapter = CartAdapter(orders)
        cartRV.adapter = adapter
    }
    companion object {

        fun newInstance() =
            CartFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    inner class CartHolder(view:View) : RecyclerView.ViewHolder(view){

        var productImgV=view.findViewById<ImageView>(R.id.product_image_v)
        var prodctNameTv=view.findViewById<TextView>(R.id.product_name_tv)
        var priceTv=view.findViewById<TextView>(R.id.price_tv)
        var colorTv=view.findViewById<TextView>(R.id.color_tv)
        var sizeTv=view.findViewById<TextView>(R.id.size_tv)
        var quantityTv=view.findViewById<TextView>(R.id.quantity_tv)
        var orderItem=OrderItem()
        var product=Product()


        fun bind(order:OrderItem){
            this.orderItem=order
            /*productViewModel.loadProduct(this.orderItem.ProductId!!)
            productViewModel.productIDetailsLiveData.observe(
                viewLifecycleOwner,
                Observer { product ->
                   // showProgress(false)
                    if(product != null){
                        this.product = product
                        prodctNameTv.text=this.product.name
                    }else{
                        context?.showMessage("product not found")
                    }
                }
            )*/
            prodctNameTv.text=this.orderItem.ProductId
            priceTv.text= "$"+this.orderItem.totalPrice.toString()
            quantityTv.text=this.orderItem.quantity.toString()
            colorTv.text=this.orderItem.color
            sizeTv.text=this.orderItem.size
            if (this.product.images.isNotEmpty()){
                Picasso.get().also {
                    val path = this.product.images[0].getUrl()
                    it.load(path)
                        .resize(75,75)
                        .centerCrop()
                        .placeholder(R.drawable.shoe)
                        .into(productImgV)
                }
            }else{
               // productImgV.setImageResource(R.drawable.shoe)
            }
        }
    }
   inner class CartAdapter(val orderList: List<OrderItem>): RecyclerView.Adapter<CartHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
            val view: View = layoutInflater.inflate(
                R.layout.cart_list_item,
                parent, false
            )
            return CartHolder(view)

        }

        override fun onBindViewHolder(holder: CartHolder, position: Int) {
            val orderItem=orderList[position]
            holder.bind(orderItem)

        }

        override fun getItemCount(): Int {
            return orderList.size

        }

    }
}


