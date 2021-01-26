package com.finalproject.profitableshopping.view.cart.dialogs


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderDetails
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.CartViewModel

private const val ARG_PRODUCT_ID = "product_id"
private const val ARG_PRODUCT_Q = "product_q"
private const val ARG_PRODUCT_P = "product_p"

class OrderItemOptions : DialogFragment() {
    lateinit var quantityEd: TextView
    lateinit var plusBtn: Button
    lateinit var menusBtn: Button
    lateinit var colorSV: Spinner
    lateinit var sizeSV: Spinner
    lateinit var totalPriceV: TextView
    lateinit var addToCartBtn: Button
    lateinit var exitBtn: Button
    var selectedColor = " "
    var selectedSizer = " "
    var proId: String = ""
    var productQuantity: Int = 0
    var productPrice: Double = 0.0
    var quantity: Int = 1
    var price: Double = 0.0
    lateinit var carttViewModel: CartViewModel
    val colors = listOf("red", "blue", "yellow")
    val sizes = listOf("L", "Xl", "XXL")

    override fun onStart() {
        super.onStart()
        addToCartBtn.setOnClickListener {
            if (!colorSV.prompt.isNullOrEmpty()
                && !sizeSV.prompt.isNullOrEmpty()
            ) {
                if (checkCart()) {
                    addItem()
                } else {
                    createCart()
                }
            } else {
                context?.showMessage("تحقق من الحقول !!")
            }
        }
        exitBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.cart_item_option_dialog, null)
        quantityEd = view!!.findViewById(R.id.ed_quantity)
        plusBtn = view.findViewById(R.id.plus_btn)
        menusBtn = view.findViewById(R.id.menus_btn)
        colorSV = view.findViewById(R.id.spinner_product_color)
        sizeSV = view.findViewById(R.id.spinner_product_size)
        totalPriceV = view.findViewById(R.id.total_tv)
        addToCartBtn = view.findViewById<Button>(R.id.btn_add_to_cart)
        exitBtn = view.findViewById<Button>(R.id.btn_exit)
        proId = arguments?.getString(ARG_PRODUCT_ID)!!
        productQuantity = arguments?.getInt(ARG_PRODUCT_Q)!!
        productPrice = arguments?.getDouble(ARG_PRODUCT_P)!!
        price = productPrice
        totalPriceV.text = price.toString()
        carttViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        val colorAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            colors
        )
        val sizeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            sizes
        )

        colorAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        colorSV.adapter = colorAdapter

        sizeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        sizeSV.adapter = sizeAdapter

        colorSV.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                colorSV.prompt = colors[p2]
                selectedColor = colors[p2]
            }

        }
        sizeSV.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sizeSV.prompt = sizes[p2]
                selectedSizer = sizes[p2]
            }

        }

        plusBtn.setOnClickListener {
            if (quantity < productQuantity) {
                quantity += 1
                price += productPrice
                quantityEd.text = quantity.toString()
                totalPriceV.text = price.toString()
            }
        }

        menusBtn.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                price -= productPrice
                quantityEd.text = quantity.toString()
                totalPriceV.text = price.toString()
            }
        }
        return AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents)
            .setView(view)
            .create()

    }


    private fun addItem() {
        val orderDetails = OrderDetails(
            product_id = proId.toInt(),
            order_id = AppSharedPreference.getCartId(requireContext())?.toInt()!!,
            quantity = quantityEd.text.toString().toInt(),
            color = selectedColor,
            size = selectedSizer,
            price = productPrice
        )
        carttViewModel.addToCart(orderDetails).observe(
            requireActivity(),
            Observer {
                if (it != null) {
                    this.dismiss()
                    requireActivity().showMessage(it)
                } else {
                    requireActivity().showMessage("null response")
                }
            }
        )
    }

    private fun createCart() {
        val order = Order(
            user_id = AppSharedPreference.getUserId(requireContext())
        )
        carttViewModel.createCart(
            order
        ).observe(
            this,
            Observer {
                AppSharedPreference.setCartId(requireContext(), it)
                addItem()
            }
        )
    }

    private fun checkCart(): Boolean {
        return AppSharedPreference.getCartId(requireContext()) != "-1"
    }

    companion object {
        fun newInstance(proId: String, quantity: Int, price: Double): OrderItemOptions {
            return OrderItemOptions().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_ID, proId)
                    putInt(ARG_PRODUCT_Q, quantity)
                    putDouble(ARG_PRODUCT_P, price)
                }
            }
        }
    }
}