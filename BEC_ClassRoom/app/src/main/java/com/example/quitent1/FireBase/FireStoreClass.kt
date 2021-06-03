package com.example.quitent1.FireBase

import android.app.Activity
import android.util.Log
import com.example.quitent1.*
import com.example.quitent1.model.*
import com.example.quitent1.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun checkSignIn(activity: Activity) {
        when (activity) {
            is IntroActivity -> {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .get().addOnSuccessListener {
                        val temp = it.toObject(AutoSignIn::class.java)
                        if (getCurrentUserId() in temp!!.cUser.keys) {
                            activity.returnValue(temp.cUser[getCurrentUserId()]!!)
                        }
                    }.addOnFailureListener {
                        activity.returnValue(-1)
                        Log.e(activity.javaClass.simpleName, "Error writing document", it)
                    }
            }
        }
    }

    fun registerAdmin(activity: AdminSignUp, userInfo: Admin, userInfo1: AutoSignIn) {
        mFireStore.collection(Constants.admin)
            .document(userInfo.adminId)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .set(userInfo1, SetOptions.merge()).addOnSuccessListener {
                        activity.adminRegisteredSuccess()
                    }.addOnFailureListener {
                        activity.adminRegisteredFailure()
                        Log.e(activity.javaClass.simpleName, "Error writing document", it)
                    }
            }.addOnFailureListener { e ->
                activity.adminRegisteredFailure()
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }


    fun registerStaff(activity: StaffSignUp, userInfo: Staff, userInfo1: AutoSignIn) {
        mFireStore.collection(Constants.staff)
            .document(userInfo.staffId)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .set(userInfo1, SetOptions.merge()).addOnSuccessListener {
                        activity.staffRegisteredSuccess()
                    }.addOnFailureListener {
                        activity.staffRegisteredFailure()
                        Log.e(activity.javaClass.simpleName, "Error writing document", it)
                    }
            }.addOnFailureListener { e ->
                activity.staffRegisteredFailure()
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }

    fun registerStudent(activity: StudentSignUp, userInfo: student, userInfo1: AutoSignIn) {
        mFireStore.collection(Constants.student)
            .document(userInfo.studId)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .set(userInfo1, SetOptions.merge()).addOnSuccessListener {
                        activity.studRegisteredSuccess()
                    }.addOnFailureListener {
                        activity.studRegisteredFailure()
                        Log.e(activity.javaClass.simpleName, "Error writing document", it)
                    }
            }.addOnFailureListener { e ->
                activity.studRegisteredFailure()
                Log.e(activity.javaClass.simpleName, "Error writing document", e)
            }
    }

    fun loadUserData(activity: Activity) {
        when (activity) {
            is AdminSignIn -> {
                mFireStore.collection(Constants.admin).document(getCurrentUserId()).get()
                    .addOnSuccessListener {
                        val temp: Admin? = it.toObject(Admin::class.java)
                        activity.signInSuccess(temp)
                    }.addOnFailureListener { e ->
                        Log.e(activity.javaClass.simpleName, "Error writing document", e)
                    }
            }
            is StaffSignIn -> {
                mFireStore.collection(Constants.staff).document(getCurrentUserId()).get()
                    .addOnSuccessListener {
                        val temp: Staff? = it.toObject(Staff::class.java)
                        activity.signInSuccess(temp)
                    }.addOnFailureListener { e ->
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error writing document", e)
                    }
            }
            is StudentSignIn -> {
                mFireStore.collection(Constants.student).document(getCurrentUserId()).get()
                    .addOnSuccessListener {
                        val temp: student? = it.toObject(student::class.java)
                        activity.signInSuccess(temp)
                    }.addOnFailureListener { e ->
                        activity.hideProgressDialog()
                        Log.e(activity.javaClass.simpleName, "Error writing document", e)
                    }
            }
        }
    }


    fun writeBoardContent(activity: Activity, userInfo: ClassModel) {
        val docId = "${userInfo.classSubCode}-${userInfo.classDiv}-${userInfo.className}"
        val cObj = mFireStore.collection(Constants.classes).document(docId)
        when (activity) {
            is boardComment -> {
                cObj.update("classNotice", userInfo.classNotice).addOnSuccessListener {
                    activity.displayMsg("Message write success")
                }.addOnFailureListener { e ->
                    Log.e(activity.javaClass.simpleName, "Error reading document", e)
                    activity.displayMsg("Message write un-success")
                }
            }

            is uploadpdf -> {
                cObj.update("classPDF", userInfo.classPDF).addOnSuccessListener {
                    activity.displayMsg("Message write success")
                }.addOnFailureListener { e ->
                    Log.e(activity.javaClass.simpleName, "Error reading document", e)
                    activity.displayMsg("Message write un-success")
                }
            }

            is CreateQuiz -> {
                cObj.update("classQuiz", userInfo.classQuiz).addOnSuccessListener {
                    activity.displayMsg("Message write success")
                }.addOnFailureListener { e ->
                    Log.e(activity.javaClass.simpleName, "Error reading document", e)
                    activity.displayMsg("Message write un-success")
                }
            }

        }
    }

    fun writeQuizContent(activity: Activity, userInfo: QuizModel) {
        val cObj = mFireStore.collection(Constants.quizzes).document(userInfo.qId)
        when (activity) {
            is display_marks -> {
                cObj.update("qMarks", userInfo.qMarks).addOnSuccessListener {
                    activity.displayMsg("Message write success")
                }.addOnFailureListener { e ->
                    Log.e(activity.javaClass.simpleName, "Error reading document", e)
                    activity.displayMsg("Message write un-success")
                }
            }
        }
    }

    fun updateAdminEP(activity: Activity, userInfo: Admin, check: Int) {
        if (check == 1) {
            when (activity) {
                is UpdateAdminEP -> {
                    val cObj = mFireStore.collection(Constants.admin).document(userInfo.adminId)
                    cObj.update("adminPassword", userInfo.adminPassword).addOnSuccessListener {
                        activity.displayMsg("Password Update Successful:)")
                    }.addOnFailureListener {
                        Log.e(activity.javaClass.simpleName, "Error reading document", it)
                        activity.displayMsg("Password Update Un-Successful:(")
                    }
                }
            }
        } else {
            when (activity) {
                is UpdateAdminEP -> {
                    val cObj = mFireStore.collection(Constants.admin).document(userInfo.adminId)
                    cObj.update("adminEmail", userInfo.adminEmail).addOnSuccessListener {
                        activity.displayMsg("Email Update Successful:)")
                    }.addOnFailureListener {
                        Log.e(activity.javaClass.simpleName, "Error reading document", it)
                        activity.displayMsg("Email Update Un-Successful:(")
                    }
                }
            }
        }
    }

    fun updateStaffEP(activity: Activity, userInfo: Staff, check: Int) {
        if (check == 1) {
            when (activity) {
                is UpdateStaffEP -> {
                    val cObj = mFireStore.collection(Constants.staff).document(userInfo.staffId)
                    cObj.update("staffPassword", userInfo.staffPassword).addOnSuccessListener {
                        activity.displayMsg("Password Update Successful:)")
                    }.addOnFailureListener {
                        Log.e(activity.javaClass.simpleName, "Error reading document", it)
                        activity.displayMsg("Password Update Un-Successful:(")
                    }
                }
            }
        } else {
            when (activity) {
                is UpdateStaffEP -> {
                    val cObj = mFireStore.collection(Constants.staff).document(userInfo.staffId)
                    cObj.update("staffEmail", userInfo.staffEmail).addOnSuccessListener {
                        activity.displayMsg("Email Update Successful:)")
                    }.addOnFailureListener {
                        Log.e(activity.javaClass.simpleName, "Error reading document", it)
                        activity.displayMsg("Email Update Un-Successful:(")
                    }
                }
            }
        }
    }

    fun updateStudEP(activity: Activity, userInfo: student, check: Int) {
        if (check == 1) {
            when (activity) {
                is UpdateStudentEP -> {
                    val cObj = mFireStore.collection(Constants.student).document(userInfo.studId)
                    cObj.update("studPassword", userInfo.studPassword).addOnSuccessListener {
                        activity.displayMsg("Password Update Successful:)")
                    }.addOnFailureListener {
                        Log.e(activity.javaClass.simpleName, "Error reading document", it)
                        activity.displayMsg("Password Update Un-Successful:(")
                    }
                }
            }
        } else {
            when (activity) {
                is UpdateStudentEP -> {
                    val cObj = mFireStore.collection(Constants.student).document(userInfo.studId)
                    cObj.update("studEmail", userInfo.studEmail).addOnSuccessListener {
                        activity.displayMsg("Email Update Successful:)")
                    }.addOnFailureListener {
                        Log.e(activity.javaClass.simpleName, "Error reading document", it)
                        activity.displayMsg("Email Update Un-Successful:(")
                    }
                }
            }
        }
    }

    fun updateSem(activity: UpdateStudentSem, userInfo: student) {
        mFireStore.collection(Constants.student)
            .document(userInfo.studId)
            .update("studSem", userInfo.studSem).addOnSuccessListener {
                activity.displayMsg("Sem update successful:)")
            }.addOnFailureListener {
                activity.displayMsg("Sem update failed")
            }
    }

    fun autoLoadData(activity: Activity, check: Int) {
        if (check > 0) {
            when (activity) {
                is IntroActivity -> {
                    if (check == 1) {
                        mFireStore.collection(Constants.admin).document(getCurrentUserId()).get()
                            .addOnSuccessListener {
                                val temp: Admin? = it.toObject(Admin::class.java)
                                activity.signInSuccess1(temp)
                            }.addOnFailureListener { e ->
                                activity.hideProgressDialog()
                                Log.e(activity.javaClass.simpleName, "Error writing document", e)
                            }
                    } else if (check == 2) {
                        mFireStore.collection(Constants.staff).document(getCurrentUserId()).get()
                            .addOnSuccessListener {
                                val temp: Staff? = it.toObject(Staff::class.java)
                                activity.signInSuccess2(temp)
                            }.addOnFailureListener { e ->
                                activity.hideProgressDialog()
                                Log.e(activity.javaClass.simpleName, "Error writing document", e)
                            }
                    } else if (check == 3) {
                        mFireStore.collection(Constants.student).document(getCurrentUserId()).get()
                            .addOnSuccessListener {
                                val temp: student? = it.toObject(student::class.java)
                                activity.signInSuccess3(temp)
                            }.addOnFailureListener { e ->
                                activity.hideProgressDialog()
                                Log.e(activity.javaClass.simpleName, "Error writing document", e)
                            }
                    }
                }
            }
        } else {
            when (activity) {
                is AdminIntro -> {
                    mFireStore.collection(Constants.admin).document(getCurrentUserId()).get()
                        .addOnSuccessListener {
                            val temp: Admin? = it.toObject(Admin::class.java)
                            activity.signInSuccess(temp)
                        }.addOnFailureListener { e ->
                            activity.hideProgressDialog()
                            Log.e(activity.javaClass.simpleName, "Error writing document", e)
                        }
                }
                is StaffIntro -> {
                    mFireStore.collection(Constants.staff).document(getCurrentUserId()).get()
                        .addOnSuccessListener {
                            val temp: Staff? = it.toObject(Staff::class.java)
                            activity.signInSuccess(temp)
                        }.addOnFailureListener { e ->
                            activity.hideProgressDialog()
                            Log.e(activity.javaClass.simpleName, "Error writing document", e)
                        }
                }
                is StudentIntro -> {
                    mFireStore.collection(Constants.student).document(getCurrentUserId()).get()
                        .addOnSuccessListener {
                            val temp: student? = it.toObject(student::class.java)
                            activity.signInSuccess(temp)
                        }.addOnFailureListener { e ->
                            activity.hideProgressDialog()
                            Log.e(activity.javaClass.simpleName, "Error writing document", e)
                        }
                }
            }
        }
    }


    fun deleteStaff(activity: Activity, userInfo: Staff) {
        when (activity) {
            is StaffDelete -> {
                mFireStore.collection(Constants.staff)
                    .document(userInfo.staffId)
                    .delete()
                    .addOnSuccessListener {
                        deleteAutoSignIn(activity, userInfo)
                    }.addOnFailureListener {
                        activity.displayMessage(it.message!!)
                    }
            }
        }
    }

    fun deleteStudent(activity: Activity, userInfo: student) {
        when (activity) {
            is StudentDelete -> {
                mFireStore.collection(Constants.student)
                    .document(userInfo.studId)
                    .delete()
                    .addOnSuccessListener {
                        deleteAutoSignIn1(activity, userInfo)
                    }.addOnFailureListener {
                        activity.displayMessage(it.message!!)
                    }
            }
        }
    }

    private fun deleteAutoSignIn1(activity: Activity, userInfo: student) {
        when (activity) {
            is StudentDelete -> {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .get()
                    .addOnSuccessListener {
                        val temp = it.toObject(AutoSignIn::class.java)
                        if (userInfo.studId in temp!!.cUser.keys) {
                            temp.cUser.remove(userInfo.studId)
                        }
                        autoUpdateAutoSignIn(activity, temp)
                    }.addOnFailureListener {
                        activity.displayMessage(it.message!!)
                    }
            }
        }
    }

    private fun deleteAutoSignIn(activity: Activity, userInfo: Staff) {
        when (activity) {
            is StaffDelete -> {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .get()
                    .addOnSuccessListener {
                        val temp = it.toObject(AutoSignIn::class.java)
                        if (userInfo.staffId in temp!!.cUser.keys) {
                            temp.cUser.remove(userInfo.staffId)
                        }
                        autoUpdateAutoSignIn(activity, temp)
                    }.addOnFailureListener {
                        activity.displayMessage(it.message!!)
                    }
            }
        }
    }

    fun autoUpdateAutoSignIn(activity: Activity, userInfo: AutoSignIn) {
        when (activity) {
            is StaffDelete -> {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .set(userInfo, SetOptions.merge())
                    .addOnSuccessListener {
                        activity.displayMessage("Staff Deleted successfully")
                    }.addOnFailureListener {
                        activity.displayMessage(it.message!!)
                    }
            }
            is StudentDelete -> {
                mFireStore.collection(Constants.autoSGIN)
                    .document(Constants.crossCheck)
                    .set(userInfo, SetOptions.merge())
                    .addOnSuccessListener {
                        activity.displayMessage("Student Deleted successfully")
                    }.addOnFailureListener {
                        activity.displayMessage(it.message!!)
                    }
            }
        }
    }


    fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUser != null) {
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getCurrentUserRef(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}
