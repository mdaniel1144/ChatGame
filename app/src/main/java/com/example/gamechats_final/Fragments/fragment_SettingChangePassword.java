package com.example.gamechats_final.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Interface.Update;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class fragment_SettingChangePassword extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Clear the flags of Window Alert
        fragment_Setting.GetSettingDialog().getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        fragment_Setting.GetSettingDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        View view =  inflater.inflate(R.layout.fragment_setting_password, container, false);

        ((TextView)view.findViewById(R.id.buttonSettingPasswordOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = ((EditText)view.findViewById(R.id.editTextSettingPasswordOld)).getText().toString().trim();
                String newPassword = ((EditText)view.findViewById(R.id.editTextSettingPasswordNew)).getText().toString().trim();
                String confirmPassword = ((EditText)view.findViewById(R.id.editTextSettingPasswordConfirm)).getText().toString().trim();
                if(confirmPassword.equals(newPassword) && !oldPassword.isEmpty() && !newPassword.isEmpty()) {
                    Update.ChangePasswordUser(oldPassword, newPassword).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if(task.getResult())
                            {
                                fragment_Setting.GetSettingDialog().dismiss();
                                InitializeDataSet.GetAuthFirebase().signOut();
                                Toast.makeText(getActivity(), "Password Changed", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), MainActivityMain.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getActivity(), "Something Gose Worng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    if(oldPassword.isEmpty())
                        ((EditText)view.findViewById(R.id.editTextSettingPasswordOld)).setError("Password Its Empty");
                    if(confirmPassword.equals(newPassword)){
                        ((EditText)view.findViewById(R.id.editTextSettingPasswordNew)).setError("Password Not Match");
                    (   (EditText)view.findViewById(R.id.editTextSettingPasswordConfirm)).setError("Password Not Match");}
                }
            }
        });
        ((TextView)view.findViewById(R.id.buttonSettingPasswordCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_fragment_SettingChangePassword_to_fragment_SettingMenu);
                fragment_Setting.GetSettingDialog().dismiss();
            }
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
