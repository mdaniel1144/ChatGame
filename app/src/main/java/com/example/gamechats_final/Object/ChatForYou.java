package com.example.gamechats_final.Object;

import android.util.Log;
import static android.content.ContentValues.TAG;
import com.google.firebase.Timestamp;


import java.util.ArrayList;
import java.util.Calendar;

public class ChatForYou {

    protected   String m_ID;
    protected Integer m_Followers;
    protected Timestamp m_DateCreated;
    protected String m_ImageSrc;
    protected String m_NameChat;
    protected String m_Description;
    protected ArrayList<Tag> m_Tags;

    public ChatForYou(Timestamp i_DateCreated ,String i_ImageSrc , Integer Followers , String i_NameChat ,String i_Description , String i_Id , ArrayList<Tag> i_Tags)
    {
            this.m_DateCreated = i_DateCreated;
            this.m_ImageSrc = i_ImageSrc;
            this.m_Followers = Followers;
            this.m_NameChat = i_NameChat;
            this.m_ID = i_Id;
            this.m_Description = i_Description;
            this.m_Tags = i_Tags;
    }
    public ChatForYou(String i_NameChat ,String i_Id)
    {
        this.m_DateCreated = null;
        this.m_ImageSrc = "";
        this.m_Followers = 0;
        this.m_NameChat = i_NameChat;
        this.m_ID = i_Id;
        this.m_Description = "";
        this.m_Tags = new ArrayList<Tag>();
    }
        public String GetID(){return this.m_ID;}

        public Integer GetFollowers(){return this.m_Followers;}
        public Timestamp GetDateCreated(){return this.m_DateCreated;}

        public ArrayList<Tag> GetTags(){return this.m_Tags;}
        public String GetImageSrc(){return this.m_ImageSrc;}
        public String GetChatName(){return this.m_NameChat;}
        public  String GetDescription(){return this.m_Description;}

        public String GetTagsAsString(){
            String tags = "";
            if(m_Tags != null) {
                int i = 0;
                for (; i < m_Tags.size() - 1; i++)
                    tags += m_Tags.get(i).GetTagName() + ", ";
                if (m_Tags.size() > 1)
                    tags += m_Tags.get(i).GetTagName();
            }
            return tags;
        }

    public ArrayList<String> GetTagsAsArrayListString(){
        ArrayList<String> tags = new ArrayList<>();
        for ( Tag tag : m_Tags)
            tags.add(tag.GetTagName());
        return tags;
    }

    public String GetDate()
    {
        String date = "";
        if(m_DateCreated!= null) {
            Log.d(TAG, "Fatal - the chat" + GetID());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(m_DateCreated.getSeconds() * 1000);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based, so adding 1
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            date =  day+":"+month+":"+year;
        }
        return date;
    }
}

