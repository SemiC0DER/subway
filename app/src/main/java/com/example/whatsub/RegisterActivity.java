package com.example.whatsub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수 선언
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    EditText title_et, content_et;
    Button reg_button;

    // 유저아이디 변수
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

// ListActivity 에서 넘긴 userid 를 변수로 받음
        userid = getIntent().getStringExtra("userid");

// 컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);

// 버튼 이벤트 추가
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 게시물 등록 함수
                RegBoard regBoard = new RegBoard();
                regBoard.execute(userid, title_et.getText().toString(), content_et.getText().toString());

                // 리스트 액티비티로 이동하는 Intent 생성
                Intent intent = new Intent(RegisterActivity.this, ListActivity.class);
                intent.putExtra("userid", userid); // userid를 리스트 액티비티로 전달
                startActivity(intent);
            }
        });

    }

    class RegBoard extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String userid = params[0];
            String title = params[1];
            String content = params[2];

            // Firebase 데이터베이스 초기화
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference boardsRef = database.getReference("boards");

            // 새로운 게시글에 대한 데이터 객체 생성
            Board newBoard = new Board(userid, title, content);

            // "boards" 노드 아래에 새로운 게시글 추가
            boardsRef.push().setValue(newBoard);

            return null;
        }
    }
}




