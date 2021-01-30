package com.finalproject.profitableshopping.view.products

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.OrderDetails
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.squareup.picasso.Picasso

class OrderDetailsFragment : Fragment() {
    lateinit var PurchaseViewModelDetails: CartViewModel
    lateinit var orderProductsRV: RecyclerView
    private var adapter:OrderAdapter = OrderAdapter(emptyList())
   var orderId=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PurchaseViewModelDetails= ViewModelProviders.of(this).get(CartViewModel::class.java)
        arguments?.let {
          orderId=it.getInt("ORDERID")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      val view=inflater.inflate(R.layout.fragment_order_details, container, false)
        orderProductsRV=view.findViewById(R.id.order_recyclerview)
        orderProductsRV.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PurchaseViewModelDetails.loadOrder(orderId)
        PurchaseViewModelDetails.orderDetailsListLiveData1.observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)

            }
        )

    }
    private fun updateUI(order: List<OrderDetails>) {
        adapter = OrderAdapter(order)
        orderProductsRV.adapter = adapter

    }
    companion object {

        @JvmStatic
        fun newInstance(orderId: Int) =
            OrderDetailsFragment().apply {
                arguments = Bundle().apply {
                  putInt("ORDERID",orderId)
                }
            }
    }
    inner class OrderHolder(view: View) : RecyclerView.ViewHolder(view) {

        var productImgV = view.findViewById<ImageView>(R.id.product_image_view)
        var prodctNameTv = view.findViewById<TextView>(R.id.pro_name_tv)
        var priceTv = view.findViewById<TextView>(R.id.pro_price_tv)
        var colorTv = view.findViewById<TextView>(R.id.pro_color_tv)
        /*var sizeTv = view.findViewById<TextView>(R.id.size_tv)*/
        var quantityTv = view.findViewById<TextView>(R.id.pro_qantity_tv)


        @SuppressLint("SetTextI18n")
        fun bind(order: OrderDetails) {
            prodctNameTv.text = order.product.name
            priceTv.text = "RY : " + order.price.toString()
            quantityTv.text = "Q : " + order.quantity.toString()
            colorTv.text = "COLOR : " + order.color

            if (order.product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = order.product.images[0].getUrl()
                    it.load(path)
                        .resize(75, 75)
                        .centerCrop()
                        .placeholder(R.drawable.laptop)
                        .into(productImgV)
                }
            }

        }


    }

    inner class OrderAdapter(private val productsList: List<OrderDetails>) :
        RecyclerView.Adapter<OrderHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
            val view: View = layoutInflater.inflate(
                R.layout.list_item_product_previous_orders,
                parent, false
            )
            return OrderHolder(view)

        }

        override fun onBindViewHolder(holder: OrderHolder, position: Int) {
            val orderItem = productsList[position]
            holder.bind(orderItem)

        }

        override fun getItemCount(): Int {
            return productsList.size

        }
    }
}