package com.example.lyword

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.lyword.databinding.ActivityMainBinding
import com.example.lyword.home.HomeFragment
import com.example.lyword.home.notify.PushNotificationReceiver
import com.example.lyword.mypage.MypageFragment
import com.example.lyword.studying.StudyingFragment
import com.example.lyword.today.TodayFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar
import java.util.Date
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import kotlin.system.exitProcess


private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        checkNotificationPermission()

    }

    private fun checkNotificationPermission(){
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            setAlarm(this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAlarm(this)
            } else {
                Toast.makeText(this, "알림권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initBottomNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> { //홈 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.studyingFragment -> { //친구목록 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, StudyingFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.todayFragment -> { //그룹목록 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, TodayFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> { //커스텀 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun getBottomNavigation() : BottomNavigationView {
        return binding.mainBottomNavi
    }
}


fun setAlarm(context: Context) {
    val id = 100

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, PushNotificationReceiver::class.java)
    intent.putExtra("notification_id", id)
    intent.putExtra("notification_title", "오늘의 학습을 진행해주세요!")
    intent.putExtra("notification_content", "하루의 마무리를 Lyword와 함께하세요.")

    val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
    } else {
        PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val calendar : Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 1)
        set(Calendar.MINUTE, 50)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    if (calendar.timeInMillis < System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    } else {
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}