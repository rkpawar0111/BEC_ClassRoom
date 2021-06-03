package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.AutoSignIn
import com.example.quitent1.model.student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_student_sign_up.*

class StudentSignUp : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_sign_up)

        student_sign_up_submit.setOnClickListener {
            onclick()
        }

    }

    fun studRegisteredSuccess() {
        Toast.makeText(
            this,
            "Sign up successful",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    fun studRegisteredFailure() {
        Toast.makeText(
            this,
            "Sign up Un-successful",
            Toast.LENGTH_LONG
        ).show()
        hideProgressDialog()
        finish()
    }

    private fun onclick() {
        val studName = student_sign_up_name.text.toString().trim { it <= ' ' }
        val studBranch = student_sign_up_branch.text.toString().trim { it <= ' ' }
        val studSem = student_sign_up_sem.text.toString().trim() { it <= ' ' }
        val studUSN = student_sign_up_USN.text.toString().trim { it <= ' ' }
        val studDiv = student_sign_up_div.text.toString().trim { it <= ' ' }
        val studEmail = student_sign_up_email.text.toString().trim { it <= ' ' }
        val studPassword = student_sign_up_password.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(
                    studName,
                    studBranch,
                    studSem,
                    studUSN,
                    studDiv,
                    studEmail,
                    studPassword
                )
            ) {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(studEmail, studPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email!!
                            val user =
                                student(
                                    firebaseUser.uid,
                                    studName,
                                    studBranch,
                                    studSem.toInt(),
                                    studUSN,
                                    studDiv,
                                    registeredEmail,
                                    studPassword
                                )
                            val temp = AutoSignIn()
                            temp.cUser[firebaseUser.uid] = 3
                            FireStoreClass().registerStudent(this, user, temp)
                        } else {
                            Toast.makeText(
                                this,
                                task.exception!!.message, Toast.LENGTH_LONG
                            ).show()
                            hideProgressDialog()
                            finish()
                        }
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


    private fun validateForm(
        studName: String,
        studBranch: String,
        studSem: String,
        studUSN: String,
        studDiv: String,
        email: String,
        password: String
    ): Boolean {
        if (studSem.isNotEmpty()) {
            var check = 0
            val temp = studSem.toInt()
            for (i in 1 until 9) {
                if (i == temp) {
                    check = 1
                    break
                }
            }
            if (check == 0) {
                showErrorSnackBar("Please Enter Sem")
                return false
            }
        } else {
            showErrorSnackBar("Please Enter Sem")
            return false
        }
        return when {
            TextUtils.isEmpty(studName) -> {
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(studBranch) -> {
                showErrorSnackBar("Please Enter Branch")
                false
            }
            TextUtils.isEmpty(studUSN) -> {
                showErrorSnackBar("Please Enter USN")
                false
            }
            TextUtils.isEmpty(studDiv) -> {
                showErrorSnackBar("Please Enter Div")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please Enter Email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please Enter Password")
                false
            }
            else -> true
        }
    }

}