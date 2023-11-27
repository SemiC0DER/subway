package com.example.whatsub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_WhatSub)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        var startText = ""
        var destText = ""

        var communityBtn: Button = findViewById(R.id.communityBtn)

        startstation.inputType = EditorInfo.TYPE_CLASS_TEXT
        deststation.inputType = EditorInfo.TYPE_CLASS_TEXT

        startstation.setOnEditorActionListener { _, actionId, event ->//출발역 입력
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                startText = startstation.text.toString()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }

        deststation.setOnEditorActionListener { _, actionId, event ->//도착역 입력
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                destText = deststation.text.toString()

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(deststation.windowToken, 0) // 키보드 닫기
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        findroad.setOnClickListener {
            val startStation = stationMap[startText] ?: -1
            val endStation = stationMap[destText] ?: -1

            if (startStation != -1 && endStation != -1) {
                val intent = Intent(this, SelectActivity::class.java)
                intent.putExtra("startStation", startStation)
                intent.putExtra("endStation", endStation)
                startActivity(intent)
            } else
                Toast.makeText(this,"입력한 역 이름이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }





        // 버튼 이벤트 추가
        communityBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })
    }
}
