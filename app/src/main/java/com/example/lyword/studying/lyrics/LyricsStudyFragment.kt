package com.example.lyword.studying.lyrics

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lyword.databinding.FragmentStudyLyricsBinding
import com.example.lyword.studying.Word
import com.example.lyword.studying.lyrics.separate.*
import com.example.lyword.data.WordDatabase
import com.example.lyword.studying.lyrics.word.WordEntity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class LyricsStudyFragment  : Fragment(), SeparateView {
    lateinit var binding: FragmentStudyLyricsBinding
    lateinit var db: WordDatabase

    private var exampleLyrics: ArrayList<String> = arrayListOf(
                "두려워한다는 거 (ayy)",
                "나도 알아 우린" ,
                "석양이 지나가고",
                "밤의 시작 앞에 (yeah, yeah)",
                "밤은 낮과 달리 어두워",
                "지금 해가 지면 우리는 더한 추위를 겪어",
                "하지만 다 알고 있었잖아 굳이 뭘 더" ,
                "걱정하는 거야, 우리는 별이잖아 서로의" ,
                "둘이 손을 맞잡고서" ,
                "서로 등을 맞대고서" ,
                "한 걸음씩 이렇게 가기로 해, oh, na" ,
                "어떤 어둠 속을 걸을지라도 (oh, whoa, whoa)" ,
                "사방이 막힌 길뿐일지라도 (I let you know, yeah)" ,
//                "내가 했던 말 기억해" ,
//                "지금 잡은 손 놓치지 않아" ,
//                "너에게 난 약속해" ,
//                "No one take you down (whoa-oh, whoa-oh, ayy)" ,
//                "너와 나의 promise (ooh, ooh)" ,
//                "No one take you down (whoa-oh, whoa-oh, ayy)" ,
//                "Don't worry, I'll be there (별처럼 빛나)" ,
//                "지나리 언젠간 닿겠지" ,
//                "꽃이 지고 또 어둠이 널 삼키겠지" ,
//                "빛을 밝히리, 새로운 싹이 트겠지" ,
//                "이 순간에, 불빛이 우릴 감싸주겠지" ,
//                "우리 우리를 향한 시린 바람 다 막아 (ooh)" ,
//                "줄 수는 없어도 하나 꼭 약속, 항상 안아 더 꽉 (ooh)" ,
//                "내 온기가 너에게 느껴질 수 있게" ,
//                "해가 뜰 때쯤, 어깨너머로 빛을 보여줄게" ,
//                "어떤 어둠 속을 걸을지라도 (oh, whoa, whoa)" ,
//                "사방이 막힌 길뿐일지라도 (I let you know, yeah)" ,
//                "내가 했던 말 기억해" ,
//                "지금 잡은 손 놓치지 않아" ,
//                "너에게 난 약속해" ,
//                "No one take you down (whoa-oh, whoa-oh, ayy)" ,
//                "너와 나의 promise (ooh, ooh)" ,
//                "No one take you down (whoa-oh, whoa-oh, ayy)" ,
//                "Don't worry, I'll be there (별처럼 빛나)" ,
//                "피어난 노을에" ,
//                "석양이 부르네" ,
//                "이곳에 꽃이 필 때" ,
//                "이 공간은 붉은빛으로 물드네" ,
//                "나와 함께 맞이하게 될" ,
//                "미래를 기대해" ,
//                "막을 수 없는 걸" ,
//                "No one take you down (whoa-oh, whoa-oh, ayy)" ,
//                "너와 나의 promise (너와 나의 promise, ooh)" ,
//                "No one take you down (whoa-oh, whoa-oh, ayy)" ,
//                "Don't worry, I'll be there (별처럼 빛나)" ,
//                "La-la-la-la, la-la-la-la" ,
//                "La-la-la-la (no one take you down)" ,
//                "La-la-la-la, la-la-la-la (whoa-oh, whoa-oh, ayy)" ,
//                "La-la-la-la (no one take you down)"
    )

    private var wordConverted = ArrayList<Word>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyLyricsBinding.inflate(inflater, container, false)
        db = WordDatabase.getInstance(requireContext())!!


        // 나중에 곡 정보 받아오면 인덱스 설정해서 songIndex에 넣으면 됨
        val songIndex = 0;
        createWords(songIndex)
        initRV()

        return binding.root
    }

    // RecyclerView 세팅
    private fun initRV(){
        val lyrics = ArrayList<Lyrics>()
        val translateCount = exampleLyrics.size
        var completedCount = 0

        for(li in exampleLyrics){
            val translate = Translate(li, object : Translate.TranslateCallback {
                override fun onTranslateResult(result: String) {
                    lyrics.add(Lyrics(completedCount, li, "tbc", result))
                    completedCount++
                    if (completedCount == translateCount) {
                        val rvAdapter = LyricsRVAdapter(lyrics, requireContext())
                        binding.studyLyricsRv.adapter = rvAdapter
                        binding.studyLyricsRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    }
                }
            })
            translate.execute()
        }
    }
    // 단어 객체 만들고 룸디비에 저장하는 과정
    @OptIn(DelicateCoroutinesApi::class)
    private fun createWords(songIndex: Int){
        GlobalScope.launch(Dispatchers.IO) {
            if (db.wordDao().hasSongIndex(songIndex) <= 0) {
                Log.d("LyricsStudy-createWords", songIndex.toString())
                for (i in 0 until exampleLyrics.size) {
                    getSeparateLyrics(exampleLyrics[i], i)
                }
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun initWords(separateWord: ArrayList<String>, linenum: Int) {
        var wordIndex = 0
        var completedCount = separateWord.size

        GlobalScope.launch(Dispatchers.IO) {
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
                db.wordDao().insertWord(WordEntity(word, "notyet", meaning, 0, linenum))
                wordIndex += 1
                if (wordIndex == completedCount) {
                    withContext(Dispatchers.Main) {
                        Log.d("Lyrics - Word", wordConverted.toString())
                    }
                }
            }
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
    // 형태소 분석 결과 받아오기
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