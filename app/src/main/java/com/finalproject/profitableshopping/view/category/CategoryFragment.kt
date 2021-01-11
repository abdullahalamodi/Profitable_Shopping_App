package com.finalproject.profitableshopping.view.category
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category

import com.finalproject.profitableshopping.viewmodel.CategoryViewModel


class CategoryFragment : Fragment(), MenuItem.OnMenuItemClickListener {


    private lateinit var categoryNameEt: EditText
    private lateinit var addBtn: Button
    private var test = 0
    private var category = Category()
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryRecyclerView: RecyclerView

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
                        Toast.makeText(context,message.toString(),Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                categoryViewModel.updateCategory(category.id,cat)
            }
        }
    }
    private var adapter: CategoryAdapter? = CategoryAdapter(emptyList())
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
                Toast.makeText(context,categoriesList.size.toString(),Toast.LENGTH_SHORT).show()
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
        var categoryNameBtn = view.findViewById(R.id.pop_menue) as Button
        var categoryNameTv: TextView = view.findViewById(R.id.category_name) as TextView

        init {
            categoryNameBtn.setOnClickListener(this)
        }

//        fun showPopUpMenu(v: View) {
//            popupMenu = PopupMenu(requireContext(), v)
//            popupMenu.setOnMenuItemClickListener(this@CategoryCrudFragment)
//            // do inflate for menu xml file her
//            popupMenu.inflate()
//            popupMenu.show()
//        }

        fun bind(cat: Category) {
            categoryNameTv.text = cat.name
        }

        override fun onClick(p0: View?) {
//            showPopUpMenu(p0!!)
        }
    }

    private inner class CategoryAdapter(val categoriesList: List<Category>) :
        RecyclerView.Adapter<CategoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {

            val view: View = layoutInflater.inflate(
                R.layout.category_lis_item,
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