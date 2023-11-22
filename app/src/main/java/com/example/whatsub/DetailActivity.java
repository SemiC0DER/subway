package com.example.whatsub;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reply, null);
        replyEditText = dialogView.findViewById(R.id.reply_edit_text); // 대댓글 입력

        board_seq = getIntent().getStringExtra("board_seq");
        userid = getIntent().getStringExtra("userid");

        // Firebase 데이터베이스 초기화
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        boardsRef = database.getReference("boards").child(board_seq);

        // 댓글 불러오기
        loadComments(board_seq);

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

        Button reg_button = findViewById(R.id.reg_button);
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
    }

    private void saveComment(String userid, String content, String board_seq) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("boards")
                .child(board_seq)
                .child("comments"); // comments 노드를 만들어 댓글 저장

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

    // 댓글을 Firebase에서 불러오는 함수 (댓글과 대댓글을 모두 로드하도록 수정)
    private void loadComments(String board_seq) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("boards")
                .child(board_seq)
                .child("comments"); // comments 노드에 저장된 댓글과 대댓글 불러오기

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment_layout.removeAllViews(); // 기존 댓글 제거

                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    if (comment != null) {
                        comment.setKey(commentSnapshot.getKey());
                        View commentView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                        ((TextView) commentView.findViewById(R.id.cmt_userid_tv)).setText(comment.getUserid());
                        ((TextView) commentView.findViewById(R.id.cmt_content_tv)).setText(comment.getContent());
                        String timestamp = comment.getTimestamp();
                        if (timestamp != null && !timestamp.isEmpty()) {
                            ((TextView) commentView.findViewById(R.id.cmt_date_tv)).setText(timestamp);
                        }

                        comment_layout.addView(commentView); // 댓글 추가

                        // 대댓글 로드하는 메서드 호출
                        loadReplies(board_seq, comment.getKey(), (LinearLayout) commentView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Error fetching comments", databaseError.toException());
            }
        });
    }

    private void setupReplyButton(View customView, String commentKey) {
        Button replyButton = customView.findViewById(R.id.reply_button); // 대댓글 버튼 참조
        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "대댓글 버튼이 눌렸습니다.");
                showReplyDialog(commentKey); // 대댓글 다이얼로그 표시
            }
        });
    }


    private String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    // 대댓글 입력 다이얼로그 표시
    // 대댓글 입력 다이얼로그 표시
    private void showReplyDialog(String commentKey) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reply, null);
        dialogBuilder.setView(dialogView);

        EditText replyEditText = dialogView.findViewById(R.id.reply_edit_text);
        Button confirmButton = dialogView.findViewById(R.id.confirm_button);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyContent = replyEditText.getText().toString().trim();
                if (!replyContent.isEmpty()) {
                    saveReply(commentKey, replyContent, dialog); // 대댓글을 Firebase에 저장
                } else {
                    replyEditText.setError("대댓글 내용을 입력하세요.");
                }
            }
        });
    }


    // 대댓글을 Firebase에 저장하는 함수
    // 대댓글을 Firebase에 저장하는 함수
    private void saveReply(String commentKey, String replyContent, AlertDialog dialog) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference commentsRef = database.getReference("boards")
                .child(board_seq)
                .child("comments")
                .child(commentKey)
                .child("replies"); // 대댓글 저장 위치

        String replyKey = commentsRef.push().getKey(); // 대댓글에 대한 고유한 키 생성
        if (replyKey != null) {
            String currentTime = getCurrentTimestamp();
            Reply newReply = new Reply(userid, replyContent);
            newReply.setTimestamp(currentTime); // 대댓글 작성 시간 추가

            commentsRef.child(replyKey).setValue(newReply) // 대댓글 데이터 저장
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.v(TAG, "대댓글 저장 성공.");
                                // 대댓글을 Firebase에 저장한 후 화면을 갱신하여 대댓글을 표시하는 메서드 호출
                                loadReplies(board_seq, commentKey, comment_layout); // loadReplies 메서드를 호출하여 대댓글 표시
                                dialog.dismiss(); // 다이얼로그 닫기
                            } else {
                                Log.e(TAG, "대댓글 저장 실패.", task.getException());
                            }
                        }
                    });
        } else {
            Log.e(TAG, "키 생성에 실패하여 대댓글을 저장할 수 없다.");
        }
    }


    // 대댓글을 Firebase에서 불러오는 함수
    private void loadReplies(String board_seq, String commentKey, LinearLayout commentLayout) {
        DatabaseReference repliesRef = FirebaseDatabase.getInstance().getReference("boards")
                .child(board_seq)
                .child("comments")
                .child(commentKey)
                .child("replies"); // 대댓글 노드에 저장된 대댓글 불러오기

        repliesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout replyLayout = new LinearLayout(DetailActivity.this);
                replyLayout.setOrientation(LinearLayout.VERTICAL);

                commentLayout.addView(replyLayout); // 새로운 LinearLayout을 댓글 레이아웃에 추가

                for (DataSnapshot replySnapshot : dataSnapshot.getChildren()) {
                    Reply reply = replySnapshot.getValue(Reply.class);
                    if (reply != null) {
                        // 대댓글 레이아웃을 인플레이트하여 작성자의 아이디를 설정하는 부분
                        View replyView = getLayoutInflater().inflate(R.layout.custom_comment, null);
                        ((TextView) replyView.findViewById(R.id.reply_userid_tv)).setText(reply.getUserid()); // 대댓글 작성자의 아이디 설정
                        ((TextView) replyView.findViewById(R.id.reply_content_tv)).setText(reply.getContent());
                        String timestamp = reply.getTimestamp();
                        if (timestamp != null && !timestamp.isEmpty()) {
                            ((TextView) replyView.findViewById(R.id.reply_date_tv)).setText(timestamp);
                        }

                        replyLayout.addView(replyView); // 대댓글 추가
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("DetailActivity", "Error fetching replies", databaseError.toException());
            }
        });
    }


}
