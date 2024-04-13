package com.example.gamechats_final.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamechats_final.R;


public class fragment_Setting extends Fragment {

    private static AlertDialog m_SettingDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }
    public static void SetSettingDialog(AlertDialog i_Dialog){
        m_SettingDialog = i_Dialog;

    }
    public static AlertDialog GetSettingDialog(){return m_SettingDialog;};

}