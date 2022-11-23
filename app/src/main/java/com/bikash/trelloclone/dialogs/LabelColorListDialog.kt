package com.bikash.trelloclone.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bikash.trelloclone.R
import com.bikash.trelloclone.adpters.LabelColorListItemsAdapter
import com.bikash.trelloclone.databinding.ActivityCardDetailsBinding
import com.bikash.trelloclone.databinding.DialogListBinding


// TODO (Step 5: Create an dialogs package and a class for showing the label color list dialog.)
// START
abstract class LabelColorListDialog(
    context: Context,
    private var list: ArrayList<String>,
    private val title: String = "",
    private val mSelectedColor: String = ""
) : Dialog(context) {

    private var adapter: LabelColorListItemsAdapter? = null
    private var binding: DialogListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogListBinding.inflate(layoutInflater)
        setContentView(binding?.root!!)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(binding!!)
    }

    private fun setUpRecyclerView(view: DialogListBinding) {
        view.tvTitle.text = title
        view.rvList.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListItemsAdapter(context, list, mSelectedColor)
        view.rvList.adapter = adapter

        adapter!!.onItemClickListener = object : LabelColorListItemsAdapter.OnItemClickListener {

            override fun onClick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }
        }
    }

    protected abstract fun onItemSelected(color: String)
}
// END