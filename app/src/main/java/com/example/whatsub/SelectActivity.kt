package com.example.whatsub

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.inputmethod.TextAppearanceInfo
import com.google.android.material.internal.ViewUtils.hideKeyboard

//결과를 대략적으로 표시하는 창이다.
class SelectActivity : AppCompatActivity(){

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_)

        //컴포넌트 초기화
        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        val cost: Button = findViewById(R.id.cost)
        val distance: Button = findViewById(R.id.distance)
        val time: Button = findViewById(R.id.time)
        val cost_distance: TextView = findViewById(R.id.cost_distance)
        val cost_cost: TextView = findViewById(R.id.cost_cost)
        val cost_time: TextView = findViewById(R.id.cost_time)
        val distance_distance: TextView = findViewById(R.id.distance_distance)
        val distance_cost: TextView = findViewById(R.id.distance_cost)
        val distance_time: TextView = findViewById(R.id.distance_time)
        val time_distance: TextView = findViewById(R.id.time_distance)
        val time_cost: TextView = findViewById(R.id.time_cost)
        val time_time: TextView = findViewById(R.id.time_time)

        var startText = intent.getStringExtra("startText")
        var destText = intent.getStringExtra("destText")
        Log.d("SelectActivity", "startText: $startText")
        Log.d("SelectActivity", "destText: $destText")
        val gotomain: Button = findViewById(R.id.button4)

        //받은 값을 토대로 초기화
        setGraph()
        var startStation = stationMap[startText] ?: -1
        var endStation = stationMap[destText] ?: -1
        val timeResult = dijkstra(graph, startStation, endStation, "time")
        val distResult = dijkstra(graph, startStation, endStation, "distance")
        val costResult = dijkstra(graph, startStation, endStation, "cost")
        val timeResultData = printResult(timeResult)
        val distResultData = printResult(distResult)
        val costResultData = printResult(costResult)
        time_time.setText(timeResultData[0])
        time_distance.setText(timeResultData[1])
        time_cost.setText(timeResultData[2])
        distance_time.setText(distResultData[0])
        distance_distance.setText(distResultData[1])
        distance_cost.setText(distResultData[2])
        cost_time.setText(costResultData[0])
        cost_distance.setText(costResultData[1])
        cost_cost.setText(costResultData[2])

        startstation.inputType = EditorInfo.TYPE_CLASS_TEXT
        deststation.inputType = EditorInfo.TYPE_CLASS_TEXT

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
                }
                else
                    Toast.makeText(this,"경로가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this,"입력한 역 이름이 유효하지 않습니다.",Toast.LENGTH_SHORT).show()
        }

        time.setOnClickListener {
            val intent = Intent(this, ShortestActivity::class.java)
        }

        distance.setOnClickListener {
            val intent = Intent(this, ShortestActivity::class.java)
        }

        cost.setOnClickListener {
            val intent = Intent(this, ShortestActivity::class.java)
        }

        gotomain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}