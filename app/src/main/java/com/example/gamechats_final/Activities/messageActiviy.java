package com.example.gamechats_final.Activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.CustumeAdapter.CustomAdapter_ChatMessage;
import com.example.gamechats_final.Interface.AlertDialogBuilder;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.Message;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class messageActiviy extends AppCompatActivity {


    private ArrayList<Message> m_ChatMessageData;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomAdapter_ChatMessage adapter;
    private Bundle m_InfoChatGroup;
    private ImageView m_ImageProfileChat;
    private EditText m_MessageContent;
    private AlertDialog m_LoadingBox;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        m_InfoChatGroup = getIntent().getBundleExtra("InfoChat");

        m_ImageProfileChat = this.findViewById(R.id.imageViewMessageChatProfile);
        m_MessageContent = this.findViewById(R.id.editTextMessageSend);
        ((TextView)this.findViewById(R.id.editTextMessageChatName)).setText(m_InfoChatGroup.getString("NameGroup"));
        ((ImageButton)this.findViewById(R.id.imageButtonMessageChatClose)).setOnClickListener(v->{
            Intent intent = new Intent(this , ChatActivity.class);
            startActivity(intent);;
        });

        recyclerView =  this.findViewById(R.id.recyclerviewChatMessage);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        this.findViewById(R.id.imageButtonMessageSend).setOnClickListener(v->{
            uploadMessageToChat();
            m_MessageContent.setText("");
        });

        String userID = ChatActivity.m_UserInfo.GetUserID();
        initializeDataSet(m_InfoChatGroup.getString("ID") , userID);
        SetImageChat();

        m_LoadingBox = AlertDialogBuilder.builderAlertLoading(this);
    }

    private void initializeDataSet(String i_IdChat ,String i_IdCurrentUser)
    {
        InitializeDataSet.GetMessage(i_IdChat , i_IdCurrentUser).addOnCompleteListener(new OnCompleteListener<ArrayList<Message>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Message>> task) {
                if(task.isSuccessful()) {
                    m_ChatMessageData = task.getResult();
                    setDataSetOnAdapter();
                    recyclerView.smoothScrollToPosition(m_ChatMessageData.size());
                    if(m_LoadingBox.isShowing())
                    {
                        m_LoadingBox.dismiss();
                    }
                    Log.d(TAG, "Succsesful Add DataSet From Chat Foryou" + m_ChatMessageData.size());
                }
            }
        });
    }

    private void setDataSetOnAdapter() {
        adapter = new CustomAdapter_ChatMessage(m_ChatMessageData  , ChatActivity.m_UserInfo.GetUserID());
        recyclerView.setAdapter(adapter);
    }
    private void SetImageChat()
    {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String path = "ChatGroup/"+ m_InfoChatGroup.getString("ImageSrc");
        StorageReference islandRef = storageRef.child(path);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                m_ImageProfileChat.setImageBitmap(bmp);
                Log.d(TAG ,"Successful Get ProfileImage Chat");
            }
        });
    }

    private void uploadMessageToChat()
    {
        String content = m_MessageContent.getText().toString();
        if(content.isEmpty() == false)
        {
            User user = ChatActivity.m_UserInfo;
            Map<String, Object> docData = new HashMap<>();
            docData.put("SenderName", user.GetNickName());
            docData.put("SenderID", user.GetUserID());
            docData.put("DateCreated", new Timestamp(new Date()));
            docData.put("Context", m_MessageContent.getText().toString());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("ChatGroup").document(m_InfoChatGroup.getString("ID")).collection("Message").add(docData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    String userID =ChatActivity.m_UserInfo.GetUserID();
                    initializeDataSet(m_InfoChatGroup.getString("ID") , userID);
                    Log.d(TAG , "Successful add user message");
                }
            });
            m_LoadingBox.show();
        }
    }
}
