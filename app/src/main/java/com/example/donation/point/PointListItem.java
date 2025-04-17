package com.example.donation.point;

public class PointListItem {
    private String Title;
    private String Point;
    private String Time;
    private String PostId;
    public PointListItem () {}
    public String getPostId() {return PostId;}
    public void setPostId(String postId) {PostId = postId;}
    public String getTitle() {return Title;}
    public void setTitle(String title) {Title = title;}
    public String getPoint() {return Point;}
    public void setPoint(String point) {Point = point;}
    public String getTime() {return Time;}
    public void setTime(String time) {Time = time;}
}
