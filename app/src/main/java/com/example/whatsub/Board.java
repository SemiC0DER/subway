package com.example.whatsub;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class Board {
    private String userid;
    private String title;
    private String content;

    private String date;

    public Board() {
        // 기본 생성자가 필요함 (Firebase의 데이터 읽기/쓰기를 위해)
    }

    public Board(String userid, String title, String content) {
        this.userid = userid;
        this.title = title;
        this.content = content;
        //this.date = getCurrentDeviceDate(); // 기기의 현재 시간을 가져와 date 필드에 할당
        this.date = getCurrentDate();
    }

    // Getter 및 Setter 메서드 추가
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /*public String getCurrentDeviceDate() {
        // 기기의 현재 시간을 가져와서 반환
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date()); // 기기의 현재 시간을 반환
    }*/

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}