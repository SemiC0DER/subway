package com.example.whatsub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.textInputServiceFactory
import com.google.android.material.internal.ViewUtils.hideKeyboard

class MainActivity : AppCompatActivity() {
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

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


        findroad.setOnClickListener {
            startText = startstation.getText().toString()
            destText = deststation.getText().toString()
            val startStation = stationMap[startText] ?: -1
            val endStation = stationMap[destText] ?: -1

            if (startStation != -1 && endStation != -1) {
                val intent = Intent(this, SelectActivity::class.java)
                intent.putExtra("startText", startText)
                intent.putExtra("destText", destText)
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
