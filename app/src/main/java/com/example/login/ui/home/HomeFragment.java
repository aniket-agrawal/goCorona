package com.example.login.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.login.R;

public class HomeFragment extends Fragment {

    private ImageView i1,i2;

    private String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/India_COVID-19_cases_density_map.svg/768px-India_COVID-19_cases_density_map.svg.png";
    private String url1 = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/95/COVID-19_India_Total_Cases_Animated_Map.gif/800px-COVID-19_India_Total_Cases_Animated_Map.gif";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        i1 = root.findViewById(R.id.imageView);
        i2 = root.findViewById(R.id.imageView1);
        Glide.with(getContext()).load(url).into(i1);
        Glide.with(getContext()).load(url1).into(i2);
        return root;
    }
}