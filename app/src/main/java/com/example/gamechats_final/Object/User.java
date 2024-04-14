package com.example.gamechats_final.Object;

import java.util.ArrayList;

public class User extends Friend {

    private String m_UserMail;
    private String m_UserLastName;
    private String m_UserFirstName;
    private String m_Password;
    private String m_Phone;
    private String m_Gender;
    protected ArrayList<Friend> m_Friend;
    protected ArrayList<Chat> m_Chat;

    public User()
    {
        super( "" , "" , "", new ArrayList<>()  , new ArrayList<>() );
        this.m_UserLastName = "";
        this.m_UserFirstName = "";
        this.m_UserMail = "";
        this.m_Password = "";
        this.m_Phone = "";
        this.m_Friend =  new ArrayList<>() ;;
        this.m_Chat =  new ArrayList<>() ;;
    }


    public User(String i_ID ,String i_Nickname ,String i_ImageSrc,ArrayList<Tag> i_CategoryTag ,ArrayList<Tag> i_PlatformTag){
        super( i_ID , i_Nickname , i_ImageSrc, i_CategoryTag , i_PlatformTag);
        this.m_UserLastName = "";
        this.m_UserFirstName = "";
        this.m_UserMail = "";
        this.m_Password = "";
        this.m_Phone = "";
        this.m_Friend = new ArrayList<>() ;
        this.m_Chat = new ArrayList<>() ;
    }

    public User(String i_ID, String i_FirstName , String i_LastName, String i_NickName ,String i_Phone , String i_Mail , String i_Password , String i_ImageSrc , ArrayList<Tag> i_CategoryTag , ArrayList<Tag> i_PlatformGameTag , ArrayList<Chat> i_Chat ,ArrayList<Friend> i_Friend)
    {
        super( i_ID , i_NickName , i_ImageSrc, i_CategoryTag , i_PlatformGameTag);
        //For Register
        this.m_UserLastName = i_LastName;
        this.m_UserFirstName = i_FirstName;
        this.m_UserMail = i_Mail;
        this.m_Password = i_Password;
        this.m_Phone = i_Phone;
        this.m_Friend = i_Friend;
        this.m_Chat = i_Chat;
    }

    public User(String i_ID , String i_NickName ,String i_ImageSrc , String i_Phone)
    {
        super( i_ID , i_NickName , i_ImageSrc, new ArrayList<>() , new ArrayList<>());
        //For CreateGroup
        this.m_UserLastName = "";
        this.m_UserFirstName = "";
        this.m_UserMail = "";
        this.m_Password = "";
        this.m_Phone = i_Phone;
        this.m_Friend =  new ArrayList<>();
        this.m_Chat = new ArrayList<>() ;
    }

    public User(String firstname ,String lastname ,String i_NickName ,String  phone ,ArrayList<Tag> i_CategoryTag ,ArrayList<Tag> i_PlatformGameTag)
    {
        super( "" , i_NickName , "",i_CategoryTag , i_PlatformGameTag);
        //For UpdateProfile
        this.m_UserLastName = lastname;
        this.m_UserFirstName = firstname;
        this.m_UserMail = "";
        this.m_Password = "";
        this.m_Phone = phone;
        this.m_Friend =  new ArrayList<>() ;
        this.m_Chat = new ArrayList<>() ;
    }



    public User(String i_UserID , String i_ImageSrc)
    {
        super( i_UserID , "" , i_ImageSrc,new ArrayList<>() , new ArrayList<>());
        //For CreateGroup
        this.m_UserID = i_UserID;
        this.m_UserMail = null;
        this.m_Password = null;
        this.m_Phone = null;
        this.m_Friend =  new ArrayList<>() ;
        this.m_Chat = new ArrayList<>() ;
    }

    public String GetUserID(){return this.m_UserID;}
    public String GetFullName(){
        return this.m_UserFirstName + " " + this.m_UserLastName;
    }

    public String GetFirstName(){ return this.m_UserFirstName;}

    public String GetNickName(){ return this.m_NickName;}
    public ArrayList<Tag> GetCategoryTags(){return this.m_CategoryTags;}
    public ArrayList<Tag> GetPlatformGameTags(){return this.m_PlatformGameTags;}

    public ArrayList<Tag> GetAllTags(){
        ArrayList<Tag> tags = new ArrayList<>();
        tags.addAll(m_PlatformGameTags);
        tags.addAll(m_CategoryTags);
        return tags;}
    public ArrayList<Friend> GetFriend(){return this.m_Friend;}
    public void SetFriend(ArrayList<Friend> i_Friend){this.m_Friend = i_Friend;}
    public String GetLastName(){ return this.m_UserLastName;}
    public String GetPhone(){ return this.m_Phone;}


    public String GetImageSrc(){ return this.m_ImageSrc;}
    public void SetImageSrc(String i_ImageSrc){this.m_ImageSrc = i_ImageSrc;}
    public void SetLastName(String i_ImageSrc){this.m_UserLastName = i_ImageSrc;}
    public void SetFirstName(String i_FirstName){this.m_UserFirstName = i_FirstName;}
    public void SetNickName(String i_NickName){this.m_NickName = i_NickName;}
    public void SetPhone(String i_Phone){this.m_Phone = i_Phone;}
    public void SetCategoryTag(ArrayList<Tag> i_CategoryTag){this.m_CategoryTags = i_CategoryTag;}
    public void SetPlatformGameTag(ArrayList<Tag> i_PlatformGameTag){this.m_PlatformGameTags = i_PlatformGameTag;}
    public String GetPassword(){
        return m_Password;
    }

    public String GetMail(){
        return m_UserMail;
    }

    public ArrayList<Chat> GetChat(){
        return m_Chat;
    }


}
