package com.example.quitent1


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_start_quiz.*

class StartQuiz : CommonData() {
    private var count = 0
    private var inc = 0
    private var selectedChoice = 0
    private var selectedButton = -1
    private var sum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_quiz)

        start_quiz.setOnClickListener {
            loadQuiz()
            loadQuizQuestion(count, inc)
            count++
            inc += 4
        }

        quiz_next_btn.setOnClickListener {
            if (StoreData.cQuiz!!.arrCorrectOptions[count - 1] == selectedButton) {
                sum += 1
            }
            onClickChangeColor(5)
            loadQuizQuestion(count, inc)
            count++
            inc += 4
        }

        quiz_option1.setOnClickListener {
            selectedChoice = 1
            onClickChangeColor(1)
        }

        quiz_option2.setOnClickListener {
            selectedChoice = 2
            onClickChangeColor(2)
        }

        quiz_option3.setOnClickListener {
            selectedChoice = 3
            onClickChangeColor(3)
        }

        quiz_option4.setOnClickListener {
            selectedChoice = 4
            onClickChangeColor(4)
        }

        quiz_submit_btn.setOnClickListener {
            if (StoreData.cQuiz!!.arrCorrectOptions[count - 1] == selectedButton) {
                sum += 1
            }
            StoreData.cMarks =
                (sum.toString() + "/" + StoreData.cQuiz!!.arrQuestions.size.toString())
            val intent = Intent(this, display_marks::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadQuiz() {
        show_start_quiz.visibility = View.GONE
        show_quiz.visibility = View.VISIBLE
        quiz_name.text = StoreData.cQuiz!!.qName
    }


    private fun loadQuizQuestion(i: Int, j: Int) {
        if (i == storeData.cQuiz!!.arrQuestions.size - 1) {
            quiz_next_btn.visibility = View.GONE
            quiz_submit_btn.visibility = View.VISIBLE
        }
        quiz_question.text = storeData.cQuiz!!.arrQuestions[i]
        quiz_option1.text = storeData.cQuiz!!.arrOptions[j]
        quiz_option2.text = storeData.cQuiz!!.arrOptions[j + 1]
        quiz_option3.text = storeData.cQuiz!!.arrOptions[j + 2]
        quiz_option4.text = storeData.cQuiz!!.arrOptions[j + 3]
    }

    private fun onClickChangeColor(i: Int) {
        when (i) {
            1 -> {
                selectedButton = 1
                quiz_option1.background =
                    ContextCompat.getDrawable(this, R.drawable.selected_option)
                quiz_option2.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option3.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option4.background = ContextCompat.getDrawable(this, R.drawable.default_option)
            }
            2 -> {
                selectedButton = 2
                quiz_option1.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option2.background =
                    ContextCompat.getDrawable(this, R.drawable.selected_option)
                quiz_option3.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option4.background = ContextCompat.getDrawable(this, R.drawable.default_option)
            }
            3 -> {
                selectedButton = 3
                quiz_option1.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option2.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option3.background =
                    ContextCompat.getDrawable(this, R.drawable.selected_option)
                quiz_option4.background = ContextCompat.getDrawable(this, R.drawable.default_option)
            }
            4 -> {
                selectedButton = 4
                quiz_option1.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option2.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option3.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option4.background =
                    ContextCompat.getDrawable(this, R.drawable.selected_option)
            }
            5 -> {
                quiz_option1.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option2.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option3.background = ContextCompat.getDrawable(this, R.drawable.default_option)
                quiz_option4.background = ContextCompat.getDrawable(this, R.drawable.default_option)
            }
        }
    }
}