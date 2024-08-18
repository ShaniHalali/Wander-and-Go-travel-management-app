package com.example.myapplication.ui.flight;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FlightViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FlightViewModel() {
        mText = new MutableLiveData<>();
      //  mText.setValue("This is Trip Planner fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}