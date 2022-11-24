package com.bikash.trelloclone.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikash.trelloclone.R
import com.bikash.trelloclone.adpters.MemberListItemsAdapter
import com.bikash.trelloclone.databinding.DialogListBinding
import com.bikash.trelloclone.models.User



abstract class MembersListDialog(
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
) : Dialog(context) {

    private var adapter: MemberListItemsAdapter? = null
    private var binding: DialogListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())

        binding = DialogListBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(binding!!)
    }

    private fun setUpRecyclerView(view: DialogListBinding) {
        view.tvTitle.text = title

        if (list.size > 0) {

            view.rvList.layoutManager = LinearLayoutManager(context)
            adapter = MemberListItemsAdapter(context, list)
            view.rvList.adapter = adapter

            adapter!!.setOnClickListener(object :
                MemberListItemsAdapter.OnClickListener {
                override fun onClick(position: Int, user: User, action:String) {
                    dismiss()
                    onItemSelected(user, action)
                }
            })
        }
    }

    protected abstract fun onItemSelected(user: User, action:String)
}
// END