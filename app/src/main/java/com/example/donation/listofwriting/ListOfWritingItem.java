package com.example.donation.listofwriting;

public class ListOfWritingItem {
    private String title;
    private String money;
    private String DateAndTime;
    private String Detail;
    private String UserName;
    private int ClickCount;
    private String key;
    private String img; // 첨부 이미지
    private String UserImg; // 사용자 이미지
    private String UserEmailID;      // 사용자 이메일 아이디
    private String id;
    private int Heart;           // 관심수
    private String likedBy;
    private int Receive_Money;
    private String region;  // 사용자 지역설정
    private String UserNickName;// 사용자 닉네임

    public  ListOfWritingItem() {}

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String userNickName) {
        UserNickName = userNickName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getReceive_Money() {return Receive_Money;}
    public void setReceive_Money(int receive_Money) {Receive_Money = receive_Money;}
    public int getClickCount() {return ClickCount;}
    public String getKey() {return key;}
    public String getDetail() {
        return Detail;
    }
    public String getDateAndTime() {
        return DateAndTime;
    }
    public String getTitle() {
        return title;
    }
    public String getMoney() {
        return money;
    }
    public String getUserName() {
        return UserName;
    }
    public String getImg() {
        return img;
    }
    public String getUserEmailID() {
        return UserEmailID;
    }
    public String getId() {
        return id;
    }
    public int getHeart() {
        return Heart;
    }
    public String getLikedBy() {
        return likedBy;
    }
    public String getUserImg() {
        return UserImg;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDetail(String detail) {
        this.Detail = detail;
    }
    public void setDateAndTime(String dateAndTime) {
        this.DateAndTime = dateAndTime;
    }
    public void setUserName(String userName) {
        this.UserName = userName;
    }
    public void setClickCount(int clickCount) {ClickCount = clickCount;}
    public void setKey(String key) {this.key = key;}
    public void setImg(String img) {
        this.img = img;
    }
    public void setUserEmailID(String userEmailID) {
        UserEmailID = userEmailID;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setHeart(int heart) {
        Heart = heart;
    }
    public void setLikedBy(String likedBy) {
        this.likedBy = likedBy;
    }
    public void setUserImg(String userImg) {
        this.UserImg = userImg;
    }
}
