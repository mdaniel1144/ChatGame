package com.example.gamechats_final.CustumeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Object.Message;
import com.example.gamechats_final.R;


import java.util.ArrayList;

public class CustomeAdapter_ChatMessage extends RecyclerView.Adapter<CustomeAdapter_ChatMessage.MyViewHolder>{

    private ArrayList<Message> dataSetChat;
    private String m_UserCurrentID;

    private final Integer SENDER_TYPE = 0;
    private final Integer GETTER_TYPE = 1;
    private final Integer Time_Type = 2;
    public CustomeAdapter_ChatMessage(ArrayList<Message> i_MessageDataSet , String i_UserCurrentID) {
        this.dataSetChat = i_MessageDataSet;
        this.m_UserCurrentID = i_UserCurrentID;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSender;
        public TextView textViewDateCreated;
        public ImageView imageViewChatProfile;
        public TextView textViewMessage;
        private String m_UserID;
        private String m_DateCreated;
        private String m_ImageSrc;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageViewChatProfile = itemView.findViewById(R.id.imageViewMessageChatProfile);
            this.textViewSender = itemView.findViewById(R.id.textViewMessageSender);
            this.textViewDateCreated = itemView.findViewById(R.id.textViewMessageCreatedDate);
            this.textViewMessage = itemView.findViewById(R.id.textViewMessageContext);
        }
        public String GetID() {return this.m_UserID;};
        public void SetID(String i_ID){this.m_UserID = i_ID;}
        public String GetDateCreated() {return this.m_DateCreated;};
        public void SetDateCreated(String i_DateCreated){this.m_DateCreated = i_DateCreated;}
        public String GetImageSrc() {return this.m_ImageSrc;}
        public void SetImageSrc(String i_ImageSrc){this.m_ImageSrc = i_ImageSrc;}
    }



    @Override
    public int getItemViewType(int position)
    {
        switch (dataSetChat.get(position).getViewType()) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public CustomeAdapter_ChatMessage.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == SENDER_TYPE)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_sender, parent, false);
        if (viewType == GETTER_TYPE){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_getter, parent, false);
        }
        if (viewType == Time_Type) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_date, parent, false); //TODO:
        }
        CustomeAdapter_ChatMessage.MyViewHolder myViewHolder = new CustomeAdapter_ChatMessage.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter_ChatMessage.MyViewHolder holder, int key) {
        if(holder.getItemViewType() != Time_Type) {
            holder.textViewSender.setText(dataSetChat.get(key).GetSenderName());
            holder.textViewMessage.setText(dataSetChat.get(key).GetContext());
            holder.textViewDateCreated.setText(dataSetChat.get(key).GetTime());
        }
        else
            holder.textViewDateCreated.setText(dataSetChat.get(key).GetDate());
    }

    @Override
    public int getItemCount() {
        return dataSetChat.size();
    }
}
