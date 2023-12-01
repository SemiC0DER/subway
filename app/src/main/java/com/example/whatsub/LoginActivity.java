    package com.example.whatsub;
    
    import androidx.annotation.NonNull;
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
    
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.InputStreamReader;
    import java.io.OutputStream;
    import java.io.OutputStreamWriter;
    import java.net.HttpURLConnection;
    import java.net.URL;
    
    import javax.net.ssl.HttpsURLConnection;
    
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    
    
    public class LoginActivity extends AppCompatActivity {
    
        // Firebase Authentication 및 Realtime Database 사용을 위한 초기화
        FirebaseAuth mAuth;
        DatabaseReference mDatabase;
    
        // 로그에 사용할 TAG 변수 선언
        final private String TAG = getClass().getSimpleName();
    
        // 사용할 컴포넌트 선언
        EditText userid_et, passwd_et;
        Button login_button, join_button;
    
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setTheme(R.style.Theme_WhatSub);
            setContentView(R.layout.activity_login);
    
    //Firebase 인스턴스 초기화
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
    
    // 사용할 컴포넌트 초기화
            userid_et = findViewById(R.id.userid_et);
            passwd_et = findViewById(R.id.passwd_et);
            login_button = findViewById(R.id.login_button);
            join_button = findViewById(R.id.join_button);
    
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userId = userid_et.getText().toString();
                    String password = passwd_et.getText().toString();
    
                    // Firebase 인증을 사용하여 로그인 처리
                    mAuth.signInWithEmailAndPassword(userId, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // 로그인 성공 시
                                        Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                                        intent.putExtra("userid", userId);
                                        startActivity(intent);
                                    } else {
                                        // 로그인 실패 시
                                        Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
    
    // 조인 버튼 이벤트
            join_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(intent);
                }
            });

        }
    
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                currentUser.reload();
            }
        }
    
    
    
    }