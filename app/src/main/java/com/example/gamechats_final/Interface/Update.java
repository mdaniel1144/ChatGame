package com.example.gamechats_final.Interface;

import static android.content.ContentValues.TAG;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Object.Chat;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Update {

    private static FirebaseAuth m_Auth = FirebaseAuth.getInstance();
    private static FirebaseFirestore m_database = FirebaseFirestore.getInstance();


    public static Task<Void> UpdateImageSrc(String i_Collection, String i_DocumentId , String i_ImageSrc)
    {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        m_database.collection(i_Collection).document(i_DocumentId).update("ImageSrc" , i_ImageSrc).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Update Your ImageSrc Profile Image");
            }
        });

        return taskCompletionSource.getTask();
    }


    public static Task<Void> AddUserMember(User i_User)
    {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        Map<String, Object> friend = new HashMap<>();
        friend.put("NickName", i_User.GetNickName());
        friend.put("Phone", i_User.GetPhone());
        friend.put("ImageSrc", i_User.GetImageSrc());

        Map<String, Object> me = new HashMap<>();
        me.put("NickName", ChatActivity.m_UserInfo.GetNickName());
        me.put("Phone", ChatActivity.m_UserInfo.GetPhone());
        me.put("ImageSrc", ChatActivity.m_UserInfo.GetImageSrc());

        Task<Void> task_ForME= m_database.collection("User").document(m_Auth.getUid()).collection("Friend").document(i_User.GetUserID()).set(friend); // For me;
        Task<Void> task_ForHim= m_database.collection("User").document(i_User.GetUserID()).collection("Friend").document(m_Auth.getUid()).set(me); //For Him
        Tasks.whenAllComplete(task_ForHim , task_ForME).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
            @Override
            public void onSuccess(List<Task<?>> tasks) {
                Log.d(TAG, "Add User To Your Friend List successfully written!");
            }
        });

        return taskCompletionSource.getTask();
    }
    public static Task<Void> RemoveUserMember(User i_User)
    {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Task<Void> task_ForME = m_database.collection("User").document(m_Auth.getUid()).collection("Friend").document(i_User.GetUserID()).delete();
        Task<Void> task_ForHim = m_database.collection("User").document(i_User.GetUserID()).collection("Friend").document(m_Auth.getUid()).delete();
        Tasks.whenAllComplete(task_ForHim , task_ForME).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
            @Override
            public void onSuccess(List<Task<?>> tasks) {
                Log.d(TAG, "Remove User To Your Friend List successfully written!");
            }
        });

        return taskCompletionSource.getTask();
    }


    public static Task<Boolean> ChangePasswordUser(String i_OldPassword , String i_NewPassword)
    {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        FirebaseUser user = m_Auth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), i_OldPassword);
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.updatePassword(i_NewPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User password updated.");
                                            taskCompletionSource.setResult(true);
                                        }
                                        else
                                            taskCompletionSource.setResult(false);
                                    }
                                });
                    }
                });

        return   taskCompletionSource .getTask();
    }

    public static Task<Boolean> UpdateProfileCurrentUser(User i_newUser)
    {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();
        taskCompletionSource.setResult(false);

        HashMap<String , Object> profile = new HashMap<>();
        profile.put("FirstName" , i_newUser.GetFirstName());
        profile.put("LastName" , i_newUser.GetLastName());
        profile.put("Phone" , i_newUser.GetPhone());
        profile.put("NickName" , i_newUser.GetNickName());
        Task<QuerySnapshot> taskCategory =  m_database.collection("User").document(m_Auth.getUid()).collection("Category").get();
        Task<QuerySnapshot> taskPlatform = m_database.collection("User").document(m_Auth.getUid()).collection("PlatformGame").get();
        Task<Void> taskUser = m_database.collection("User").document(m_Auth.getUid()).update(profile);
        Tasks.whenAllComplete(taskUser , taskCategory ,taskPlatform ).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
        public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if (task.isSuccessful()) {
                    ArrayList<Task<Void>> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : taskCategory.getResult()) {
                            {
                                Log.d(TAG, "User Category Delete: "+document.getId());
                                tasks.add(m_database.collection("User").document(m_Auth.getUid()).collection("Category").document(document.getId()).delete());
                            }
                        for (QueryDocumentSnapshot doc : taskPlatform.getResult()) {
                            Log.d(TAG, "User PlatformGame Delete: "+document.getId());
                            tasks.add(m_database.collection("User").document(m_Auth.getUid()).collection("PlatformGame").document(doc.getId()).delete());
                        }
                        Tasks.whenAllComplete(tasks).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                                @Override
                                public void onComplete(@NonNull Task<List<Task<?>>> task) {
                                    ArrayList<Task<Void>> tasksADD = new ArrayList<>();
                                    for(Tag tg : i_newUser.GetCategoryTags())
                                        tasksADD.add(AddTagToUser(tg));
                                    for(Tag tg : i_newUser.GetPlatformGameTags())
                                        tasksADD.add(AddTagToUser(tg));
                                    Tasks.whenAllComplete(tasksADD).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                                            @Override
                                            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                                                Log.d(TAG , "It Succses add Tags");
                                                taskCompletionSource.setResult(true);
                                            }
                                        });
                                }
                            });
                    }
                }
            }});
        return  taskCompletionSource .getTask();
    }

    public static Task<Void> AddTagToUser(Tag i_Tag)
    {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        HashMap<String , String> tg = new HashMap<>();
        tg.put("Name" , i_Tag.GetTagName());
        m_database.collection("User").document(m_Auth.getUid()).collection(i_Tag.GetTagType()).document(i_Tag.GetTagID()).set(tg).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG , "Seccsesful Add Tag");
            }
        });
        return  taskCompletionSource .getTask();
    }

    public static Task<Void> DeleteChat(Chat i_Chat)
    {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        if(i_Chat.GetType().equals("Public"))
        {
            Task<Void> task_FollowerChat = m_database.collection("ChatGroup").document(i_Chat.GetID()).update("CountFollower" , i_Chat.GetFollowers()-1);
            Task<Void> task_DeleteFromUser= m_database.collection("User").document(m_Auth.getUid()).collection("ChatGroup").document(i_Chat.GetID()).delete();
            Tasks.whenAllComplete(task_FollowerChat , task_DeleteFromUser).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                @Override
                public void onSuccess(List<Task<?>> tasks) {
                         Log.d(TAG , "The chat "+i_Chat.GetID()+" Deleted");
                         Log.d(TAG , "The chat was Update");
                }});
        }
        else
        {
            m_database.collection("User").document(m_Auth.getUid()).collection("ChatGroup").document(i_Chat.GetID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
                public void onSuccess(Void uVoid) {
                    Log.d(TAG , "The chat "+i_Chat.GetID()+" Deleted");
                    Log.d(TAG , "The chat was Update");
                }
            });
        }

        return  taskCompletionSource .getTask();
    }
}
