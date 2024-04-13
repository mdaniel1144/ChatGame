package com.example.gamechats_final.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.CustumeAdapter.CustomeAdapter_UserMembers;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.ChatForYou;
import com.example.gamechats_final.Object.Enums;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class fragment_UserMember extends Fragment {


    private static ArrayList<User> m_UserMember;
    private static ArrayList<User> m_UserMemberDataSearch;
    private RadioGroup radioGroupMenu;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomeAdapter_UserMembers adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_user_member, container, false);

        ChatActivity.JumpToPage = Enums.FragmentType.UserMember;
        radioGroupMenu = view.findViewById(R.id.radioGroupSort);

        recyclerView =  view.findViewById(R.id.recylcerViewUserMembers);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initializeDataSet();

        radioGroupMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioButtonFriend)
                {
                    boolean isMatch = false;
                    ArrayList<User> friend = ChatActivity.m_UserInfo.GetFriend();
                    makeSearch();
                    if(!friend.isEmpty()) {
                        for (int i = 0; i < m_UserMemberDataSearch.size(); i++){
                            for (User freind : friend) {
                                if (m_UserMemberDataSearch.get(i).GetUserID().equals(freind.GetUserID())) {
                                    isMatch = true;
                                    break;
                                }
                            }
                            if(!isMatch)
                                m_UserMemberDataSearch.remove(i--);
                            isMatch = false;
                        }
                        setDataSetOnAdapter();
                    }
                    else{
                        m_UserMemberDataSearch = new ArrayList<>();
                        setDataSetOnAdapter();
                    }
                }
                else
                {
                    m_UserMemberDataSearch = new ArrayList<>(m_UserMember);
                    setDataSetOnAdapter();
                }
            }
        });

        return  view;
    }

    private void initializeDataSet()
    {
        //Bundle m_Search = fragment_search.GetSearchProperty();
        if(ChatActivity.m_Saerch != null) {
            makeSearch();
        }
        else {
            m_UserMember = new ArrayList<>();

            String idCurrentUser = ((ChatActivity)getActivity()).GetUserID();
            InitializeDataSet.GetAllUser().addOnCompleteListener(new OnCompleteListener<ArrayList<User>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<User>> task) {
                    m_UserMember = task.getResult();
                    m_UserMemberDataSearch = new ArrayList<>(m_UserMember);
                    setDataSetOnAdapter();
                }
            });
        }
    }

    private void setDataSetOnAdapter() {
        adapter = new CustomeAdapter_UserMembers(m_UserMemberDataSearch , ChatActivity.m_UserInfo.GetFriend());
        recyclerView.setAdapter(adapter);
    }

    private void makeSearch()
    {
        if(ChatActivity.m_Saerch != null) {
            int countTags = 0;
            m_UserMemberDataSearch = new ArrayList<>(m_UserMember);
            for (int i = 0; i < m_UserMemberDataSearch.size(); i++) {
                User user = m_UserMemberDataSearch.get(i);
                String name = user.GetNickName().toLowerCase();
                String nameSearch = ChatActivity.m_Saerch.getString("Text").toLowerCase().trim();

                for (String tagsSearch : ChatActivity.m_Saerch.getStringArrayList("Tags")) {
                    for (Tag tag : user.GetAllTags()) {
                        if (tagsSearch.equals(tag.GetTagName()))
                            countTags++;
                    }
                }
                if(nameSearch != "" &&  countTags > 0) {
                    if (countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size() && !name.contains(nameSearch)) {
                        m_UserMemberDataSearch.remove(i--);
                    }
                    else if(!name.contains(nameSearch)){
                        m_UserMemberDataSearch.remove(i--);
                    }
                    else if(countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size()){
                        m_UserMemberDataSearch.remove(i--);}
                }
                else if(!nameSearch.isEmpty() && !name.contains(nameSearch)){
                    m_UserMemberDataSearch.remove(i--);
                }
                else if(countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size()){
                    m_UserMemberDataSearch.remove(i--);
                }
                countTags = 0;
            }
            setDataSetOnAdapter();
        }
    }
}