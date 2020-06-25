package com.example.login.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.SocialServiceDescriptionActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    private ArrayList<String> profileNameList;
    private ArrayList<String> profilePhoneList;
    private ArrayList<String> dateandtimeList;
    private ArrayList<String> seatList;
    private ArrayList<String> idList;
    Activity activity;

    public ListAdapter(Activity activity, ArrayList<String> profileNameList, ArrayList<String> profileNumberList, ArrayList<String> seatList, ArrayList<String> dateandtimeList, ArrayList<String> idList) {
        this.profileNameList = profileNameList;
        this.idList = idList;
        this.activity = activity;
        this.profilePhoneList = profileNumberList;
        this.seatList = seatList;
        this.dateandtimeList = dateandtimeList;
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

        private TextView number,name,date,seat,id;
        private CircleImageView photo;

        public ListViewHolder(View view){
            super(view);
            name = (TextView)view.findViewById(R.id.name);
            photo = (CircleImageView)view.findViewById(R.id.profileImage);
            number = (TextView)view.findViewById(R.id.phonenumber);
            date = (TextView)view.findViewById(R.id.dateandtime);
            seat = (TextView)view.findViewById(R.id.seat);
            id = (TextView)view.findViewById(R.id.id);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            name.setText(profileNameList.get(position));
            number.setText(profilePhoneList.get(position));
            date.setText(dateandtimeList.get(position));
            seat.setText(seatList.get(position));
            id.setText(idList.get(position));
        }

        public void onClick(View view){
            Intent intent = new Intent(activity, SocialServiceDescriptionActivity.class);
            intent.putExtra("id",id.getText());
            activity.startActivity(intent);
            activity.finish();
        }

    }
}
