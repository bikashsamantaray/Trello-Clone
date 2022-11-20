package com.projemanag.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ItemMemberBinding
import com.bikash.trelloclone.models.User
import com.bumptech.glide.Glide

open class MemberListItemsAdapter(private val context: Context, private var list: ArrayList<User>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.ivMemberImage)

            holder.tvMemberName.text = model.name
            holder.tvMemberEmail.text = model.mail
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(binding: ItemMemberBinding) : RecyclerView.ViewHolder(binding.root){

        val ivMemberImage = binding.ivMemberImage
        val tvMemberName = binding.tvMemberName
        val tvMemberEmail = binding.tvMemberEmail



    }
}