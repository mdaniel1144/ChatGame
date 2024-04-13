package com.example.gamechats_final.Fragments;

import static android.app.Activity.RESULT_OK;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.Interface.CreateObj;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public class fragment_registerStepThree extends Fragment {

    private ImageView imageViewProfile;
    private Bundle m_UserProperty;

    private final int SELECT_PICTURE = 200;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_register_step_three, container, false);

        m_UserProperty = savedInstanceState;
        imageViewProfile = view.findViewById(R.id.imageViewProfilePhoto);
        Button registerActivity = view.findViewById(R.id.buttonRegisterNextStepTwo);
        registerActivity.setOnClickListener(v->{
            Registration();
        });

        ImageButton addPhoto = view.findViewById(R.id.imageButtonAddPhoto);
        addPhoto.setOnClickListener(v->{
            AddProfileImage();
        });
        view.findViewById(R.id.imageButtonRegisterCancelStepThree).setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_fragment_registerStepThree_to_fragment_login);
        });

        return  view;
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
                    imageViewProfile.setImageURI(selectedImageUri);
                }
            }
        }
    }


    private void Registration()
    {
        HashMap<String , Object> newUser = ((MainActivityMain)getActivity()).GetUserProperty();
        CreateObj.Registration(newUser, imageViewProfile).addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.getResult())
                {
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(getActivity(), "Success Registration", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Navigation.findNavController(getView()).navigate(R.id.action_fragment_registerStepThree_to_fragment_login);
    }


}