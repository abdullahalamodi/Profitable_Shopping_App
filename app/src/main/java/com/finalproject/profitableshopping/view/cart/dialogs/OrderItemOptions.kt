package ccom.finalproject.profitableshopping.view.products.dialogs

import androidx.core.view.get



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
import com.finalproject.profitableshopping.data.models.OrderItem
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.viewmodel.CartViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import java.util.*

class OrderItemOptions() : DialogFragment(), AdapterView.OnItemSelectedListener {
    lateinit var quantityEd:EditText
    lateinit var colorSV:Spinner
    lateinit var sizeSV:Spinner
    lateinit var addToCartBtn:Button
    lateinit var exitBtn:Button
    var selectedColor=" "
    var selectedSizer=" "
    var proId:String=""
    var product:Product?=null
    lateinit var productViewModel: ProductViewModel
    lateinit var carttViewModel: CartViewModel


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.cart_item_option_dialog, null)
         quantityEd=view.findViewById(R.id.ed_category_name) as EditText
         colorSV=view.findViewById(R.id.spinner_product_color) as Spinner
         sizeSV=view.findViewById(R.id.spinner_product_size) as Spinner
         addToCartBtn=view.findViewById<Button>(R.id.btn_add_to_cart)
         exitBtn=view.findViewById<Button>(R.id.btn_exit)
        proId= arguments?.getString("proId")!!
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        carttViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)
        productViewModel.loadProduct(proId!!)
        productViewModel.productIDetailsLiveData.observe(
            viewLifecycleOwner,
            Observer { pro ->
                product=pro
            }
        )
        val colors= listOf<String>("red","blue","yelew")
        val sizes= listOf<String>("L","Xl","XXL")
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
            android.R.layout.simple_spinner_dropdown_item)
        colorSV.adapter = colorAdapter

        sizeAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item)
        sizeSV.adapter = sizeAdapter
        return AlertDialog.Builder(requireContext(),R.style.ThemeOverlay_MaterialComponents)
            .setView(view)
            .create()
    }

    override fun onStart() {
        super.onStart()
        addToCartBtn.setOnClickListener {
            if(quantityEd.text.isNotEmpty()&&colorSV.prompt.isNotEmpty()&&sizeSV.prompt.isNotEmpty()){
                if (!checkCart())
                  createCart()
                else if (!AppSharedPreference.getOrderState(requireContext())){
                    createCart()
                }
               /* var order=OrderItem(
                    null ,
                    product?.id!!.toString(),
                    AppSharedPreference.getUserId(requireContext()).toString(),
                    AppSharedPreference.getCartId(requireContext()),
                    quantityEd.text.toString().toInt(),
                    selectedColor,
                    selectedSizer,
                    quantityEd.text.toString().toInt()* product!!.rialPrice
                )*/
                val order=OrderItem(
                    null ,
                    "labtop",
                    1,
                    20,
                    "red",
                    "xl",
                    2350.0
                )
                carttViewModel.pro.add(order)
                /*carttViewModel.addToCart(
                  order).observe(
                    this,
                    Observer {

                    }
                )*/





            }
            dismiss()
        }
    exitBtn.setOnClickListener {
        dismiss()

    }
    }

    private fun createCart() {
        val date=Calendar.getInstance()
        date.time= Date()
        val order=Order(
            null,
            "${date.get(Calendar.DAY_OF_MONTH)+date.get(Calendar.MONTH)}",
            AppSharedPreference.getUserId(requireContext())!!.toInt(),
            0.0,
            false
        )
        carttViewModel.createCart(
            order
            ).observe(

            this,
            Observer {
                AppSharedPreference.setOrderState(requireContext(),false)
                AppSharedPreference.setCartId(requireContext(),it)
                AppSharedPreference.setCartState(requireContext(),true)

            }
        )
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val item = p0?.get(p2).toString()
       if(p1?.id ==R.id.spinner_product_color)
       {
           colorSV.prompt=item
           selectedColor=item
       }else{
           sizeSV.prompt=item
           selectedSizer=item

       }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
    fun checkCart():Boolean{
        return  AppSharedPreference.checkHasCart(requireContext())
    }
    companion object{
        fun newInstance( proId:String):OrderItemOptions {
            return OrderItemOptions().apply {
                arguments = Bundle().apply {

                    putString("proId",proId)

                }

            }
        }
    }
}