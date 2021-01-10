package com.finalproject.profitableshopping.categorty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.CategoryModel

import com.finalproject.profitableshopping.viewmodel.CategoryViewModel

class CategoryCrudFragment : Fragment(), MenuItem.OnMenuItemClickListener,
    PopupMenu.OnMenuItemClickListener {


    lateinit var categoryName_ed: EditText
    private lateinit var save_btn: Button
    var test = 0
    var category = CategoryModel()

    private lateinit var categoryViewModel: CategoryViewModel
    lateinit var categoryRecycelerView: RecyclerView
    var categoryList: List<CategoryModel> = emptyList()
    private var adapter: CategoryAdapter? = CategoryAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel.categoriesLiveData.observe(
            viewLifecycleOwner,
            Observer { categoriesList ->

                updateUI(categoriesList)
                categoryList = categoriesList
            }
        )
    }

    private fun updateUI(categoriesList: List<CategoryModel>) {
        adapter = CategoryAdapter(categoriesList)
        categoryRecycelerView.adapter = adapter
    }

    private inner class CategoryHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        lateinit var popupMenu: PopupMenu
        var categoryNameBtn = view.findViewById(R.id.pop_menue) as Button
        var categoryNameTv: TextView = view.findViewById(R.id.category_name) as TextView


        init {
            categoryNameBtn.setOnClickListener(this)
        }

        fun showPopUpMenu(v: View) {
            popupMenu = PopupMenu(requireContext(), v)
            popupMenu.setOnMenuItemClickListener(this@CategoryCrudFragment)
            // do inflate for menu xml file her
            popupMenu.inflate()
            popupMenu.show()
        }

        fun bind(cat: CategoryModel) {
            category = cat
        }

        override fun onClick(p0: View?) {
            showPopUpMenu(p0!!)
        }
    }

    private inner class CategoryAdapter(val categoriesList: List<CategoryModel>) :
        RecyclerView.Adapter<CategoryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {

            var view: View
            view = layoutInflater.inflate(
                R.layout.category_lis_itemt,
                parent, false
            )
            return CategoryHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            val category = categoriesList.get(position)

            holder.bind(category)
        }

        override fun getItemCount(): Int {
            return categoriesList.size
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout fo+
        // r this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onStart() {
        super.onStart()
        save_btn.setOnClickListener {

            val cat = HashMap<Any, Any>()
            cat.put("name", categoryName_ed.text.toString())
            if (test == 0) {
                categoryViewModel.createCategory(cat)
            } else {
                categoryViewModel.editeCategory(category.id, cat)
            }
            updateUI(categoryList)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CategoryCrudFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        if (p0 != null) {
            when (p0.itemId) {
                // use the menu item id from menu xml file
                R.id.edit -> {
                    categoryName_ed.setText(category.categoryName)
                }
                else -> {
                    categoryViewModel.deleteCategory(category.id)
                }


            }
        }
        return true
    }


}