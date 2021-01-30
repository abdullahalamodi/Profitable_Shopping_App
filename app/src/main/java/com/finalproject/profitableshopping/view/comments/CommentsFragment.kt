package com.finalproject.profitableshopping.view.comments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalproject.profitableshopping.R
import com.finalproject.profitableshopping.data.models.Comment
import com.finalproject.profitableshopping.view.products.fragments.DetailsOfAllProductsFragment
import com.finalproject.profitableshopping.viewmodel.CommentViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CommentsFragment : BottomSheetDialogFragment() {

    private var productId: String? = null
    lateinit var commentsRecyclerView: RecyclerView
    lateinit var commentViewModel: CommentViewModel
    private var adapter:CommentsAdapter = CommentsAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view= inflater.inflate(R.layout.fragment_comments, container, false)
        commentsRecyclerView = view.findViewById(R.id.comments_recyclerView)
        commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadComments(productId.toString())

    }

        fun updateCommentsRecycler(comments: List<Comment>) {
        adapter = CommentsAdapter(comments)
        commentsRecyclerView.adapter = adapter
    }

    private fun loadComments(productId: String) {
        commentViewModel.getProductComments(productId).observe(
            viewLifecycleOwner,
            Observer {
                //averageOfRating(it)
                updateCommentsRecycler(it)
            }
        )
    }



    ////////////////////// adapter of comments ///////////////////////////////

  private inner class CommentsHolder(val view: View) : RecyclerView.ViewHolder(view) {
      // need to change next variable inflate to be comfortable with product item xml file
      var commentUser = view.findViewById(R.id.comment_user) as TextView
      var commentTitle = view.findViewById(R.id.comment_title) as TextView
      var commentDate: TextView = view.findViewById(R.id.comment_date) as TextView


      fun bind(comment: Comment) {
          // commentUser.text = comment.userId
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