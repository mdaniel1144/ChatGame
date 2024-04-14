package com.example.gamechats_final.Interface;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Fragments.fragment_ChatForYou;
import com.example.gamechats_final.Fragments.fragment_CreateGroup;
import com.example.gamechats_final.Fragments.fragment_Setting;
import com.example.gamechats_final.Object.Chat;
import com.example.gamechats_final.Object.ChatForYou;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertDialogBuilder {
    private static View view;

    private static AlertDialog alertInfoChat;
    private static AlertDialog alertSetting;
    public static AlertDialog builderAlertSearch(Bundle i_Search , ArrayList<Tag> i_Category , ArrayList<Tag> i_PlatformGame , Context i_Context)
    {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(i_Context);
        LayoutInflater layoutInflater = (LayoutInflater)i_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.layout_search , null);

        ChipGroup m_UserChoiceCategory = view.findViewById(R.id.chipGroupSearchChoiceGroup);
        ChipGroup m_UserChoicePlatformGame = view.findViewById(R.id.chipGroupSearchPlatformChoice);

        if(i_Search != null)
            if(!i_Search.getString("Text").isEmpty())
                ((EditText)view.findViewById(R.id.editTextSettingPasswordNew)).setText(i_Search.getString("Text"));

        AddChipToGroup(i_Search , m_UserChoiceCategory, i_Category ,  i_Context);
        AddChipToGroup(i_Search , m_UserChoicePlatformGame, i_PlatformGame ,  i_Context);

        builder.setView(view);

        AlertDialog alertdialog = builder.create();
        ((TextView)view.findViewById(R.id.buttonSettingPasswordCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.m_Saerch = null;
                alertdialog.dismiss(); // Dismiss the dialog
            }
        });
        ((TextView)view.findViewById(R.id.buttonSettingPasswordOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.m_Saerch = OnClickSearch(view);
                alertdialog.dismiss(); // Dismiss the dialog
            }
        });
        alertdialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        return alertdialog;
    }

    private static ChipGroup AddChipToGroup(Bundle i_Search , ChipGroup i_UserChoiceTag , ArrayList<Tag> i_Tags  ,  Context i_Context) {

        for (Tag tag : i_Tags) {
            Chip groupChat = new Chip(i_Context);
            groupChat.setText(tag.GetTagName());
            groupChat.setCheckable(true);
            groupChat.setTag(tag);
            groupChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        groupChat.setChipIcon(ContextCompat.getDrawable(i_Context, R.drawable.ic_check_circle));
                    else
                        groupChat.setChipIcon(null);
                }
            });
            if(i_Search != null)
                for(String nameTag : i_Search.getStringArrayList("Tags"))
                    if(nameTag.equals(tag.GetTagName())){
                        groupChat.setChecked(true);
                        break;
                    }
            i_UserChoiceTag.addView(groupChat);
        }
        i_UserChoiceTag.setChipSpacing(0);

        return i_UserChoiceTag;
    }

    private static Bundle OnClickSearch(View view ) {

        ChipGroup UserChoiceCategory = view.findViewById(R.id.chipGroupSearchChoiceGroup);
        ChipGroup UserChoicePlatformGame = view.findViewById(R.id.chipGroupSearchPlatformChoice);
        String textSearch = ((TextView)view.findViewById(R.id.editTextSettingPasswordNew)).getText().toString().trim();
        Bundle m_Search = new Bundle();

        boolean isSearchIsNotEmpty = UserChoicePlatformGame.getCheckedChipIds().size() > 0 || UserChoiceCategory.getCheckedChipIds().size() > 0 || !textSearch.isEmpty() ;
        if(isSearchIsNotEmpty) {
            ArrayList<String> Tags = new ArrayList<String>();
            m_Search.putString("Text", textSearch);
            for (Integer ids : UserChoiceCategory.getCheckedChipIds()) {
                Tags.add(((Chip) UserChoiceCategory.findViewById(ids)).getText().toString());
            }
            for (Integer ids : UserChoicePlatformGame.getCheckedChipIds()) {
                Tags.add(((Chip) UserChoicePlatformGame.findViewById(ids)).getText().toString());
            }
            m_Search.putStringArrayList("Tags", Tags);
        }
        else
            m_Search = null;

        return m_Search;
    }



    public static AlertDialog builderAlertSetting(Context i_Context)
    {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(i_Context);
        LayoutInflater layoutInflater = (LayoutInflater)i_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_setting , null);
        builder.setView(view);

        view.findViewById(R.id.fragmentContainerViewSetting);

        AlertDialog alertdialog = builder.create();
        fragment_Setting.SetSettingDialog(alertdialog);
        alertdialog.getWindow().setBackgroundDrawableResource(R.color.transparent);

        return alertdialog;
    }

    public static AlertDialog builderAlertLoading(Context i_Context)
    {
        AlertDialog.Builder loadingBox = new android.app.AlertDialog.Builder(i_Context);
        LayoutInflater layoutInflater = (LayoutInflater)i_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.card_loading , null);
        loadingBox.setView(view);
        loadingBox.setCancelable(false);

        AlertDialog alert = loadingBox.create();
        alert.getWindow().setBackgroundDrawableResource(R.color.transparent);

        return alert;
    }

    public static AlertDialog builderAlertBuildPrivicyChat(Context i_Context , String i_Title , String i_Action)
    {
        AlertDialog.Builder loadingBox = new android.app.AlertDialog.Builder(i_Context);
        LayoutInflater layoutInflater = (LayoutInflater)i_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_privicy_chat , null);

        ((TextView)view.findViewById(R.id.textviewBuildPrivicyText)).setText(i_Title);
        ((TextView)view.findViewById(R.id.buttonBuildPrivicyOk)).setText(i_Action);
        loadingBox.setView(view);
        loadingBox.setCancelable(true);

        AlertDialog alert = loadingBox.create();
        alert.getWindow().setBackgroundDrawableResource(R.color.transparent);

        return alert;
    }

    public static AlertDialog builderAlertBuildNewGroup(Context i_Context)
    {
        AlertDialog.Builder loadingBox = new android.app.AlertDialog.Builder(i_Context);
        LayoutInflater layoutInflater = (LayoutInflater)i_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.fragment_creategroup , null);
        loadingBox.setView(view);
        loadingBox.setCancelable(true);

        AlertDialog alert = loadingBox.create();
        alert.getWindow().setBackgroundDrawableResource(R.color.transparent);
        fragment_CreateGroup.SetAlertDIALOG(alert);
        return alert;
    }

    public static void builderAlertBuildInfoChat(Context i_Context , ChatForYou i_Chat)
    {
        AlertDialog.Builder loadingBox = new android.app.AlertDialog.Builder(i_Context);
        LayoutInflater layoutInflater = (LayoutInflater)i_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_info_chatforyou , null);

        ChipGroup chipGroupTags = view.findViewById(R.id.chipGroupCardInfoTags);
        ImageView imageProfile = view.findViewById(R.id.imageViewCardInfoProfile);

        ((TextView)view.findViewById(R.id.textViewCardInfoName)).setText(i_Chat.GetChatName());
        ((TextView)view.findViewById(R.id.textViewCardInfoFollowers)).setText(i_Chat.GetFollowers().toString() + " Followers");
        ((TextView)view.findViewById(R.id.textViewCardInfoDescription)).setText(i_Chat.GetDescription());
        ((TextView)view.findViewById(R.id.textViewCardInfoDate)).setText(i_Chat.GetDate());

        AddTagsChat(chipGroupTags, i_Chat.GetTagsAsArrayListString() , i_Context);
        GetImageChat(imageProfile , i_Chat.GetImageSrc());

        //For offLine Action
        ImageButton addAction =  (ImageButton)view.findViewById(R.id.imageButtonRegisterCancelStepOne);
        TextView textFollower = (TextView)view.findViewById(R.id.textViewCardInfoFollowers);

        if(IsUserMemberInThisGroup(i_Chat.GetID()) == true)
            addAction.setVisibility(View.GONE);

        addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                String userID = ChatActivity.m_UserInfo.GetUserID();
                data.put("Name", i_Chat.GetChatName());

                Map<String, Object> dataChat = new HashMap<>();
                data.put("ImageSrc", ChatActivity.m_UserInfo.GetImageSrc());

                Integer followerNew = new Integer(i_Chat.GetFollowers())+1;
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Task<Void> task_UpdateUserAsMemberInChat =  db.collection("ChatGroup").document(i_Chat.GetID()).collection("User").document(userID).update("CountFollower",followerNew);
                Task<Void> task_UserAddGroup = db.collection("User").document(userID).collection("ChatGroup").document(i_Chat.GetID()).set(data);
                Task<Void> task_UpdateFollower =  db.collection("ChatGroup").document(i_Chat.GetID()).update("CountFollower",followerNew);
                Tasks.whenAllComplete(task_UserAddGroup , task_UpdateFollower , task_UpdateUserAsMemberInChat).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                    @Override
                    public void onSuccess(List<Task<?>> tasks) {
                        Log.d(TAG, "Join to New Group successfully written!");
                        addAction.setVisibility(View.GONE);
                        textFollower.setText(followerNew.toString());
                        Chat chat = new Chat(i_Chat.GetID() , i_Chat.GetChatName());
                        ChatActivity.m_UserInfo.GetChat().add(chat);
                        alertInfoChat.dismiss();
                        fragment_ChatForYou.ReloadAdapter();
                  //      Toast.makeText(i_Context, "Successful add this Group", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ((ImageButton)view.findViewById(R.id.imageButtonCardInfoBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertInfoChat.dismiss();
            }
        });


        loadingBox.setView(view);
        loadingBox.setCancelable(true);

        AlertDialog dialog = loadingBox.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        fragment_CreateGroup.SetAlertDIALOG(dialog);
        alertInfoChat = dialog;
        alertInfoChat.show();
    }

    private static void AddTagsChat(ChipGroup i_Group ,ArrayList<String>  i_Tags, Context i_Context)
    {
        for (String tags : i_Tags) {
            Chip chip_Type = new Chip(i_Context);
            chip_Type.setText(tags);
            chip_Type.setEnabled(false);
            i_Group.addView(chip_Type);
        }
    }

    private static void GetImageChat(ImageView i_Image , String i_ImageSrc)
    {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String path = "ChatGroup/"+i_ImageSrc;
        StorageReference islandRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                i_Image.setImageBitmap(bmp);
            }
        });
    }

    private static boolean IsUserMemberInThisGroup(String i_ChatID)
    {
        boolean isMember = false;

        for (Chat chatGroup : ChatActivity.m_UserInfo.GetChat()) {
            if(chatGroup.GetID().equals(i_ChatID)) {
                isMember = true;
                break;
            }
        }
        return isMember;
    }

}
