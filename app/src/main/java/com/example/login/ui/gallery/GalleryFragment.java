package com.example.login.ui.gallery;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class GalleryFragment extends Fragment {

    Button b;
    private Fragment fragment;
    private ArrayList<String> profileNameList= new ArrayList<>();
    private ArrayList<String> seatList= new ArrayList<>();
    private ArrayList<String> dateandtimeList= new ArrayList<>();
    private ArrayList<String> profileNumberList= new ArrayList<>();
    private DatabaseReference reff;

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
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()){
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        String value = Objects.requireNonNull(next.getKey());
                        reff = FirebaseDatabase.getInstance().getReference().child("Social Service").child(value);
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                String name = dataSnapshot1.child("User Name").getValue().toString();
                                String phone = dataSnapshot1.child("Phone Number").getValue().toString();
                                String date = dataSnapshot1.child("Date of Service").getValue().toString();
                                String time = dataSnapshot1.child("Time of Service").getValue().toString();
                                String seat = dataSnapshot1.child("Number of People valid").getValue().toString();
                                seatList.add(seat);
                                dateandtimeList.add(time+", "+date);
                                profileNameList.add(name);
                                profileNumberList.add(phone);
                                initReceivedRecyclerView();
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
        ListAdapter listAdapter = new ListAdapter(profileNameList, profileNumberList,seatList,dateandtimeList);
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
}