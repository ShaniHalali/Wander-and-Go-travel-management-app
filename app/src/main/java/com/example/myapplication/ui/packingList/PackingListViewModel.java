package com.example.myapplication.ui.packingList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PackingListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PackingListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is packing fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}