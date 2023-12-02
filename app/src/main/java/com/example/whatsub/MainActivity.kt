package com.example.whatsub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
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
        setGraph()

        //컴포넌트 초기화
        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        val communityBtn: Button = findViewById(R.id.communityBtn)

        //입력값을 저장할 변수
        var startText = ""
        var destText = ""

        //길찾기 버튼
        findroad.setOnClickListener {
            startText = startstation.getText().toString()
            destText = deststation.getText().toString()
            val startStation = stationMap[startText] ?: -1
            val endStation = stationMap[destText] ?: -1

            if (startStation != -1 && endStation != -1) {
                val timeResult = dijkstra(graph, startStation, endStation, "time")

                if (timeResult.time != Int.MAX_VALUE) {
                    val intent = Intent(this, SelectActivity::class.java)
                    intent.putExtra("startText", startText)
                    intent.putExtra("destText", destText)
                    startActivity(intent)
                } else
                    Toast.makeText(this, "경로가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "입력한 역 이름이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        // 커뮤니티 버튼
        communityBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        //엔터키 키보드 숨김
        startstation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        //엔터키 키보드 숨김
        deststation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

    }

    //터치 키보드 숨김
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

    //키보드 숨김
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}
