package com.example.lyword.today

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lyword.R
import net.crizin.KoreanCharacter
import net.crizin.KoreanRomanizer
import java.util.Locale

class TodayWordRVAdapter(private var todayWords: ArrayList<TodayWord>): RecyclerView.Adapter<TodayWordViewHolder>(),  TextToSpeech.OnInitListener{

    private var tts: TextToSpeech? = null
    private var romaza_speaker: ImageButton? = null
    var tmp_lyrics: TextView? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayWordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_today_word, parent, false)

        return TodayWordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todayWords.size
    }

    override fun onBindViewHolder(holder: TodayWordViewHolder, position: Int) {
        holder.todayLyrics.text=todayWords[position].lyrics
        holder.todayKoreanLyrics.text=todayWords[position].korean_lyrics

        holder.pronunciationButton?.setOnClickListener {
            showCustomDialog(holder)
        }



    }
    private fun showCustomDialog(holder: TodayWordViewHolder) {
        val dialogView =
            LayoutInflater.from(holder.pronunciationButton?.context)
                .inflate(R.layout.dialog_pronunciation, null)

        romaza_speaker = dialogView.findViewById<ImageButton>(R.id.romaza_speaker)
        val romaza_close_button = dialogView.findViewById<ImageButton>(R.id.romaza_close_button)

        val dialog_pron_original = dialogView.findViewById<TextView>(R.id.dialog_pron_original)
        val dialog_pron_romaza = dialogView.findViewById<TextView>(R.id.dialog_pron_romaza)


        val dialogBuilder = AlertDialog.Builder(holder.pronunciationButton?.context)
            .setView(dialogView)
            .setCancelable(false)

        val dialog = dialogBuilder.create()

        dialog_pron_original!!.text=holder.todayKoreanLyrics!!.text

        tmp_lyrics=dialogView.findViewById<TextView>(R.id.dialog_pron_original)

        tmp_lyrics!!.text=holder.todayKoreanLyrics!!.text

        dialog_pron_romaza!!.text =
            KoreanRomanizer.romanize(dialog_pron_original!!.text.toString(), KoreanCharacter.ConsonantAssimilation.Progressive)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        tts = TextToSpeech(holder.pronunciationButton?.context, this)

        romaza_speaker!!.setOnClickListener (

            View.OnClickListener { speakOut() }
        )

        romaza_close_button.setOnClickListener {

            dialog.dismiss()
        }

        dialog.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun speakOut() {

        val text: CharSequence = tmp_lyrics!!.text
        tts!!.setPitch(0.6.toFloat()) // 음성 톤 높이 지정
        tts!!.setSpeechRate(1.5.toFloat()) // 음성 속도 지정

        // 첫 번째 매개변수: 음성 출력을 할 텍스트
        // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
        //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1")
        Log.e("TTS", "Initialization Success!!!!!!")
    }

    fun onDestroy() {
        if (tts != null) { // 사용한 TTS객체 제거
            tts!!.stop()
            tts!!.shutdown()
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onInit(status: Int) { // OnInitListener를 통해서 TTS 초기화

        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.KOREA) // TTS언어 한국어로 설정
            if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("TTS", "This Language is not supported")
            } else {

                romaza_speaker!!.isEnabled = true
                speakOut() // onInit에 음성출력할 텍스트를 넣어줌
                Log.e("TTS", "Initialization Success!!!!!!")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }
}

class TodayWordViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var todayLyrics = view.findViewById<TextView>(R.id.item_today_lyrics)
    var todayKoreanLyrics = view.findViewById<TextView>(R.id.item_today_korean_lyrics)
    var pronunciationButton = view.findViewById<Button>(R.id.pronunciation_button)
}