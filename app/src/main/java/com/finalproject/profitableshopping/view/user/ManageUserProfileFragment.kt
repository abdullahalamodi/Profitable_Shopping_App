package com.finalproject.profitableshopping.view.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.firebase.NotifationActivity
import com.finalproject.profitableshopping.view.MainActivity
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.fragments.ManageProductsFragment
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.finalproject.profitableshopping.viewmodel.ReportViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso

class ManageUserProfileFragment : Fragment() {
    lateinit var reportViewModel: ReportViewModel
    lateinit var manage: ImageView
    lateinit var location: TextView
    var userCountOfReport = 0
    lateinit var notifationM: ImageView

    private  var userId: String?=null
    private lateinit var productViewModel: ProductViewModel
    private lateinit var manageProductsRv: RecyclerView
    private lateinit var productSearchEt: EditText
    private lateinit var addProductBtn: Button
    private var openMoreOptions = true
    private var adapterManageProduct: ManageUserProfileFragment.ManageProductAdapter =
        ManageProductAdapter(emptyList())
    private lateinit var progressBar: ProgressBar

    ///////////////////////////////////
    var callbacks: Callbacks? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onStart() {
        super.onStart()
        reportViewModel.getUserReports(AppSharedPreference.getUserId(requireContext())!!).observe(
            this,
            Observer {
                userCountOfReport = it.size
                Log.d("user report", it.size.toString())
            }
        )
        manage.setOnClickListener {
            callbacks?.onOpenProfile()
        }
        location.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }
        notifationM.setOnClickListener {
            val intent = Intent(requireContext(), NotifationActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        //userId = AppSharedPreference.getUserId(context!!)!!

        //////////////////////////////
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        arguments?.let {
        userId=it.getString("USERID")!!
        }
        productViewModel.refreshUserList(userId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manage_user, container, false)
        manage = view.findViewById(R.id.UserManage)
        location = view.findViewById(R.id.location)
        notifationM = view.findViewById(R.id.notification)

        ///////////////////////////
        manageProductsRv = view.findViewById(R.id.rv_my_product)
//        progressBar = view.findViewById(R.id.progress_circular_manage_product)
        manageProductsRv.layoutManager = LinearLayoutManager(context)
        manageProductsRv.adapter = adapterManageProduct
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        showProgress(true)
        productViewModel.userProductsLiveData.observe(
            viewLifecycleOwner,
            Observer { prodcts ->
             //   showProgress(false)
                updateUI(prodcts)
            })
    }

    private fun updateUI(productsList: List<Product>) {
        adapterManageProduct = ManageProductAdapter(productsList)
        manageProductsRv.adapter = adapterManageProduct
    }


    private inner class ManageProductHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var manageProductImageIv = view.findViewById(R.id.img_manage_product) as ImageView
        var manageProductNameTv: TextView =
            view.findViewById(R.id.tv_name_manage_product) as TextView
        var manageProductRialPriceTv: TextView =
            view.findViewById(R.id.tv_price_manage_product) as TextView
        var manageProductMoreOptionIV: ImageView =
            view.findViewById(R.id.img_more_options_product) as ImageView

        var product = Product()

        init {
            view.setOnClickListener(this)
        }


        fun bind(pro: Product) {
            product = pro
            manageProductNameTv.text = pro.name
            manageProductRialPriceTv.text = "RY:" + pro.rialPrice.toString()
            manageProductMoreOptionIV.visibility = View.GONE

            if (product.images.isNotEmpty()) {
                Picasso.get().also {
                    val path = product.images[0].getUrl()
                    it.load(path)
                        .resize(45, 45)
                        .centerCrop()
                        .placeholder(R.drawable.shoe)
                        .into(manageProductImageIv)
                }
            } else {
                manageProductImageIv.setImageResource(R.drawable.shoe)
            }
        }

        override fun onClick(p0: View?) {
          //  callbacks?.onItemSelected(this.product.id)
        }
    }

    private inner class ManageProductAdapter(var productsList: List<Product>) :
        RecyclerView.Adapter<ManageProductHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageProductHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.list_item_manage_product,
                parent, false
            )
            return ManageProductHolder(view)
        }

        override fun onBindViewHolder(holder: ManageProductHolder, position: Int) {
            val product = productsList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int {
            return productsList.size
        }

        fun updateList(list: List<Product>) {
            productsList = list
            notifyDataSetChanged();
        }
    }


    interface Callbacks {

        fun onOpenProfile()
    }

    companion object {

        fun newInstance(userId:String?) =
            ManageUserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("USERID",userId)

                }
            }
    }
}