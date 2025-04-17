package com.example.donation.option;

public class OptionListViewItem {
    private int iconDrawable;
    private String titleStr;

    public void setTitle(String title)
    {
        titleStr = title;
    }
    public void setIcon(int icon)
    {
        iconDrawable = icon;
    }

    public int getIcon()
    {
        return this.iconDrawable;
    }
    public String getTitle()
    {
        return this.titleStr;
    }
}
