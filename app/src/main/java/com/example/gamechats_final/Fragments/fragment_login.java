package com.example.gamechats_final.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamechats_final.Activities.ChatActivity;
import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

public class fragment_login extends Fragment {

    private EditText m_Mail;
    private EditText m_Password;
    private FirebaseAuth m_Auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        m_Mail =  view.findViewById(R.id.editTextLoginMailUser);
        m_Password = view.findViewById(R.id.editTextLoginPassword);
        MainActivityMain activityApp = (MainActivityMain) getActivity();
        m_Auth = activityApp.GetAuth();

        // Implement navigation to SignUpFragment
        view.findViewById(R.id.ButtonRegister).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_fragment_login_to_fragment_RegisterStepOne, null);
        });

        view.findViewById(R.id.ButtonLogin).setOnClickListener(this::OnClickLogin);

        //Check if User is Connected
        FirebaseUser currentUser = m_Auth.getCurrentUser();
       // m_Auth.signOut();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            Toast.makeText(getActivity(), "Connected. "+ currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(intent);

        }
        return view;
    }

    private void OnClickLogin(View view) {
        //Example: userMail = "nir@gmail.com" , password = "12345678"
        boolean isValid= true; //validationInput();
        if(isValid) {
                    String userMail = "nir@gmail.com";
                    String password = "123456789";
                    m_Auth.signInWithEmailAndPassword(userMail, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(getActivity(), "success."+userMail, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "signInWithEmail:success");
                                Intent intent = new Intent(getActivity(), ChatActivity.class);
                                startActivity(intent);
                            } else {
                               // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                 Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                         }
                     });
        }
    }

    private boolean validationInput()
    {
        boolean isValid = true;
        String RegexPassword = "^[a-zA-Z0-9]{8,}$";
        String RegexMail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(!Pattern.matches(RegexMail , m_Mail.getText().toString().trim())) {
            isValid = false;
            m_Mail.setError("This mail in not valid");
        }
        if(!Pattern.matches(RegexPassword , m_Password.getText().toString().trim())) {
            isValid = false;
            m_Password.setError("It must include at least 8 chars{ A-Z , a-z , 0-9}");
        }
        return  isValid;
    }
}