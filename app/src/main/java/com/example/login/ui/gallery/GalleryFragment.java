package com.example.login.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
    private ArrayList<String> imageList= new ArrayList<>();
    private ArrayList<Double> distanceList= new ArrayList<>();
    private ArrayList<String> profileNumberList= new ArrayList<>();
    private ArrayList<String> idList= new ArrayList<>();

    private DatabaseReference reff;
    private String currentDate, currentTime;
    private double finalLatitude=0, finalLongitude=0;
    SimpleDateFormat curentDateFormat,currentTimeFormat;
    Calendar calForDate,calForTime;
    String image;
    Activity activity;
    public boolean check = false,isCheck = false;
    private final static int REQUEST_CODE_LOCATION_PERMISSION = 1;

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
                    distanceList.clear();
                    imageList.clear();
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
                                String time = dataSnapshot1.child("Time of Service").getValue().toString();
                                if(time.charAt(1)==':'){
                                    time = '0' + time;
                                }
                                if(time.length() == 9){
                                    time = time.substring(0,3) + '0' + time.substring(3);
                                }
                                String seat = dataSnapshot1.child("Number of People valid").getValue().toString();
                                double clat = dataSnapshot1.child("latitude").getValue(double.class);
                                double clang = dataSnapshot1.child("longitude").getValue(double.class);
                                double  distance = distcheck(finalLatitude,clat,finalLongitude,clang);
                                calForDate = Calendar.getInstance();
                                curentDateFormat = new SimpleDateFormat("dd/MM/yyyy") ;
                                currentDate=curentDateFormat.format(calForDate.getTime());
                                calForTime = Calendar.getInstance();
                                currentTimeFormat = new SimpleDateFormat("hh:mm a") ;
                                currentTime=currentTimeFormat.format(calForTime.getTime());
                                if(checkdt(date,currentDate,time,currentTime)) {
                                    if(distance<=10) {
                                        distanceList.add(distance);
                                        idList.add(id);
                                        seatList.add(seat);
                                        dateandtimeList.add(time + ", " + date);
                                        profileNameList.add(name);
                                        profileNumberList.add(phone);
                                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("users").child(phone);
                                        reference1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                                image = dataSnapshot2.child("image").getValue().toString();
                                                imageList.add(image);
                                                System.out.println(image);
                                                isCheck = true;
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
        if(check) {
            System.out.println(getView());
            RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
            activity = getActivity();
            ListAdapter listAdapter = new ListAdapter(activity, profileNameList, profileNumberList, seatList, dateandtimeList, idList, distanceList, imageList);
            recyclerView.setAdapter(listAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            isCheck = false;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        b = (Button)getActivity().findViewById(R.id.button3);
        b.setVisibility(View.VISIBLE);
        b.setEnabled(false);
        check = true;
        if(isCheck) {
            initReceivedRecyclerView();
        }
        TextView header = (TextView) getActivity().findViewById(R.id.header);
        header.setText("Social Service");
        header.setTextSize(22);
        fragment = this;

        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.setEnabled(true);
            }
        },10000);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
            }
        });
        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
//                getMap();
            } else {
                Toast.makeText(getContext(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getCurrentLocation() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity())
                .requestLocationUpdates(locationRequest, new LocationCallback() {


                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getActivity())
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestlocationIndex).getLatitude();

                            double longitude =
                                    locationResult.getLocations().get(latestlocationIndex).getLongitude();

                            finalLatitude = latitude;
                            finalLongitude = longitude;
                            readUsers();


                        }



                    }
                }, Looper.getMainLooper());

    }



    public boolean checkdt(String date, String cdate, String time, String ctime){
        int year = Integer.parseInt(date.substring(6)), cyear = Integer.parseInt(cdate.substring(6));
        if(cyear>year) return false;
        if(year>cyear) return true;
        int month = Integer.parseInt(date.substring(3,5)), cmonth = Integer.parseInt(cdate.substring(3,5));
        if(cmonth>month) return false;
        if(month>cmonth) return true;
        int d = Integer.parseInt(date.substring(0,2)), cd = Integer.parseInt(cdate.substring(0,2));
        if(cd>d) return false;
        if(d>cd) return true;
        if((ctime.charAt(6)=='P' || ctime.charAt(6)=='p') && (time.charAt(6)=='A' || time.charAt(6)=='a')) return false;
        if((time.charAt(6)=='P' || time.charAt(6)=='p') && (ctime.charAt(6)=='A' || ctime.charAt(6)=='a')) return true;
        int hour = Integer.parseInt(time.substring(0,2)), chour = Integer.parseInt(ctime.substring(0,2));
        if(chour>hour){
            if(chour == 12) return true;
            return false;
        }
        if(hour>chour){
            if(hour == 12) return false;
            return true;
        }
        int min = Integer.parseInt(time.substring(3,5)), cmin = Integer.parseInt(ctime.substring(3,5));
        return cmin <= min;
    }

    public double distcheck(double lat1,double lat2, double lon1, double lon2){


                lon1 = Math.toRadians(lon1);
                lon2 = Math.toRadians(lon2);
                lat1 = Math.toRadians(lat1);
                lat2 = Math.toRadians(lat2);

                double dlon = lon2 - lon1;
                double dlat = lat2 - lat1;
                double a = Math.pow(Math.sin(dlat / 2), 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin(dlon / 2),2);

                double c = 2 * Math.asin(Math.sqrt(a));

                double r = 6371;

                return(c * r);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        check = false;
    }
}