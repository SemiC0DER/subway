package com.example.whatsub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ListActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    ListView listView;
    Button reg_button;
    String userid = "";

    // 리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();

    // 클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기 위한 배열
    ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

// LoginActivity 에서 넘긴 userid 값 받기
        userid = getIntent().getStringExtra("userid");

// 컴포넌트 초기화
        listView = findViewById(R.id.listView);

// listView 를 클릭했을 때 이벤트 추가
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

// 어떤 값을 선택했는지 토스트를 뿌려줌
                Toast.makeText(ListActivity.this, adapterView.getItemAtPosition(i)+ " 클릭", Toast.LENGTH_SHORT).show();

// 게시물의 번호와 userid를 가지고 DetailActivity 로 이동
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra("board_seq", seqList.get(i));
                intent.putExtra("userid", userid);
                startActivity(intent);

            }
        });

// 버튼 컴포넌트 초기화
        reg_button = findViewById(R.id.reg_button);

// 버튼 이벤트 추가
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

// userid 를 가지고 RegisterActivity 로 이동
                Intent intent = new Intent(ListActivity.this, RegisterActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });
    }


    // onResume() 은 해당 액티비티가 화면에 나타날 때 호출됨
    @Override
    protected void onResume() {
        super.onResume();
// 해당 액티비티가 활성화 될 때, 게시물 리스트를 불러오는 함수를 호출
        GetBoard getBoard = new GetBoard();
        getBoard.execute();
    }


    // 게시물 리스트를 읽어오는 함수
    // GetBoard AsyncTask 클래스 수정
    class GetBoard extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Firebase 초기화
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference boardRef = database.getReference("boards");

            boardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    titleList.clear();
                    seqList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String title = snapshot.child("title").getValue(String.class);
                        String seq = snapshot.child("seq").getValue(String.class);

                        // 가져온 데이터를 리스트에 추가
                        titleList.add(title);
                        seqList.add(seq);
                    }

                    // UI 업데이트 - 어댑터 및 리스트뷰
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, titleList);
                    listView.setAdapter(arrayAdapter);
                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 필요 시 오류 처리
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // onPostExecute에서 다른 UI 업데이트 작업을 진행할 수도 있음
        }
    }

}