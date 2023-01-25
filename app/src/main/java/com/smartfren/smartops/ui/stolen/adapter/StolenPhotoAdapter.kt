package com.smartfren.smartops.ui.stolen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartfren.smartops.R
import com.smartfren.smartops.data.models.Stolen

class StolenPhotoAdapter(private val context: Context, private val photo: MutableList<Stolen>) :
    RecyclerView.Adapter<StolenPhotoAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(inflater, parent)
    }

    inner class ContactViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.item_stolen_photo, parent, false)
    ) {

        private val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        private val ivStolenPhoto: ImageView = itemView.findViewById(R.id.ivStolenPhotoItem)

        fun bind(stolen: Stolen) {
            Glide.with(context)
                .load(stolen.image)
                .into(ivStolenPhoto)
            btnDelete.setOnClickListener { removeContact() }
        }

        private fun removeContact() {
            photo.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }
    }

    fun addPhoto(stolen: Stolen) {
        photo.add(stolen)
        notifyItemInserted(itemCount)
    }

    fun clearAll() {
        photo.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(photo[position])
    }

    override fun getItemCount() = photo.size

}