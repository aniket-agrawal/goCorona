package com.example.login.ui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.login.R;
import com.example.login.SocialServiceInputActivity;

public class service extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.poster, container, false);
        Intent intent = new Intent(getActivity(), SocialServiceInputActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
        return root;
    }
}