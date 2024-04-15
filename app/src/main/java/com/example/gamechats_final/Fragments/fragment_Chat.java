package com.example.gamechats_final.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.Chat;
import com.example.gamechats_final.CustumeAdapter.CustomeAdapter_Chat;
import com.example.gamechats_final.Object.Enums.MenuOption;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class fragment_Chat extends Fragment {

    private static ArrayList<Chat> m_ChatDataSearch = new ArrayList<>();
    private static ArrayList<Chat> m_ChatData = new ArrayList<>();
    private static RecyclerView recyclerView;
    private  LinearLayoutManager layoutManager;

    private static CustomeAdapter_Chat adapter;

    private Bundle m_UserProperty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatgroup, container, false);
        //m_UserProperty = ((ChatActivity)getActivity()).GetUserProperty();
        ChatActivity.JumpToPage = MenuOption.ChatGroup;

        recyclerView =  view.findViewById(R.id.recylcerViewChatGroup);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initializeChatGroupDataSet();

        return  view;
    }

    private void initializeChatGroupDataSet()
    {
        if(ChatActivity.m_Saerch != null) {
            int countTags = 0;
            m_ChatDataSearch = new ArrayList<>(m_ChatData);
            for (int i = 0; i < m_ChatDataSearch.size(); i++) {
                Chat chat = m_ChatDataSearch.get(i);
                String name = chat.GetChatName().toLowerCase();
                String nameSearch = ChatActivity.m_Saerch.getString("Text").toLowerCase().trim();

                for (String tagsSearch : ChatActivity.m_Saerch.getStringArrayList("Tags")) {
                    for (Tag tag : chat.GetTags()) {
                        if (tagsSearch.equals(tag.GetTagName()))
                            countTags++;
                    }
                }
                if(nameSearch != "" &&  countTags > 0) {
                    if (countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size() && !name.contains(nameSearch)) {
                        m_ChatDataSearch.remove(i--);
                    }
                    else if(!name.contains(nameSearch)){
                        m_ChatDataSearch.remove(i--);
                    }
                    else if(countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size()){
                        m_ChatDataSearch.remove(i--);}
                }
                else if(!nameSearch.isEmpty() && !name.contains(nameSearch)){
                    m_ChatDataSearch.remove(i--);
                }
                else if(countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size()){
                    m_ChatDataSearch.remove(i--);
                }
                countTags = 0;
            }
            setDataSetOnAdapter();
        }
        else {
            InitializeDataSet.GetChatsByCurrentUser().addOnCompleteListener(new OnCompleteListener<ArrayList<Chat>>() {
                @Override
                public void onComplete(@NonNull Task<ArrayList<Chat>> task) {
                    if (task.isSuccessful()) {
                        m_ChatData = task.getResult();
                        m_ChatDataSearch = new ArrayList<>(m_ChatData);
                        setDataSetOnAdapter();
                        Log.d(TAG, "Succsesful add DataSet From Chat" + m_ChatData.size());
                    } else
                        Log.d(TAG, "Error getting documents: ", task.getException());
                }
            });
        }
    }

    private static void setDataSetOnAdapter() {
        adapter = new CustomeAdapter_Chat(m_ChatDataSearch);
        recyclerView.setAdapter(adapter);
    }

    public static void ReloadAdapter(String i_IdChat) {

        for(int i=0 ; i<m_ChatDataSearch.size() ; i++)
        {
            Chat chat = m_ChatDataSearch.get(i);
            if(chat.GetID().equals(i_IdChat)){
                m_ChatDataSearch.remove(i);
                break;
            }
        }
        setDataSetOnAdapter();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}