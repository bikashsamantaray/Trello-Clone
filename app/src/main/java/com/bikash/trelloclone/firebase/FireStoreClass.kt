package com.bikash.trelloclone.firebase

import com.bikash.trelloclone.activities.SignUpActivity
import com.bikash.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User

class FireStoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity,userInfo: com.bikash.trelloclone.models.User){

        mFireStore.collection(Constants.USERS).document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.userRegisteredSuccess()
        }

    }

    private fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}