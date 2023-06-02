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

    // 생성자를 통해 studyId를 전달받음
    companion object {
        fun newInstance(studyId: Long): LyricsStudyFragment {
            val fragment = LyricsStudyFragment()
            val args = Bundle()
            args.putLong("studyId", studyId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyQuizBinding.inflate(inflater, container, false)
        // 전달받은 studyId를 사용하여 필요한 작업 수행
        val studyId = arguments?.getLong("studyId")

        binding.quiz5Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 5)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz10Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 10)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz15Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 15)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz20Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 20)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz25Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 25)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz30Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 30)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz35Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 35)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz40Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 40)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz45Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 45)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz50Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 50)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz55Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 55)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz60Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 60)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz65Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 65)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz70Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 70)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz75Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 75)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz80Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 80)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz85Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 85)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz90Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 90)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz95Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 95)
            intent.putExtra("tmpStudyId",studyId)
            startActivity(intent)

        }
        binding.quiz100Percent.setOnClickListener {
            val intent = Intent(requireContext(), SolvingQuizActivity::class.java)
            intent.putExtra("percent", 100)
            intent.putExtra("tmpStudyId",studyId)
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
    var tmpStudyId : Long = 0

    lateinit var db : LywordDatabase
    private var solveQuiz : List<WordEntity> = arrayListOf()
    private var changeStudy : List<StudyEntity> = emptyList()

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

     //DB에 접근해서 퀴즈 데이터 불러 오는 부분
     db = LywordDatabase.getInstance(this)

        percent = intent.getIntExtra("percent", 0)
        tmpStudyId = intent.getLongExtra("tmpStudyId", 0)

        getWordFromDB(tmpStudyId)

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
        binding.quizCheck.text= quizContents[quizIndex].wordEnglish // 정답 확인용

        binding.quizSelect1.text=quizContents[0].wordEnglish
        binding.quizSelect2.text=quizContents[1].wordEnglish
        binding.quizSelect3.text=quizContents[2].wordEnglish
        binding.quizSelect4.text=quizContents[3].wordEnglish

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

                    // 정답시 퍼센트를 올리는 부분
                    upgradePercentFromDB(tmpStudyId)

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

    // 퀴즈에 사용할 단어를 db에서 가져오는 부분
    private fun getWordFromDB(tmpId: Long) {
        val thread : Thread = Thread {

            val studyEntity = db.studyDao.getStudyById(tmpId)
            solveQuiz = studyEntity.wordList!!

        }
        thread.start()

        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
    }

    // 퀴즈에서 정답시에 studyId를 사용해서 해당 StudyEntity의 퍼센트를 올리는 부분
    private fun upgradePercentFromDB(tmpStudyId: Long) {
        val thread : Thread = Thread {

            val studyEntity = db.studyDao.getStudyById(tmpStudyId)
            studyEntity.percent += 5
            db.studyDao.updateStudy(studyEntity)

        }
        thread.start()

        try {
            thread.join()
        } catch (e : InterruptedException) {
            e.printStackTrace()
        }
    }

}