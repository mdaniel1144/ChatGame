package com.example.gamechats_final.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.R;

public class fragment_SettingMenu extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_setting_menu, container, false);

        view.findViewById(R.id.buttonSettingUpdateProfileUser).setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_fragment_SettingMenu_to_fragment_setting_deatils);
        });
        view.findViewById(R.id.buttonSettingProfileImage).setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_fragment_SettingMenu_to_fragment_SettingUploadImage );
        });
        view.findViewById(R.id.buttonSettingChangePassword).setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_fragment_SettingMenu_to_fragment_SettingChangePassword);
        });


        //MakeLogOut
        view.findViewById(R.id.buttonSettingLogOut).setOnClickListener(v->{
            InitializeDataSet.GetAuthFirebase().signOut();
            Intent intent = new Intent(getActivity(), MainActivityMain.class);
            startActivity(intent);
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
