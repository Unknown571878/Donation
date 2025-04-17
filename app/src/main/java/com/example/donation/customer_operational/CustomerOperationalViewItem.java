package com.example.donation.customer_operational;

public class CustomerOperationalViewItem {
    private String titleStr;
    private String contentStr;

    public void setTitle(String title)
    {
        titleStr = title;
    }
    public void setContent(String content)
    {
        contentStr = content;
    }
    public String getTitle()
    {
        return this.titleStr;
    }

    public String getContent()
    {
        return this.contentStr;
    }
}
