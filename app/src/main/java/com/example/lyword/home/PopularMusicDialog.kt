package com.example.lyword.home

import android.app.Activity
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.lyword.BuildConfig
import com.example.lyword.R
import com.example.lyword.data.LywordDatabase
import com.example.lyword.data.entity.SentenceEntity
import com.example.lyword.data.entity.StudyEntity
import com.example.lyword.data.entity.WordEntity
import com.example.lyword.databinding.DialogPopularMusicBinding
import com.example.lyword.studying.lyrics.LyricsStudyFragment
import com.example.lyword.studying.lyrics.separate.*
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import kotlinx.coroutines.*
import net.crizin.KoreanCharacter
import net.crizin.KoreanRomanizer
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class PopularMusicDialog : AppCompatActivity(), SeparateView {

    lateinit var binding : DialogPopularMusicBinding
    lateinit var db : LywordDatabase

    private lateinit var title : String
    private lateinit var artist : String
    private lateinit var albumCover : String
    private lateinit var previewUrl : String
    var updateWord = mutableListOf<WordEntity>()

    private val YOUTUBE_KEY = BuildConfig.YOUTUBE_KEY
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    private var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogPopularMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.popularStartBtn.text = "Start to Learn"
        binding.popularLoadingLv.visibility = View.GONE

        db = LywordDatabase.getInstance(this)

        intent?.let {
            title = it.getStringExtra("title") ?: ""
            artist = it.getStringExtra("artist") ?: ""
            albumCover = it.getStringExtra("albumCover") ?: ""
            previewUrl = it.getStringExtra("previewUrl") ?: ""
        }

        setMusic()
        clickListener()
    }

    private fun setMusic() {
        binding.popularContentTitleTv.text = title
        binding.popularContentArtistTv.text = artist

        Glide.with(this)
            .load(albumCover)
            .placeholder(R.drawable.ic_logo_splash)
            .error(R.drawable.ic_logo_splash)
            .fallback(R.drawable.ic_logo_splash)
            .transform(CenterCrop(), RoundedCorners(10))
            .into(binding.popularContentAlbumIv)

        if (previewUrl.isNotBlank()) {
            setMediaPlayer()
        } else {
            Toast.makeText(this, "재생할 음원 파일이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            setDataSource(previewUrl)
            prepare()
//            setVolume(0f,0f)

            isLooping = true
            start()

//            val fadeInDuration = 1000
//            val maxVolume = 1.0f
//
//            val fadeInAnimator = ValueAnimator.ofFloat(0f, maxVolume).apply {
//                duration = fadeInDuration.toLong()
//                addUpdateListener { animation ->
//                    val volume = animation.animatedValue as Float
//                    setVolume(volume, volume)
//                }
//            }
//
//            fadeInAnimator.start()
        }

        Log.d("MEDIA_PLAYER", "url : $previewUrl")
    }

    var studying = StudyEntity()
    private fun clickListener(){
        binding.popularContentCloseBtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_CANCELED, resultIntent)
            finish()
        }

        binding.popularStartBtn.setOnClickListener {
            binding.popularStartBtn.text = ""
            binding.popularLoadingLv.visibility = View.VISIBLE

            //var studying = StudyEntity()
            studying.title = title
            studying.artist = artist
            studying.album_art = albumCover

            CoroutineScope(Dispatchers.Main).launch {
                // StudyEntity에 넣을 SentenceEntity 객체
                val sentenceList = ArrayList<SentenceEntity>()
                // 문장 불러오기 - by 어진
                val getSentence = withContext(Dispatchers.IO) {
                    getLyrics(title, artist)
                }

                studying.videoId = withContext(Dispatchers.IO) {
                    searchVideos("$title $artist Official")
                }

//                for(i in 0 until getSentence.size){
//                    getSeparateLyrics(getSentence[i], i)
//                }
                // 문장 발음과 해석 불러오기
                var count = 0
                for(line in getSentence){
                    val pron = KoreanRomanizer.romanize(line, KoreanCharacter.ConsonantAssimilation.Progressive)
                    val translate = TranslateSentence(line, object : TranslateSentence.TranslateSentenceCallback {
                        override fun onTranslateSentenceResult(result: String) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val input = SentenceEntity()
                                input.sentenceOrigin = line
                                input.sentencePronunciation = pron
                                input.sentenceEnglish = result
                                Log.d("ADD_SENTENCE_NEW", input.toString())
                                sentenceList.add(input)
                                count++
                                if (count == getSentence.size) {
                                    addStudyInDao(sentenceList, getSentence)
                                }
                            }
                        }
                    })
                    translate.execute()
                }
            }
        }
    }

    private fun addStudyInDao(sentenceList: ArrayList<SentenceEntity>, getSentence: List<String>){
        val thread = Thread {
            val sentenceIds = db.sentenceDao.insertSentenceList(sentenceList)
            studying.sentenceList = db.sentenceDao.getSentencesById(sentenceIds)
            var count = 0
            Log.d("ADD_SID", sentenceIds.toString())
            for (sentenceId in sentenceIds){
                Log.d("ADD_SID_IC", sentenceId.toString()+ (sentenceIds.size-1 == count) +getSentence[count])
                getSeparateLyrics(getSentence[count], sentenceId.toInt(), sentenceIds.size-1 == count)
                count++
            }
            val wordIds = db.wordDao.insertWordList(updateWord)
            studying.wordList = db.wordDao.getWordsById(wordIds)

            Log.d("SENTENCE_ADD", sentenceIds.toString())
            Log.d("SENTENCE_ADD", studying.sentenceList.toString())
        }
        thread.start()
        thread.join()
    }

    private fun addWordInDao(){
        val thread = Thread {
            val wordIds = db.wordDao.insertWordList(updateWord)
            studying.wordList = db.wordDao.getWordsById(wordIds)

            Log.d("WORD_ADD", wordIds.toString())
            Log.d("WORD_ADD", studying.wordList.toString())
        }
        thread.start()
        thread.join()

        val addStudyThread = Thread {
            val studyIdx = db.studyDao.insertStudy(studying)

            Log.d("ADD_STUDY", "study idx : $studyIdx")
            Log.d("ADD_FINAL", studying.toString())

            runOnUiThread {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        addStudyThread.start()
    }

    // 원래 가사를 받기
    private suspend fun getLyrics(title: String, artist: String): List<String> = withContext(Dispatchers.IO) {
        val input = URLEncoder.encode(artist, "UTF-8") + "+" + URLEncoder.encode(title, "UTF-8")
        Log.d("SEARCH_ACT_GET_LYRICS", input)
        val url = "https://search.naver.com/search.naver?ie=UTF-8&sm=whl_hty&query=$input"
        Log.d("SEARCH_ACT_GET_LYRICS", url)

        var sentenceList = emptyList<String>()
        val selectedSentenceList = ArrayList<SentenceEntity>()

        try {
            val doc: org.jsoup.nodes.Document? = Jsoup.connect(url).get()
            val lyrics: Elements? = doc?.select(".text_expand")
            val isEmpty = lyrics?.isEmpty()

            if (isEmpty == false) {
                var content = lyrics?.get(0)?.outerHtml()!!.split("<", ">").toMutableList()
                content = content.subList(4, content.size - 8)
                content.removeAll { it == "br" }
                sentenceList = content

                Log.d("SEARCH_ACT_GET_LYRICS", content.toString())
            } else {
                Log.d("SEARCH_ACT_GET_LYRICS", "lyrics is empty")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

//        for (i in sentenceList) {
//            val input = SentenceEntity()
//            input.sentenceOrigin = i
//            selectedSentenceList.add(input)
//        }

        sentenceList
    }
    // 문장 번역
    class TranslateSentence(private val str: String, private val callback: TranslateSentenceCallback) : AsyncTask<String, Void, String>() {
        var result = ""
        interface TranslateSentenceCallback {
            fun onTranslateSentenceResult(result: String)
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
            callback.onTranslateSentenceResult(result)
        }
    }
    // 형태소 분석
    private fun getSeparateLyrics(text : String, index: Int, isFinished: Boolean){
        val separateService = SeparateService()
        separateService.setSeparateView(this)

        val request = SeparateRequest (
            "test",
            SeparateArgument (
                "morp",
                text
            )
        )
        separateService.getSeparateLyrics(request, index, isFinished)
    }
    // 형태소 분석 결과 받아오는 인터페이스 내 함수
    override fun onGetLyricsSuccess(result: ArrayList<MorpResult>, index: Int, isFinished: Boolean) {
        var separateWord = ArrayList<String>()
        for (i in result){
            if(i.type.slice(IntRange(0,0)) == "N"){
                separateWord.add(i.text)
            }
            else if(i.type.slice(IntRange(0,0)) == "V"){
                separateWord.add(i.text+"다")
            }
        }
        // API 호출이 완료되었을 때, 단어를 하나씩 저장한다
        initWords(separateWord, index, isFinished)
    }
    // 각 단어들의 뜻과 발음 불러와서 객체에 저장
    private fun initWords(separateWord: ArrayList<String>, linenum: Int, isFinished: Boolean){
        var wordIndex = 0
        var wordEntities = ArrayList<WordEntity>()

        for (word in separateWord) {
            val pron = KoreanRomanizer.romanize(word, KoreanCharacter.ConsonantAssimilation.Progressive)
            val translate2 = TranslateWord(word)
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
            wordEntity.wordPronunciation = pron
            wordEntity.wordEnglish = meaning
            wordIndex += 1
            updateWord.add(wordEntity)
            Log.d("ADD_WORD_FIN", updateWord.toString())
        }
        if (isFinished) {
            Log.d("ADD_WORD_CALL+LASTDAO", "hi")
            addWordInDao()
        }
    }
    // 단어 번역
    class TranslateWord(private val str: String) : AsyncTask<String, Void, ArrayList<String>>() {
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

    private suspend fun searchVideos(query: String): String = withContext(Dispatchers.IO) {
        val transport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val youtube = YouTube.Builder(transport, JSON_FACTORY, null)
            .setApplicationName("YoutubeSearch")
            .build()

        val searchRequest = youtube.search().list("id")
        searchRequest.key = YOUTUBE_KEY
        searchRequest.q = query
        searchRequest.type = "video"
        searchRequest.maxResults = 1
        Log.d("YOUTUBE_SEARCH", searchRequest.toString())

        var result: List<SearchResult> = emptyList()

        try {
            val searchResponse: SearchListResponse = searchRequest.execute()
            val searchResult: List<SearchResult> = searchResponse.items ?: emptyList()
            result = searchResult
        } catch (e: Exception) {
            e.printStackTrace()
        }

        result[0].id.videoId
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d("POPULAR_DIALOG", "onDestroy")
    }
}