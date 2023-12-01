package com.example.whatsub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    // 로그에 사용할 TAG 변수
    final private String TAG = getClass().getSimpleName();

    // 사용할 컴포넌트 선언
    ListView listView;
    Button reg_button;
    Button subway_back_button;
    String userid = "";
    SearchView search_view;


    // 검색을 했는지 확인하는 변수 -> 검색한 게시물을 클릭할 때 사용
    Boolean search = false;


    // 리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();

    // 클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기 위한 배열
    ArrayList<String> seqList = new ArrayList<>();
    // 검색어가 포함된 리스트를 보여주기 위한 배열
    ArrayList<String> searchList = new ArrayList<>();
    // 검색한 게시물을 클릭했을 때 그 제목에 맞는 값을 주기 위한 배열
    ArrayList<String> searchSeqList = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // 컴포넌트 초기화
        listView = findViewById(R.id.listView);

        // LoginActivity 에서 넘긴 userid 값 받기
        userid = getIntent().getStringExtra("userid");

        // ListView 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0 && i < seqList.size()) {
                    Log.d(TAG, "search" + search);
                    String selectedSeq = seqList.get(i);
                    // 만약에 검색을 했다면 selectedSeq값을 검색에 맞춰 변경
                    if (search) {
                        selectedSeq = searchSeqList.get(i);
                    }
                    Log.d(TAG, "selectedSeq: " + selectedSeq);

                    // 어떤 값을 선택했는지 토스트를 뿌려줌
                    Toast.makeText(ListActivity.this, adapterView.getItemAtPosition(i) + " 클릭", Toast.LENGTH_SHORT).show();

                    // 게시물의 번호와 userid를 가지고 DetailActivity 로 이동
                    Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                    intent.putExtra("board_seq", selectedSeq);
                    intent.putExtra("userid", userid);
                    startActivity(intent);
                }
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

        // 길찾기로 되돌아가는 버튼 추가
        subway_back_button = findViewById(R.id.subway_back_button);
        subway_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 검색어 입력
        search_view = findViewById(R.id.search_view);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색창에 글자를 입력할 때마다 결과를 변경
                filter(newText);
                return false;
            }
        });

        // 어댑터 초기화
        arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, titleList);
        listView.setAdapter(arrayAdapter);
    }

    // 입력받은 검색어를 지금 있는 제목과 비교해서 일치하는 것만 표시
    private void filter(String searchText) {
        searchList.clear();
        searchSeqList.clear();

        if (searchText.length() == 0) {
            // 검색어가 없으면 모든 게시물 표시
            searchList.addAll(titleList);
            searchSeqList.addAll(seqList);
        } else {
            // 검색어가 있으면 포함하는 게시물만 표시
            for (int i = 0; i < titleList.size(); i++) {
                String title = titleList.get(i);
                if (title.toLowerCase().contains(searchText.toLowerCase())) {
                    searchList.add(title);
                    searchSeqList.add(seqList.get(i));
                }
            }

            // 검색이 발생했는지 여부를 search 변수에 설정
            search = !searchList.isEmpty();

            // 검색한 결과로 어댑터를 업데이트
            arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, searchList);
            listView.setAdapter(arrayAdapter);
        }

        // 어댑터 업데이트
        arrayAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 해당 액티비티가 활성화 될 때, 게시물 리스트를 불러오는 함수를 호출
        getBoardData();
    }


    // Firebase에서 게시물 리스트 가져오기
    private void getBoardData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference boardRef = database.getReference("boards");

        search = false;

        boardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                titleList.clear();
                seqList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String seq = snapshot.getKey(); // Getting Firebase unique key as sequence

                    if (title != null) {
                        // 리스트의 맨 앞에 새로운 게시물 추가
                        titleList.add(0, title);
                        seqList.add(0, seq != null ? seq : "");
                    }
                }
                // 데이터가 변경되었음을 어댑터에 알림
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 필요 시 오류 처리
                Log.e(TAG, "Firebase 데이터베이스 오류: " + databaseError.getMessage());
            }
        });
    }
}
