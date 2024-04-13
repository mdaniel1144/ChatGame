package com.example.gamechats_final.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Interface.Update;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class fragment_setting_deatils extends Fragment {



    private  EditText m_LastName;
    private  EditText m_FirstName;
    private  EditText m_NickName;
    private  EditText m_Phone;
    private  ChipGroup m_UserChoiceCategory;
    private ChipGroup m_UserChoicePlatformGame;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragment_Setting.GetSettingDialog().getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        fragment_Setting.GetSettingDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        View view = inflater.inflate(R.layout.fragment_setting_deatils, container, false);

        User user = ChatActivity.m_UserInfo;

        m_UserChoiceCategory = view.findViewById(R.id.chipGroupSearchChoiceGroup);
        m_UserChoicePlatformGame = view.findViewById(R.id.chipGroupSettingUserPlatformChoice);

        m_FirstName = ((EditText)view.findViewById(R.id.editTextSettingUserFirstName));
        m_LastName = ((EditText)view.findViewById(R.id.editTextSettingUserLastName));
        m_NickName = ((EditText)view.findViewById(R.id.editTextSettingUserNickName));
        m_Phone = ((EditText)view.findViewById(R.id.editTextSettingUserPhone));

        m_FirstName.setHint(user.GetFirstName());
        m_LastName.setHint(user.GetLastName());
        m_NickName.setHint(user.GetNickName());
        m_Phone.setHint(user.GetPhone());

        AddChipToGroup(ChatActivity.GetAllCategory() ,m_UserChoiceCategory, user.GetCategoryTags() , getActivity());
        AddChipToGroup(ChatActivity.GetAllPlatformGame() ,m_UserChoicePlatformGame, user.GetPlatformGameTags() , getActivity());

        ((TextView)view.findViewById(R.id.buttonSettingPasswordOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickOk();
                Navigation.findNavController(v).navigate(R.id.action_fragment_setting_deatils_to_fragment_SettingMenu);
                fragment_Setting.GetSettingDialog().dismiss();
            }
        });
        ((TextView)view.findViewById(R.id.buttonSettingPasswordCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragment_setting_deatils_to_fragment_SettingMenu);
                fragment_Setting.GetSettingDialog().dismiss();
            }
        });

        return view;
    }

    private static ChipGroup AddChipToGroup(ArrayList<Tag> i_AllTagsGroup, ChipGroup i_UserChoiceTag , ArrayList<Tag> i_TagsUser  , Context i_Context) {

        for (Tag tag : i_AllTagsGroup) {
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
            for(Tag tag1 :i_TagsUser)
                 if(tag1.GetTagName().equals(tag.GetTagName())){
                        groupChat.setChecked(true);
                        break;
           }
            i_UserChoiceTag.addView(groupChat);
        }
        i_UserChoiceTag.setChipSpacing(0);

        return i_UserChoiceTag;
    }

    private void OnClickOk()
    {
        String lastname = m_LastName.getText().toString();
        String firstname = m_FirstName.getText().toString();
        String nickname = m_NickName.getText().toString();
        String phone = m_Phone.getText().toString();
        ArrayList<Tag> categoryTag =new ArrayList<>();
        ArrayList<Tag> platformTag =new ArrayList<>();

        if(lastname.isEmpty())
            lastname = m_LastName.getHint().toString();
        if(firstname.isEmpty())
            firstname = m_FirstName.getHint().toString();
        if(nickname.isEmpty())
            nickname = m_NickName.getHint().toString();
        if(phone.isEmpty())
            phone = m_Phone.getHint().toString();

        for (Integer ids : m_UserChoiceCategory.getCheckedChipIds()) {
            categoryTag.add( new Tag(ids.toString(),((Chip)m_UserChoiceCategory.findViewById(ids)).getText().toString() , "Category"));
        }
        for (Integer ids : m_UserChoicePlatformGame.getCheckedChipIds()) {
            platformTag.add( new Tag(ids.toString(),((Chip)m_UserChoicePlatformGame.findViewById(ids)).getText().toString() , "PlatformGame"));
        }
        User user = new User(firstname.trim() , lastname.trim() , nickname.trim() , phone.trim() , categoryTag , platformTag);
        ChatActivity.UpdateCurrentUser(user);//UpdateOffline
        Update.UpdateProfileCurrentUser(user); //UpdateDatabase
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}





