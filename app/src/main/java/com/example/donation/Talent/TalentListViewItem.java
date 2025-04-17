package com.example.donation.Talent;

public class TalentListViewItem {
    private String id;
    private String Title;
    private String DateAndTime;
    private String Detail;
    private String UserName;
    private String key;
    private String Activity;
    private String UserImg; // 사용자 이미지
    private String img; // 첨부 이미지
    private String UserNickName;// 사용자 닉네임
    private String region;  // 사용자 지역설정
    public TalentListViewItem() {}

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public String getDateAndTime() {
        return DateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        DateAndTime = dateAndTime;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRegion() {
        return region;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
