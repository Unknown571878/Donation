package com.example.donation.login;

/**
 * 사용자 계정 정보 모델 클래스
 */
public class UserAccout {
    private String idToken;     // firebase Uid(고유 토큰 정보)
    private String UserID;      // 이메일 ID
    private String UserPW;      // 페스워드
    private String UserPWck;    // 패스워드 체크
    private String UserName;    // 사용자 이름
    private String UserYear;    // 사용자 년
    private String UserMonth;   // 사용자 월
    private String UserDay;     // 사용자 월
    private String UserEmail;   // 사용자 이메일
    private String UserSex;     // 사용자 성별
    private String UserPhone;   // 사용자 휴대폰
    private String UserNickName;// 사용자 닉네임
    private String UserImg;     // 사용자 이미지
    private String UserLoc;     // 사용자 위치
    private int UserPoint;      // 사용자 포인트
    private String PostNum;
    private String ActivityPostNum;
    private String WatchList;   //최근 본 목록

    public UserAccout(){}
    public String getWatchList() {return WatchList;}
    public void setWatchList(String watchList) {WatchList = watchList;}
    public String getActivityPostNum() {return ActivityPostNum;}
    public void setActivityPostNum(String activityPostNum) {ActivityPostNum = activityPostNum;}
    public String getPostNum() {return PostNum;}
    public void setPostNum(String postNum) {PostNum = postNum;}
    public String getUserID() {
        return UserID;
    }
    public String getUserPW() {
        return UserPW;
    }
    public String getUserPWck() {
        return UserPWck;
    }
    public String getUserName() {
        return UserName;
    }
    public String getUserYear() {
        return UserYear;
    }
    public String getUserMonth() {
        return UserMonth;
    }
    public String getUserDay() {
        return UserDay;
    }
    public String getUserEmail() {
        return UserEmail;
    }
    public String getIdToken() {
        return idToken;
    }
    public String getUserSex() {
        return UserSex;
    }
    public String getUserPhone() {
        return UserPhone;
    }
    public String getUserNickName() {
        return UserNickName;
    }
    public String getUserImg() {
        return UserImg;
    }
    public String getUserLoc() {
        return UserLoc;
    }
    public int getUserPoint() {
        return UserPoint;
    }


    public void setUserID(String userID) {
        this.UserID = userID;
    }
    public void setUserPW(String userPW) {
        this.UserPW = userPW;
    }
    public void setUserPWck(String userPWck) {
        this.UserPW = userPWck;
    }
    public void setUserName(String userName) {
        this.UserName = userName;
    }
    public void setUserYear(String userYear) {
        this.UserYear = userYear;
    }
    public void setUserMonth(String userMonth) {
        this.UserMonth = userMonth;
    }
    public void setUserDay(String userDay) {
        this.UserDay = userDay;
    }
    public void setUserEmail(String userEmail) {
        this.UserEmail = userEmail;
    }
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
    public void setUserSex(String userSex) {
        this.UserSex = userSex;
    }
    public void setUserPhone(String userPhone) {
        this.UserPhone = userPhone;
    }
    public void setUserNickName(String userNickName) {
        this.UserNickName = userNickName;
    }
    public void setUserImg(String userImg) {
        this.UserImg = userImg;
    }
    public void setUserLoc(String userLoc) {
        this.UserLoc = userLoc;
    }
    public void setUserPoint(int userPoint) {
        this.UserPoint = userPoint;
    }


    public UserAccout(String idToken, String UserID, String UserPW, String UserName, String UserYear, String UserMonth,
                      String UserDay, String UserEmail, String UserPhone, String UserSex, String UserNickName, String UserImg,
                      String UserLoc, int UserPoint){
        this.idToken = idToken;
        this.UserID = UserID;
        this.UserPW = UserPW;
        this.UserName = UserName;
        this.UserYear = UserYear;
        this.UserMonth = UserMonth;
        this.UserDay = UserDay;
        this.UserEmail = UserEmail;
        this.UserPhone = UserPhone;
        this.UserSex = UserSex;
        this.UserNickName = UserNickName;
        this.UserImg = UserImg;
        this.UserLoc = UserLoc;
        this.UserPoint = UserPoint;
    }
}
