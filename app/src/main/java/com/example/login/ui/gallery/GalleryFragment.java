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
    private ArrayList<String> profileNumberList= new ArrayList<>();
    private DatabaseReference reff;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        readUsers();
    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Social Service");
        Query query = reference.orderByKey();
        ValueEventListener queryValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    profileNameList.clear();
                    profileNumberList.clear();
                    Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                    while (iterator.hasNext()){
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        String value = Objects.requireNonNull(next.getKey());
                        reff = FirebaseDatabase.getInstance().getReference().child("Social Service").child(value);
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                String name = dataSnapshot1.child("name").getValue().toString();
                                String phone = dataSnapshot1.child("phone").getValue().toString();
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
    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        b = (Button)getActivity().findViewById(R.id.button3);
        fragment = this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
            }
        });
        profileNumberList.add("9636541817");
        profileNumberList.add("9308365765");
        profileNameList.add("Aniket Agrawal");
        profileNameList.add("Mummy");
        System.out.println(profileNameList.size());
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        ListAdapter listAdapter = new ListAdapter(getContext(),profileNameList,profileNumberList);
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return root;
    }
}