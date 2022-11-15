package com.bikash.trelloclone.adpters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.R
import com.bikash.trelloclone.databinding.ItemBoardBinding
import com.bikash.trelloclone.models.Board
import com.bumptech.glide.Glide

open class BoardItemAdapter(private val context: Context, private var list: ArrayList<Board>):

    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemBoardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_board_place_holder)
                .into(holder.boardImage)

            holder.tvName.text = model.name
            holder.createdBy.text = "Created by: ${model.createdBy}"

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }

            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: Board)
        
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

     private class MyViewHolder(binding: ItemBoardBinding): RecyclerView.ViewHolder(binding.root){
         val boardImage = binding.boardImage
         val tvName = binding.tvName
         val createdBy = binding.createdBy
     }

}