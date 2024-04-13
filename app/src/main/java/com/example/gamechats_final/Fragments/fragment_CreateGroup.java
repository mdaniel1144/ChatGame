package com.example.gamechats_final.Fragments;
import android.app.AlertDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.example.gamechats_final.R;


public class fragment_CreateGroup extends Fragment {
    private static AlertDialog alertdialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_creategroup, container, false);

        return view;
    }
    public static AlertDialog GetAlertDIALOG(){return alertdialog;}
    public static void SetAlertDIALOG(AlertDialog alert){alertdialog = alert;}

}