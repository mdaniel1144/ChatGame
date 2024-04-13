package com.example.gamechats_final.Object;

import android.os.Bundle;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chat extends ChatForYou {
    private Bundle m_Users;
    private ArrayList<Message> m_Message;

    private String m_Type;


    public Chat(String i_Id , String i_NameChat){
        super(null , "" , 0 , i_NameChat ,"" , i_Id , new ArrayList<Tag>());
        m_Users = new Bundle();
        m_Message = new ArrayList<>();
        m_Type = "Public";
    }

    public Chat(Timestamp i_DateCreated , String i_ImageSrc , String i_NameChat , String i_Id , String i_Type){
        super(i_DateCreated , "" , 0 , i_NameChat ,"" , i_Id , new ArrayList<Tag>());
        m_Users = new Bundle();
        m_Message = new ArrayList<>();
        m_Type = i_Type;
    }
    public Chat(Timestamp i_DateCreated , String i_ImageSrc , Integer Followers , String i_NameChat , String i_Description , String i_Id , ArrayList<Tag> i_Tags, Bundle i_UserMembers , ArrayList<Message> i_Message )
    {
        super(i_DateCreated , i_ImageSrc ,  Followers ,  i_NameChat , i_Description ,  i_Id , i_Tags);
        this.m_Users = i_UserMembers;
        this.m_Message = new ArrayList<Message>();
        m_Type = "Public";
    }

    public  Bundle GetUsers(){return this.m_Users;}

    public String GetType(){return  this.m_Type;}
    public void SetUser( Bundle i_User){this.m_Users = i_User;}
    public ArrayList<Message> GetMessage(){return this.m_Message;}

    public void SetMessage(ArrayList<Message> i_Message){this.m_Message = i_Message;}


    public String GetTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(m_DateCreated.getSeconds() *1000);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return hour+":"+minute;
    }
    public String GetLastMessage()
    {
        return this.m_Message.get(m_Message.size()).GetSummary();
    }

}
