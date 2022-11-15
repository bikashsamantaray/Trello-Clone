package com.bikash.trelloclone.firebase

import android.app.Activity
import android.os.Build.VERSION_CODES.S
import android.util.Log
import android.widget.Toast
import com.bikash.trelloclone.activities.*
import com.bikash.trelloclone.models.Board
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

    fun updateUserProfileDataFirestore(activity: MyProfileActivity, userHashMap: HashMap<String, Any>){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).update(userHashMap).addOnSuccessListener {
            Log.i(activity.javaClass.simpleName,"profile data updated successfully!")
            Toast.makeText(activity,"Profile updated successfully",Toast.LENGTH_SHORT).show()
            activity.profileUpdateSuccess()
        }.addOnFailureListener{
            e ->
            activity.hideProgressDialog()
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
}