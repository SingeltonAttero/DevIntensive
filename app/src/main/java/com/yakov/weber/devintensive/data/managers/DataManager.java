package com.yakov.weber.devintensive.data.managers;

public class DataManager {

    private static DataManager sDataManager = null;
    private DevPreferencesManager mDevPreferencesManager;

    private DataManager() {
        this.mDevPreferencesManager = new DevPreferencesManager();
    }

    public static DataManager getInstance(){
        if (sDataManager == null){
            sDataManager = new DataManager();
        }
        return sDataManager;
    }

    public DevPreferencesManager getDevPreferencesManager() {
        return mDevPreferencesManager;
    }
}
