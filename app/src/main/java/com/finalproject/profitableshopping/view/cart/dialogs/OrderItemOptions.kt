package com.finalproject.profitableshopping.view.cart.dialogs


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Order
import com.finalproject.profitableshopping.data.models.OrderDetails
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel

class OrderItemOptions() : DialogFragment(), AdapterView.OnItemSelectedListener {
    lateinit var quantityEd: EditText
    lateinit var colorSV: Spinner
    lateinit var sizeSV: Spinner
    lateinit var addToCartBtn: Button
    lateinit var exitBtn: Button
    var selectedColor = " "
    var selectedSizer = " "
    var proId: String = ""
    var product: Product? = null
    lateinit var productViewModel: ProductViewModel
    lateinit var carttViewModel: CartViewModel

    override fun onStart() {
        super.onStart()
        addToCartBtn.setOnClickListener {
            if (quantityEd.text.isNotEmpty() &&
                quantityEd.text.toString().toDouble() > product?.quantity!!
                && colorSV.prompt.isNotEmpty()
                && sizeSV.prompt.isNotEmpty()
            ) {
                if (checkCart())
                    addItem()
                else {
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
        val view = layoutInflater.inflate(R.layout.cart_item_option_dialog, null)
        quantityEd = view.findViewById(R.id.ed_category_name) as EditText
        colorSV = view.findViewById(R.id.spinner_product_color) as Spinner
        sizeSV = view.findViewById(R.id.spinner_product_size) as Spinner
        addToCartBtn = view.findViewById<Button>(R.id.btn_add_to_cart)
        exitBtn = view.findViewById<Button>(R.id.btn_exit)
        proId = arguments?.getString("proId")!!
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        carttViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        productViewModel.loadProduct(proId!!)
        productViewModel.productIDetailsLiveData.observe(
            viewLifecycleOwner,
            Observer { pro ->
                product = pro
            }
        )
        val colors = listOf("red", "blue", "yellow")
        val sizes = listOf("L", "Xl", "XXL")
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
        return AlertDialog.Builder(requireContext(), R.style.ThemeOverlay_MaterialComponents)
            .setView(view)
            .create()
    }



    private fun addItem(){
        val orderDetails = OrderDetails(
            product_id = product?.id!!,
            order_id = AppSharedPreference.getCartId(requireContext()),
            quantity = quantityEd.text.toString().toInt(),
            color = selectedColor,
            size = selectedSizer,
            price = product?.rialPrice!!
        )
        carttViewModel.addToCart(orderDetails).observe(
            viewLifecycleOwner,
            Observer {
                this.dismiss()
                context?.showMessage(it)
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.get(p2).toString()
        if (p1?.id == R.id.spinner_product_color) {
            colorSV.prompt = item
            selectedColor = item
        } else {
            sizeSV.prompt = item
            selectedSizer = item

        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun checkCart(): Boolean {
        return AppSharedPreference.getCartId(requireContext()) != -1
    }

    companion object {
        fun newInstance(proId: String): OrderItemOptions {
            return OrderItemOptions().apply {
                arguments = Bundle().apply {
                    putString("proId", proId)
                }
            }
        }
    }
}