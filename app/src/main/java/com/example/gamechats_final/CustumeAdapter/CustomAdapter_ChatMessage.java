package com.example.gamechats_final.CustumeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Object.Enums.MessageType;
import com.example.gamechats_final.Object.Message;
import com.example.gamechats_final.R;


import java.util.ArrayList;

public class CustomAdapter_ChatMessage extends RecyclerView.Adapter<CustomAdapter_ChatMessage.MyViewHolder>{

    private ArrayList<Message> dataSetChat;
    private String m_UserCurrentID;

    public CustomAdapter_ChatMessage(ArrayList<Message> i_MessageDataSet , String i_UserCurrentID) {
        this.dataSetChat = i_MessageDataSet;
        this.m_UserCurrentID = i_UserCurrentID;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewSender;
        private TextView textViewDateCreated;
        private ImageView imageViewChatProfile;
        private TextView textViewMessage;
        private Message m_Message;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageViewChatProfile = itemView.findViewById(R.id.imageViewMessageChatProfile);
            this.textViewSender = itemView.findViewById(R.id.textViewMessageSender);
            this.textViewDateCreated = itemView.findViewById(R.id.textViewMessageCreatedDate);
            this.textViewMessage = itemView.findViewById(R.id.textViewMessageContext);
        }

        public Message GetMessage() {return this.m_Message;};
        public void Message(Message i_Message) {this.m_Message = i_Message;}
    }



    @Override
    public int getItemViewType(int position)
    {
        return  dataSetChat.get(position).GetMessageType().type;
    }

    @NonNull
    @Override
    public CustomAdapter_ChatMessage.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == MessageType.Sender.type)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_sender, parent, false);
        if (viewType == MessageType.Receive.type){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_getter, parent, false);
        }
        if (viewType == MessageType.Date.type) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message_date, parent, false); //TODO:
        }
        CustomAdapter_ChatMessage.MyViewHolder myViewHolder = new CustomAdapter_ChatMessage.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_ChatMessage.MyViewHolder holder, int key) {
        if(holder.getItemViewType() != MessageType.Date.type) {
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
