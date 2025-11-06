package com.fadhil_068.post5

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.widget.PopupMenu

class PostAd(private val postList: MutableList<Post>) :
    RecyclerView.Adapter<PostAd.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userProfileImage: ImageView = itemView.findViewById(R.id.img_post_profile)
        val username: TextView = itemView.findViewById(R.id.tv_post_username)
        val postImage: ImageView = itemView.findViewById(R.id.img_post_image)
        val caption: TextView = itemView.findViewById(R.id.tv_post_caption)
        val optionsButton: ImageView = itemView.findViewById(R.id.btn_post_options)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        holder.userProfileImage.setImageResource(post.userImageUrl)
        holder.username.text = post.username
        holder.caption.text = post.caption

        when (post.postImageUrl) {
            is Int -> holder.postImage.setImageResource(post.postImageUrl as Int)
            is Uri -> holder.postImage.setImageURI(post.postImageUrl as Uri)
        }

        holder.optionsButton.setOnClickListener {
            val context = holder.itemView.context
            val popup = PopupMenu(context, holder.optionsButton)
            popup.inflate(R.menu.post_options_menu)

            popup.setOnMenuItemClickListener { item ->
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition == RecyclerView.NO_POSITION) return@setOnMenuItemClickListener false

                when (item.itemId) {
                    R.id.menu_edit -> {
                        if (context is MainActivity) {
                            context.editPost(currentPosition)
                        } else {
                            Toast.makeText(context, "Gagal membuka mode edit", Toast.LENGTH_SHORT).show()
                        }
                        true
                    }

                    R.id.menu_delete -> {
                        postList.removeAt(currentPosition)
                        notifyItemRemoved(currentPosition)
                        notifyItemRangeChanged(currentPosition, postList.size)
                        Toast.makeText(context, "Postingan berhasil dihapus", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }

            popup.show()
        }
    }

    override fun getItemCount(): Int = postList.size
}
