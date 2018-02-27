package com.yakov.weber.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.yakov.weber.devintensive.utils.ConstantManager;
import com.yakov.weber.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class DevPreferencesManager {

    private SharedPreferences mPreferences;

    public DevPreferencesManager() {
        mPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY, ConstantManager.USER_EMAIL_KEY
            , ConstantManager.USER_VK_KEY, ConstantManager.USER_ABOUT_KEY, ConstantManager.USER_GIT_2_KEY
            , ConstantManager.USER_GIT_1_KEY, ConstantManager.USER_GIT_3_KEY};

    public void saveUserProfileData(List<String> userField) {
        SharedPreferences.Editor editor = mPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userField.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> dataList = new ArrayList<>();
        dataList.add(mPreferences.getString(ConstantManager.USER_SAVE_PHOTO_KEY,"89210071385"));
        dataList.add(mPreferences.getString(ConstantManager.USER_EMAIL_KEY,"askont@mail.ru"));
        dataList.add(mPreferences.getString(ConstantManager.USER_VK_KEY,"vk.com"));
        dataList.add(mPreferences.getString(ConstantManager.USER_ABOUT_KEY,"Yakov Weber android dev"));
        dataList.add(mPreferences.getString(ConstantManager.USER_GIT_1_KEY,"github.com/askont/DevIntensive"));
        dataList.add(mPreferences.getString(ConstantManager.USER_GIT_2_KEY,"github.com"));
        dataList.add(mPreferences.getString(ConstantManager.USER_GIT_3_KEY,"github.com"));
        return dataList;
    }

    public void saveUserPhotoUri(Uri imageUri){
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(ConstantManager.USER_SAVE_PHOTO_KEY,imageUri.toString());
        editor.apply();
    }

    public Uri loadUserPhotoUri(){
        return Uri.parse(mPreferences.getString(ConstantManager.USER_SAVE_PHOTO_KEY,
                "android.resource://com.yakov.weber.devintensive/drawable/deni_profile"));
    }


}
