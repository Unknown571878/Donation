package com.example.donation.Talent;

public class Talent_Writing_Info {
    private String UserId;      // 사용자 토큰 아이디
    private String id;
    private String Title;
    private String UserNickName;
    private String Detail;
    private String DateAndTime;
    private String UserName;    // 사용자 이름
    private String UserEmailID;      // 사용자 이메일 아이디
    private int Heart;           // 관심수
    private int ClickCount;
    private String key;
    private String Activity;
    private String UserImg; // 사용자 이미지
    private String img; // 첨부 이미지
    private String region;  // 사용자 지역설정
    public Talent_Writing_Info() {}

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getDateAndTime() {
        return DateAndTime;
    }

    public void setUserImg(String userImg) {
        UserImg = userImg;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUserImg() {
        return UserImg;
    }

    public String getImg() {
        return img;
    }

    public void setDateAndTime(String dateAndTime) {
        DateAndTime = dateAndTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserEmailID() {
        return UserEmailID;
    }

    public void setUserEmailID(String userEmailID) {
        UserEmailID = userEmailID;
    }

    public int getHeart() {
        return Heart;
    }

    public void setHeart(int heart) {
        Heart = heart;
    }

    public int getClickCount() {
        return ClickCount;
    }

    public void setClickCount(int clickCount) {
        ClickCount = clickCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
}
