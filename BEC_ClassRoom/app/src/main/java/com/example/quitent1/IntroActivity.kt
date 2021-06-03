package com.example.quitent1

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.quitent1.FireBase.FireStoreClass
import com.example.quitent1.model.Admin
import com.example.quitent1.model.Staff
import com.example.quitent1.model.student
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : CommonData() {
    private var numCheck: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        checkToLoadData()

        intro_admin.setOnClickListener {
            startActivity(Intent(this, AdminIntro::class.java))
        }

        intro_staff.setOnClickListener {
            startActivity(Intent(this, StaffIntro::class.java))
        }

        intro_student.setOnClickListener {
            startActivity(Intent(this, StudentIntro::class.java))
        }

    }

    fun returnValue(num: Int) {
        try {
            numCheck = num
            if (numCheck == 1) {
                if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
                    FireStoreClass().autoLoadData(this, 1)
                    hideProgressDialog()
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                } else {
                    hideProgressDialog()
                    val intent = Intent(this, AdminSignIn::class.java)
                    startActivity(intent)
                    finish()
                }
            } else if (numCheck == 2) {
                if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
                    FireStoreClass().autoLoadData(this, 2)
                    hideProgressDialog()
                    startActivity(Intent(this, StaffActivity::class.java))
                    finish()
                } else {
                    hideProgressDialog()
                    val intent = Intent(this, StaffSignIn::class.java)
                    startActivity(intent)
                    finish()
                }
            } else if (numCheck == 3) {
                if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
                    FireStoreClass().autoLoadData(this, 3)
                    hideProgressDialog()
                    startActivity(Intent(this, StudentActivity::class.java))
                    finish()
                } else {
                    hideProgressDialog()
                    val intent = Intent(this, StudentSignIn::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                hideProgressDialog()
                Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkToLoadData() {
        if (FireStoreClass().getCurrentUserId().isNotEmpty()) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FireStoreClass().checkSignIn(this)
        }
    }


    fun signInSuccess1(userInfo: Admin?) {
        StoreData.cAdmin = userInfo
        hideProgressDialog()
    }

    fun signInSuccess2(userInfo: Staff?) {
        StoreData.cStaff = userInfo
        hideProgressDialog()
    }

    fun signInSuccess3(userInfo: student?) {
        StoreData.cStud = userInfo
        hideProgressDialog()
    }
}