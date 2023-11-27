/*
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



class PathActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finded_load)

        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        val timeinfo: TextView = findViewById(R.id.time_start_station_info)
        val timeinfo2: TextView = findViewById(R.id.time_transfer_station_info)
        val distinfo: TextView = findViewById(R.id.distance_start_station_info)
        val distinfo2: TextView = findViewById(R.id.distance_transfer_station_info)
        val costinfo: TextView = findViewById(R.id.cost_start_station_info)
        val costinfo2: TextView = findViewById(R.id.cost_transfer_station_info)
        var startText = ""
        var destText = ""

        startstation.inputType = EditorInfo.TYPE_CLASS_TEXT
        deststation.inputType = EditorInfo.TYPE_CLASS_TEXT

        startstation.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                startText = startstation.text.toString()
                return@setOnEditorActionListener false
            }
            return@setOnEditorActionListener false
        }

        deststation.setOnEditorActionListener { _, actionId, event ->
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
                val timeResult = dijkstra(graph, startStation, endStation, "time")
                val distResult = dijkstra(graph, startStation, endStation, "distance")
                val costResult = dijkstra(graph, startStation, endStation, "cost")

                if (timeResult.time != Int.MAX_VALUE && distResult.distance != Int.MAX_VALUE && costResult.cost != Int.MAX_VALUE) {
                    //이 부분 수정 필요 -- 텍스트랑 연결
                    timeinfo.setText(printResult(timeResult))
                    timeinfo2.setText(printStationNames(timeResult.path))

                    distinfo.setText(printResult(distResult))
                    distinfo2.setText(printStationNames(distResult.path))

                    costinfo.setText(printResult(costResult))
                    costinfo2.setText(printStationNames(costResult.path))
                }
                else
                    Toast.makeText(this,"경로가 존재하지 않습니다.",Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this,"입력한 역 이름이 유효하지 않습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}*/
