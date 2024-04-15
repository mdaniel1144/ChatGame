package com.example.gamechats_final.CustumeAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Fragments.fragment_CreateMenu;
import com.example.gamechats_final.Interface.Storage;
import com.example.gamechats_final.Object.Friend;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomeAdapter_Friend  extends RecyclerView.Adapter<CustomeAdapter_Friend.MyViewHolder>{
    private ArrayList<Friend> dataSetUserMembers;
    private ArrayList<Friend> dataSetFriendSelected;

    public CustomeAdapter_Friend(ArrayList<Friend> i_DataSet , ArrayList<Friend> i_FriendSelected) {
        this.dataSetUserMembers = i_DataSet;
        this.dataSetFriendSelected = i_FriendSelected;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUserName;
        public ImageView imageViewUserSrc;
        public TextView textViewCreateChatPhone;
        public CheckBox checkBoxFriend;
        private Friend m_User;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUserName = itemView.findViewById(R.id.textViewUserName);
            this.imageViewUserSrc = itemView.findViewById(R.id.imageViewUserSrc);
            this.textViewCreateChatPhone = itemView.findViewById(R.id.textViewCreateChatPhone);
            this.checkBoxFriend = itemView.findViewById(R.id.checkBoxFriend);
        }

        public Friend GetUser() {return this.m_User;};
        public void SetUser(Friend i_User){this.m_User = i_User;}

    }

    @NonNull
    @Override
    public CustomeAdapter_Friend.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_friend, parent, false);
        CustomeAdapter_Friend.MyViewHolder myViewHolder = new CustomeAdapter_Friend.MyViewHolder(view);

        myViewHolder.checkBoxFriend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    fragment_CreateMenu.AddOrRemoveFriendOrTag(myViewHolder.GetUser() , null , "Friend" , true);
                }
                else
                {
                    fragment_CreateMenu.AddOrRemoveFriendOrTag(myViewHolder.GetUser() ,null , "Friend", false);
                }
            };
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter_Friend.MyViewHolder holder, int key) {

        holder.textViewUserName.setText(dataSetUserMembers.get(key).GetNickName());
        //holder.textViewCreateChatPhone.setText(dataSetUserMembers.get(key).GetPhone());

        Storage.GetImageFromStorage("User" ,dataSetUserMembers.get(key).GetImageSrc() ,holder.imageViewUserSrc);

        holder.SetUser(dataSetUserMembers.get(key));
        for(Friend friend : dataSetFriendSelected)
        {
            if(friend.GetUserID().equals(dataSetUserMembers.get(key).GetUserID()))
            {
                holder.checkBoxFriend.setChecked(true);
            }
        }

    }


    @Override
    public int getItemCount() {return dataSetUserMembers.size();}
}

