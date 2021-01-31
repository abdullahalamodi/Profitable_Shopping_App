package com.finalproject.profitableshopping.view.category


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Category
import com.finalproject.profitableshopping.getFileName
import com.finalproject.profitableshopping.showMessage
import com.finalproject.profitableshopping.view.products.UploadRequestBody
import com.finalproject.profitableshopping.viewmodel.CategoryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

const val ARG_CAT_ID = "cat_id";

class AddCategoryFragment : BottomSheetDialogFragment(), UploadRequestBody.UploadCallback {

    private lateinit var categoryNameEt: EditText
    private lateinit var addBtn: Button
    private lateinit var loadBtn: ImageView
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var progressBar: ProgressBar
    private var selectedImageUri: Uri? = null
    private var categoryId: String? = null
    private var isUpdate = false
    private var callBacks:CallBacks? = null


    override fun onStart() {
        super.onStart()
        callBacks = (context as CallBacks)
        addBtn.setOnClickListener {
            showProgress(true)
            val category = Category();
            category.name = categoryNameEt.text.toString()
            if (isUpdate) {
                updateCategory(category)
            } else {
                addCategory(category)
            }
        }


        loadBtn.setOnClickListener {
            pickImages()
        }
    }

    private fun addCategory(category: Category) {
        categoryViewModel.addCategory(category).observe(
            viewLifecycleOwner,
            Observer { id ->
                showProgress(false)
                uploadImage(id).observe(
                    viewLifecycleOwner,
                    Observer {
                        showProgress(false)
                        categoryViewModel.refresh()
                        callBacks?.onSuccess(this)
                    }
                )
            }
        )
    }

    private fun updateCategory(category: Category) {
        category.id = categoryId?.toInt()
        categoryViewModel.updateCategory(category.id, category).observe(
            viewLifecycleOwner,
            Observer {
                showProgress(false)
                uploadImage(categoryId!!).observe(
                    viewLifecycleOwner,
                    Observer {
                        showProgress(false)
                        categoryViewModel.refresh()
                        callBacks?.onSuccess(this)
                    }
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        arguments?.let {
            categoryId = it.getString(ARG_CAT_ID)
            if (categoryId != null)
                categoryViewModel.loadCategory(categoryId?.toInt()!!)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_add_category, container, false)
        categoryNameEt = view.findViewById(R.id.category_name_et)
        addBtn = view.findViewById(R.id.add_category_btn)
        loadBtn = view.findViewById(R.id.load_image_btn)
        progressBar = view.findViewById(R.id.progress_circular)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (categoryId != null) {
            showProgress(true)
            categoryViewModel.categoryDetailsLiveData.observe(
                viewLifecycleOwner,
                Observer { category ->
                    updateUi(category)
                    showProgress(false)
                }
            )
        }

    }

    private fun updateUi(category: Category) {
        isUpdate = true
        addBtn.text = "تعديل"
        categoryNameEt.setText(category.name)
        if (category.path != null)
            Picasso.get().also {
                val path = category.getUrl()
                it.load(path)
                    .resize(45, 45)
                    .centerCrop()
                    .placeholder(R.drawable.laptop)
                    .into(loadBtn)
            }

    }


    private fun showProgress(show: Boolean) {
        if (show)
            progressBar.visibility = View.VISIBLE
        else
            progressBar.visibility = View.GONE
    }

    private fun pickImages() {
        openImageChooser()
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    loadBtn.setImageURI(selectedImageUri)
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
            context?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null)
                ?: return responseLiveData
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


    override fun onProgressUpdate(percentage: Int) {
        progressBar.progress = percentage
    }

    interface CallBacks{
        fun onSuccess(fragment: AddCategoryFragment)
    }


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101

        @JvmStatic
        fun newInstance(categoryId: String?) = AddCategoryFragment().apply {
            arguments = Bundle().apply {
                categoryId?.let {
                    putString(ARG_CAT_ID, it)
                }
            }
        }

    }
}