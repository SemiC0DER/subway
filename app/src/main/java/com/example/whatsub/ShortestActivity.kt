package com.example.whatsub

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ShortestActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shortest_distance)

        val startstation: EditText = findViewById(R.id.start_station)
        val deststation: EditText = findViewById(R.id.destination_station)
        val findroad: Button = findViewById(R.id.find_road)
        val criteria: TextView = findViewById(R.id.textView5)
        val start_st: TextView = findViewById(R.id.start_st)
        val end_st: TextView = findViewById(R.id.textView7)
        val transStations: TextView = findViewById(R.id.cost_trans_staion)
        val allStations: TextView = findViewById(R.id.cost_all_station)
    }
}