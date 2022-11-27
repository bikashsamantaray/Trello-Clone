package com.bikash.trelloclone.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.bikash.trelloclone.activities.*
import com.bikash.trelloclone.models.Board
import com.bikash.trelloclone.models.User
import com.bikash.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity,userInfo: com.bikash.trelloclone.models.User){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.userRegisteredSuccess()
        }.addOnFailureListener{
            excepetion ->
            activity.hideProgressDialog()
            Log.e(activity.javaClass.simpleName,"Error writing board", excepetion)
        }

    }

    fun createBoard(activity: CreateBoardActivity,board: Board){
        mFireStore.collection(Constants.BOARDS).document().set(board, SetOptions.merge()).addOnSuccessListener {
            Log.e(activity.javaClass.simpleName,"Board Created successfully")
            Toast.makeText(activity,"Board created successfully",Toast.LENGTH_LONG).show()
            activity.boardCreatedSuccessFully()
        }.addOnFailureListener{
            Log.e(activity.javaClass.simpleName,"Error writing document")
        }
    }

    fun getBoardsList(activity: MainActivity) {

        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val boardsList: ArrayList<Board> = ArrayList()

                for (i in document.documents) {

                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id

                    boardsList.add(board)
                }

                activity.populateBoardsListToUI(boardsList)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }



    fun loadUserData(activity: Activity,readBoardList: Boolean = false){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).get().addOnSuccessListener {
            document ->
            val loggedInUser = document.toObject(com.bikash.trelloclone.models.User::class.java)

            when(activity){
                is SignInActivity -> {
                    activity.signInSuccess(loggedInUser)
                }
                is MainActivity -> {
                    activity.updateNavigationUserDetails(loggedInUser,readBoardList)
                }

                is MyProfileActivity -> {
                    activity.setUserData(loggedInUser)
                }
            }


        }.addOnFailureListener{
            when(activity){
                is SignInActivity -> {
                    activity.hideProgressDialog()
                }
                is MainActivity -> {
                    activity.hideProgressDialog()
                }
            }
            Log.e(activity.javaClass.simpleName,"Error writing document")
        }

    }


    fun addUpdateTaskList(activity: Activity,board: Board){
        val taskListHashMap = HashMap<String,Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName,"TaskList Updated successfully")
                if (activity is TaskListActivity){
                    activity.addUpdateTaskListSuccess()
                }else if(activity is CardDetailsActivity){
                    activity.addUpdateTaskListSuccess()

                }



            }.addOnFailureListener {
                exception ->
                if (activity is TaskListActivity){
                    activity.hideProgressDialog()
                }else if(activity is CardDetailsActivity){
                    activity.hideProgressDialog()

                }

                Log.e(activity.javaClass.simpleName,"Error while creating",exception)
            }
    }


    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).update(userHashMap).addOnSuccessListener {
            Log.i(activity.javaClass.simpleName,"profile data updated successfully!")
            Toast.makeText(activity,"Profile updated successfully",Toast.LENGTH_SHORT).show()
            when(activity){
                is MainActivity ->{
                    activity.tokenUpdateSuccess()
                }
                is MyProfileActivity ->{
                    activity.profileUpdateSuccess()
                }
            }

        }.addOnFailureListener{
            e ->
            when(activity){
                is MainActivity ->{
                    activity.hideProgressDialog()
                }
                is MyProfileActivity ->{
                    activity.hideProgressDialog()
                }
            }
            Log.e(activity.javaClass.simpleName,"Error while creating a board",e)
            Toast.makeText(activity,"Profile not updated successfully",Toast.LENGTH_SHORT).show()

        }

    }


     fun getCurrentUserId():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getBoardSDetails(activity: TaskListActivity, documentId: String) {

        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id

                activity.boardDetails(board)

            }
            .addOnFailureListener { e ->


                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }

    }

    fun getAssignedMembersListDetails(activity: Activity,assignedTo: ArrayList<String>){
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID,assignedTo)
            .get()
            .addOnSuccessListener {
                document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val userList :ArrayList<User> = ArrayList()

                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    userList.add(user)
                }

                if (activity is MembersActivity){
                    activity.setUpMemberList(userList)
                }else if (activity is TaskListActivity){
                    activity.boardMembersList(userList)
                }



            }.addOnFailureListener { e ->

                if (activity is MembersActivity){
                    activity.hideProgressDialog()
                }else if (activity is TaskListActivity){
                    activity.hideProgressDialog()
                }


                Log.e(activity.javaClass.simpleName,"Error while creating Board",e)

            }
    }

    fun getMemberDetails(activity: MembersActivity, email: String){
        mFireStore.collection(Constants.USERS)
            .whereEqualTo(Constants.EMAIl,email)
            .get()
            .addOnSuccessListener {
                document ->
                if (document.documents.size > 0){
                    val user = document.documents[0].toObject(User::class.java)!!
                    activity.memberDetails(user)
                }else{
                    activity.hideProgressDialog()
                    activity.showErrorSnackBar("No such member found")
                }
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while getting User Details",e)

            }
    }

    fun assignMemberToBoard(activity: MembersActivity, board: Board, user: User){

        val assignedToToHashMap = HashMap<String,Any>()
        assignedToToHashMap[Constants.ASSIGNED_TO] = board.assignedTo

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(assignedToToHashMap)
            .addOnSuccessListener {
                activity.memberAssignSuccess(user)
            }.addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while creating a board",e)

            }

    }

}