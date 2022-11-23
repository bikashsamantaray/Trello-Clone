package com.bikash.trelloclone.adpters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.databinding.ItemCardBinding
import com.bikash.trelloclone.databinding.ItemLabelColorBinding

class LabelColorListItemsAdapter(private val context: Context,
                                 private var list:ArrayList<String>,
                                 private val mSelectedColor: String):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemLabelColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is MyViewHolder){
            holder.viewMain.setBackgroundColor(Color.parseColor(item))
            if (item == mSelectedColor){
                holder.ivSelectedColor.visibility = View.VISIBLE
            }else{
                holder.ivSelectedColor.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                if (onItemClickListener != null){
                    onItemClickListener!!.onClick(position,item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    private class MyViewHolder(binding: ItemLabelColorBinding) : RecyclerView.ViewHolder(binding.root){

        val viewMain = binding.viewMain
        val ivSelectedColor = binding.ivSelectedColor




    }

    public interface OnItemClickListener {

        fun onClick(position: Int, color: String)
    }

}