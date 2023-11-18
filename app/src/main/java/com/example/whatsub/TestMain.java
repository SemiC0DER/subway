package com.example.whatsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TestMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_WhatSub);
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
    }

    public void onClickButton(View view) {
        // 버튼 클릭 시 다음 화면으로이동하는 Intent 생성
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}