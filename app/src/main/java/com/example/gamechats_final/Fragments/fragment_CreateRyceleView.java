package com.example.gamechats_final.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.CustumeAdapter.CustomeAdapter_Friend;
import com.example.gamechats_final.CustumeAdapter.CustomeAdpter_Tags;
import com.example.gamechats_final.Object.Friend;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_CreateRyceleView extends Fragment {
    private ArrayList<Friend> m_friend = fragment_CreateMenu.m_friend;
    private ArrayList<Tag> m_Category= fragment_CreateMenu.m_Category;
    private ArrayList<Tag> m_PlatformGame= fragment_CreateMenu.m_PlatformGame;

    private Bundle JumpToPage;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomeAdapter_Friend adapterFriend;

    private CustomeAdpter_Tags adapterTags;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.layout_create, container, false);

        JumpToPage = getArguments();

        recyclerView =  view.findViewById(R.id.recyclerviewCreateGroup);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        ((TextView)view.findViewById(R.id.textViewCreateName)).setText("Choose "+JumpToPage.getString("Page"));
        if(!JumpToPage.getString("Page").isEmpty()) {
            if (JumpToPage.getString("Page") == "Friend") {
                adapterFriend = new CustomeAdapter_Friend(m_friend ,(ArrayList<Friend>)fragment_CreateMenu.GetInfoCreateGroupCopy().get("Friend"));
                recyclerView.setAdapter(adapterFriend);
            }
            if (JumpToPage.getString("Page") == "Category") {
                adapterTags = new CustomeAdpter_Tags(m_Category ,(ArrayList<Tag>) fragment_CreateMenu.GetInfoCreateGroupCopy().get("Category"));
                recyclerView.setAdapter(adapterTags);
            }
            if (JumpToPage.getString("Page") == "PlatformGame") {
                adapterTags = new CustomeAdpter_Tags(m_PlatformGame , (ArrayList<Tag>)fragment_CreateMenu.GetInfoCreateGroupCopy().get("PlatformGame"));
                recyclerView.setAdapter(adapterTags);
            }
        }

        view.findViewById(R.id.imageButtonCreateBackToMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_CreateMenu.m_InfoCreateGroupCopy = fragment_CreateMenu.m_InfoCreateGroup;
                Navigation.findNavController(v).navigate(R.id.action_fragment_CreateRyceleView_to_fragment_CreateMenu);
            }
        });

        return view;
    }
}
