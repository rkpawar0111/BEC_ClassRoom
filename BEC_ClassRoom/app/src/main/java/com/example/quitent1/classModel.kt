package com.example.quitent1

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.ClassModel
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_class_model.*

class classModel : CommonData() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_model)

        add_class.setOnClickListener {
            createClass()
        }
    }

    private fun createClass() {
        val cName = class_model_class_name.text.toString().trim { it <= ' ' }
        val cSubCode = class_model_sub_code.text.toString().trim { it <= ' ' }
        val cSem = class_model_sem.text.toString().trim { it <= ' ' }
        val cDiv = class_model_div.text.toString().trim { it <= ' ' }

        try {
            if (validateForm(cName, cSubCode, cSem, cDiv)) {
                showProgressDialog(resources.getString(R.string.please_wait))
                val tpClassId = "${cSubCode}-${cDiv}-${cName}"
                val tpClassUser = FireStoreClass().getCurrentUserId()
                val tpBranch = StoreData.cStaff!!.staffBranch
                val newClass = ClassModel(
                    tpClassId,
                    cName,
                    tpClassUser,
                    cSem.toInt(),
                    cDiv,
                    cSubCode,
                    tpBranch
                )
                newClass.classNotice.add("This is $cName class comments")
                newClass.classPDF.add("This is $cName class NOTES")
                newClass.classQuiz["Default"] = "This is $cName class Quizzes"
                mFireStore.collection("Classes")
                    .document(tpClassId)
                    .set(newClass, SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Class $cName created successfully:)",
                            Toast.LENGTH_LONG
                        ).show()
                        hideProgressDialog()
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        hideProgressDialog()
                    }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun validateForm(cName: String, cSubCode: String, cSem: String, cDiv: String): Boolean {
        if (cSem.isNotEmpty()) {
            var check = 0
            val temp = cSem.toInt()
            for (i in 1 until 9) {
                if (i == temp) {
                    check = 1
                    break
                }
            }
            if (check == 0) {
                return false
            }
        } else {
            showErrorSnackBar("Please Enter Sem")
            return false
        }
        return when {
            TextUtils.isEmpty(cName) -> {
                showErrorSnackBar("Please Enter Class Name")
                false
            }
            TextUtils.isEmpty(cSubCode) -> {
                showErrorSnackBar("Please Enter Subject Code")
                false
            }
            TextUtils.isEmpty(cSem) -> {
                showErrorSnackBar("Please Enter Sem")
                false
            }
            TextUtils.isEmpty(cDiv) -> {
                showErrorSnackBar("Please Enter Division")
                false
            }
            else -> true
        }
    }

}