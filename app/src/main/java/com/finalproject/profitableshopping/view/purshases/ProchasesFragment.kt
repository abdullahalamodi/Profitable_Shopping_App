package com.finalproject.profitableshopping.view.purshases

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.MySales
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.view.MySales.MySalesFragment
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.squareup.picasso.Picasso

class ProchasesFragment : Fragment() {
    lateinit var PurchaseViewModel: CartViewModel
    lateinit var ordersRV: RecyclerView
    private var adapter: OrderAdapter = OrderAdapter(emptyList())
    var callbacks:Callbacks?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PurchaseViewModel= ViewModelProviders.of(this).get(CartViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view= inflater.inflate(R.layout.fragment_prochases, container, false)
      ordersRV=view.findViewById(R.id.sales_recyclerview)
        ordersRV.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PurchaseViewModel.getUserOrders(AppSharedPreference.getUserId(requireContext())!!).observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
            }
        )
    }
    private fun updateUI(orders: List<Order>) {
        adapter = OrderAdapter(orders)
        ordersRV.adapter = adapter

    }
    companion object {

        @JvmStatic
        fun newInstance() =
            ProchasesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    inner class OrderHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener  {



        var priceTv = view.findViewById<TextView>(R.id.order_price_tv)
        var orderDateTv = view.findViewById<TextView>(R.id.order_date_tv)
        var quantityTv = view.findViewById<TextView>(R.id.order_quantity_tv)
      var order=Order()
init {
      view.setOnClickListener(this)
}
        @SuppressLint("SetTextI18n")
        fun bind(order: Order) {
            this.order=order
            orderDateTv.text = order.date
            priceTv.text = "$ : " + order.total_price.toString()
            quantityTv.text = "Q : " + order.total_quantity.toString()


        }

        override fun onClick(v: View?) {
           callbacks?.onOpenOrderDatails(this.order.id!!)
        }


    }

    inner class OrderAdapter(private val ordersList: List<Order>) :
        RecyclerView.Adapter<OrderHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
            val view: View = layoutInflater.inflate(
                R.layout.list_item_cart_previous_orders,
                parent, false
            )
            return OrderHolder(view)

        }

        override fun onBindViewHolder(holder: OrderHolder, position: Int) {
            val orderItem = ordersList[position]
            holder.bind(orderItem)

        }

        override fun getItemCount(): Int {
            return ordersList.size

        }
    }

    interface Callbacks{
      fun  onOpenOrderDatails( OrderId:Int)

    }
}
