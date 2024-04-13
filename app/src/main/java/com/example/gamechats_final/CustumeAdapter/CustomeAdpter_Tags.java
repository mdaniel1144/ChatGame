package com.example.gamechats_final.CustumeAdapter;
import static android.content.ContentValues.TAG;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamechats_final.Fragments.fragment_CreateMenu;
import com.example.gamechats_final.Object.Tag;
import com.example.gamechats_final.Object.User;
import com.example.gamechats_final.R;

import java.util.ArrayList;

public class CustomeAdpter_Tags extends RecyclerView.Adapter<CustomeAdpter_Tags.MyViewHolder>{
    private ArrayList<Tag> m_dataSetTags;
    private ArrayList<Tag> m_DataSetTagSelected;

    public CustomeAdpter_Tags(ArrayList<Tag> i_DataSet , ArrayList<Tag> i_DataSetTagSelected) {
        Log.d(TAG , "Fatal Size" + i_DataSetTagSelected.size());
        this.m_dataSetTags = i_DataSet;
        this.m_DataSetTagSelected =i_DataSetTagSelected;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox CheckBoxTag;
        private Tag m_Tag;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.CheckBoxTag = itemView.findViewById(R.id.CheckBoxCreateChatTags);
        }

        public Tag GetTag() {return this.m_Tag;};
        public void SetTag(Tag i_Tag){this.m_Tag = i_Tag;}
    }

    @NonNull
    @Override
    public CustomeAdpter_Tags.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card__tags, parent, false);
        CustomeAdpter_Tags.MyViewHolder myViewHolder = new CustomeAdpter_Tags.MyViewHolder(view);

        myViewHolder.CheckBoxTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    fragment_CreateMenu.AddOrRemoveFriendOrTag(null ,myViewHolder.GetTag() ,myViewHolder.GetTag().GetTagType() , true);
                }
                else
                {
                    fragment_CreateMenu.AddOrRemoveFriendOrTag(null ,myViewHolder.GetTag() ,myViewHolder.GetTag().GetTagType() , false);
                }
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomeAdpter_Tags.MyViewHolder holder, int key) {
        holder.CheckBoxTag.setText(m_dataSetTags.get(key).GetTagName());
        holder.SetTag(m_dataSetTags.get(key));

        String name = m_dataSetTags.get(key).GetTagName();
        for(Tag tag : m_DataSetTagSelected)
        {
            if(tag.GetTagName().equals(name))
            {
                Log.d(TAG , "Client Choose the Tag: " + key);
                holder.CheckBoxTag.setChecked(true);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {return m_dataSetTags.size();}
}

