package com.example.gamechats_final.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.gamechats_final.Activities.MainActivityMain;
import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class fragment_RegisterStepTwo extends Fragment {

    private ChipGroup m_UserChoiceCategory;
    private ChipGroup m_UserChoicePlatform;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_step_two, container, false);
        m_UserChoiceCategory = view.findViewById(R.id.chipGroupChoiceGroup);
        m_UserChoicePlatform = view.findViewById(R.id.chipGroupPlatformChoice);

        Button registerActivity = view.findViewById(R.id.buttonRegisterNextStepTwo);
        registerActivity.setOnClickListener(v->{
            AddChoiceToUser();
            Navigation.findNavController(v).navigate(R.id.action_fragment_RegisterStepTwo_to_fragment_registerStepThree);
        });


        AddChipToGroup(view);
        return view;
    }


    private void AddChipToGroup(View view)
    {
        ArrayList<Tag> taskCategory = ((MainActivityMain)getActivity()).GetCategoryTags();
        ArrayList<Tag> taskPlatformGame =  ((MainActivityMain)getActivity()).GetPlatformGameTags();

        for (Tag tag : taskCategory) {
            Chip groupChat = new Chip(getContext());
            groupChat.setText(tag.GetTagName());
            groupChat.setCheckable(true);
            groupChat.setTag(tag);
            groupChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        groupChat.setChipIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_circle));
                    else
                        groupChat.setChipIcon(null);
                }
            });
            m_UserChoiceCategory.addView(groupChat);
        }
        for (Tag tag : taskPlatformGame) {
            Chip groupChat = new Chip(getContext());
            groupChat.setText(tag.GetTagName());
            groupChat.setCheckable(true);
            groupChat.setTag(tag);
            groupChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        groupChat.setChipIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_circle));
                    else
                        groupChat.setChipIcon(null);
                }
            });
            m_UserChoicePlatform.addView(groupChat);
        }

        m_UserChoiceCategory.setChipSpacing(0);
        m_UserChoicePlatform.setChipSpacing(0);
    }


    private void AddChoiceToUser()
    {
        HashMap<String , Object> newUser = ((MainActivityMain)getActivity()).GetUserProperty();
        List<Integer> ids_GroupChat = m_UserChoiceCategory.getCheckedChipIds();
        List<Integer> ids_PlatformGame = m_UserChoicePlatform.getCheckedChipIds();
        ArrayList<Tag> Category = new ArrayList<Tag>();
        ArrayList<Tag> PlatformGame = new ArrayList<Tag>();

        for (Integer id:ids_GroupChat){
            Chip chip = m_UserChoiceCategory.findViewById(id);
            if(chip.isChecked()  == true)
            {
                Category.add((Tag)chip.getTag());
           }
        }
        for (Integer id:ids_PlatformGame){
            Chip chip = m_UserChoicePlatform.findViewById(id);
            if(chip.isChecked() == true)
            {
                PlatformGame.add((Tag)chip.getTag());
            }
        }
        newUser.put("PlatformGame" , PlatformGame);
        newUser.put("Category" , Category);
    }
}

