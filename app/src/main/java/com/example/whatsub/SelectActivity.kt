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

//결과를 대략적으로 표시하는 창
class SelectActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        //컴포넌트 초기화
        val startstation: EditText = findViewById(R.id.start_station)//검색창
        val deststation: EditText = findViewById(R.id.destination_station)//검색창
        val findroad: Button = findViewById(R.id.find_road)//검색버튼
        //역 정보 상세 버튼
        val time: Button = findViewById(R.id.time)
        val distance: Button = findViewById(R.id.distance)
        val cost: Button = findViewById(R.id.cost)
        //최단 시간 정보
        val time_distance: TextView = findViewById(R.id.time_distance)
        val time_cost: TextView = findViewById(R.id.time_cost)
        val time_time: TextView = findViewById(R.id.time_time)
        //최단 거리 정보
        val distance_distance: TextView = findViewById(R.id.distance_distance)
        val distance_cost: TextView = findViewById(R.id.distance_cost)
        val distance_time: TextView = findViewById(R.id.distance_time)
        //최소 비용 정보
        val cost_distance: TextView = findViewById(R.id.cost_distance)
        val cost_cost: TextView = findViewById(R.id.cost_cost)
        val cost_time: TextView = findViewById(R.id.cost_time)


        //하단의 메인 화면으로 가는 버튼
        val gotomain: Button = findViewById(R.id.subwayBtn)
        val communityBtn: Button = findViewById(R.id.communityBtn)

        //이전 화면의 정보를 넘겨 받음
        var startText = intent.getStringExtra("startText")
        var destText = intent.getStringExtra("destText")
        Log.d("SelectActivity", "startText: $startText")
        Log.d("SelectActivity", "destText: $destText")

        //검색창에 입력했던 정보들을 남김
        //        startstation.setText(startText)
        startstation.setText("출발역: $startText")
//        deststation.setText(destText)
        deststation.setText("도착역: $destText")

        //받은 값을 토대로 다익스트라 계산
        setGraph()
        var startStation = stationMap[startText] ?: -1
        var endStation = stationMap[destText] ?: -1
        val timeResult = dijkstra(graph, startStation, endStation, "time")
        val distResult = dijkstra(graph, startStation, endStation, "distance")
        val costResult = dijkstra(graph, startStation, endStation, "cost")
        val timeResultData = printResult(timeResult)
        val distResultData = printResult(distResult)
        val costResultData = printResult(costResult)

        //최단 시간, 최단 거리, 최소 비용 정보 초기화
        time_time.setText(timeResultData[0])
        time_distance.setText(timeResultData[1])
        time_cost.setText(timeResultData[2])
        distance_time.setText(distResultData[0])
        distance_distance.setText(distResultData[1])
        distance_cost.setText(distResultData[2])
        cost_time.setText(costResultData[0])
        cost_distance.setText(costResultData[1])
        cost_cost.setText(costResultData[2])


        //길찾기 버튼
        findroad.setOnClickListener {
            startText = startstation.getText().toString()
            destText = deststation.getText().toString()
            startStation = stationMap[startText] ?: -1
            endStation = stationMap[destText] ?: -1

            if (startStation != -1 && endStation != -1) {
                val newtimeResult = dijkstra(graph, startStation, endStation, "time")
                val newdistResult = dijkstra(graph, startStation, endStation, "distance")
                val newcostResult = dijkstra(graph, startStation, endStation, "cost")

                if (timeResult.time != Int.MAX_VALUE && distResult.distance != Int.MAX_VALUE && costResult.cost != Int.MAX_VALUE) {
                    //입력값에 따라 새로 결과값을 표시
                    val newtimeResultData = printResult(newtimeResult)
                    val newdistResultData = printResult(newdistResult)
                    val newcostResultData = printResult(newcostResult)
                    time_time.setText(newtimeResultData[0])
                    time_distance.setText(newtimeResultData[1])
                    time_cost.setText(newtimeResultData[2])
                    distance_time.setText(newdistResultData[0])
                    distance_distance.setText(newdistResultData[1])
                    distance_cost.setText(newdistResultData[2])
                    cost_time.setText(newcostResultData[0])
                    cost_distance.setText(newcostResultData[1])
                    cost_cost.setText(newcostResultData[2])
                } else
                    Toast.makeText(this, "경로가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "입력한 역 이름이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        time.setOnClickListener {
            val path = timeResult.path
            val pathArray: IntArray = path.toIntArray()
            val intent = Intent(this, ShortestActivity::class.java)
            Log.d("SelectActivity", "pathArray[0]: ${pathArray?.get(0)}")
            intent.putExtra("startText", startText)
            intent.putExtra("destText", destText)
            intent.putExtra("pathArray", pathArray)
            intent.putExtra("title", "최단 시간")
            startActivity(intent)
        }

        distance.setOnClickListener {
            val path = distResult.path
            val pathArray: IntArray = path.toIntArray()
            val intent = Intent(this, ShortestActivity::class.java)
            Log.d("SelectActivity", "pathArray[0]: ${pathArray?.get(0)}")
            intent.putExtra("startText", startText)
            intent.putExtra("destText", destText)
            intent.putExtra("pathArray", pathArray)
            intent.putExtra("title", "최단 거리")
            startActivity(intent)
        }

        cost.setOnClickListener {
            val path = costResult.path
            val pathArray: IntArray = path.toIntArray()
            val intent = Intent(this, ShortestActivity::class.java)
            Log.d("SelectActivity", "pathArray[0]: ${pathArray?.get(0)}")
            intent.putExtra("startText", startText)
            intent.putExtra("destText", destText)
            intent.putExtra("pathArray", pathArray)
            intent.putExtra("title", "최소 비용")
            startActivity(intent)
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