package com.example.donation.Talent_Chat_;

public class ChatData {
    private String msg;
    private String nickname;
    private String Time;
    private String img;
    private String UserId;
    private String key;
    public  ChatData() {}

    public String getTime() {
        return Time;
    }
    public void setTime(String time) {
        Time = time;
    }
    public String getNickname() {return nickname;}
    public void setNickname(String nickname) {this.nickname = nickname;}
    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}
    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    public String getUserId() {
        return UserId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
