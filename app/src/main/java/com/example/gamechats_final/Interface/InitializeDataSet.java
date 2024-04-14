package com.example.gamechats_final.Interface;

import static android.content.ContentValues.TAG;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;


import com.example.gamechats_final.Object.Chat;
import com.example.gamechats_final.Object.ChatForYou;
import com.example.gamechats_final.Object.Friend;
import com.example.gamechats_final.Object.Message;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InitializeDataSet {

    private static FirebaseAuth m_Auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore m_database = FirebaseFirestore.getInstance();

    public static FirebaseUser GetAuthCurrentUserFirebase()
    {
        return m_Auth.getCurrentUser();
    }
    public static FirebaseAuth GetAuthFirebase()
    {
        return m_Auth;
    }

    public static Task<ArrayList<User>> GetAllUser() {
        TaskCompletionSource<ArrayList<User>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<User> i_DataSet = new ArrayList<User>();
        ArrayList<Task<ArrayList<Tag>>> tasks = new ArrayList<>();

        m_database.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if(!document.getId().equals(m_Auth.getUid()))
                        {
                        String nickName = document.getString("NickName");
                        String image = document.getString("ImageSrc");
                        Task<ArrayList<Tag>> taskCategoryTag =  GetUserCategoryTag(document.getId());
                        Task<ArrayList<Tag>> taskPlatformTag =  GetUserPlatformGameTag(document.getId());
                        Tasks.whenAllComplete(taskPlatformTag , taskPlatformTag).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                            @Override
                            public void onSuccess(List<Task<?>> tasks) {
                                User user = new User(document.getId(), nickName, image, taskCategoryTag.getResult() , taskPlatformTag.getResult());
                                i_DataSet.add(user);
                            }});
                            tasks.add(taskCategoryTag);
                            tasks.add(taskPlatformTag);
                        }
                    }
                    Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                            taskCompletionSource.setResult(i_DataSet);
                        }
                    });
                    Log.d(TAG , "Get All User: "+ i_DataSet.size());
                }
                else
                    taskCompletionSource.setResult(i_DataSet);
            }
        });


        return taskCompletionSource.getTask();

    }

    public static Task<ArrayList<Friend>> GetUserFriend() {
        TaskCompletionSource<ArrayList<Friend>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Friend> i_DataSet = new ArrayList<Friend>();
        ArrayList<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        m_database.collection("User").document(m_Auth.getUid()).collection("Friend").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Task<DocumentSnapshot> taskGetUserFriend = m_database.collection("User").document(document.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot result = task.getResult();
                                String nickName = result.getString("NickName");
                                String image = result.getString("ImageSrc");
                                //String phone = result.getString("Phone");
                                Friend user = new Friend(document.getId(), nickName, image);
                                i_DataSet.add(user);
                            }
                        });
                        tasks.add(taskGetUserFriend);
                    }
                    Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                            taskCompletionSource.setResult(i_DataSet);
                        }
                    });
                }
                else{
                    taskCompletionSource.setException(task.getException());
                    Log.d(TAG , "Get All User: "+ i_DataSet.size());}
            }
        });
        return taskCompletionSource.getTask();

    }
   public static Task<ArrayList<Tag>> GetAllCategory()
    {
        TaskCompletionSource<ArrayList<Tag>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Tag> i_DataSet = new ArrayList<Tag>();

        m_database.collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        Tag tag = new Tag(document.getId() ,document.getString("Name") , "Category");
                        i_DataSet.add(tag);
                    }
                    taskCompletionSource.setResult(i_DataSet);
                }
                else taskCompletionSource.setException(task.getException());
            }
        });
        return taskCompletionSource.getTask();
    }
    public static Task<ArrayList<Tag>> GetAllPlatformGame()
    {
        TaskCompletionSource<ArrayList<Tag>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Tag> i_DataSet = new ArrayList<Tag>();

        m_database.collection("PlatformGame").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        Tag tag = new Tag(document.getId() ,document.getString("Name") , "PlatformGame");
                        i_DataSet.add(tag);
                    }
                    taskCompletionSource.setResult(i_DataSet);
                }
                else taskCompletionSource.setException(task.getException());
            }
        });
        return taskCompletionSource.getTask();
    }
    public static Task<ArrayList<Message>> GetMessage(String i_IdChatGroup , String i_IdCurrentUser) {
        TaskCompletionSource<ArrayList<Message>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Message>  i_DataSet = new ArrayList<>();

        m_database.collection("ChatGroup").document(i_IdChatGroup).collection("Message").orderBy("DateCreated").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Date tempDate = null;
                    boolean isnewDay = true;
                    boolean isFirstDate = true;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Integer typeSender;
                        String sender = document.getString("SenderName");
                        String senderID = document.getString("SenderID");
                        Date date = document.getDate("DateCreated");
                        String context= document.getString("Context");
                        if(isFirstDate == true) {
                            tempDate = date;
                            isFirstDate = false;
                            //It will show the card_date
                            Message messageNewDate = new Message("" , "" ,date, "" , 2);
                            i_DataSet.add(messageNewDate);
                        }
                        if(tempDate.getDay() != date.getDay())
                            isnewDay = true;
                        else
                            isnewDay = false;

                        if(i_IdCurrentUser.equals(senderID))
                            typeSender = 0;
                        else
                            typeSender = 1;

                        if(isnewDay == true)
                        {
                            //It will show the card_date
                            Message messageNewDate = new Message("" , "" ,date, "" , 2);
                            i_DataSet.add(messageNewDate);
                        }
                        Message message = new Message(sender , senderID ,date, context , typeSender);
                        i_DataSet.add(message);
                        tempDate = date;
                    }
                    taskCompletionSource.setResult(i_DataSet);
                    Log.d(TAG, "Succsesful Get Message: " + i_DataSet.size());
                } else {
                    taskCompletionSource.setException(task.getException());
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        return taskCompletionSource.getTask();
    }

    public static Task<ArrayList<Chat>> GetChatsByCurrentUser()
    {
        TaskCompletionSource<ArrayList<Chat>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Task<Chat>> tasks = new ArrayList<>();

        Task<QuerySnapshot> task_ChatOfUser =  m_database.collection("User").document(m_Auth.getUid()).collection("ChatGroup").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        Task<Chat> getInfoChatTask = GetInfoChat(document.getId());
                        //Task<ArrayList<User>> task_getUser = GetUserMemberOfChat(document.getId());
                        tasks.add(getInfoChatTask);
                    }
                    Tasks.whenAllComplete(tasks).addOnCompleteListener(allTasks -> {
                                ArrayList<Chat> dataSet = new ArrayList<>();
                                for (Task<Chat> getInfoChatTask : tasks) {
                                    if (getInfoChatTask.isSuccessful()) {
                                        dataSet.add(getInfoChatTask.getResult());
                                    }
                                }
                                taskCompletionSource.setResult(dataSet);
                            });
                }
            }
        });

        return taskCompletionSource.getTask();
    }

    public static Task<Chat> GetInfoChat(String i_ChatID)
    {
        TaskCompletionSource<Chat> taskCompletionSource = new TaskCompletionSource<>();

        Task<DocumentSnapshot> taskInfoChat = m_database.collection("ChatGroup").document(i_ChatID).get();
        Task<ArrayList<Tag>> taskTags = GetChatAllTag(i_ChatID);

        Tasks.whenAllComplete(taskInfoChat , taskTags).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        Chat chat;
                        DocumentSnapshot document = taskInfoChat.getResult();               //---->For Public Chat
                        if(document.getString("Type").equals("Public")) {
                            Timestamp date = document.getTimestamp("DateCreated");
                            Integer followers = document.getLong("CountFollower").intValue();
                            String image = document.getString("ImageSrc");
                            String name = document.getString("NameGroup");
                            String description = document.getString("Description");
                            //String type = document.getString("Type");
                            ArrayList<Tag> tags = taskTags.getResult();
                            chat = new Chat(date, image, followers,name ,  description , i_ChatID , tags , null ,null);
                            taskCompletionSource.setResult(chat);
                        }
                        else                                                                 //---->For Praivacy Chat
                        {
                            Timestamp date = document.getTimestamp("DateCreated");
                            String image_1 = document.getString("ImageSrcUserOne");
                            String image_2 = document.getString("ImageSrcUserTwo");
                            String name_1 = document.getString("NameUserOne");
                            String name_2 = document.getString("NameUserTwo");
                            String id_1 = document.getString("IdUserOne");
                            String id_2= document.getString("IdUserTwo");
                            if(!id_1.equals(m_Auth.getUid()))
                                chat = new Chat(date, image_1, name_1  , i_ChatID , "Privacy");
                            else
                                chat = new Chat(date, image_2, name_2  , i_ChatID , "Privacy");
                            taskCompletionSource.setResult(chat);
                        }
                    }
                });
        return taskCompletionSource.getTask();
    }

    public static Task<ArrayList<Tag>> GetChatAllTag(String i_IdCurrentChat) {
        TaskCompletionSource<ArrayList<Tag>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Tag>  i_DataSet = new ArrayList<>();
        //Create Two Task in Firebase;

        Task<QuerySnapshot> task_Category = m_database.collection("ChatGroup").document(i_IdCurrentChat).collection("Category").get();
        Task<QuerySnapshot> task_PlatformGame = m_database.collection("ChatGroup").document(i_IdCurrentChat).collection("PlatformGame").get();

        Tasks.whenAllComplete(task_Category , task_PlatformGame).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task_Category.getResult()) {
                        String name = document.getString("Name");
                        Tag tag = new Tag(document.getId() , name , "Category");
                        i_DataSet.add(tag);
                    }
                    for (QueryDocumentSnapshot document : task_PlatformGame.getResult()) {
                        String name = document.getString("Name");
                        Tag tag = new Tag(document.getId() , name , "PlatformGame");
                        i_DataSet.add(tag);
                    }
                    taskCompletionSource.setResult(i_DataSet);
                    Log.d(TAG, "Successful Get Tags Info Group");
                }
                else {
                    taskCompletionSource.setException(task.getException());
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }});
        return taskCompletionSource.getTask();
    }

    private static Task<ArrayList<User>> GetUserMemberOfChat(String chatID)
    {
        TaskCompletionSource<ArrayList<User>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<User> i_DataSet = new ArrayList<>();

        m_database.collection("ChatGroup").document(chatID).collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = new User(document.getId() ,document.getString("ImageSrc"));
                        i_DataSet.add(user);
                    }
                    taskCompletionSource.setResult(i_DataSet);
                }
            }});

        return taskCompletionSource.getTask();
    }


    public static Task<ArrayList<Tag>> GetUserAllTags(String i_IdCurrentUser) {
        TaskCompletionSource<ArrayList<Tag>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Tag>  i_DataSet = new ArrayList<>();
        //Create Two Task in Firebase;

        Task<QuerySnapshot> task_Category = m_database.collection("User").document(i_IdCurrentUser).collection("Category").get();
        Task<QuerySnapshot> task_PlatformGame = m_database.collection("User").document(i_IdCurrentUser).collection("PlatformGame").get();

        Tasks.whenAllComplete(task_Category , task_PlatformGame).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task_Category.getResult()) {
                                String name = document.getString("Name");
                                i_DataSet.add(new Tag(document.getId() ,name , "Category"));
                            }
                            for (QueryDocumentSnapshot document : task_PlatformGame.getResult()) {
                                String name= document.getString("Name");
                                i_DataSet.add(new Tag(document.getId() ,name , "Category"));
                            }
                            taskCompletionSource.setResult(i_DataSet);
                            Log.d(TAG, "Successful Get Tags Info Group");
                        }
                        else {
                            taskCompletionSource.setException(task.getException());
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                }});
        return taskCompletionSource.getTask();
    }


    public static Task<User> GetCurrentUserInfo()
    {
        TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();

        Task<ArrayList<Tag>>  taskCategory = GetUserCategoryTag(m_Auth.getUid());
        Task<ArrayList<Tag>>  taskPlatformGame = GetUserPlatformGameTag(m_Auth.getUid());
        Task<DocumentSnapshot> task_User =  m_database.collection("User").document(m_Auth.getUid()).get();
        Task<ArrayList<Friend>> task_Friend =  GetUserFriend();
        Task<ArrayList<Chat>> task_Chat =  GetChatsByCurrentUser();
        Tasks.whenAllComplete(taskCategory ,taskPlatformGame , task_User , task_Friend , task_Chat ).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
               @Override
               public void onComplete(@NonNull Task<List<Task<?>>> task) {
                   if(task.isSuccessful()){
                   DocumentSnapshot document = task_User.getResult();
                   String firstName = document.getString("FirstName");
                   String lastName = document.getString("LastName");
                   String nickName = document.getString("NickName");
                   String gender = document.getString("Gender");
                   String phone = document.getString("Phone");
                   String imageSrc = document.getString("ImageSrc");
                   Timestamp birthday = document.getTimestamp("BirthDay");
                   User user = new User(m_Auth.getUid() , firstName , lastName , nickName , phone  ,m_Auth.getCurrentUser().getEmail() , "", imageSrc , taskCategory.getResult() , taskPlatformGame.getResult() ,task_Chat.getResult() ,task_Friend.getResult());
                   taskCompletionSource.setResult(user);
               }
               else {taskCompletionSource.setResult(null);}
               }
        });
        return taskCompletionSource.getTask();
    }

    public static Task<ArrayList<Tag>> GetUserCategoryTag(String i_IdUser) {
        TaskCompletionSource<ArrayList<Tag>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Tag>  i_DataSet = new ArrayList<>();
        //Create Two Task in Firebase;

        m_database.collection("User").document(i_IdUser).collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("Name");
                        Tag tag = new Tag(document.getId() , name , "Category");
                        i_DataSet.add(tag);
                    }
                    taskCompletionSource.setResult(i_DataSet);
                    Log.d(TAG, "Successful Get Tags Info Group");
            }
                else{
                    taskCompletionSource.setException(task.getException());
                Log.d(TAG, "Error getting documents: ", task.getException());
        }}});
        return taskCompletionSource.getTask();
    }

    public static Task<ArrayList<Tag>> GetUserPlatformGameTag(String i_IdUser) {
        TaskCompletionSource<ArrayList<Tag>> taskCompletionSource = new TaskCompletionSource<>();
        ArrayList<Tag>  i_DataSet = new ArrayList<>();
        //Create Two Task in Firebase;

        m_database.collection("User").document(i_IdUser).collection("PlatformGame").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String name = document.getString("Name");
                        Tag tag = new Tag(document.getId() , name , "PlatformGame");
                        i_DataSet.add(tag);
                    }
                    taskCompletionSource.setResult(i_DataSet);
                    Log.d(TAG, "Successful Get Tags Info Group");
                }
                else{
                    taskCompletionSource.setException(task.getException());
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }}});
        return taskCompletionSource.getTask();
    }
}
