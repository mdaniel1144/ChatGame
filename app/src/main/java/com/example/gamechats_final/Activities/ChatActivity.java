package com.example.gamechats_final.Activities;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.Navigation;


import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Interface.AlertDialogBuilder;
import com.example.gamechats_final.Object.Enums;
import com.example.gamechats_final.Object.Friend;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout m_layoutTopActivity;
    private View m_continerView;
    private static ArrayList<Tag> m_Category;
    private static ArrayList<Tag> m_PlatformGame;
    public static Enums.FragmentType JumpToPage;
    public static User m_UserInfo;
    public static Bundle m_Saerch;
    private AlertDialog alertDialogSearch;
    private AlertDialog alertDialogSetting;
    private AlertDialog alertDialogCreate;
    private AlertDialog alertDialogLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        m_continerView = this.findViewById(R.id.fragmentContainerView);
        m_layoutTopActivity = this.findViewById(R.id.layoutTopActivity);

        JumpToPage = Enums.FragmentType.ChatForYou;
        m_Category = new ArrayList<Tag>();
        m_PlatformGame = new ArrayList<Tag>();


        initializeDataSet();

        //Navigtion menu bottom
        BottomNavigationView navView = this.findViewById(R.id.nav_viewMenuChat);
        navView.getMenu().findItem(R.id.navigation_chat_foryou).setChecked(true);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Integer idItem = item.getItemId();
                boolean isSelected = false;
                if (idItem == R.id.navigation_chat_group) {
                    Navigation.findNavController(m_continerView).navigate(R.id.fragment_chatgroup);
                    isSelected =  true;
                }
                if (idItem == R.id.navigation_chat_foryou) {
                    Navigation.findNavController(m_continerView).navigate(R.id.fragment_ForYou);
                    isSelected =  true;
                }
                if (idItem == R.id.navigation_chat_usermember) {
                    Navigation.findNavController(m_continerView).navigate(R.id.fragment_UserMember);
                    isSelected =  true;
                }

                return isSelected;
            }
        });

        findViewById(R.id.menu_top).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenuInflater().inflate(R.menu.nav_menu_top, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    int id = item.getItemId();
                    if(id == R.id.menu_top_advance_search) {
                        alertDialogSearch.show();
                        return true;
                    } else if (id == R.id.menu_top_create_chat) {
                        alertDialogCreate.show();
                        return true;
                    }
                    else {
                        alertDialogSetting.show();
                        return true;
                    }
                }
            });
            popup.show();
        });
    }

    public String GetUserID() {return m_UserInfo.GetUserID();}
    public ArrayList<Friend> GetFriend() {return m_UserInfo.GetFriend();}

    public ArrayList<Tag> GetPlatformGameData(){return this.m_Category;}
    public ArrayList<Tag> GetCategoryData(){return this.m_PlatformGame;}


    public static void SetAllFriend()
    {
        InitializeDataSet.GetUserFriend().addOnCompleteListener(new OnCompleteListener<ArrayList<Friend>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<Friend>> task) {
                if(task.isSuccessful())
                    m_UserInfo.SetFriend(task.getResult());
            }
        });
    }
    private void initializeDataSet()
    {

       Task<ArrayList<Tag>> task_InitializeCategoryDataSet = InitializeDataSet.GetAllCategory();
       Task<ArrayList<Tag>> task_InitializePlatformDataSet = InitializeDataSet.GetAllPlatformGame();
       Task<User> task_InitializeUserData = InitializeDataSet.GetCurrentUserInfo();
        Tasks.whenAllComplete( task_InitializeCategoryDataSet,task_InitializePlatformDataSet , task_InitializeUserData).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                m_PlatformGame = task_InitializePlatformDataSet.getResult();
                m_Category = task_InitializeCategoryDataSet.getResult();
                m_UserInfo = task_InitializeUserData.getResult();
                Log.d(TAG, "Successful add First Parameter to the App");
                initializeDialogs();
            }
        });
    }
    public static void UpdateCurrentUser(User i_NewUser)
    {
        m_UserInfo.SetFirstName(i_NewUser.GetFirstName());
        m_UserInfo.SetLastName(i_NewUser.GetLastName());
        m_UserInfo.SetNickName(i_NewUser.GetNickName());
        m_UserInfo.SetPhone(i_NewUser.GetPhone());
        m_UserInfo.SetCategoryTag(i_NewUser.GetCategoryTags());
        m_UserInfo.SetPlatformGameTag(i_NewUser.GetPlatformGameTags());
    }

    private void initializeDialogs() {
        alertDialogSearch = AlertDialogBuilder.builderAlertSearch(m_Saerch, m_Category, m_PlatformGame, ChatActivity.this);
        alertDialogSearch.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(TAG, "Make Refresh Fragment");
                if (ChatActivity.JumpToPage == Enums.FragmentType.ChatForYou) {
                    Navigation.findNavController(m_continerView).navigate(R.id.fragment_ForYou);
                }
                if (ChatActivity.JumpToPage == Enums.FragmentType.ChatGroup) {
                    Navigation.findNavController(m_continerView).navigate(R.id.fragment_chatgroup);
                }
                if (ChatActivity.JumpToPage == Enums.FragmentType.UserMember) {
                    Navigation.findNavController(m_continerView).navigate(R.id.fragment_UserMember);
                }
            }
        });

        alertDialogSetting = AlertDialogBuilder.builderAlertSetting(ChatActivity.this);
        alertDialogSetting.getWindow().setBackgroundDrawableResource(R.color.transparent);

        alertDialogCreate =  AlertDialogBuilder.builderAlertBuildNewGroup(ChatActivity.this);
        Log.d(TAG, "Build Dialogs Currently");
    }

    public static ArrayList<Tag> GetAllCategory(){return  m_Category;}
    public static ArrayList<Tag> GetAllPlatformGame(){return  m_PlatformGame;}
}