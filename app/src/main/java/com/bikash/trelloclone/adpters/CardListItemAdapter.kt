package com.bikash.trelloclone.adpters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.R
import com.bikash.trelloclone.activities.TaskListActivity
import com.bikash.trelloclone.databinding.ItemCardBinding
import com.bikash.trelloclone.databinding.ItemTaskBinding
import com.bikash.trelloclone.models.Card
import com.bikash.trelloclone.models.SelectedMembers


open class CardListItemsAdapter(
    private val context: Context,
    private var list: ArrayList<Card>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            if (model.labelColor.isNotEmpty()){

                holder.viewLabelColor.visibility = View.VISIBLE
                holder.viewLabelColor.setBackgroundColor(Color.parseColor(model.labelColor))

            }else{

                holder.viewLabelColor.visibility = View.GONE
            }

            holder.tvCardName.text = model.name

            if ((context as TaskListActivity).mAssignedMembersDetailList.size > 0){
                val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()

                for ( i in context.mAssignedMembersDetailList.indices){
                    for (j in model.assignedTo){
                        if (context.mAssignedMembersDetailList[i].id == j){
                            val selectedMembers = SelectedMembers(
                                context.mAssignedMembersDetailList[i].id,
                                context.mAssignedMembersDetailList[i].image
                            )
                            selectedMembersList.add(selectedMembers)
                        }
                    }
                }
                if (selectedMembersList.size > 0){
                    if (selectedMembersList.size == 1 && selectedMembersList[0].id == model.createdBy){
                        holder.rvCardSelectedMemberList.visibility = View.GONE
                    }else{
                        holder.rvCardSelectedMemberList.visibility = View.VISIBLE
                        holder.rvCardSelectedMemberList.layoutManager = GridLayoutManager(context,4)
                        val adapter = CardMemberListItemsAdapter(context,selectedMembersList,false)
                        holder.rvCardSelectedMemberList.adapter = adapter
                        adapter.setOnClickListener(object : CardMemberListItemsAdapter.OnClickListener{
                            override fun onClick() {
                                if (onClickListener != null){
                                    onClickListener!!.onClick(position)
                                }
                            }
                        })
                    }
                }else{
                    holder.rvCardSelectedMemberList.visibility = View.GONE
                }
            }

            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position)
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
        fun onClick(position: Int)
    }

    private class MyViewHolder(binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root){
        val tvCardName = binding.tvCardName
        val viewLabelColor = binding.viewLabelColor
        val rvCardSelectedMemberList = binding.rvCardSelectedMemberList

    }
}
