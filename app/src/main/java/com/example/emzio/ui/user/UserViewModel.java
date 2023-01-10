package com.example.emzio.ui.user;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emzio.User;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    User user;
    SharedPreferences share;

    public UserViewModel() {



    }



    public LiveData<String> getText() {
        return mText;
    }
}