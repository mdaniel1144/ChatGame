package com.example.gamechats_final.Object;

import java.util.ArrayList;

public class Tag
{
    private String m_TagID;
    private String m_TagName;

    private String m_Type;

    public Tag(String i_TagID, String i_TagName , String i_Type)
    {
        this.m_TagID = i_TagID;
        this.m_TagName = i_TagName;
        this.m_Type = i_Type;
    }

    public String GetTagID(){return this.m_TagID;}
    public String GetTagName(){
        return this.m_TagName;
    }
    public String GetTagType(){
        return this.m_Type;
    }
}
