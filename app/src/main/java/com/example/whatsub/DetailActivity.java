package com.example.whatsub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    DatabaseReference boardsRef;

    TextView title_tv, content_tv, date_tv;
    LinearLayout comment_layout;
    EditText comment_et;
    Button reg_button,reply_button;

    String board_seq = "";
    String userid = "";

    private EditText replyEditText; // 대댓글을 입력받을 EditText 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        title_tv = findViewById(R.id.title_tv);
        content_tv = findViewById(R.id.content_tv);
        date_tv = findViewById(R.id.date_tv);

        comment_layout = findViewById(R.id.comment_layout);
        comment_et = findViewById(R.id.comment_et);
        reg_button = findViewById(R.id.reg_button);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reply, null);
        EditText replyEditText = dialogView.findViewById(R.id.reply_edit_text);

        board_seq = getIntent().getStringExtra("board_seq");
        userid = getIntent().getStringExtra("userid");

        // Firebase 데이터베이스 초기화
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        boardsRef = database.getReference("boards").child(board_seq);

        // 댓글 불러오기
        loadComments(board_seq);

        // 각 댓글에 대한 대댓글 버튼 리스너 설정
        setReplyButtonListeners(board_seq);


        boardsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String crt_dt = dataSnapshot.child("currentDate").getValue(String.class);

                    title_tv.setText(title);
                    content_tv.setText(content);
                    date_tv.setText(crt_dt);
                    Log.v(TAG, "Title: " + title_tv.getText().toString());
                    Log.v(TAG, "Content: " + content_tv.getText().toString());
                    Log.v(TAG, "Date: " + date_tv.getText().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Error fetching board details", databaseError.toException());
            }
        });

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentContent = comment_et.getText().toString().trim();
                if (!commentContent.isEmpty()) {
                    Log.v(TAG, "Attempting to save comment: " + board_seq);
                    saveComment(userid, commentContent, board_seq); // 댓글 저장
                    comment_et.setText(""); // 입력창 초기화
                }
            }
        });

        // 댓글 불러오기
        loadComments(board_seq);
    }

    // 댓글을 Firebase에 저장하는 함수
    private void saveComment(String userid, String content, String board_seq) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("boards")
                .child(board_seq)
                .child("comments"); // comments 노드를 만들어 댓글 저장

        Log.v(TAG, "세이브코맨트 실행");

        String commentKey = commentsRef.push().getKey(); // 댓글에 대한 고유한 키 생성
        if (commentKey != null) {

            String currentTime = getCurrentTimestamp();

            Comment newComment = new Comment(userid, content);

            newComment.setTimestamp(currentTime); // 댓글 작성 시간 추가
            commentsRef.child(commentKey).setValue(newComment) // 댓글 데이터 저장
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.v(TAG, "댓글 저장 성공.");
                            } else {
                                Log.e(TAG, "댓글 저장 실패.", task.getException());
                            }
                        }
                    });
        } else {
            Log.e(TAG, "키 생성에 실패하여 댓글을 저장할 수 없다.");
        }
    }



    // 댓글을 Firebase에서 불러오는 함수
    private void loadComments(String board_seq) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("boards")
                .child(board_seq)
                .child("comments"); // comments 노드에 저장된 댓글 불러오기

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_layout.removeAllViews(); // 기존 댓글 제거

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        comment.setKey(snapshot.getKey());
                        View customView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                        ((TextView) customView.findViewById(R.id.cmt_userid_tv)).setText(comment.getUserid());
                        ((TextView) customView.findViewById(R.id.cmt_content_tv)).setText(comment.getContent());
                        String timestamp = comment.getTimestamp();
                        if (timestamp != null && !timestamp.isEmpty()) {
                            ((TextView) customView.findViewById(R.id.cmt_date_tv)).setText(timestamp);
                        }

                        comment_layout.addView(customView); // 댓글 추가
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Error fetching comments", databaseError.toException());
            }
        });
    }
    // getCurrentTimestamp() 메서드를 사용하여 현재 시간을 문자열로 반환하는 함수
    private String getCurrentTimestamp() {
        // 현재 시간을 가져오는 코드
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    // 각 댓글에 대한 대댓글 버튼 리스너 설정 메서드
    private void setReplyButtonListeners(String board_seq) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("boards")
                .child(board_seq)
                .child("comments");

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_layout.removeAllViews(); // 기존 댓글 제거

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    if (comment != null) {
                        View customView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                        ((TextView) customView.findViewById(R.id.cmt_userid_tv)).setText(comment.getUserid());
                        ((TextView) customView.findViewById(R.id.cmt_content_tv)).setText(comment.getContent());
                        String timestamp = comment.getTimestamp();
                        if (timestamp != null && !timestamp.isEmpty()) {
                            ((TextView) customView.findViewById(R.id.cmt_date_tv)).setText(timestamp);
                        }

                        Button replyButton = customView.findViewById(R.id.reply_button); // 대댓글 버튼 참조
                        replyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.v(TAG, "대댓글 버튼이 눌렸습니다.");
                                String replyContent = replyEditText.getText().toString().trim();
                                if (!replyContent.isEmpty()) {
                                    //saveReply(userid, replyContent, board_seq, comment.getKey()); // 대댓글을 Firebase에 저장
                                    // Clear the EditText after saving the reply
                                    replyEditText.setText("");
                                } else {
                                    // 대댓글 내용이 비어 있을 때 사용자에게 알림을 표시하거나 처리할 내용을 추가할 수 있습니다.
                                }
                            }
                        });

                        comment_layout.addView(customView); // 댓글과 대댓글 버튼 추가
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Error fetching comments", databaseError.toException());
            }
        });
    }
}
