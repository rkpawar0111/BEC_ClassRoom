package com.example.quitent1

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.student
import com.example.quitent1.utils.Constants
import kotlinx.android.synthetic.main.activity_student_delete.*
import java.lang.Exception

class StudentDelete : CommonData() {
    private var adapter: LoadStudentsNames? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_delete)

        search_students.setOnClickListener {
            onClick()
        }

        try {
            show_students.setOnItemClickListener { adapterView, view, i, l ->
                val temp = adapterView.adapter.getItem(i) as student
                val inflater: LayoutInflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view1 = inflater.inflate(R.layout.display_warning_message, null)
                val popupWindow = PopupWindow(
                    view1,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                popupWindow.elevation = 10.0F
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                val bYes = view1.findViewById<Button>(R.id.staff_delete_yes)
                val bNo = view1.findViewById<Button>(R.id.staff_delete_no)

                bYes.setOnClickListener {
                    showProgressDialog(resources.getString(R.string.please_wait))
                    FireStoreClass().deleteStudent(this, temp)
                    adapter!!.remove(temp)
                    popupWindow.dismiss()
                }

                bNo.setOnClickListener {
                    popupWindow.dismiss()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    fun displayMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }

    private fun onClick() {
        val branch = search_branch_stud.text.toString().trim { it <= ' ' }
        val sem = search_sem_std.text.toString().trim() { it <= ' ' }
        val div = search_div_stud.text.toString().trim() { it <= ' ' }

        if (validateForm(branch, sem, div)) {
            val studList: ArrayList<student> = ArrayList()
            mFireStore.collection(Constants.student).get().addOnSuccessListener {
                if (it.documents.size != 0) {
                    for (i in it.documents) {
                        val temp = i.toObject(student::class.java)!!
                        if (temp.studBranch.equals(branch, ignoreCase = true)
                            && temp.studSem == sem.toInt()
                            && temp.studDiv.equals(div, ignoreCase = true)
                        ) {
                            studList.add(temp)
                        }
                    }
                }
                if (studList.size != 0) {
                    show_students.visibility = View.VISIBLE
                    student_delete_message.visibility = View.GONE
                    adapter = LoadStudentsNames(this, R.layout.stud_display_shape, studList)
                    show_students.adapter = adapter
                } else {
                    show_students.visibility = View.GONE
                    student_delete_message.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun validateForm(
        branch: String,
        sem: String,
        div: String
    ): Boolean {
        if (sem.isNotEmpty()) {
            var check = 0
            val temp = sem.toInt()
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
            TextUtils.isEmpty(branch) -> {
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(div) -> {
                showErrorSnackBar("Please Enter Division")
                false
            }
            else -> true
        }
    }

    class LoadStudentsNames(var mCtx: Context, var resources: Int, var items: List<student>) :
        ArrayAdapter<student>(mCtx, resources, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val layoutIn: LayoutInflater = LayoutInflater.from(mCtx)
            val vw: View = layoutIn.inflate(resources, null)

            val sName: TextView = vw.findViewById(R.id.stud_shape_name)
            val sUSN: TextView = vw.findViewById(R.id.stud_shape_usn)
            val oClass: student = items[position]

            sName.text = oClass.studName
            sUSN.text = oClass.studUSN

            return vw
        }
    }

}