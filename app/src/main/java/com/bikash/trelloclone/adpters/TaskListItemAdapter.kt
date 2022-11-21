package com.bikash.trelloclone.adpters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bikash.trelloclone.activities.TaskListActivity
import com.bikash.trelloclone.databinding.ItemTaskBinding
import com.bikash.trelloclone.models.Card
import com.bikash.trelloclone.models.Task


open class TaskListItemAdapter(private val context: Context, private var list: ArrayList<Task>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        //val layoutParams = LinearLayout.LayoutParams( (parent.width*0.7).toInt(),LinearLayout.LayoutParams.WRAP_CONTENT)
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
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
                val listName = holder.etTaskListName.text.toString()
                if (listName.isNotEmpty()){
                    if (context is TaskListActivity){
                        context.createTaskList(listName)
                    }
                }else{
                    Toast.makeText(context,"Please Enter Your List Name, It Cant be Empty",Toast.LENGTH_SHORT).show()
                }
            }
            holder.ibEditListName.setOnClickListener {
                holder.etEditTaskListName.setText(model.title)
                holder.llTitleView.visibility = View.GONE
                holder.cvEditTaskListName.visibility = View.VISIBLE


            }

            holder.ibCloseEditableView.setOnClickListener {
                holder.llTitleView.visibility = View.VISIBLE
                holder.cvEditTaskListName.visibility = View.GONE
            }
            holder.ibDoneEditListName.setOnClickListener {
                val listName = holder.etEditTaskListName.text.toString()
                if (listName.isNotEmpty()){
                    if (context is TaskListActivity){
                        context.updateTaskList(position,listName,model)
                    }
                }else{
                    Toast.makeText(context,"Please Enter A List Name",Toast.LENGTH_SHORT).show()
                }

            }

            holder.ibDeleteList.setOnClickListener {
                alertDialogForDeleteList(position,model.title)
            }
            holder.tvAddCard.setOnClickListener {
                holder.tvAddCard.visibility = View.GONE
                holder.cvAddCard.visibility = View.VISIBLE
            }

            holder.ibCloseCardName.setOnClickListener {
                holder.tvAddCard.visibility = View.VISIBLE
                holder.cvAddCard.visibility = View.GONE
            }

            holder.ibDoneCardName.setOnClickListener {
                val cardName = holder.etCardName.text.toString()
                if (cardName.isNotEmpty()){
                    if (context is TaskListActivity){
                        context.addCardToTaskList(position,cardName)
                    }
                }else{
                    Toast.makeText(context,"Please Enter Your List Name, It Cant be Empty",Toast.LENGTH_SHORT).show()
                }
            }

            holder.rvCardList.layoutManager = LinearLayoutManager(context)
            holder.rvCardList.setHasFixedSize(true)
            val adapter = CardListItemsAdapter(context,model.cards)
            holder.rvCardList.adapter = adapter

            adapter.setOnClickListener(
                object : CardListItemsAdapter.OnClickListener{
                    override fun onClick(cardPosition: Int) {
                        if (context is TaskListActivity){
                            context.cardDetails(position, cardPosition)
                        }
                    }
                }
            )

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun alertDialogForDeleteList(position: Int, title: String) {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Alert")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to delete $title.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed

            if (context is TaskListActivity) {
                context.deleteTaskList(position)
            }
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

   // private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

   // private fun Int.toPx(): Int = (this* Resources.getSystem().displayMetrics.density).toInt()

    private class MyViewHolder(binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {

        val tvAddTaskList = binding.tvAddTaskList
        val llTaskItem = binding.llTaskItem
        val tvTaskListTitle = binding.tvTaskListTitle
        val cvAddTaskListName = binding.cvAddTaskListName
        val ibCloseListName = binding.ibCloseListName
        val ibDoneListName = binding.ibDoneListName
        val etTaskListName = binding.etTaskListName
        val ibEditListName = binding.ibEditListName
        val etEditTaskListName = binding.etEditTaskListName
        val llTitleView = binding.llTitleView
        val cvEditTaskListName = binding.cvEditTaskListName
        val ibCloseEditableView = binding.ibCloseEditableView
        val ibDoneEditListName = binding.ibDoneEditListName
        val ibDeleteList = binding.ibDeleteList
        val tvAddCard = binding.tvAddCard
        val cvAddCard = binding.cvAddCard
        val ibCloseCardName = binding.ibCloseCardName
        val ibDoneCardName = binding.ibDoneCardName
        val etCardName = binding.etCardName
        val rvCardList = binding.rvCardList
    }
}