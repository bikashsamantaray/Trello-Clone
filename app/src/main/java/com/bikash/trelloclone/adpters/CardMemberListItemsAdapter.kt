package com.bikash.trelloclone.adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ItemCardBinding
import com.bikash.trelloclone.databinding.ItemCardSelectedMemberBinding
import com.bikash.trelloclone.models.SelectedMembers
import com.bumptech.glide.Glide

open class CardMemberListItemsAdapter(
    private val context: Context,
    private val list: ArrayList<SelectedMembers>,
    private val assignedMembers: Boolean): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemCardSelectedMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            if (position == list.size - 1  && assignedMembers){
                holder.ivAddMember.visibility = View.VISIBLE
                holder.ivSelectedMemberImage.visibility = View.GONE
            }else{
                holder.ivAddMember.visibility = View.GONE
                holder.ivSelectedMemberImage.visibility = View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_place_holder)
                    .into(holder.ivSelectedMemberImage)
            }
            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {

        fun onClick()
    }

    private class MyViewHolder(binding: ItemCardSelectedMemberBinding) : RecyclerView.ViewHolder(binding.root){

        val ivAddMember = binding.ivAddMember
        val ivSelectedMemberImage = binding.ivSelectedMemberImage


    }
}