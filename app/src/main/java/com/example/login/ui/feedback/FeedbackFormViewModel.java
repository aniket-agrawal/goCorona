package com.example.login.ui.feedback;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FeedbackFormViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FeedbackFormViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is feedback fragment");
        //this is my fragment
    }

    public LiveData<String> getText() {
        return mText;
    }
}