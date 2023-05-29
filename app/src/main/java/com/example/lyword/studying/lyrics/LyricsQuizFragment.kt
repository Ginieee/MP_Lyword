package com.example.lyword.studying.lyrics

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.data.entity.WordEntity
import com.example.lyword.databinding.ActivitySolveQuizBinding
import com.example.lyword.databinding.FragmentStudyQuizBinding

class
LyricsQuizFragment  : Fragment() {
    lateinit var binding: FragmentStudyQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyQuizBinding.inflate(inflater, container, false)


        binding.quiz5Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 5)
            startActivity(intent)

        }

        return binding.root
    }
}

class SolvingQuizActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySolveQuizBinding
    var percent: Int = 0
    var totalCount : Int = 0
    var count : Int = 0
    lateinit var db : LywordDatabase
    private var solveQuiz : List<WordEntity> = arrayListOf()
    val randomIndices = mutableListOf<Int>()
    val randomValues = mutableListOf<WordEntity>()
    var quizContents: List<WordEntity> = mutableListOf()
    var quizIndex: Int = 0
    var choice: CharSequence = ""
    var resultCheck: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolveQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

    /* DB에 접근해서 퀴즈 데이터 불러 오는 부분
     db = LywordDatabase.getInstance(this)

        getWordFromDB()

        percent = intent.getIntExtra("percentNum", 0)

        totalCount=solveQuiz.size
        count = (totalCount * percent) / 100


        while (randomIndices.size < count) {
            val randomIndex = (0 until totalCount).random()
            if (!randomIndices.contains(randomIndex)) {
                randomIndices.add(randomIndex)
                randomValues.add(solveQuiz[randomIndex])
            }
        }

        quizContents = randomValues.shuffled().take(4)
        quizIndex = (0..3).random()

        binding.lyricsQuiz.text= quizContents[quizIndex].wordOrigin
        binding.romazaQuiz.text= quizContents[quizIndex].wordPronunciation
        bidning.quizCheck.text= quizContents[quizIndex].wordEnglish // 정답 확인용

        binding.quizSelect1.text=quizContents[0].wordEnglish
        binding.quizSelect2.text=quizContents[1].wordEnglish
        binding.quizSelect3.text=quizContents[2].wordEnglish
        binding.quizSelect4.text=quizContents[3].wordEnglish*/

        binding.quizSelect1.setOnClickListener{
            choice=binding.quizSelect1.text
            binding.quizSelect1.setBackgroundResource(R.drawable.round_button3)
            binding.quizSelect2.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect3.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect4.setBackgroundResource(R.drawable.round_button2)
        }

        binding.quizSelect2.setOnClickListener{
            choice=binding.quizSelect2.text
            binding.quizSelect1.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect2.setBackgroundResource(R.drawable.round_button3)
            binding.quizSelect3.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect4.setBackgroundResource(R.drawable.round_button2)
        }

        binding.quizSelect3.setOnClickListener{
            choice=binding.quizSelect3.text
            binding.quizSelect1.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect2.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect3.setBackgroundResource(R.drawable.round_button3)
            binding.quizSelect4.setBackgroundResource(R.drawable.round_button2)
        }

        binding.quizSelect4.setOnClickListener{
            choice=binding.quizSelect4.text
            binding.quizSelect1.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect2.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect3.setBackgroundResource(R.drawable.round_button2)
            binding.quizSelect4.setBackgroundResource(R.drawable.round_button3)
        }

        binding.quizSubmit.setOnClickListener{
                if(binding.quizCheck.text==choice){ // 정답
                    resultCheck=1
                    showCustomDialog()


                }else{ // 오답
                    resultCheck=0
                    showCustomDialog()
                }
        }

        binding.backButtonQuiz.setOnClickListener {
            finish()

        }
    }

    private fun showCustomDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_quiz_answer, null)


        val closeButton = dialogView.findViewById<Button>(R.id.quiz_final_button)
        val dialogText = dialogView.findViewById<TextView>(R.id.quiz_result)


        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = dialogBuilder.create()



        if(resultCheck==0){

            dialogText.text = "Incorrect!"
            closeButton.text="Retry"

            closeButton.setOnClickListener {
                dialog.dismiss()
            }
        }else{

            dialogText.text = "Correct!"
            closeButton.text="Next"

            closeButton.setOnClickListener {
                dialog.dismiss()
                finish()
            }
        }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)




        dialog.show()
    }

    private fun getWordFromDB() {
        val thread : Thread = Thread {
            solveQuiz = db.wordDao.getWord()
        }
        thread.start()

        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
    }
}