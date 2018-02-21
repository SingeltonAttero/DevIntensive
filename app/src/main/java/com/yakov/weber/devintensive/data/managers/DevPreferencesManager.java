package com.yakov.weber.devintensive.data.managers;

import android.content.SharedPreferences;

import com.yakov.weber.devintensive.utils.ConstantManager;
import com.yakov.weber.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yakov on 20.02.18.
 */

public class DevPreferencesManager {

    private SharedPreferences mPreferences;

    public DevPreferencesManager() {
        mPreferences = DevIntensiveApplication.getSharedPreferences();
    }

    private static final String[] USER_FIELDS = {ConstantManager.USER_EMAIL_KEY, ConstantManager.USER_ABOUT_KEY
            , ConstantManager.USER_GIT_2_KEY, ConstantManager.USER_GIT_1_KEY
            , ConstantManager.USER_GIT_3_KEY, ConstantManager.USER_PHONE_KEY, ConstantManager.USER_VK_KEY};

    public void saveUserProfileData(List<String> userField) {
        SharedPreferences.Editor editor = mPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userField.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            dataList.add(mPreferences.getString(USER_FIELDS[i], ""));
        }
        return dataList;
    }


}
