package com.example.lyword.studying.lyrics

import android.util.Log
import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

// c66f16a8-9de7-49a9-a19a-ab675301f744
class SeparateLyricsActivity {
    data class Morpheme(val text: String, val type: String, var count: Int?)

    data class NameEntity(val text: String, val type: String, var count: Int?)

    fun getMorpheme() {
        // 언어 분석 기술 문어/구어 중 한가지만 선택해 사용
        // 언어 분석 기술(문어)
        val openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU"
        // 언어 분석 기술(구어)
        // val openApiURL = "http://aiopen.etri.re.kr:8000/WiseNLU_spoken"
        val accessKey = "c66f16a8-9de7-49a9-a19a-ab675301f744" // 발급받은 API Key
        val analysisCode = "ner" // 언어 분석 코드
        var text = "" // 분석할 텍스트 데이터
        val gson = Gson()

        // 언어 분석 기술(문어)
        text += "윤동주(尹東柱, 1917년 12월 30일 ~ 1945년 2월 16일)는 한국의 독립운동가, 시인, 작가이다." +
                "중국 만저우 지방 지린 성 연변 용정에서 출생하여 명동학교에서 수학하였고, 숭실중학교와 연희전문학교를 졸업하였다. 숭실중학교 때 처음 시를 발표하였고, 1939년 연희전문 2학년 재학 중 소년(少年) 지에 시를 발표하며 정식으로 문단에 데뷔했다." +
                "일본 유학 후 도시샤 대학 재학 중 , 1943년 항일운동을 했다는 혐의로 일본 경찰에 체포되어 후쿠오카 형무소(福岡刑務所)에 투옥, 100여 편의 시를 남기고 27세의 나이에 옥중에서 요절하였다. 사인이 일본의 생체실험이라는 견해가 있고 그의 사후 일본군에 의한 마루타, 생체실험설이 제기되었으나 불확실하다. 사후에 그의 시집 《하늘과 바람과 별과 시》가 출간되었다." +
                "일제 강점기 후반의 양심적 지식인으로 인정받았으며, 그의 시는 일제와 조선총독부에 대한 비판과 자아성찰 등을 소재로 하였다. 그의 친구이자 사촌인 송몽규 역시 독립운동에 가담하려다가 체포되어 일제의 생체 실험으로 의문의 죽음을 맞는다. 1990년대 후반 이후 그의 창씨개명 '히라누마'가 알려져 논란이 일기도 했다. 본명 외에 윤동주(尹童柱), 윤주(尹柱)라는 필명도 사용하였다."
        // 언어 분석 기술(구어)
        text += "네 안녕하세요 홍길동 교숩니다"

        val request: MutableMap<String, Any> = HashMap()
        val argument: MutableMap<String, String> = HashMap()

        argument["analysis_code"] = analysisCode
        argument["text"] = text

        request["argument"] = argument

        var url: URL?
        var responseCode: Int? = null
        var responseBodyJson: String? = null
        var responseBody: MutableMap<String, Any>? = null

        try {
            url = URL(openApiURL)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.doOutput = true
            con.setRequestProperty("Authorization", accessKey)

            val wr = DataOutputStream(con.outputStream)
            wr.write(gson.toJson(request).toByteArray(charset("UTF-8")))
            wr.flush()
            wr.close()

            responseCode = con.responseCode
            val `is`: InputStream = con.inputStream
            val br = BufferedReader(InputStreamReader(`is`))
            val sb = StringBuffer()

            var inputLine: String?
            while (br.readLine().also { inputLine = it } != null) {
                sb.append(inputLine)
            }
            responseBodyJson = sb.toString()

            // http 요청 오류 시 처리
            if (responseCode != 200) {
                // 오류 내용 출력
                println("[error] $responseBodyJson")
                return
            }

            responseBody = gson.fromJson(responseBodyJson, MutableMap::class.java) as MutableMap<String, Any>?
            val result = (responseBody!!["result"] as Double).toInt()
            var returnObject: MutableMap<String, Any>?
            var sentences: List<MutableMap<String, Any>>?

            // 분석 요청 오류 시 처리
            if (result != 0) {

                // 오류 내용 출력
                println("[error] ${responseBody["result"]}")
                return
            }

            // 분석 결과 활용
            returnObject = responseBody["return_object"] as MutableMap<String, Any>?
            sentences = returnObject!!["sentence"] as List<MutableMap<String, Any>>?

            val morphemesMap: MutableMap<String, Morpheme> = HashMap()
            val nameEntitiesMap: MutableMap<String, NameEntity> = HashMap()
            var morphemes: List<Morpheme>? = null
            var nameEntities: List<NameEntity>? = null

            for (sentence in sentences!!) {
                // 형태소 분석기 결과 수집 및 정렬
                val morphologicalAnalysisResult =
                    sentence["morp"] as List<MutableMap<String, Any>>
                for (morphemeInfo in morphologicalAnalysisResult) {
                    val lemma = morphemeInfo["lemma"] as String
                    var morpheme = morphemesMap[lemma]
                    if (morpheme == null) {
                        morpheme = Morpheme(
                            lemma,
                            morphemeInfo["type"] as String,
                            1
                        )
                        morphemesMap[lemma] = morpheme
                    } else {
                        morpheme.count = morpheme.count?.plus(1)
                    }
                }

                // 개체명 분석 결과 수집 및 정렬
                val nameEntityRecognitionResult =
                    sentence["NE"] as List<MutableMap<String, Any>>
                for (nameEntityInfo in nameEntityRecognitionResult) {
                    val name = nameEntityInfo["text"] as String
                    var nameEntity = nameEntitiesMap[name]
                    if (nameEntity == null) {
                        nameEntity = NameEntity(
                            name,
                            nameEntityInfo["type"] as String,
                            1
                        )
                        nameEntitiesMap[name] = nameEntity
                    } else {
                        nameEntity.count = nameEntity.count?.plus(1)
                    }
                }
            }

            if (morphemesMap.size > 0) {
                morphemes = ArrayList<Morpheme>(morphemesMap.values)
                morphemes.sortByDescending { morpheme -> morpheme.count }
            }

            if (nameEntitiesMap.size > 0) {
                nameEntities = ArrayList<NameEntity>(nameEntitiesMap.values)
                nameEntities.sortByDescending { nameEntity -> nameEntity.count }
            }

            // 형태소들 중 명사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
            morphemes
                ?.filter { morpheme ->
                    morpheme.type == "NNG" ||
                            morpheme.type == "NNP" ||
                            morpheme.type == "NNB"
                }
                ?.take(5)
                ?.forEach { morpheme ->
                    Log.d("hello", "[명사] ${morpheme.text} (${morpheme.count})")
                }

            // 형태소들 중 동사들에 대해서 많이 노출된 순으로 출력 ( 최대 5개 )
            println("")
            morphemes
                ?.filter { morpheme ->
                    morpheme.type == "VV"
                }
                ?.take(5)
                ?.forEach { morpheme ->
                    Log.d("hello2", "[동사] ${morpheme.text} (${morpheme.count})")
                }

            // 인식된 개채명들 많이 노출된 순으로 출력 ( 최대 5개 )
            println("")
            nameEntities
                ?.take(5)
                ?.forEach { nameEntity ->
                    Log.d("hello3", "[개체명] ${nameEntity.text} (${nameEntity.count})")
                }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}