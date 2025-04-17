package com.example.donation.writing;

import java.util.PrimitiveIterator;

/**
 *  게시글 정보 관리
 */

public class WritingInfo {
    private String UserId;      // 사용자 토큰 아이디
    private String id;
    private String Title;
    private String Money;
    private String Detail;
    private String DateAndTime;
    private String UserName;    // 사용자 이름
    private String UserEmailID;      // 사용자 이메일 아이디
    private int Heart;           // 관심수
    private String key;
    private String img; // 첨부 이미지
    private String UserImg; // 사용자 이미지
    private int Receive_Money;
    private String region;  // 사용자 지역설정
    private String UserNickName;// 사용자 닉네임

    public WritingInfo(){}

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getRegion() {return region;}
    public void setRegion(String region) {this.region = region;}
    public int getReceive_Money() {return Receive_Money;}
    public void setReceive_Money(int receive_Money) {Receive_Money = receive_Money;}

    public String getKey() {return key;}
    public String getUserId() {
        return UserId;
    }
    public String getId(){return id;}
    public String getTitle() {
        return Title;
    }
    public String getDetail() {
        return Detail;
    }
    public String getMoney() {
        return Money;
    }
    public String getDateAndTime() {
        return DateAndTime;
    }
    public String getUserName() {
        return UserName;
    }
    public String getUserEmailID() {
        return UserEmailID;
    }
    public int getHeart() {
        return Heart;
    }
    public String getImg() {
        return img;
    }

    public String getUserImg() {
        return UserImg;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.Title = title;
    }
    public void setDetail(String detail) {
        this.Detail = detail;
    }
    public void setMoney(String money) {
        this.Money = money;
    }
    public void setDateAndTime(String dateAndTime) {
        this.DateAndTime = dateAndTime;
    }
    public void setUserName(String userName) {
        this.UserName = userName;
    }
    public void setUserEmailID(String userEmailID) {
        this.UserEmailID = userEmailID;
    }
    public void setHeart(int heart) {
        this.Heart = heart;
    }
    public void setKey(String key) {this.key = key;}
    public void setImg(String img) {
        this.img = img;
    }

    public void setUserImg(String userImg) {
        UserImg = userImg;
    }
}
