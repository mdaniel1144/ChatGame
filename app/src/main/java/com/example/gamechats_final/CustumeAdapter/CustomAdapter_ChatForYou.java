package com.example.gamechats_final.CustumeAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Interface.AlertDialogBuilder;
import com.example.gamechats_final.Interface.Storage;
import com.example.gamechats_final.Object.ChatForYou;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomAdapter_ChatForYou extends RecyclerView.Adapter<CustomAdapter_ChatForYou.MyViewHolder>{
    private ArrayList<ChatForYou> dataSetChatForYou;
    public CustomAdapter_ChatForYou(ArrayList<ChatForYou> i_ChatDataSet) {this.dataSetChatForYou = i_ChatDataSet;}
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameChat;
        private ImageView imageViewChatSrc;
        private TextView textViewChatFollowers;
        private ChatForYou m_chatForYou;

        public MyViewHolder(View itemView) { //TODO: CardView
            super(itemView);
            this.textViewNameChat = itemView.findViewById(R.id.textViewCardForYouName);
            this.imageViewChatSrc = itemView.findViewById(R.id.imageViewChatForYou);
            this.textViewChatFollowers = itemView.findViewById(R.id.textViewCardForYouMembers);
        }

        public void SetChatForYou(ChatForYou i_ChatForYou){this.m_chatForYou = i_ChatForYou;}
        public ChatForYou GetChatForYou(){return this.m_chatForYou;}

    }

    @NonNull
    @Override
    public CustomAdapter_ChatForYou.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_foryou, parent, false);
        CustomAdapter_ChatForYou.MyViewHolder myViewHolder = new CustomAdapter_ChatForYou.MyViewHolder(view);
        view.setOnClickListener(v->{

             AlertDialogBuilder.builderAlertBuildInfoChat(view.getContext(),  myViewHolder.m_chatForYou);
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_ChatForYou.MyViewHolder holder, int key) {

        holder.SetChatForYou(dataSetChatForYou.get(key));
        holder.textViewNameChat.setText(dataSetChatForYou.get(key).GetChatName());
        holder.textViewChatFollowers.setText(dataSetChatForYou.get(key).GetFollowers() + " Followers");
        Storage.GetImageFromStorage("ChatGroup" ,dataSetChatForYou.get(key).GetImageSrc() ,holder.imageViewChatSrc);
    }

    @Override
    public int getItemCount() {return dataSetChatForYou.size();}

}
