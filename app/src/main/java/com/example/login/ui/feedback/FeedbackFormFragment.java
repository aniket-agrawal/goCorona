package com.example.login.ui.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.login.R;

public class FeedbackFormFragment extends Fragment {

    Button b;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);
        b = (Button)getActivity().findViewById(R.id.button3);
        b.setVisibility(View.INVISIBLE);
        return root;
    }
}