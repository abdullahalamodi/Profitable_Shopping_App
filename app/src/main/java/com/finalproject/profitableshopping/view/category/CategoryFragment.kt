package com.finalproject.profitableshopping.view.category

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.UploadRequestBody
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.delete_category.view.*
import kotlinx.android.synthetic.main.update_category.view.*

const val USER_ID_ARG = "userId"

class CategoryFragment : Fragment(), UploadRequestBody.UploadCallback {
    interface CategoryCallbacks {
        fun onOpenProductManager(catgoryId: Int)
    }

    private lateinit var categoryNameEt: EditText
    private lateinit var addBtn: Button
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoriesList: List<Category>
    private lateinit var progressBar: ProgressBar
    private lateinit var addCatFloatingABtn: FloatingActionButton
    private var adapter: CategoryAdapter? = CategoryAdapter(emptyList())
    private var openMoreOptions = true
    var callbacks: CategoryCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as CategoryCallbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onStart() {
        super.onStart()
        addCatFloatingABtn.setOnClickListener {
            var bottomSheetAddCat = AddCategoryFragment();
            bottomSheetAddCat.show(childFragmentManager, "Tag")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catergory_list, container, false)
        categoryRecyclerView = view.findViewById(R.id.rv_category)
        categoryNameEt = view.findViewById(R.id.category_name_et)
        addBtn = view.findViewById(R.id.add_category_btn)
        categoryRecyclerView.layoutManager = LinearLayoutManager(context)
        progressBar = view.findViewById(R.id.progress_circular)
        addCatFloatingABtn = view.findViewById(R.id.floatingBtn_add_cat_)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress(true)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { categoriesList ->
                showProgress(false)
                updateUI(categoriesList)
            }
        )
    }

    private fun updateUI(categoriesList: List<Category>) {
        this.categoriesList = categoriesList
        adapter = CategoryAdapter(categoriesList)
        categoryRecyclerView.adapter = adapter
    }

    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101

        @JvmStatic
        fun newInstance() = CategoryFragment()

    }


    //adapter code is last thing in the fragment or activity
    private inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        var categoryNameTv: TextView = view.findViewById(R.id.txt_category_name) as TextView
        var categoryImageV: ImageView = view.findViewById(R.id.img_category) as ImageView
        var categoryUpdateTv: TextView = view.findViewById(R.id.tv_update_category) as TextView
        var categoryDeleteTv: TextView = view.findViewById(R.id.tv_delete_category) as TextView
        var categoryMoreOPtionIV: ImageView = view.findViewById(R.id.img_more_options) as ImageView
        var categoryUpdeteDeleteLy: LinearLayout =
            view.findViewById(R.id.ly_update_delete_category) as LinearLayout
        var category = Category()

        init {
            view.setOnClickListener(this)
        }

        ////////////////////this function to update the category

        fun categoryDialogUpdate(cat: Category) {

            val alertBuilder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.update_category, null)
            alertBuilder.setView(view)
            val alertDialog = alertBuilder.create()
            alertDialog.show()

            view.ed_update_category.setText(cat.name)
            Toast.makeText(requireContext(), cat.id.toString() + ".", Toast.LENGTH_SHORT).show()
            view.btn_update.setOnClickListener {
                showProgress(true)
                val catMap = Category()
                catMap.name = view.ed_update_category.text.toString()
                val response = categoryViewModel.updateCategory(cat.id, catMap)
                response.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        showProgress(false)
                        Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_SHORT)
                            .show()
                        categoryViewModel.refresh()
                    }
                )
                alertDialog.dismiss()
            }
            view.btn_cancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        ////////////////////this function to delete the category

        fun categoryDialogDelete(cat: Category) {

            var alertBuilder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.delete_category, null)
            alertBuilder.setView(view)
            var alertDialog = alertBuilder.create()
            alertDialog.show()

            view.ed_category_name.setText(cat.name)
            view.btn_delete.setOnClickListener {
                showProgress(true)
                val response = categoryViewModel.updateCategoryCase(cat.id!!, cat.isActive)
                response.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        showProgress(false)
                        Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_SHORT)
                            .show()
                        categoryViewModel.refresh()
                    }
                )
                alertDialog.dismiss()
            }

            view.btn_exit.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        fun bind(cat: Category) {

            this.category = cat
            categoryNameTv.text = cat.name

            if (this.category.isActive()) {
                categoryDeleteTv.text = "إخفاء"
                categoryNameTv.setTextColor(Color.BLACK)
            } else {
                categoryDeleteTv.text = "إظهار"
                categoryNameTv.setTextColor(Color.RED)
            }

            if (cat.path != "" && cat.path != null) {
                Picasso.get().also {
                    val path = cat.getUrl()
                    it.load(path)
                        .resize(45, 45)
                        .centerCrop()
                        .placeholder(R.drawable.laptop)
                        .into(categoryImageV)
                }
            } else {
                categoryImageV.setImageResource(R.drawable.laptop)
            }
            categoryMoreOPtionIV.setOnClickListener {
                if (openMoreOptions) {
                    openMoreOptions = false
                    categoryUpdeteDeleteLy.visibility = View.VISIBLE
                } else {
                    openMoreOptions = true
                    categoryUpdeteDeleteLy.visibility = View.GONE
                }
            }

            categoryUpdateTv.setOnClickListener {
//                categoryDialogUpdate(cat)
                AddCategoryFragment.newInstance(cat.id.toString()).show(
                    childFragmentManager, "Tag"
                )
            }

            categoryDeleteTv.setOnClickListener {
                //disable delete in category
//                categoryDialogDelete(cat)

                cat.isActive = cat.changeIsActive()
                categoryViewModel.updateCategoryCase(cat.id, cat.isActive).observe(
                    viewLifecycleOwner,
                    Observer {
                        context?.showMessage(it)
                        categoryViewModel.refresh()
                    }
                )
            }
        }

        override fun onClick(p0: View?) {
//            showPopUpMenu(p0!!)
            Log.d("catId",this.category.id!!.toString())
            callbacks?.onOpenProductManager(this.category.id!!)

        }
    }

    private inner class CategoryAdapter(val categoriesList: List<Category>) :
        RecyclerView.Adapter<CategoryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {

            val view: View = layoutInflater.inflate(
                R.layout.list_item_category,
                parent, false
            )
            return CategoryHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categoriesList[position]
            holder.bind(category)
        }

        override fun getItemCount(): Int {
            return categoriesList.size
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }


}