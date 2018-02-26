package com.yakov.weber.devintensive.ui.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.yakov.weber.devintensive.R;
import com.yakov.weber.devintensive.data.managers.DataManager;
import com.yakov.weber.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = ConstantManager.PREFIX_TAG + "MainActivity";

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.image_mobile_phone_left)
    ImageView leftImagePhone;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer_container)
    DrawerLayout mDrawerLayoutContainer;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindViews({R.id.mobile_phone_edit_text,
            R.id.email_edit_text, R.id.about_me_edit_text,
            R.id.vk_edit_text, R.id.git_hub_edit_text,
            R.id.git_hub_2_edit_text, R.id.git_hub_3_edit_text})
    List<EditText> mUserInfoViews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        if (savedInstanceState == null) {

        } else {
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.MODE_EDIT_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }
        setupToolBar();
        setupDrawer();
        loadUserInfoValue();


        Log.d(TAG, "onCreate: ");
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.action_name);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayoutContainer.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.image_mobile_phone_left)
    public void clickImage(View view) {
        showMessage("привет мир");
    }

    @OnClick(R.id.fab)
    public void clickFab(View view) {

        if (mCurrentEditMode == 0) {
            changeEditMode(1);
            mCurrentEditMode = 1;
            showMessage(getString(R.string.text_mode_edit));
            fab.setImageResource(R.drawable.fab_view_mode_done);
        } else {
            changeEditMode(0);
            mCurrentEditMode = 0;
            showMessage(getString(R.string.text_mode_view));
            fab.setImageResource(R.drawable.fab_create_edit);
        }
        saveUserInfoValue();

    }

    /**
     * switching mode
     *
     * @param mode if 1 edit mode, 0 view mode
     */
    private void changeEditMode(int mode) {
        for (EditText infoValue : mUserInfoViews) {
            if (mode == 1) {
                infoValue.setEnabled(true);
                infoValue.setFocusable(true);
                infoValue.setFocusableInTouchMode(true);
            } else {
                infoValue.setEnabled(false);
                infoValue.setFocusable(false);
                infoValue.setFocusableInTouchMode(false);
            }
        }
    }

    /**
     * load user info
     */
    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getDevPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }

    }

    /**
     * save user info
     */
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldViews : mUserInfoViews) {
            userData.add(userFieldViews.getText().toString());
        }
        mDataManager.getDevPreferencesManager().saveUserProfileData(userData);
    }

    private void showMessage(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void setupDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                showMessage(item.getTitle().toString());
                item.setCheckable(true);
                mDrawerLayoutContainer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    /** load file from gallery*/
    private void loadPhotoFromGallery(){

    }
    /** create file from camera*/
    private void loadPhotoFromCamera(){
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManager.MODE_EDIT_KEY, mCurrentEditMode);
    }
    /**
     * on back, close drawer
     */
    @Override
    public void onBackPressed() {
        if (this.mDrawerLayoutContainer.isDrawerOpen(GravityCompat.START)){
            this.mDrawerLayoutContainer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }




    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        saveUserInfoValue();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
    }


}
