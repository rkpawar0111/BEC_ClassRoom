package com.example.quitent1


import android.os.Bundle
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import kotlinx.android.synthetic.main.activity_display_marks.*

class display_marks : CommonData() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_marks)

        display_back.setOnClickListener {
            finish()
        }
        makeAnEntry()
        display_marks.text = StoreData.cMarks
    }

    fun displayMsg(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        hideProgressDialog()
    }

    private fun makeAnEntry() {
        try {
            showProgressDialog(resources.getString(R.string.please_wait))
            val usn = StoreData.cStud!!.studUSN
            StoreData.cQuiz!!.qMarks[usn] = StoreData.cMarks
            FireStoreClass().writeQuizContent(this, StoreData.cQuiz!!)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            hideProgressDialog()
        }
    }
}