package com.example.login.ui.poster;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.login.R;

public class poster extends Fragment {

    Button b;
    TextView header;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.poster, container, false);
        b = (Button)getActivity().findViewById(R.id.button3);
        header = (TextView)getActivity().findViewById(R.id.header);
        header.setText("Awareness");
        b.setVisibility(View.INVISIBLE);
        return root;
    }
}