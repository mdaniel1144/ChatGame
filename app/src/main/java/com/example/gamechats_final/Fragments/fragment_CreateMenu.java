package com.example.gamechats_final.Fragments;

import static android.content.ContentValues.TAG;
import android.util.Log;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamechats_final.Interface.CreateObj;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.Enums.RecyclerViewType;
import com.example.gamechats_final.Object.Friend;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class fragment_CreateMenu extends Fragment {

    public static ArrayList<Friend> m_friend;
    public static ArrayList<Tag> m_Category;
    public static ArrayList<Tag> m_PlatformGame;
    private String m_NameChat;
    private String m_DescriptionChat;
    private static String m_NameChatCopy;
    private static String m_DescriptionChatCopy;
    private static ImageView imageViewCreateChatProfileCopy;
    private ImageView imageViewCreateChatProfile;

    public static HashMap<String , ArrayList<?>> m_InfoCreateGroupCopy;
    public static HashMap<String , ArrayList<?>> m_InfoCreateGroup;
    private static RecyclerViewType JumpToPage;
    final int SELECT_PICTURE = 200;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_create_menu, container, false);

        m_friend = new ArrayList<>();
        m_Category = new ArrayList<>();
        m_PlatformGame = new ArrayList<>();

        m_InfoCreateGroup = new HashMap<>();
        m_InfoCreateGroup.put("Friend", new ArrayList<>());
        m_InfoCreateGroup.put("Category", new ArrayList<>());
        m_InfoCreateGroup.put("PlatformGame", new ArrayList<>());

        if(m_InfoCreateGroupCopy == null)
            m_InfoCreateGroupCopy = new HashMap<>(m_InfoCreateGroup);
        imageViewCreateChatProfile = (ImageView)view.findViewById(R.id.imageViewCreateChatProfile);

        if(imageViewCreateChatProfileCopy != null)
            imageViewCreateChatProfile.setImageDrawable(imageViewCreateChatProfileCopy.getDrawable());
        else
            imageViewCreateChatProfileCopy = new ImageView(getContext());
        if(m_NameChatCopy != "")
            ((EditText)view.findViewById(R.id.editTextCreateChatGroupName)).setText(m_NameChatCopy);
        if( m_DescriptionChatCopy != "")
            ((EditText)view.findViewById(R.id.editTextCreateChatDescription)).setText(m_DescriptionChatCopy);


       // It is Show in the activity too
        Task<ArrayList<Tag>> task_Category = InitializeDataSet.GetAllCategory();
        Task<ArrayList<Tag>> task_PlatformGame = InitializeDataSet.GetAllPlatformGame();
        Task<ArrayList<Friend>> task_Friend = InitializeDataSet.GetUserFriend();
        Tasks.whenAllComplete(task_Category , task_PlatformGame , task_Friend).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
           @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if(task.isSuccessful())
                {
                    m_Category =  task_Category.getResult();
                    m_PlatformGame = task_PlatformGame.getResult();
                    m_friend = task_Friend.getResult();

                }
            }
        });
        view.findViewById(R.id.textViewCreateChatPlatformGame).setOnClickListener(v->{
            if(!m_PlatformGame.isEmpty()){
                JumpToPage = RecyclerViewType.PlatformGameTags;
                remmberTheInfoUser(v.getRootView());
                Navigation.findNavController(v).navigate(R.id.action_fragment_CreateMenu_to_fragment_CreateRyceleView );}
        });

        view.findViewById(R.id.textViewCreateChatCategory).setOnClickListener(v->{
            if(!m_Category.isEmpty()){
                JumpToPage = RecyclerViewType.CategoryTags;
                remmberTheInfoUser(v.getRootView());
                Navigation.findNavController(v).navigate(R.id.action_fragment_CreateMenu_to_fragment_CreateRyceleView );}
        });

        view.findViewById(R.id.textViewCreateChatFriend).setOnClickListener(v->{
            if(!m_friend.isEmpty()){
                JumpToPage = RecyclerViewType.Friend;
                remmberTheInfoUser(v.getRootView());
                Navigation.findNavController(v).navigate(R.id.action_fragment_CreateMenu_to_fragment_CreateRyceleView );}
            else
                Toast.makeText(getActivity(), "You Have no Friend in Your Friend List", Toast.LENGTH_SHORT).show();

        });

        view.findViewById(R.id.imageViewCreateUploadImage).setOnClickListener(v->{
            AddProfileImage();
        });

        //MakeLogOut
        view.findViewById(R.id.buttonCreateGroupGroup).setOnClickListener(v->{
            if(validationInput(v.getRootView())) {
                String description = ((EditText) v.getRootView().findViewById(R.id.editTextCreateChatDescription)).getText().toString();
                String name = ((EditText) v.getRootView().findViewById(R.id.editTextCreateChatGroupName)).getText().toString();
                Timestamp time = Timestamp.now();
                CreateNewGroup(name, description, time, (ArrayList<User>) m_InfoCreateGroupCopy.get("Friend"), (ArrayList<Tag>) m_InfoCreateGroupCopy.get("Category"), (ArrayList<Tag>) m_InfoCreateGroupCopy.get("PlatformGame"));
                ResetCopy();
                fragment_CreateGroup.GetAlertDIALOG().dismiss();
            }
        });
        return view;
    }
    private void remmberTheInfoUser(View view)
    {
        String name =  ((EditText)view.findViewById(R.id.editTextCreateChatGroupName)).getText().toString();
        String description = ((EditText)view.findViewById(R.id.editTextCreateChatDescription)).getText().toString();
        m_NameChatCopy = name;
        m_DescriptionChatCopy = description;
        imageViewCreateChatProfileCopy.setImageDrawable(imageViewCreateChatProfile.getDrawable());
    }
    private void ResetCopy()
    {
        m_NameChat = "";
        m_DescriptionChat = "";
        imageViewCreateChatProfileCopy = null;
        m_InfoCreateGroupCopy = null;
    }

    public void CreateNewGroup(String i_Name, String i_Description, Timestamp i_DateCreated , ArrayList<User> i_Friend , ArrayList<Tag> i_Category , ArrayList<Tag> i_PlatformGame) {

        CreateObj.CreateNewGroup(i_Name , i_Description , i_DateCreated , i_Friend , i_Category ,i_PlatformGame, imageViewCreateChatProfile);
    }

    private void AddProfileImage() {
        //Upload Image From User
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageViewCreateChatProfile.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private boolean validationInput(View view) {
        boolean isValid = true;
        String RegexText = "^[a-zA-Z0-9 ]+";

        String name =  ((EditText)view.findViewById(R.id.editTextCreateChatGroupName)).getText().toString();
        String description = ((EditText)view.findViewById(R.id.editTextCreateChatDescription)).getText().toString();

        if (!(Pattern.matches(RegexText , name))) {
            isValid = false;
            ((EditText)view.findViewById(R.id.editTextCreateChatGroupName)).setError("It can not be Empty");
        }
        if (!Pattern.matches(RegexText, description)) {
            isValid = false;
            ((EditText)view.findViewById(R.id.editTextCreateChatDescription)).setError("It can not be Empty");
        }
        return  isValid;
    }

    public static void AddOrRemoveFriendOrTag(Friend i_User, Tag i_Tag , String i_Type  , boolean action)
    {
        if(action) {
            if (i_Type == "Friend")
                ((ArrayList<Object>) m_InfoCreateGroup.get(i_Type)).add(i_User);
            else
                ((ArrayList<Object>) m_InfoCreateGroup.get(i_Type)).add(i_Tag);
        }
        else
        {
            if (i_Type == "Friend"){
                for (int i = 0; i < fragment_CreateMenu.GetInfoCreateGroupCopy().get(i_Type).size(); i++) {
                    if (i_User.equals(m_InfoCreateGroup.get(i_Type).get(i))) {
                        m_InfoCreateGroup.get(i_Type).remove(i--);
                    }}}
            else
                for (int i = 0; i < m_InfoCreateGroup.get(i_Type).size(); i++) {
                    if (i_Tag.equals(m_InfoCreateGroup.get(i_Type).get(i))) {
                        m_InfoCreateGroup.get(i_Type).remove(i--);
                    }
                }
        }
    }

    public static RecyclerViewType GetRecyclerViewType(){return JumpToPage;}
    public static  HashMap<String , ArrayList<?>> GetInfoCreateGroupCopy(){return m_InfoCreateGroupCopy;}
    public static void SetInfoCreateGroupCopy( HashMap<String , ArrayList<?>> InfoCreateGroupCopy){m_InfoCreateGroupCopy = InfoCreateGroupCopy;}
    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }
}