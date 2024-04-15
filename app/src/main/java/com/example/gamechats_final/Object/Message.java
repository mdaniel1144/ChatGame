package com.example.gamechats_final.Object;

import java.util.Calendar;
import java.util.Date;
import com.example.gamechats_final.Object.Enums.MessageType;

public class Message {

    private  String m_SenderName;
    private String m_SenderId;
    private Date m_Date;
    private String m_Context;
    private MessageType m_TypeMessage;
    private String m_SenderImageSrc;

    public Message(String i_SenderName , String i_SenderId , Date i_Date, String i_Context , MessageType i_TypeMessage)
    {
        this.m_SenderName = i_SenderName;
        this.m_SenderId = i_SenderId;
        this.m_Date = i_Date;
        this.m_Context = i_Context;
        m_TypeMessage = i_TypeMessage;
    }

    public String GetSenderName() {return this.m_SenderName;}
    public String GetSenderId(){return  this.m_SenderId;}
    public Date GetDateCreated() {return this.m_Date;}

    public String GetDate(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(m_Date);
        return String.format("%02d.%02d.%02d",cal.get(Calendar.DAY_OF_MONTH)+1 ,cal.get(Calendar.MONTH) , cal.get(Calendar.YEAR));}
    public String GetTime(){return String.format("%02d:%02d", this.m_Date.getHours(), this.m_Date.getMinutes() );}
    public String GetContext() {return this.m_Context;}
    public String GetSenderImageSrc(){return  this.m_SenderImageSrc;}

    public MessageType GetMessageType(){return this.m_TypeMessage;}
    public  String GetSummary()
    {
        return  this.m_SenderName +": "+m_Context.substring(0,20);
    }
}
