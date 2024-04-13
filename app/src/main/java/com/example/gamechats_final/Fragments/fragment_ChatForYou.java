package com.example.gamechats_final.Fragments;

import static android.content.ContentValues.TAG;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.Chat;
import com.example.gamechats_final.Object.ChatForYou;
import com.example.gamechats_final.CustumeAdapter.CustomAdapter_ChatForYou;
import com.example.gamechats_final.Object.Enums;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;

public class fragment_ChatForYou extends Fragment {

    private static ArrayList<ChatForYou> m_ChatForYouData = new ArrayList<>();
    private static ArrayList<ChatForYou> m_ChatForYouDataSearch = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private CustomAdapter_ChatForYou adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat_for_you, container, false);
        recyclerView = view.findViewById(R.id.recycler_viewChatForYou);
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ChatActivity.JumpToPage = Enums.FragmentType.ChatForYou;

        initializeDataSet();
        return view;
    }

    private void initializeDataSet() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(ChatActivity.m_Saerch != null) {
            int countTags = 0;
            m_ChatForYouDataSearch = new ArrayList<>(m_ChatForYouData);
            for (int i = 0; i < m_ChatForYouDataSearch.size(); i++) {
                ChatForYou chat = m_ChatForYouDataSearch.get(i);
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
                        m_ChatForYouDataSearch.remove(i--);
                    }
                    else if(!name.contains(nameSearch)){
                        m_ChatForYouDataSearch.remove(i--);
                    }
                    else if(countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size()){
                        m_ChatForYouDataSearch.remove(i--);}
                }
                else if(!nameSearch.isEmpty() && !name.contains(nameSearch)){
                    m_ChatForYouDataSearch.remove(i--);
                }
                else if(countTags != ChatActivity.m_Saerch.getStringArrayList("Tags").size()){
                    m_ChatForYouDataSearch.remove(i--);
                }
                countTags = 0;
            }
            setDataSetOnAdapter(m_ChatForYouDataSearch);
        }
        else {
            m_ChatForYouData = new ArrayList<ChatForYou>();
            // Access a Cloud Firestore instance from your Activity
            //Get All Public Group
            db.collection("ChatGroup").orderBy("CountFollower", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Timestamp date = document.getTimestamp("DateCreated");
                            Integer followers = new Integer(document.get("CountFollower").toString());
                            String image = document.getString("ImageSrc");
                            String name = document.getString("NameGroup");
                            String description = document.getString("Description");
                            String type = document.getString("Type");
                            if (type.equals("Public")){
                                ArrayList<Tag> tags = new ArrayList<Tag>();
                                ChatForYou chatForYou = new ChatForYou(date, image, followers, name, description, document.getId(), tags);
                                AddTagsChat(tags, document.getId());
                                m_ChatForYouData.add(chatForYou);
                            }
                        }
                        setDataSetOnAdapter(m_ChatForYouData);
                        m_ChatForYouDataSearch = new ArrayList<>(m_ChatForYouData);
                        Log.d(TAG, "Succsesful add DataSet From Chat Foryou" + m_ChatForYouData.size());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }


    private void AddTagsChat(ArrayList<Tag> tags , String i_ChatID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ChatGroup").document(i_ChatID).collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name= document.get("Name").toString();
                        Tag tag = new Tag(document.getId() , name ,"Category");
                        tags.add(tag);
                    }
                    Log.d(TAG, "Successful Get Categroy Info Group");
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }});
        db.collection("ChatGroup").document(i_ChatID).collection("PlatformGame").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name= document.get("Name").toString();
                        Tag tag = new Tag(document.getId() , name ,"PlatformGame");
                        tags.add(tag);
                    }
                    Log.d(TAG, "Successful Get PlatformGame Info Group");
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }});
    }

    private void setDataSetOnAdapter(ArrayList<ChatForYou> i_DataSet) {
        adapter = new CustomAdapter_ChatForYou(i_DataSet);
        recyclerView.setAdapter(adapter);
    }

}