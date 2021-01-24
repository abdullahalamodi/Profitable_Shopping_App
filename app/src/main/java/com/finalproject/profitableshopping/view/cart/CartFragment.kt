package com.finalproject.profitableshopping.view.cart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
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
import com.finalproject.profitableshopping.data.models.Comment
import com.finalproject.profitableshopping.data.models.OrderDetails
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.finalproject.profitableshopping.viewmodel.CommentViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class CartFragment : Fragment() {
    lateinit var commentViewModel: CommentViewModel
    lateinit var carttViewModel: CartViewModel
    private lateinit var productViewModel: ProductViewModel
    lateinit var cartRV: RecyclerView
    lateinit var totalPriceV: TextView
    var totalPrice: Double = 0.0
    lateinit var buyBtn: TextView
    lateinit var clearCartBtn: TextView
    private var adapter: CartAdapter = CartAdapter(emptyList())


    override fun onStart() {
        super.onStart()
        buyBtn.setOnClickListener {
            carttViewModel.buy(AppSharedPreference.getCartId(requireContext())!!)
                .observe(
                    this,
                    Observer {
                        AppSharedPreference.setCartId(requireContext(), "-1")
                        context?.showMessage("تكت عملية الشراء بنجاح :)")
                        carttViewModel.loadOrder(0)
                    }
                )
        }
        clearCartBtn.setOnClickListener {
            carttViewModel.deleteCart(AppSharedPreference.getCartId(requireContext())!!).observe(
                this,
                Observer {
                    AppSharedPreference.setCartId(requireContext(), "-1")
                    context?.showMessage("تك حذف السلة بنجاح :)")
                    carttViewModel.loadOrder(0)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carttViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        carttViewModel.loadOrder(AppSharedPreference.getCartId(requireContext())?.toInt()!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart2, container, false)
        cartRV = view.findViewById(R.id.cart_resuclerview)
        totalPriceV = view.findViewById(R.id.total_tv)
        buyBtn = view.findViewById(R.id.buy_btn)
        clearCartBtn = view.findViewById(R.id.clear_cart_btn)
        cartRV.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carttViewModel.orderDetailsListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
            }
        )
    }

    private fun updateUI(orders: List<OrderDetails>) {
        adapter = CartAdapter(orders)
        cartRV.adapter = adapter
        setTotalPrice(orders)
    }

    private fun setTotalPrice(orders: List<OrderDetails>) {
        orders.forEach { details ->
            totalPrice += details.total_price
        }
        totalPriceV.text = totalPrice.toString()
    }

    private fun showDialogRating(product:Product) {
        var builder = AlertDialog.Builder(context!!)
        builder.setTitle("Rating product")
        builder.setMessage("Please fill information")
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_rating_comment, null)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.rating_bar_product)
        val edt_comment = itemView.findViewById<EditText>(R.id.et_comment)
        edt_comment.setText("")

        builder.setView(itemView)
        builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
        builder.setPositiveButton("Ok") { dialogInterface, i ->
            if (ratingBar != null) {

                val comment = Comment(
                    rate = ratingBar.rating.toInt(),
                    title = edt_comment.text.toString(),
                    productId = product.id,
                    userId = AppSharedPreference.getUserId(context!!)!!
                )
                val response = commentViewModel.addComment(comment)
                response.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        context?.showMessage(message.toString())
                        dialogInterface.dismiss()
                    }
                )
            } else {
                context?.showMessage("you should rating the product")
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        fun newInstance() =
            CartFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    inner class CartHolder(view: View) : RecyclerView.ViewHolder(view) {

        var productImgV = view.findViewById<ImageView>(R.id.ImgV_product)
        var prodctNameTv = view.findViewById<TextView>(R.id.product_name_tv)
        var priceTv = view.findViewById<TextView>(R.id.price_tv)
        var colorTv = view.findViewById<TextView>(R.id.color_tv)
        var sizeTv = view.findViewById<TextView>(R.id.size_tv)
        var quantityTv = view.findViewById<TextView>(R.id.quantity_tv)
        var rateBtn = view.findViewById<TextView>(R.id.rate_btn)

        @SuppressLint("SetTextI18n")
        fun bind(orderDetails: OrderDetails) {
            prodctNameTv.text = orderDetails.product.name
            priceTv.text = "RY : " + orderDetails.total_price.toString()
            quantityTv.text = "Q : " + orderDetails.quantity.toString()
            colorTv.text = "COLOR : " + orderDetails.color
            sizeTv.text = "SIZE : " + orderDetails.size
            if (orderDetails.product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = orderDetails.product.images[0].getUrl()
                    it.load(path)
                        .resize(75, 75)
                        .centerCrop()
                        .placeholder(R.drawable.laptop)
                        .into(productImgV)
                }
            }

            rateBtn.setOnClickListener {
                showDialogRating(orderDetails.product)
            }
        }


    }

    inner class CartAdapter(private val orderList: List<OrderDetails>) :
        RecyclerView.Adapter<CartHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
            val view: View = layoutInflater.inflate(
                R.layout.cart_list_item,
                parent, false
            )
            return CartHolder(view)

        }

        override fun onBindViewHolder(holder: CartHolder, position: Int) {
            val orderItem = orderList[position]
                        holder.bind(orderItem)

        }

        override fun getItemCount(): Int {
            return orderList.size

        }
    }

}




