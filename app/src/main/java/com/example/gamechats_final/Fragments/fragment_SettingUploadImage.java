package com.example.gamechats_final.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamechats_final.Activities.ChatActivity;

import com.example.gamechats_final.Interface.Storage;
import com.example.gamechats_final.Interface.Update;
import com.example.gamechats_final.R;
public class fragment_SettingUploadImage extends Fragment {

    private ImageView m_imageProfile;
    final int SELECT_PICTURE = 200;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_setting_uploadimage, container, false);
        m_imageProfile = view.findViewById(R.id.imageViewProfilePhoto);

        Storage.GetImageFromStorage("User" ,ChatActivity.m_UserInfo.GetImageSrc() ,m_imageProfile );

        ((ImageButton)view.findViewById(R.id.imageButtonAddPhoto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProfileImage();
            }
        });

        ((TextView)view.findViewById(R.id.buttonSettingUpload)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageSrc = ChatActivity.m_UserInfo.GetImageSrc();
                Update.UpdateImageSrc("User", ChatActivity.m_UserInfo.GetUserID() , imageSrc);
                Storage.UploadImage("User" ,  imageSrc ,  m_imageProfile);
                Toast.makeText(getActivity(), "Uplaod Your Image", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigate(R.id.action_fragment_SettingUploadImage_to_fragment_SettingMenu);
                fragment_Setting.GetSettingDialog().dismiss();
            }
        });

        ((ImageButton)view.findViewById(R.id.imageButtonUploadImageBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragment_SettingUploadImage_to_fragment_SettingMenu);
            }
        });
        return view;
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
                    m_imageProfile.setImageURI(selectedImageUri);
                }
            }
        }
    }

}
