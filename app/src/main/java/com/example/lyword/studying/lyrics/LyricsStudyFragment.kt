package com.example.lyword.studying.lyrics

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Transaction
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.studying.lyrics.separate.*
import com.example.lyword.data.entity.WordEntity
import com.example.lyword.databinding.FragmentStudyLyricsBinding
import kotlinx.coroutines.*
import net.crizin.KoreanCharacter
import net.crizin.KoreanRomanizer
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LyricsStudyFragment  : Fragment(), SeparateView {
    lateinit var binding: FragmentStudyLyricsBinding
    lateinit var db: LywordDatabase
    var updateSentence = mutableListOf<SentenceEntity>()
    var updateWord = mutableListOf<WordEntity>()

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
        binding = FragmentStudyLyricsBinding.inflate(inflater, container, false)
        db = LywordDatabase.getInstance(requireContext())

        // 전달받은 studyId를 사용하여 필요한 작업 수행
        val studyId = arguments?.getLong("studyId")


        if (studyId != null) {
            createSentencesAndWords(studyId)
        }

        return binding.root
    }

    // 기존에 저장되어 있던 노래인지 확인하기
    @OptIn(DelicateCoroutinesApi::class)
    private fun createSentencesAndWords(studyId: Long){
        GlobalScope.launch(Dispatchers.IO) {
            val study = db.studyDao.getStudyById(studyId)
            createSentences(studyId)
            createWords(studyId)
        }
    }

    // 가사 RecyclerView 세팅
    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun initSentenceRV(studyId: Long) {
        var lyrics = ArrayList<SentenceEntity>()
        GlobalScope.launch(Dispatchers.IO) {
            lyrics = db.studyDao.getStudyById(studyId).sentenceList as ArrayList
            Log.d("LSFragment - initRV", lyrics.toString())
        }
        withContext(Dispatchers.Main) {
            val rvAdapter = LyricsRVAdapter(lyrics, requireContext())
            binding.studyLyricsRv.adapter = rvAdapter
            binding.studyLyricsRv.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
    // 한 문장씩 정보 세팅
    @OptIn(DelicateCoroutinesApi::class)
    fun createSentences(studyId: Long){
        GlobalScope.launch(Dispatchers.IO) {
            val lyrics = db.studyDao.getStudyById(studyId).sentenceList as ArrayList
            for (i in 0 until lyrics.size) {
                setLyrics(lyrics[i], studyId, i == lyrics.size -1)
            }
        }
    }
    // 형태소 분석 함수 호출
    @OptIn(DelicateCoroutinesApi::class)
    private fun createWords(studyId: Long){
        GlobalScope.launch(Dispatchers.IO) {
            val lyrics = db.studyDao.getStudyById(studyId).sentenceList as ArrayList
            for (i in 0 until lyrics.size) {
                getSeparateLyrics(lyrics[i].sentenceOrigin, i)
            }
        }
    }

    @Transaction
    @OptIn(DelicateCoroutinesApi::class)
    private fun setLyrics(line: SentenceEntity, studyId: Long, isFinished: Boolean){
        val pron = KoreanRomanizer.romanize(line.sentenceOrigin, KoreanCharacter.ConsonantAssimilation.Progressive)
        val translate = Translate(line.sentenceOrigin, object : Translate.TranslateCallback {
            override fun onTranslateResult(result: String) {
                GlobalScope.launch {
                    Log.d("LSFragment - setLyrics", pron+result)
                    updateSentence.add(SentenceEntity(line.sentenceId, line.sentenceOrigin, pron, result))
                    if (isFinished){
                        Log.d("LSFragment - createS", "RVSetting")
                        val study = db.studyDao.getStudyById(studyId)
                        db.studyDao.updateStudy(StudyEntity(study.studyId, study.title, study.artist, study.album_art, study.percent, updateSentence, updateWord, study.videoId))
                        initSentenceRV(studyId)
                    }
                }
            }
        })
        translate.execute()
    }
    // 각 단어들의 뜻과 발음 불러와서 객체에 저장
    private fun initWords(separateWord: ArrayList<String>, linenum: Int) {
        var wordIndex = 0
        val completedCount = separateWord.size
        var wordEntities = ArrayList<WordEntity>()

        for (word in separateWord) {
            val translate2 = Trans(word)
            val meaningList: ArrayList<String> = translate2.execute().get()
            var meaning = ""
            for (m in meaningList) {
                if (meaning != "") {
                    meaning += ", "
                }
                meaning += m.replace(";", "")
            }
            val wordEntity = WordEntity()
            wordEntity.wordSentenceIdx = linenum
            wordEntity.wordOrigin = word
            wordEntity.wordPronunciation = "notyet"
            wordEntity.wordEnglish = meaning
            wordIndex += 1
            updateWord.add(wordEntity)
        }
    }

    // API
    // 형태소 분석
    private fun getSeparateLyrics(text : String, index: Int) {
        val separateService = SeparateService()
        separateService.setSeparateView(this)

        val request : SeparateRequest = SeparateRequest (
            "test",
            SeparateArgument (
                "morp",
                text
            )
        )
        separateService.getSeparateLyrics(request, index)
    }
    // 형태소 분석 결과 받아오는 인터페이스 내 함수
    override fun onGetLyricsSuccess(result: ArrayList<MorpResult>, index: Int) {
        var separateWord = ArrayList<String>()
        for (i in result){
            if(i.type.slice(IntRange(0,0)) == "N"){
                separateWord.add(i.text)
            }
            else if(i.type.slice(IntRange(0,0)) == "V"){
                separateWord.add(i.text+"다")
            }
        }
        Log.d("Lyrics - getWord", separateWord.toString())
        // API 호출이 완료되었을 때, 단어를 하나씩 저장한다
        initWords(separateWord, index)
    }
    // 단어 번역
    class Trans(private val str: String) : AsyncTask<String, Void, ArrayList<String>>() {
        override fun doInBackground(vararg strings: String): ArrayList<String> {
            lateinit var br: BufferedReader
            var list = ArrayList<String>()
            val apiKey = "54A280A5792642AC9065EB1E305684DE"
            try{
                val urlStr = "https://krdict.korean.go.kr/api/search"+
                        "?key="+apiKey+
                        "&part=word&sort=popular&num=10&q="+str+
                        "&translated=y&trans_lang=1";

                val url = URL(urlStr)
                val urlconnection = url.openConnection() as HttpURLConnection
                urlconnection.requestMethod = "GET"
                br = BufferedReader(InputStreamReader(urlconnection.inputStream, "UTF-8"));
                var line = " "
                var dict: String? = null
                var i = 0
                // 응답 결과가 xml 형식으로 옴. 그 중 <trans_word> 태그 안에 있는 요소에서 원하는 결과 추출
                // 예) <trans_word> <![CDATA[house]]> </trans_word> 이렇게 옴. 이 안에서 'house'만 가져오는 것
                // 단어들이 세미콜론으로 나누어져 있는데, 이에 대한 처리 추가적으로 필요함
                while(br.readLine().also { line = it } != null){
                    if(line.contains("<trans_word>") && i<2){
                        val temp = line.split("\\[".toRegex()).toTypedArray()
                        val temp2 = temp[2].split("]".toRegex()).toTypedArray()
                        if (line.contains("<trans_word>")) {
                            dict = temp2[0]
                            list.add("$dict")
                            i++
                        }
                    }
                }
            } catch (e: Exception){
                Log.e("exceptionError", e.toString())
            }
            return list
        }
    }
    // 문장 번역
    class Translate(private val str: String, private val callback: TranslateCallback) : AsyncTask<String, Void, String>() {
        var result = ""
        interface TranslateCallback {
            fun onTranslateResult(result: String)
        }

        override fun doInBackground(vararg strings: String): String? {
            //네이버 API
            val clientId = "HryFaoEG1FrRw23gbvNK"
            val clientSecret = "TNJrxS8SKg"

            try {
                val text = URLEncoder.encode(str, "UTF-8")  // 번역할 문장 Edittext  입력
                val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
                val url = URL(apiURL)
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "POST"
                con.setRequestProperty("X-Naver-Client-Id", clientId)
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
                // post request
                val postParams = "source=ko&target=en&text=$text"
                con.doOutput = true
                val wr = DataOutputStream(con.outputStream)
                wr.writeBytes(postParams)
                wr.flush()
                wr.close()
                val responseCode = con.responseCode
                val br: BufferedReader = if (responseCode == 200) { // 정상 호출
                    BufferedReader(InputStreamReader(con.inputStream))
                } else {  // 에러 발생
                    BufferedReader(InputStreamReader(con.errorStream))
                }
                var inputLine: String?
                val response = StringBuffer()
                while (br.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                br.close()

                val element = response.toString()
                val jsonObject = JSONObject(element).getJSONObject("message")
                    .getJSONObject("result")
                result = jsonObject.getString("translatedText")

            } catch (e: Exception) {
                println(e)
            }
            return result
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            callback.onTranslateResult(result)
        }
    }
}