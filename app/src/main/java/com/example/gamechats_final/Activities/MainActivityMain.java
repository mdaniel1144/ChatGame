package com.example.gamechats_final.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.gamechats_final.Interface.InitializeDataSet;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivityMain extends AppCompatActivity {
    private FirebaseAuth m_Auth;
    private HashMap<String , Object> m_userProperty;
    private ArrayList<Tag> m_Category;
    private ArrayList<Tag> m_PlatformGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_userProperty = new HashMap<>();
        m_Category = new ArrayList<>();
        m_PlatformGame = new ArrayList<>();
        m_Auth = FirebaseAuth.getInstance();

        initializeDataSetTags();
    }

    private void initializeDataSetTags()
    {
        Task<ArrayList<Tag>> taskCategory = InitializeDataSet.GetAllCategory();
        Task<ArrayList<Tag>> taskPlatformGame = InitializeDataSet.GetAllPlatformGame();

        Tasks.whenAllComplete(taskCategory ).addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
            @Override
            public void onComplete(@NonNull Task<List<Task<?>>> task) {
                if(task.isSuccessful()) {
                    m_Category = taskCategory.getResult();
                    m_PlatformGame = taskPlatformGame.getResult();
                }
                else {
                    // Handle error
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("MainActivityMain", "Error initializing data set: " + exception.getMessage());
                    }
                }
            }
        });
    }


    public FirebaseAuth GetAuth(){return  this.m_Auth;}
    public HashMap<String , Object> GetUserProperty(){return m_userProperty;}
    public ArrayList<Tag> GetCategoryTags(){return m_Category;}
    public ArrayList<Tag> GetPlatformGameTags(){return m_PlatformGame;}

}