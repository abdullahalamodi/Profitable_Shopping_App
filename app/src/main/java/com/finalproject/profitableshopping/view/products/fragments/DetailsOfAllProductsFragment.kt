package com.finalproject.profitableshopping.view.products.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Comment
import com.finalproject.profitableshopping.data.models.Product
import com.finalproject.profitableshopping.data.models.Report
import com.finalproject.profitableshopping.data.AppSharedPreference
import com.finalproject.profitableshopping.data.models.*
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.cart.dialogs.OrderItemOptions
import com.finalproject.profitableshopping.view.report.dialog.AddComplainDialog
import com.finalproject.profitableshopping.view.report.dialog.ComplainDialog
import com.finalproject.profitableshopping.viewmodel.CommentViewModel
import com.finalproject.profitableshopping.viewmodel.FavoriteViewModel
import com.finalproject.profitableshopping.viewmodel.ProductViewModel
import com.finalproject.profitableshopping.viewmodel.ReportViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


private const val ARG_PRODUCT_ID = "product_id"

class DetailsOfAllProductsFragment : Fragment() {

    private var productId: String? = null
    private lateinit var progressBar: ProgressBar
    lateinit var productViewModel: ProductViewModel
    lateinit var reportViewModel: ReportViewModel
    lateinit var commentViewModel: CommentViewModel
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var productImageIv: ImageView
    lateinit var productNameTv: TextView
    lateinit var productQuantityTv: TextView
    lateinit var productReviewsTv: TextView
    lateinit var productRialPriceTv: TextView
    lateinit var productDollarPriceTv: TextView
    lateinit var productDescriptionTv: TextView
    lateinit var productCommentDescriptionTv: TextView
    lateinit var favoriteFABtn: FloatingActionButton
    lateinit var cartBtn: FloatingActionButton
    lateinit var reportBtn: Button
    lateinit var showCommentBtn: Button
    lateinit var ratingBar: RatingBar
    var callbacks: Callbacks?=null
    lateinit var product: Product
    lateinit var commentsRecyclerView: RecyclerView
    lateinit var showComments: Button
    private var adapter: CommentsAdapter = CommentsAdapter(emptyList())
    var countOfReports: Int = 0
    var productReports: List<Report> = emptyList()
    lateinit var comment: Comment
    lateinit var imageSlider: ImageSlider


    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks?.onDetailsOpen(true)
        callbacks=null
    }
    override fun onStart() {
        super.onStart()
//        callbacks = (context as Callbacks)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
       // mainActivity?.anim(false)
        callbacks?.onDetailsOpen(false)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            productViewModel.loadProduct(productId!!)
            reportViewModel.getProductReports(productId!!).observe(
                this,
                Observer {
                    countOfReports = it.size
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_details_of_all_products, container, false)
        //  progressBar = view.findViewById(R.id.progress_circular)
//        productImageIv = view.findViewById(R.id.img_product_details) as ImageView
        productNameTv = view.findViewById(R.id.tv_product_name_details) as TextView
        showCommentBtn = view.findViewById(R.id.btnShowComment2) as Button
        //  productReviewsTv = view.findViewById(R.id.reviews_tv) as TextView
        productQuantityTv = view.findViewById(R.id.tv_product_quantity_details) as TextView
        productRialPriceTv = view.findViewById(R.id.tv_product_price_rial_details) as TextView
        productDollarPriceTv = view.findViewById(R.id.tv_product_price_details) as TextView
        productDescriptionTv = view.findViewById(R.id.tv_product_desc_details) as TextView
        productCommentDescriptionTv =
            view.findViewById(R.id.tv_product_description_details) as TextView
        favoriteFABtn = view.findViewById(R.id.btn_rating) as FloatingActionButton
        cartBtn = view.findViewById(R.id.btn_cart) as FloatingActionButton
        reportBtn = view.findViewById(R.id.btnShowreport)
        productReviewsTv = view.findViewById(R.id.tv_product_reports) as TextView
        ratingBar = view.findViewById(R.id.ratingBar) as RatingBar
        commentsRecyclerView = view.findViewById(R.id.comments_recycler_view)
        showComments = view.findViewById(R.id.btnShowComment)
        imageSlider = view.findViewById(R.id.img_product_details)



        reportBtn.setOnClickListener {
            ComplainDialog.newInstance(productId!!, product.userId!!).apply {
                show(this@DetailsOfAllProductsFragment.parentFragmentManager, "report")
            }
        }
        showCommentBtn.setOnClickListener {
            AddComplainDialog.newInstance().apply {
                show(this@DetailsOfAllProductsFragment.parentFragmentManager, "report")
            }
        }
        cartBtn.setOnClickListener {
            OrderItemOptions.newInstance(product.id.toString(), product.quantity, product.rialPrice)
                .apply {
                    show(this@DetailsOfAllProductsFragment.parentFragmentManager, "cart")
                }
        }

        showComments.setOnClickListener {
            if (commentsRecyclerView.visibility == View.VISIBLE)
                commentsRecyclerView.visibility = View.GONE
            else
                commentsRecyclerView.visibility = View.VISIBLE

        }

        return view
    }

    private fun averageOfRating(comments: List<Comment>) {
        var total = 0
        if (!comments.isNullOrEmpty()) {
            comments.forEach { comment ->
                total += comment.rate
            }
            val average = total / comments.size
            ratingBar.rating = average.toFloat()
        }
    }

    fun updateCommentsRecycler(comments: List<Comment>) {
        adapter = CommentsAdapter(comments)
        commentsRecyclerView.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//          showProgress(true)
        productViewModel.productIDetailsLiveData.observe(
            viewLifecycleOwner,
            Observer { product ->
                this.product = product
                //   showProgress(false)
                updateUi(product)
            }
        )
        loadComments(productId.toString())

    }

    private fun loadComments(productId: String) {
        commentViewModel.getProductComments(productId).observe(
            viewLifecycleOwner,
            Observer {
                averageOfRating(it)
                updateCommentsRecycler(it)
            }
        )
    }

    private fun updateUi(product: Product) {
        productNameTv.text = product.name
        productRialPriceTv.text = product.rialPrice.toString() + " RY"
        productDollarPriceTv.text = product.dollarPrice.toString() + " $"
        productQuantityTv.text = product.quantity.toString()
        productReviewsTv.text = countOfReports.toString()
        //     productQuantityTv.text = product.quantity.toString()
        productDescriptionTv.text = product.description
        if (product.images.isNotEmpty())
            Picasso.get().also {
                val path = product.images[0].getUrl()
                it.load(path)
                    .resize(350, 350)
                    .centerCrop()
                    .placeholder(R.drawable.shoe)
                    .into(productImageIv)

            }
    }

    interface Callbacks {
        fun onAddToCartClicked()
        fun onDetailsOpen(show:Boolean)
    }

    private fun addItemToFavorite() {
        val FavoriteDetails = FavoriteItem(
            product_id = productId!!,
            favorite_id = AppSharedPreference.getFavoriteId(requireContext()),
            id = null
        )
        favoriteViewModel.addFavoriteItem(FavoriteDetails).observe(
            requireActivity(),
            Observer {
                if (it != null) {

                    requireActivity().showMessage(it)
                } else {
                    requireActivity().showMessage("null response")
                }
            }
        )
    }

    private fun createFavorite() {
        val favorite = Favorite(
            user_id = AppSharedPreference.getUserId(requireContext())!!,
            id = null
        )
        favoriteViewModel.createFavorite(
            favorite
        ).observe(
            viewLifecycleOwner,
            Observer {
                AppSharedPreference.setFavoriteId(requireContext(),it!!.toInt())
                addItemToFavorite()
            }
        )
    }

    private fun checkFavorite(): Boolean {
        return AppSharedPreference.getFavoriteId(requireContext())>0
    }
    companion object {
        @JvmStatic
        fun newInstance(productId: String) =
            DetailsOfAllProductsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRODUCT_ID, productId)
                }
            }
    }


////////comments recycler view and adapter //////////////

    private inner class CommentsHolder(val view: View) : RecyclerView.ViewHolder(view) {
        // need to change next variable inflate to be comfortable with product item xml file
        var commentUser = view.findViewById(R.id.comment_user) as TextView
        var commentTitle = view.findViewById(R.id.comment_title) as TextView
        var commentDate: TextView = view.findViewById(R.id.comment_date) as TextView


        fun bind(comment: Comment) {
            commentUser.text = comment.userId
            commentTitle.text = comment.title
            commentDate.text = comment.date
        }

    }

    private inner class CommentsAdapter(var comments: List<Comment>) :
        RecyclerView.Adapter<CommentsHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsHolder {
            // need to change inflate to be product list item xml
            val view: View = layoutInflater.inflate(
                R.layout.comments_lis_item,
                parent, false
            )
            return CommentsHolder(view)
        }

        override fun onBindViewHolder(holder: CommentsHolder, position: Int) {
            val comment = comments[position]
            holder.bind(comment)
        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }


}