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
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomeAdapter_Friend  extends RecyclerView.Adapter<CustomeAdapter_Friend.MyViewHolder>{
    private ArrayList<User> dataSetUserMembers;
    private ArrayList<User> dataSetFriendSelected;

    public CustomeAdapter_Friend(ArrayList<User> i_DataSet , ArrayList<User> i_FriendSelected) {
        this.dataSetUserMembers = i_DataSet;
        this.dataSetFriendSelected = i_FriendSelected;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUserName;
        public ImageView imageViewUserSrc;
        public TextView textViewCreateChatPhone;
        public CheckBox checkBoxFriend;
        private User m_User;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUserName = itemView.findViewById(R.id.textViewUserName);
            this.imageViewUserSrc = itemView.findViewById(R.id.imageViewUserSrc);
            this.textViewCreateChatPhone = itemView.findViewById(R.id.textViewCreateChatPhone);
            this.checkBoxFriend = itemView.findViewById(R.id.checkBoxFriend);
        }

        public User GetUser() {return this.m_User;};
        public void SetUser(User i_User){this.m_User = i_User;}

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
        holder.textViewCreateChatPhone.setText(dataSetUserMembers.get(key).GetPhone());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String path = "User/"+ dataSetUserMembers.get(key).GetImageSrc();
        StorageReference islandRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imageViewUserSrc.setImageBitmap(bmp);
            }
        });

        holder.SetUser(dataSetUserMembers.get(key));
        for(User friend : dataSetFriendSelected)
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

