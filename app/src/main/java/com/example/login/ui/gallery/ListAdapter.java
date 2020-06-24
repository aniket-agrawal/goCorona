package com.example.login.ui.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private Context mcontext;
    private ArrayList<String> profileNameList;
    private ArrayList<String> profilePhoneList;

    public ListAdapter(Context context, ArrayList<String> profileNameList, ArrayList<String> profileNumberList) {
        this.mcontext = context;
        this.profileNameList = profileNameList;
        this.profilePhoneList = profileNumberList;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_req_card,parent,false);
        return new ListViewHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return profileNameList.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView number,name,date,seat;
        private CircleImageView photo;

        public ListViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            photo = (CircleImageView)view.findViewById(R.id.profileImage);
            number = (TextView)view.findViewById(R.id.phonenumber);
            date = (TextView)view.findViewById(R.id.dateandtime);
            seat = (TextView)view.findViewById(R.id.seat);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            name.setText(profileNameList.get(position));
            number.setText(profilePhoneList.get(position));
        }

        public void onClick(View view){

        }

    }
}
