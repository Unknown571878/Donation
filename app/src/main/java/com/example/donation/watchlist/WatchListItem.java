package com.example.donation.watchlist;

public class WatchListItem {
        private String title;
        private String money;
        private String DateAndTime;
        private String Detail;
        private String UserName;
        private int ClickCount;
        private String key;
        private String img; // 첨부 이미지
        private String UserEmailID;      // 사용자 이메일 아이디
        private String id;
        private int Heart;           // 관심수
        private String likedBy;

        public  WatchListItem() {}

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
}
