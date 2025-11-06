package com.fadhil_068.post5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CeritaAd(private val ceritaList: List<Cerita>) :
    RecyclerView.Adapter<CeritaAd.CeritaViewHolder>() {

    class CeritaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.img_story_profile)
        val username: TextView = itemView.findViewById(R.id.tv_story_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CeritaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_story, parent, false)
        return CeritaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CeritaViewHolder, position: Int) {
        val cerita = ceritaList[position]
        holder.profileImage.setImageResource(cerita.imageUrl)
        holder.username.text = cerita.username
    }

    override fun getItemCount(): Int {
        return ceritaList.size
    }
}
