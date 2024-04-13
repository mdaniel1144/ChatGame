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

        public TextView textViewNameChat;
        public ImageView imageViewChatSrc;
        public TextView textViewChatFollowers;

        private ChatForYou m_chatForYou;

        private String m_ID;
        private String m_Descriptiopn;
        private String m_DateCreated;
        private String m_ImageSrc;
        private ArrayList<String> m_Tags;

        public MyViewHolder(View itemView) { //TODO: CardView
            super(itemView);
            this.textViewNameChat = itemView.findViewById(R.id.textViewCardForYouName);
            this.imageViewChatSrc = itemView.findViewById(R.id.imageViewChatForYou);
            this.textViewChatFollowers = itemView.findViewById(R.id.textViewCardForYouMembers);
        }

        public void SetChatForYou(ChatForYou i_ChatForYou){this.m_chatForYou = i_ChatForYou;}
        public void SetDateCreated(String i_DateCreated){this.m_DateCreated = i_DateCreated;}


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

        holder.textViewNameChat.setText(dataSetChatForYou.get(key).GetChatName());
        holder.textViewChatFollowers.setText(dataSetChatForYou.get(key).GetFollowers() + " Followers");

        holder.SetDateCreated(dataSetChatForYou.get(key).GetDate());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String path = "ChatGroup/"+dataSetChatForYou.get(key).GetImageSrc();
        StorageReference islandRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imageViewChatSrc.setImageBitmap(bmp);
            }
        });

        holder.SetChatForYou(dataSetChatForYou.get(key));
    }

    @Override
    public int getItemCount() {return dataSetChatForYou.size();}

}
