package com.example.gamechats_final.CustumeAdapter;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Activities.messageActiviy;
import com.example.gamechats_final.Fragments.fragment_Chat;
import com.example.gamechats_final.Fragments.fragment_ChatForYou;
import com.example.gamechats_final.Interface.AlertDialogBuilder;
import com.example.gamechats_final.Interface.CreateObj;
import com.example.gamechats_final.Interface.Update;
import com.example.gamechats_final.Object.Chat;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomeAdapter_Chat extends RecyclerView.Adapter<CustomeAdapter_Chat.MyViewHolder>{

    private ArrayList<Chat> dataSetChat;
    public CustomeAdapter_Chat(ArrayList<Chat> i_ChatDataSet) {
        this.dataSetChat = i_ChatDataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNameChat;
        public TextView textViewDateChat;
        public ImageView imageViewChat;
        public TextView textViewLastMessageChat;

        private Chat m_Chat;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewNameChat = itemView.findViewById(R.id.textViewUserName);
            this.textViewDateChat = itemView.findViewById(R.id.textViewTags);
            this.imageViewChat = itemView.findViewById(R.id.imageViewUserSrc);
            this.textViewLastMessageChat = itemView.findViewById(R.id.textViewChatTags);
        }

        public Chat GetChat() {return this.m_Chat;};
        public void SetChat(Chat i_Chat){this.m_Chat = i_Chat;}

    }

    @NonNull
    @Override
    public CustomeAdapter_Chat.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat, parent, false);
        CustomeAdapter_Chat.MyViewHolder myViewHolder = new CustomeAdapter_Chat.MyViewHolder(view);
        view.setOnClickListener(v->{
            Bundle infoGroup = new Bundle();
            infoGroup.putString("UserID" , ChatActivity.m_UserInfo.GetUserID());
            infoGroup.putString("ID" , myViewHolder.GetChat().GetID());
            infoGroup.putString("ImageSrc" , myViewHolder.GetChat().GetImageSrc());
            infoGroup.putString("NameGroup" , myViewHolder.GetChat().GetChatName());
            infoGroup.putBundle("User" , myViewHolder.GetChat().GetUsers());

            Intent intent = new Intent(view.getContext() , messageActiviy.class);
            intent.putExtra("InfoChat", infoGroup); // Replace "key" with your desired key and "value" with the data you want to pass
            view.getContext().startActivity(intent);;
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String title = "Are You Sure You want Delete";
                    AlertDialog dialog = AlertDialogBuilder.builderAlertBuildPrivicyChat(view.getContext() , title , "Delete");
                    dialog.show();
                    dialog.getWindow().getDecorView().findViewById(R.id.buttonBuildPrivicyOk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Delete Chat --> Chat ONLY
                            dialog.getWindow().getDecorView().findViewById(R.id.buttonBuildPrivicyOk).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Add Chat --> Chat ONLY
                                    String idChat = myViewHolder.GetChat().GetID();
                                    Update.DeleteChat(myViewHolder.GetChat());
                                    dialog.dismiss();
                                    fragment_Chat.ReloadAdapter(idChat);
                                 //   fragment_ChatForYou.ReloadAdapter();
                                }
                            });
                            dialog.getWindow().getDecorView().findViewById(R.id.buttonBuildPrivicyCancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                return false;
            }});
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter_Chat.MyViewHolder holder, int key) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String path = "ChatGroup/"+dataSetChat.get(key).GetImageSrc();
        StorageReference islandRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imageViewChat.setImageBitmap(bmp);
            }
        });
        holder.textViewNameChat.setText(dataSetChat.get(key).GetChatName());
        holder.textViewDateChat.setText(dataSetChat.get(key).GetDate());
        holder.textViewLastMessageChat.setText(dataSetChat.get(key).GetTagsAsString());
        holder.SetChat(dataSetChat.get(key));
    }

    @Override
    public int getItemCount() {
        return dataSetChat.size();
    }
}
