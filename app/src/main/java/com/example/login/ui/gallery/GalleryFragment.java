package com.example.login.ui.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Objects;

public class GalleryFragment extends Fragment {

    Button b;
    private Fragment fragment;
    private ArrayList<String> profileNameList= new ArrayList<>();
    private ArrayList<String> seatList= new ArrayList<>();
    private ArrayList<String> dateandtimeList= new ArrayList<>();
    private ArrayList<String> profileNumberList= new ArrayList<>();
    private ArrayList<String> idList= new ArrayList<>();
    private DatabaseReference reff;
    private String currentDate, currentTime;
    SimpleDateFormat curentDateFormat,currentTimeFormat;
    Calendar calForDate,calForTime;
    Activity activity;

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Social Service");
        Query query = reference.orderByKey();
        ValueEventListener queryValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    profileNameList.clear();
                    profileNumberList.clear();
                    seatList.clear();
                    dateandtimeList.clear();
                    idList.clear();
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()){
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        String value = Objects.requireNonNull(next.getKey());
                        reff = FirebaseDatabase.getInstance().getReference().child("Social Service").child(value);
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                String id = dataSnapshot1.getKey();
                                String name = dataSnapshot1.child("User Name").getValue().toString();
                                String phone = dataSnapshot1.child("Phone Number").getValue().toString();
                                String date = dataSnapshot1.child("Date of Service").getValue().toString();
                                date= date.substring(0,6) + "20" + date.substring(6);
                                String time = dataSnapshot1.child("Time of Service").getValue().toString();
                                if(time.charAt(1)==':'){
                                    time = '0' + time;
                                }
                                String seat = dataSnapshot1.child("Number of People valid").getValue().toString();
                                calForDate = Calendar.getInstance();
                                curentDateFormat = new SimpleDateFormat("dd/MM/yyyy") ;
                                currentDate=curentDateFormat.format(calForDate.getTime());
                                calForTime = Calendar.getInstance();
                                currentTimeFormat = new SimpleDateFormat("hh:mm a") ;
                                currentTime=currentTimeFormat.format(calForTime.getTime());
                                //System.out.println(currentTime);
                                if(check(date,currentDate,time,currentTime)) {
                                    idList.add(id);
                                    seatList.add(seat);
                                    dateandtimeList.add(time + ", " + date);
                                    profileNameList.add(name);
                                    profileNumberList.add(phone);
                                    initReceivedRecyclerView();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(queryValueListener);
    }

    private void initReceivedRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        activity = getActivity();
        ListAdapter listAdapter = new ListAdapter(activity, profileNameList, profileNumberList,seatList,dateandtimeList, idList);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        b = (Button)getActivity().findViewById(R.id.button3);
        b.setVisibility(View.VISIBLE);
        fragment = this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
            }
        });
        readUsers();
        return root;
    }

    public boolean check(String date, String cdate, String time, String ctime){
        int year = Integer.parseInt(date.substring(6)), cyear = Integer.parseInt(cdate.substring(6));
        if(cyear>year) return false;
        if(year>cyear) return true;
        int month = Integer.parseInt(date.substring(3,5)), cmonth = Integer.parseInt(cdate.substring(3,5));
        if(cmonth>month) return false;
        if(month>cmonth) return true;
        int d = Integer.parseInt(date.substring(0,2)), cd = Integer.parseInt(date.substring(0,2));
        if(cd>d) return false;
        if(d>cd) return true;
        if((ctime.charAt(6)=='P' || ctime.charAt(6)=='p') && (time.charAt(6)=='A' || time.charAt(6)=='a')) return false;
        if((time.charAt(6)=='P' || time.charAt(6)=='p') && (ctime.charAt(6)=='A' || ctime.charAt(6)=='a')) return false;
        int hour = Integer.parseInt(time.substring(0,2)), chour = Integer.parseInt(time.substring(0,2));
        if(chour>hour) return false;
        if(hour>chour) return true;
        int min = Integer.parseInt(time.substring(3,5)), cmin = Integer.parseInt(time.substring(3,5));
        return cmin <= min;
    }

}