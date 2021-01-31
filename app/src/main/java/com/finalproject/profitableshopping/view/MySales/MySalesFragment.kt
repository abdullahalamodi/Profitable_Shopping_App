package com.finalproject.profitableshopping.view.MySales

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
import com.finalproject.profitableshopping.view.purshases.ProchasesFragment
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso


class MySalesFragment : Fragment() {

    private lateinit var productViewModel: ProductViewModel
    lateinit var salesRV: RecyclerView
    private lateinit var emptySalesTV: TextView
    private var adapter: SalesAdapter = SalesAdapter(emptyList())

    var callbacks:Callbacks?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
        callbacks?.onOpen(false)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks=null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel= ViewModelProviders.of(this).get(ProductViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_my_sales, container, false)
        salesRV=view.findViewById(R.id.sales_recyclerview)
        salesRV.layoutManager = LinearLayoutManager(requireContext())
        emptySalesTV = view.findViewById(R.id.tv_empty_sales
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productViewModel.getMySales(AppSharedPreference.getUserId(requireContext())!!).observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
            }
        )
    }
    private fun updateUI(sales: List<MySales>) {
       if(sales.isEmpty()){
           emptySalesTV.visibility=View.VISIBLE
       }else
           emptySalesTV.visibility=View.GONE
        adapter = SalesAdapter(sales)
        salesRV.adapter = adapter

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MySalesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    interface Callbacks{

        fun onOpen(show:Boolean)

    }
    inner class SalesHolder(view: View) : RecyclerView.ViewHolder(view) {

        var productImgV = view.findViewById<ImageView>(R.id.ImgV_product)
        var prodctNameTv = view.findViewById<TextView>(R.id.product_name_tv)
        var priceTv = view.findViewById<TextView>(R.id.price_tv)
        var colorTv = view.findViewById<TextView>(R.id.color_tv)
        var sizeTv = view.findViewById<TextView>(R.id.size_tv)
        var quantityTv = view.findViewById<TextView>(R.id.quantity_tv)


        @SuppressLint("SetTextI18n")
        fun bind(sales: MySales) {
            prodctNameTv.text = sales.name
            priceTv.text = "RY : " + sales.totalPrice.toString()
            quantityTv.text = "Q : " + sales.quantity.toString()
            colorTv.text = "COLOR : " + sales.color
            sizeTv.text = "SIZE : " + sales.size
            if (sales.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = sales.images[0].getUrl()
                    it.load(path)
                        .resize(75, 75)
                        .centerCrop()
                        .placeholder(R.drawable.laptop)
                        .into(productImgV)
                }
            }

        }


    }

    inner class SalesAdapter(private val salesList: List<MySales>) :
        RecyclerView.Adapter<SalesHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalesHolder {
            val view: View = layoutInflater.inflate(
                R.layout.cart_list_item,
                parent, false
            )
            return SalesHolder(view)

        }

        override fun onBindViewHolder(holder: SalesHolder, position: Int) {
            val salesItem = salesList[position]
            holder.bind(salesItem)

        }

        override fun getItemCount(): Int {
            return salesList.size

        }
    }
}