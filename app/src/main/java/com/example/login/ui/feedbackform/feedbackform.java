package com.example.login.ui.feedbackform;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.login.FeedBackFormActivity;
import com.example.login.R;

public class feedbackform extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.poster, container, false);
        Intent intent = new Intent(getActivity(), FeedBackFormActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
        return root;
    }
}