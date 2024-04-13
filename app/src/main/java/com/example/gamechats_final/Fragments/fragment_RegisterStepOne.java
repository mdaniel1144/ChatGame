package com.example.gamechats_final.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.R;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class fragment_RegisterStepOne extends Fragment {
    private  EditText m_UserFirstname;
    private  EditText m_UserLastname;
    private  EditText m_UserPhone;
    private  EditText m_UserEmail;
    private EditText m_UserBirthDate;
    private EditText m_UserPassword;
    private EditText m_UserConfirmPassword;
    private EditText m_UserNickName;

    private DatePickerDialog datePickerDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_step_one, container, false);
        m_UserFirstname = view.findViewById(R.id.editTextUserFirstName);
        m_UserLastname = view.findViewById(R.id.editTextUserLastName);
        m_UserPhone = view.findViewById(R.id.editTextUserPhone);
        m_UserEmail = view.findViewById(R.id.editTextUserMail);
        m_UserBirthDate = view.findViewById(R.id.editTextUserBirthDate);
        m_UserPassword = view.findViewById(R.id.editTextTextPassword);
        m_UserConfirmPassword = view.findViewById(R.id.editTextConfrimPassword);
        m_UserNickName =  view.findViewById(R.id.editTextUserNickName);
        initDatePicker();

        view.findViewById(R.id.buttonRegisterNextStepTwo).setOnClickListener(this::OnClickNext);
        m_UserBirthDate.setOnClickListener(this::openDatePicker);
        return view;
    }

    public void OnClickNext(View view)
    {
        boolean isValid = true; // validtionInput();
        HashMap<String , Object> newUser =  ((MainActivityMain)getActivity()).GetUserProperty();
        if (!isValid)
        {
            Toast.makeText(getActivity(), "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
        else {
            newUser.put("FirstName", m_UserFirstname.getText().toString().trim());
            newUser.put("LastName", m_UserLastname.getText().toString().trim());
            newUser.put("NickName", m_UserNickName.getText().toString().trim());
            newUser.put("Phone", m_UserPhone.getText().toString().trim());
            newUser.put("BirthDate", m_UserBirthDate.getText().toString().trim());
            newUser.put("Email", m_UserEmail.getText().toString().trim());
            newUser.put("Password", m_UserPassword.getText().toString().trim());
           //  newUser.put("FirstName", "ido");
          //  newUser.put("LastName", "Yossi");
        ///    newUser.put("NickName", "Yossi");
        //    newUser.put("Phone", "0508422256");
        //    newUser.put("BirthDate", Timestamp.now());
        //    newUser.put("Email", "wa@n-k.org.il");
        //    newUser.put("Password", "12345678");
            Navigation.findNavController(view).navigate(R.id.action_fragment_RegisterStepOne_to_fragment_RegisterStepTwo);
        }
    }

    private boolean validtionInput()
    {
        boolean isValid = true;
        String RegexText = "^[a-zA-Z0-9]+";
        String RegexPassword = "^[a-zA-Z0-9]{8,}$";
        String RegexMail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String RegexPhone = "^05[02487][0-9]{7}";
        if(!Pattern.matches(RegexText , m_UserFirstname.getText().toString().trim())) {
            isValid = false;
            m_UserFirstname.setError("It must include only letters");
        }
        if(!Pattern.matches(RegexText , m_UserLastname.getText().toString().trim())) {
            isValid = false;
            m_UserLastname.setError("It must include only letters");
        }
        if(!Pattern.matches(RegexText , m_UserNickName.getText().toString().trim())) {
            isValid = false;
            m_UserNickName.setError("It must include only letters");
        }
        if(!Pattern.matches(RegexPhone , m_UserPhone.getText().toString().trim())) {
            isValid = false;
            if(!Pattern.matches("^05[02487][0-9]*" , m_UserPhone.getText().toString().trim()))
                m_UserPhone.setError("The prefix is illegal - It must start with 050/052/054/058/057");
            else
                m_UserPhone.setError("It must include 10 digit");
        }
        if(!Pattern.matches(RegexMail , m_UserEmail.getText().toString().trim())) {
            isValid = false;
            m_UserEmail.setError("This mail in not valid");
        }
        if(!Pattern.matches(RegexPassword , m_UserPassword.getText().toString().trim())) {
            isValid = false;
            m_UserPassword.setError("It must include at least 8 chars{ A-Z , a-z , 0-9}");
        }
        if(!m_UserPassword.getText().toString().equals(m_UserConfirmPassword.getText().toString().trim())) {
            isValid = false;
            m_UserConfirmPassword.setError("It must be same to Password");
        }
        Calendar dateToday = Calendar.getInstance();
        Calendar dateInput = Calendar.getInstance();
        dateInput.set(datePickerDialog.getDatePicker().getYear() , datePickerDialog.getDatePicker().getMonth()+1,datePickerDialog.getDatePicker().getDayOfMonth());
        if(dateToday.before(dateInput)) {
            isValid = false;
            m_UserBirthDate.setError("It it illegal date");
        }
        else{m_UserBirthDate.setError(null);}
        return  isValid;
    }

    //Dialog of DateInput
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateListener =  new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dataBirthDate = String.format("%s-%s-%s",dayOfMonth, month+1,year);
                m_UserBirthDate.setText(dataBirthDate);
            }
        };
        Calendar cal = Calendar.getInstance() ;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateListener, year, month, day);
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}