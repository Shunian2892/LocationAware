package com.example.location_aware.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UIViewModel extends ViewModel {
    private MutableLiveData<Integer> currentFragment;

    public void init(int currentFragment){
        this.currentFragment = new MutableLiveData<>(currentFragment);
    }

    public LiveData<Integer> getCurrentFragment(){
        return currentFragment;
    }

    public void setCurrentFragment(int currentFragment) {
        this.currentFragment.setValue(currentFragment);
    }
}
