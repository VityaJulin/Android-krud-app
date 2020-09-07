package com.example.android_krud_app.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android_krud_app.R
import com.example.android_krud_app.dto.PostModel
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.android.synthetic.main.item_post.view.*
import splitties.toast.toast

class PostAdapter(val list: List<PostModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var repostsBtnClickListener: OnRepostBtnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(this, view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.v("test", "position is $position")
        with(holder as PostViewHolder) {
            bind(list[position])
        }
    }

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnRepostBtnClickListener {
        fun onRepostsBtnClicked(item: PostModel, position: Int)
    }
}


class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {
    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(context.getString(R.string.like_in_progress))
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }
            repostsBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[adapterPosition]
                    if (item.repostedByMe) {
                        context.toast("Can't repost repost)")
                    } else {
                        showDialog(context) {
                            adapter.repostsBtnClickListener?.onRepostsBtnClicked(
                              item,
                              currentPosition
                            )
                        }
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.ownerName
            contentTv.text = post.content
            likesTv.text = post.likes.toString()
            repostsTv.text = post.reposts.toString()

            if (post.likeActionPerforming) {
                likeBtn.setImageResource(R.drawable.ic_favorite_pending_24dp)
            } else if (post.likedByMe) {
                likeBtn.setImageResource(R.drawable.ic_favorite_active_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                likeBtn.setImageResource(R.drawable.ic_favorite_inactive_24dp)
                likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }

            if (post.repostActionPerforming) {
                repostsBtn.setImageResource(R.drawable.ic_reposts_pending)
            } else if (post.repostedByMe) {
                repostsBtn.setImageResource(R.drawable.ic_reposts_active)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            } else {
                repostsBtn.setImageResource(R.drawable.ic_reposts_inactive)
                repostsTv.setTextColor(ContextCompat.getColor(context, R.color.colorBrown))
            }
        }
    }

    fun showDialog(context: Context, createBtnClicked: (content: String) -> Unit) {
        val dialog = AlertDialog.Builder(context)
            .setView(R.layout.activity_create_post)
            .show()
        dialog.createPostBtn.setOnClickListener {
            createBtnClicked(dialog.contentEdt.text.toString())
            dialog.dismiss()
        }
    }
}
