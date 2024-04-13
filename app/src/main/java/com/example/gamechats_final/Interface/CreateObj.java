package com.example.gamechats_final.Interface;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Object.ChatForYou;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateObj {

    private static FirebaseAuth m_Auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore m_database = FirebaseFirestore.getInstance();
    private static FirebaseStorage m_Storage = FirebaseStorage.getInstance();

    public static Task<Boolean> CreateNewGroup(String i_Name, String i_Description, Timestamp i_DateCreated , ArrayList<User> i_UserMember,ArrayList<Tag> i_PlatformGame , ArrayList<Tag> i_Category , ImageView image) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        String imageSrc = "";
        Map<String, Object> chatGroup = new HashMap<>();
        User me = new User(m_Auth.getUid(), ChatActivity.m_UserInfo.GetImageSrc());
        i_UserMember.add(me);
        chatGroup.put("CountFollower", i_UserMember.size());
        chatGroup.put("DateCreated", i_DateCreated);
        chatGroup.put("ImageSrc", "");
        chatGroup.put("Description", i_Description);
        chatGroup.put("NameGroup", i_Name);
        chatGroup.put("Type", "Public");

        m_database.collection("ChatGroup").add(chatGroup).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        String imageSrc = documentReference.getId().toString() + ".jpg"; //AutoID
                        Task<Void> addImage = m_database.collection("ChatGroup").document(documentReference.getId()).update("ImageSrc", imageSrc);
                        Task<Void> addCategory = CreateTagInChat(documentReference.getId() , i_Category);
                        Task<Void> addPlatform = CreateTagInChat(documentReference.getId() , i_PlatformGame);
                        Task<Void> addUserMember = CreateUserInChat(documentReference.getId() , i_UserMember);
                        Task<Void> addChatToUser = CreateJoinUserToChat( m_Auth.getUid() , new ChatForYou(i_Name , documentReference.getId()));
                        Task<Void> uploadImage = Storage.UploadImage("ChatGroup" , documentReference.getId()+".jpg" , image);
                        if(Tasks.whenAllComplete(addImage , addCategory ,addPlatform  , addUserMember , addChatToUser , uploadImage).isSuccessful())
                            taskCompletionSource.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setResult(false);
                    }
                });
        return taskCompletionSource.getTask();
    }

    public static Task<Void> CreateTagInChat(String i_ChatID ,ArrayList<Tag> i_Tags) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        for(Tag tag: i_Tags) {
            Map<String, Object> tagAdd = new HashMap<>();
            tagAdd.put("Name", tag.GetTagName());
            m_database.collection("ChatGroup").document(i_ChatID).collection(tag.GetTagType()).document(tag.GetTagID()).set(tagAdd);
        }
        return taskCompletionSource.getTask();
    }

    public static Task<Void> CreateJoinUserToChat(String i_UserID ,ChatForYou i_Chat) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Map<String, Object> chatADD = new HashMap<>();
        chatADD.put("Name", i_Chat.GetChatName());
        m_database.collection("User").document(i_UserID).collection("ChatGroup").document(i_Chat.GetID()).set(chatADD);
        return taskCompletionSource.getTask();
    }

    public static Task<Void> CreateUserInChat(String i_ChatID ,ArrayList<User> i_UserMember) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        for(User user: i_UserMember) {
            Map<String, Object> userADD = new HashMap<>();
            userADD.put("ImageSrc", user.GetImageSrc());
            m_database.collection("ChatGroup").document(i_ChatID).collection("User").document(user.GetUserID()).set(userADD);
        }
        return taskCompletionSource.getTask();
    }
    public static Task<Boolean> Registration(HashMap<String , Object > i_User , ImageView Image)
    {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        m_Auth.createUserWithEmailAndPassword((String)i_User.get("Email") , (String)i_User.get("Password")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) {
                       UpdateProfileUser(task.getResult().getUser().getUid() , i_User);
                       Storage.UploadImage("User" , task.getResult().getUser().getUid()+".jpg" , Image);
                       Log.d(TAG, "createUserWithEmail:success");
                       taskCompletionSource.setResult(true);
                  } else {
                     Log.d(TAG, "createUserWithEmail:failure", task.getException());
                       taskCompletionSource.setResult(false);
                  }
              }
          });
        return taskCompletionSource.getTask();
    }

    public static Task<Void> UpdateProfileUser(String i_UserID , HashMap<String , Object> i_User)
    {
        ArrayList<Tag> category =  (ArrayList<Tag>) i_User.get("Category");
        ArrayList<Tag> platformGame =  (ArrayList<Tag>) i_User.get("PlatformGame");
        i_User.remove("Category");
        i_User.remove("PlatformGame");
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        m_database.collection("User").document(i_UserID).set(i_User);

        for(Tag tag : category)
        {
            HashMap<String , String> tg = new HashMap<>();
            tg.put("Name" , tag.GetTagName());
            m_database.collection("User").document(i_UserID).collection("Category").document(tag.GetTagID()).set(tg);
        }
        for(Tag tag : platformGame)
        {
            HashMap<String , String> tg = new HashMap<>();
            tg.put("Name" , tag.GetTagName());
            m_database.collection("User").document(i_UserID).collection("PlatformGame").document(tag.GetTagID()).set(tg);
        }
        return taskCompletionSource.getTask();
    }


    public static Task<Boolean> CreateNewChatPraivcy(User i_Friend , User i_CurrentUser) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        String imageSrc = "";
        Map<String, Object> chatPrivacy = new HashMap<>();
        chatPrivacy.put("DateCreated", Timestamp.now());
        chatPrivacy.put("ImageSrcUserOne", i_Friend.GetImageSrc());
        chatPrivacy.put("ImageSrcUserTwo",i_CurrentUser.GetImageSrc() );
        chatPrivacy.put("NameUserOne", i_Friend.GetNickName());
        chatPrivacy.put("NameUserTwo", i_CurrentUser.GetNickName());
        chatPrivacy.put("IdUserOne", i_Friend.GetUserID());
        chatPrivacy.put("IdUserTwo", i_CurrentUser.GetUserID());
        chatPrivacy.put("Type", "Privacy");

        m_database.collection("ChatGroup").add(chatPrivacy).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        ArrayList<User> userMember = new ArrayList<>();
                        userMember.add(i_Friend);
                        userMember.add(i_CurrentUser);
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Task<Void> addUserMember = CreateUserInChat(documentReference.getId() , userMember);
                        Task<Void> addChatToUser_1 = CreateJoinUserToChat( m_Auth.getUid() , new ChatForYou(i_CurrentUser.GetNickName() ,documentReference.getId())); //To CurrentUser
                        Task<Void> addChatToUser_2 = CreateJoinUserToChat( m_Auth.getUid() , new ChatForYou(i_Friend.GetNickName(), documentReference.getId())); //To CurrentUser
                        if(Tasks.whenAllComplete(addUserMember, addChatToUser_1  , addChatToUser_2, addUserMember).isSuccessful())
                            taskCompletionSource.setResult(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        taskCompletionSource.setResult(false);
                    }
                });
        return taskCompletionSource.getTask();
    }
}
