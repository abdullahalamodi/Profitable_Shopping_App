package com.finalproject.profitableshopping.view.category

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.getFileName
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.UploadRequestBody
import com.finalproject.profitableshopping.view.products.fragments.AddProductFragment

import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.delete_category.view.*
import kotlinx.android.synthetic.main.fragment_catergory_list.view.*
import kotlinx.android.synthetic.main.update_category.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

const val USER_ID_ARG = "userId";

class CategoryFragment : Fragment(),UploadRequestBody.UploadCallback {

    private lateinit var categoryNameEt: EditText
    private lateinit var addBtn: Button
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoriesList: List<Category>
    private lateinit var progressBar: ProgressBar
    private lateinit var addCatFloatingABtn: FloatingActionButton
    private var adapter: CategoryAdapter? = CategoryAdapter(emptyList())
    private var openMoreOptions = true
    private var selectedImageUri: Uri? = null

    override fun onStart() {
        super.onStart()
//        pickImagesV.setOnClickListener {
//            showProgress(true)
//            pickImages()
//        }
        addBtn.setOnClickListener {
            showProgress(true)
            val catMap = Category();
            catMap.name = categoryNameEt.text.toString()
            val response = categoryViewModel.addCategory(catMap)
            //will display message after get response
            response.observe(
                viewLifecycleOwner,
                Observer { message ->
                    showProgress(false)
                    Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show()
                    categoryViewModel.refresh()
                }
            )
        }

       addCatFloatingABtn.setOnClickListener{
           var bottomSheetAddCat =AddCategoryFragment();
           bottomSheetAddCat.show(childFragmentManager,"Tag")
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

    private fun pickImages() {
        showProgress(false)
        openImageChooser()
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, AddProductFragment.REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                AddProductFragment.REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
//                    pickImagesV.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadImage(productId: String): MutableLiveData<String> {
        val responseLiveData = MutableLiveData<String>()
        if (selectedImageUri == null) {
            context?.showMessage("Select an Image First")
            responseLiveData.value = ""
            return responseLiveData
        }
        showProgress(true)
        val parcelFileDescriptor =
            context?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null) ?:
            return responseLiveData
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file =
            File(context?.cacheDir, context?.contentResolver?.getFileName(selectedImageUri!!)!!)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        categoryViewModel.uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            RequestBody.create(MediaType.parse("multipart/form-data"), productId)
        ).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                context?.showMessage(t.message!!)
                progressBar.progress = 0
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                response.body()?.let {
                    context?.showMessage(it)
                    progressBar.progress = 100
                    showProgress(false)
                }
            }

        })
        responseLiveData.value = ""
        return responseLiveData
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
        var categoryUpdateTv: TextView = view.findViewById(R.id.tv_update_category) as TextView
        var categoryDeleteTv: TextView = view.findViewById(R.id.tv_delete_category) as TextView
        var categoryMoreOPtionIV: ImageView = view.findViewById(R.id.img_more_options) as ImageView
        var categoryUpdeteDeleteLy: LinearLayout = view.findViewById(R.id.ly_update_delete_category) as LinearLayout


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
                val response = categoryViewModel.deleteCategory(cat.id!!)
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
                categoryDialogUpdate(cat)
            }

            categoryDeleteTv.setOnClickListener {
                categoryDialogDelete(cat)
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

    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }


}