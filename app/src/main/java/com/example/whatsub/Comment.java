package com.example.whatsub;

public class Comment {
    private String userId;
    private String content;
    private String boardSeq;

    public Comment() {
        // Firebase에서 객체를 가져올 때 기본 생성자가 필요합니다.
    }

    public Comment(String userId, String content, String boardSeq) {
        this.userId = userId;
        this.content = content;
        this.boardSeq = boardSeq;
    }

    // Getter와 Setter 메서드
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBoardSeq() {
        return boardSeq;
    }

    public void setBoardSeq(String boardSeq) {
        this.boardSeq = boardSeq;
    }
}
