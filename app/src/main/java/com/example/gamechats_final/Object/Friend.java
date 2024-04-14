package com.example.gamechats_final.Object;

import java.util.ArrayList;

public class Friend {

    protected String m_UserID;
    protected String m_NickName;
    protected String m_ImageSrc;
    protected ArrayList<Tag> m_CategoryTags;
    protected ArrayList<Tag> m_PlatformGameTags;

    public Friend(String i_ID ,String i_Nickname , String i_ImageSrc ,ArrayList<Tag> i_CategoryTag ,ArrayList<Tag> i_PlatformTag)
    {
        this.m_UserID = i_ID;
        this.m_ImageSrc = i_ImageSrc;
        this.m_NickName = i_Nickname;
        this.m_CategoryTags = i_CategoryTag;
        this.m_PlatformGameTags = i_PlatformTag;
    }

    public Friend(String i_ID ,String i_Nickname , String i_ImageSrc)
    {
        this.m_UserID = i_ID;
        this.m_ImageSrc = i_ImageSrc;
        this.m_NickName = i_Nickname;
        this.m_CategoryTags = new ArrayList<>();
        this.m_PlatformGameTags =  new ArrayList<>();
    }

    public String GetUserID(){return this.m_UserID;}
    public String GetNickName(){ return this.m_NickName;}
    public ArrayList<Tag> GetCategoryTags(){return this.m_CategoryTags;}
    public ArrayList<Tag> GetPlatformGameTags(){return this.m_PlatformGameTags;}

    public ArrayList<Tag> GetAllTags(){
        ArrayList<Tag> tags = new ArrayList<>();
        tags.addAll(m_PlatformGameTags);
        tags.addAll(m_CategoryTags);
        return tags;}
    public String GetImageSrc(){ return this.m_ImageSrc;}
    public void SetImageSrc(String i_ImageSrc){this.m_ImageSrc = i_ImageSrc;}
    public void SetNickName(String i_NickName){this.m_NickName = i_NickName;}
    public void SetCategoryTag(ArrayList<Tag> i_CategoryTag){this.m_CategoryTags = i_CategoryTag;}
    public void SetPlatformGameTag(ArrayList<Tag> i_PlatformGameTag){this.m_PlatformGameTags = i_PlatformGameTag;}
}
