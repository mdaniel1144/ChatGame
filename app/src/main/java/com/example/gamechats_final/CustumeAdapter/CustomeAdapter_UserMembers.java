package com.example.gamechats_final.CustumeAdapter;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View.OnLongClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Fragments.fragment_UserMember;
import com.example.gamechats_final.Interface.AlertDialogBuilder;
import com.example.gamechats_final.Interface.CreateObj;
import com.example.gamechats_final.Interface.Update;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class CustomeAdapter_UserMembers extends RecyclerView.Adapter<CustomeAdapter_UserMembers.MyViewHolder>{
    private ArrayList<User> dataSetUserMembers;
    private ArrayList<User> m_FriendUser;
    public CustomeAdapter_UserMembers(ArrayList<User> i_UserMemberDataSet ,ArrayList<User> i_FriendUser ) {
        this.dataSetUserMembers = i_UserMemberDataSet;
        this.m_FriendUser = i_FriendUser;
    }
    public CustomeAdapter_UserMembers(ArrayList<User> i_UserMemberDataSet ) {
        this.dataSetUserMembers = i_UserMemberDataSet;
        this.m_FriendUser = null;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewUserName;
        public ImageView imageViewUserSrc;
        public TextView textViewTags;
        public ImageButton imageButtonAddFriend;

        private User m_User;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewUserName = itemView.findViewById(R.id.textViewUserName);
            this.imageViewUserSrc = itemView.findViewById(R.id.imageViewUserSrc);
            this.textViewTags = itemView.findViewById(R.id.textViewTags);
            this.imageButtonAddFriend = itemView.findViewById(R.id.imageButtonAddFriend);
        }


        public User GetUser() {return this.m_User;};
        public void SetUser(User i_User) {this.m_User = i_User;};
    }

    @NonNull
    @Override
    public CustomeAdapter_UserMembers.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user, parent, false);
        CustomeAdapter_UserMembers.MyViewHolder myViewHolder = new CustomeAdapter_UserMembers.MyViewHolder(view);

        myViewHolder.imageButtonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((String)myViewHolder.imageButtonAddFriend.getTag()).equals("Add"))
                {
                    Update.AddUserMember(myViewHolder.GetUser());
                    myViewHolder.imageButtonAddFriend.setTag("Remove");
                    myViewHolder.imageButtonAddFriend.setImageResource(R.drawable.ic_remove);
                    ChatActivity.SetAllFriend();

                }
                else
                {
                    Update.RemoveUserMember(myViewHolder.GetUser());
                    myViewHolder.imageButtonAddFriend.setTag("Add");
                    myViewHolder.imageButtonAddFriend.setImageResource(R.drawable.ic_add);
                   // fragment_UserMember.ReloadAdapter();
                }
            }
        });
        myViewHolder.itemView.findViewById(R.id.layoutDialog).setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG , "Fatal Problem");
                if(((String)myViewHolder.imageButtonAddFriend.getTag()).equals("Remove"))
                {
                    String title = "Are you sure, Create Group";
                    AlertDialog dialog = AlertDialogBuilder.builderAlertBuildPrivicyChat(view.getContext() , title , "Create");
                    dialog.show();
                    dialog.getWindow().getDecorView().findViewById(R.id.buttonBuildPrivicyOk).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Add Chat --> Chat ONLY
                            CreateObj.CreateNewChatPraivcy(myViewHolder.GetUser() , ChatActivity.m_UserInfo);
                            dialog.dismiss();
                        }
                    });
                    dialog.getWindow().getDecorView().findViewById(R.id.buttonBuildPrivicyCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                      public void onClick(View v) {
                            dialog.dismiss();
                        }
                   });
                }
                return true;
            }

        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdapter_UserMembers.MyViewHolder holder, int key) {
        holder.textViewUserName.setText(dataSetUserMembers.get(key).GetNickName());
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

        String allTags ="";
        for(Tag tag : dataSetUserMembers.get(key).GetAllTags())
        {
            allTags += tag.GetTagName()+", ";
        }
        holder.textViewTags.setText(allTags);

        holder.imageButtonAddFriend.setTag("Add");
        holder.imageButtonAddFriend.setImageResource(R.drawable.ic_add);
        for (User idFriendUsers : m_FriendUser) {
            if(idFriendUsers.GetUserID().equals(dataSetUserMembers.get(key).GetUserID()))
            {
                holder.imageButtonAddFriend.setTag("Remove");
                holder.imageButtonAddFriend.setImageResource(R.drawable.ic_remove);
                break;
            }
        }
        holder.SetUser(dataSetUserMembers.get(key));
    }

    private void RemoveUserMember()
    {

    }

    @Override
    public int getItemCount() {return dataSetUserMembers.size();}

}
