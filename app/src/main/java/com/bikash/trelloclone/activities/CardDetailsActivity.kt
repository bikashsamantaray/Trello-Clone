package com.bikash.trelloclone.activities

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bikash.trelloclone.R
import com.bikash.trelloclone.adpters.CardMemberListItemsAdapter
import com.bikash.trelloclone.databinding.ActivityCardDetailsBinding
import com.bikash.trelloclone.databinding.ActivityMembersBinding
import com.bikash.trelloclone.dialogs.LabelColorListDialog
import com.bikash.trelloclone.dialogs.MembersListDialog
import com.bikash.trelloclone.firebase.FireStoreClass
import com.bikash.trelloclone.models.*
import com.bikash.trelloclone.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CardDetailsActivity : BaseActivity() {

    private  lateinit var mBoardDetails: Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    private var mSelectedColor = ""
    private lateinit var mMemberDetailList: ArrayList<User>
    private var mSelectedDueDateMilliSeconds: Long = 0

    private var binding: ActivityCardDetailsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = ActivityCardDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        getIntentData()
        setupActionBar()
        binding?.etNameCardDetails?.setText(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
        binding?.etNameCardDetails?.setSelection(binding?.etNameCardDetails?.text.toString().length)

        mSelectedColor = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].labelColor

        if (mSelectedColor.isNotEmpty()){
            setColor()
        }



        binding?.btnUpdateCardDetails?.setOnClickListener {
            if (binding?.etNameCardDetails?.text.toString().isNotEmpty()){
                updateCardDetails()
            }else{
                    Toast.makeText(this,"Enter a card Name",Toast.LENGTH_SHORT).show()
            }

        }

        binding?.tvSelectLabelColor?.setOnClickListener {
            labelColorsListDialog()
        }

        binding?.tvSelectMembers?.setOnClickListener {
            membersListDialog()
        }

        setupSelectedMemberList()

        mSelectedDueDateMilliSeconds = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].dueDate

        if (mSelectedDueDateMilliSeconds > 0){
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH)
            val selectedDate = simpleDateFormat.format(Date(mSelectedDueDateMilliSeconds))
            binding?.tvSelectDueDate?.text = selectedDate
        }

        binding?.tvSelectDueDate?.setOnClickListener {
            showDataPicker()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(binding?.toolbarCardDetailsActivity)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        supportActionBar?.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.toolbarCardDetailsActivity?.setNavigationOnClickListener{
            onBackPressed()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card,menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun colorList(): ArrayList<String>{
        val colorsList: ArrayList<String> = ArrayList()
        colorsList.add("#43C86F")
        colorsList.add("#0C90F1")
        colorsList.add("#F72400")
        colorsList.add("#7A8089")
        colorsList.add("#D57C1D")
        colorsList.add("#770000")
        colorsList.add("#0022F8")
        return colorsList
    }

    private fun setColor(){

        binding?.tvSelectLabelColor?.text = ""

        binding?.tvSelectLabelColor?.setBackgroundColor(Color.parseColor(mSelectedColor))


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_delete_card -> {
                alertDialogForDeleteCard(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getIntentData(){
        if (intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAILS)!!
        }
        if (intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition = intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION,-1)
        }
        if (intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition = intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION,-1)
        }
        if (intent.hasExtra(Constants.BOARD_MEMBERS_LIST)){
            mMemberDetailList = intent.getParcelableArrayListExtra(Constants.BOARD_MEMBERS_LIST)!!

        }
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateCardDetails(){
        val card = Card(
            binding?.etNameCardDetails?.text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo,
            mSelectedColor,
            mSelectedDueDateMilliSeconds
        )

        val taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size-1)
        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition] = card
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this@CardDetailsActivity, mBoardDetails)
    }
    private fun deleteCard(){
        val cardList: ArrayList<Card> = mBoardDetails.taskList[mTaskListPosition].cards
        cardList.removeAt(mCardPosition)

        val taskList: ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size-1)

        taskList[mTaskListPosition].cards = cardList
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    private fun alertDialogForDeleteCard(cardName: String) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(resources.getString(R.string.alert))

        builder.setMessage(
            resources.getString(
                R.string.confirmation_message_to_delete_card,
                cardName
            )
        )
        builder.setIcon(android.R.drawable.ic_dialog_alert)


        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, which ->
            dialogInterface.dismiss()
            deleteCard()
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun labelColorsListDialog(){
        val colorList: ArrayList<String> = colorList()
        val listDialog = object : LabelColorListDialog(
            this,
            colorList,
            resources.getString(R.string.str_select_label_color),
            mSelectedColor){
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()

            }

        }
        listDialog.show()

    }

    private fun membersListDialog(){
        val cardAssignedMemberList = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo

        if (cardAssignedMemberList.size > 0){
            for (i in mMemberDetailList.indices){
                for (j in cardAssignedMemberList){
                    if (mMemberDetailList[i].id == j){
                        mMemberDetailList[i].selected = true
                    }
                }
            }
        }else{
            for (i in mMemberDetailList.indices){
                mMemberDetailList[i].selected = false
            }

        }
        val listDialog = object : MembersListDialog(
            this,
            mMemberDetailList,
            resources.getString(R.string.select_members)
        ){
            override fun onItemSelected(user: User, action: String) {
                if (action == Constants.SELECT){
                    if (!mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.contains(user.id)){
                        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.add(user.id)
                    }
                }else{
                    mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.remove(user.id)


                    for (i in mMemberDetailList.indices){
                        if (mMemberDetailList[i].id == user.id){
                            mMemberDetailList[i].selected = false
                        }
                    }
                }

                setupSelectedMemberList()


            }

        }
        listDialog.show()
    }

    private fun setupSelectedMemberList(){
        val cardAssignedMemberList =mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo
        val selectedMembersList: ArrayList<SelectedMembers> = ArrayList()
        for (i in mMemberDetailList.indices){
            for (j in cardAssignedMemberList){
                if (mMemberDetailList[i].id == j){
                    val selectedMembers = SelectedMembers(
                        mMemberDetailList[i].id,
                        mMemberDetailList[i].image
                    )
                    selectedMembersList.add(selectedMembers)
                }
            }
        }
        if (selectedMembersList.size > 0){
            selectedMembersList.add(SelectedMembers("",""))
            binding?.tvSelectMembers?.visibility = View.GONE
            binding?.rvSelectedMembersList?.visibility = View.VISIBLE

            binding?.rvSelectedMembersList?.layoutManager = GridLayoutManager(
                this,6
            )
            val adapter = CardMemberListItemsAdapter(this,selectedMembersList,true)

            binding?.rvSelectedMembersList?.adapter = adapter

            adapter.setOnClickListener(
                object : CardMemberListItemsAdapter.OnClickListener{
                    override fun onClick() {
                        membersListDialog()
                    }
                }
            )
        }else{
            binding?.tvSelectMembers?.visibility = View.VISIBLE
            binding?.rvSelectedMembersList?.visibility = View.GONE
        }
    }

    private fun showDataPicker() {

        val c = Calendar.getInstance()
        val year =
            c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val sMonthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"

                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
                binding?.tvSelectDueDate?.text = selectedDate


                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)


                val theDate = sdf.parse(selectedDate)

                mSelectedDueDateMilliSeconds = theDate!!.time
            },
            year,
            month,
            day
        )
        dpd.show()
    }

}

