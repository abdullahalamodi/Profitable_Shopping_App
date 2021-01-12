package com.finalproject.profitableshopping.view.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category

import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import kotlinx.android.synthetic.main.delete_category.view.*
import kotlinx.android.synthetic.main.update_category.view.*


class CategoryFragment : Fragment(), MenuItem.OnMenuItemClickListener {

    private lateinit var categoryNameEt: EditText
    private lateinit var addBtn: Button
    private var test = 0
    private var category = Category()
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private var adapter: CategoryAdapter? = CategoryAdapter(emptyList())
    private var openMoreOptions = true

    override fun onStart() {
        super.onStart()
        addBtn.setOnClickListener {
            val cat = HashMap<String, Any>()
            cat["name"] = categoryNameEt.text.toString()
            if (test == 0) {
                val response = categoryViewModel.addCategory(cat)
                //will display message after get response
                response.observe(
                    viewLifecycleOwner,
                    Observer { message ->
                        Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                categoryViewModel.updateCategory(category.id, cat)
            }
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
        categoryRecyclerView.layoutManager=LinearLayoutManager(context)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { categoriesList ->
                updateUI(categoriesList)
                Toast.makeText(context, categoriesList.size.toString(), Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateUI(categoriesList: List<Category>) {
        adapter = CategoryAdapter(categoriesList)
        categoryRecyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CategoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        if (p0 != null) {
//            when (p0.itemId) {
//                // use the menu item id from menu xml file
//                R.id.edit -> {
//                    categoryName_ed.setText(category.categoryName)
//                }
//                else -> {
//                    categoryViewModel.deleteCategory(category.id)
//                }
//            }
        }
        return true
    }


    //adapter code is last thing in the fragment or activity
    private inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        //        var categoryNameBtn = view.findViewById(R.id.pop_menue) as Button
        var categoryNameTv: TextView = view.findViewById(R.id.txt_category_name) as TextView
        var categoryUpdateTv: TextView = view.findViewById(R.id.tv_update_category) as TextView
        var categoryDeleteTv: TextView = view.findViewById(R.id.tv_delete_category) as TextView
        var categoryMoreOPtionIV: ImageView = view.findViewById(R.id.img_more_options) as ImageView
        var categoryUpdeteDeleteLy: LinearLayout =
            view.findViewById(R.id.ly_update_delete_category) as LinearLayout

        init {
            //   categoryNameBtn.setOnClickListener(this)
        }

//        fun showPopUpMenu(v: View) {
//            popupMenu = PopupMenu(requireContext(), v)
//            popupMenu.setOnMenuItemClickListener(this@CategoryCrudFragment)
//            // do inflate for menu xml file her
//            popupMenu.inflate()
//            popupMenu.show()
//        }

        ////////////////////this function to update the category
        fun categoryDialogUpdate(){
            var alertBuilder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.update_category, null)
            alertBuilder.setView(view)
            var alertDialog = alertBuilder.create()
            alertDialog.show()
            view.btn_cancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        ////////////////////this function to delete the category
        fun categoryDialogDelete(){
            var alertBuilder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.delete_category, null)
            alertBuilder.setView(view)
            var alertDialog = alertBuilder.create()
            alertDialog.show()

            view.btn_exit.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        fun bind(cat: Category) {
            categoryNameTv.text = cat.name

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
                categoryDialogUpdate()
            }

            categoryDeleteTv.setOnClickListener {
                categoryDialogDelete()
            }
        }

        override fun onClick(p0: View?) {
//            showPopUpMenu(p0!!)
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


}