package com.example.whatsub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShortestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subway_detail)

        //컴포넌트 초기화
        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        val criteria: TextView = findViewById(R.id.route)
        val start_st: TextView = findViewById(R.id.start_st)
        val dest_st: TextView = findViewById(R.id.dest_st)
        val transStations: TextView = findViewById(R.id.cost_trans_station)
        val allStations: TextView = findViewById(R.id.cost_all_station)
        val gotomain: Button = findViewById(R.id.subwayBtn)
        val communityBtn: Button = findViewById(R.id.communityBtn)

        //이전 화면의 정보들을 넘겨받아 저장
        val pathArray: IntArray? = intent.getIntArrayExtra("pathArray")
        Log.d("ShortestActivity", "pathArray[0]: ${pathArray?.get(0)}")
        val path: List<Int>? = pathArray?.toList()
        val title = intent.getStringExtra("title")
        var startText = intent.getStringExtra("startText")
        var destText = intent.getStringExtra("destText")
        Log.d("SelectActivity", "startText: $startText")
        Log.d("SelectActivity", "destText: $destText")

        //검색창에 입력했던 정보들을 남김
        startstation.setText("출발역: $startText")
        deststation.setText("도착역: $destText")

        //역 탐색 조건, 시작역, 도착역 출력
        criteria.setText(title)
        start_st.setText("출발역: $startText")
        dest_st.setText("도착역: $destText")

        //길찾기 결과
        val route = path?.let { getStationsRoute(it) }
        transStations.setText(route?.get(0))
        allStations.setText(route?.get(1))

        //길찾기 버튼
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
                Toast.makeText(this, "입력한 역 이름이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        // 길찾기 메인 화면 버튼
        gotomain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 커뮤니티 메인 화면 버튼
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