package com.bikash.trelloclone.adpters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.databinding.ItemTaskBinding
import com.bikash.trelloclone.models.Task


open class TaskListItemAdapter(private val context: Context, private var list: ArrayList<Task>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        //val layoutParams = LinearLayout.LayoutParams( (parent.width*0.7).toInt(),LinearLayout.LayoutParams.WRAP_CONTENT)
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            if (position == list.size-1){
                holder.tvAddTaskList.visibility = View.VISIBLE
                holder.llTaskItem.visibility = View.GONE

            }else{

                holder.tvAddTaskList.visibility = View.GONE
                holder.llTaskItem.visibility = View.VISIBLE

            }
            holder.tvTaskListTitle.text = model.title
            holder.tvAddTaskList.setOnClickListener {
                holder.tvAddTaskList.visibility = View.GONE
                holder.cvAddTaskListName.visibility = View.VISIBLE
            }
            holder.ibCloseListName.setOnClickListener {
                holder.tvAddTaskList.visibility = View.VISIBLE
                holder.cvAddTaskListName.visibility = View.GONE
            }
            holder.ibDoneListName.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int = (this* Resources.getSystem().displayMetrics.density).toInt()

    private class MyViewHolder(binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {

        val tvAddTaskList = binding.tvAddTaskList
        val llTaskItem = binding.llTaskItem
        val tvTaskListTitle = binding.tvTaskListTitle
        val cvAddTaskListName = binding.cvAddTaskListName
        val ibCloseListName = binding.ibCloseListName
        val ibDoneListName = binding.ibDoneListName
    }
}