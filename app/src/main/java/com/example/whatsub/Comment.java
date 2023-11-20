package com.example.whatsub;

public class Comment {
    private String userid;
    private String content;
    private String date;

    public Comment() {
        // 기본 생성자가 필요함 (Firebase의 데이터 읽기/쓰기를 위해)
    }

    public Comment(String userid, String content, String date) {
        this.userid = userid;
        this.content = content;
        this.date = date;
    }

    public Comment(String userid, String content) {
        this.userid = userid;
        this.content = content;
    }

    // Getter 및 Setter 메서드 추가
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
