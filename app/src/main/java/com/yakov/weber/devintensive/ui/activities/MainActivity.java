package com.yakov.weber.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yakov.weber.devintensive.R;
import com.yakov.weber.devintensive.data.managers.DataManager;
import com.yakov.weber.devintensive.utils.ConstantManager;
import com.yakov.weber.devintensive.utils.DevIntensiveApplication;
import com.yakov.weber.devintensive.utils.TextWatherValidField;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.PhoneNumberUnderscoreSlotsParser;
import ru.tinkoff.decoro.parser.SlotsParser;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = ConstantManager.PREFIX_TAG + "MainActivity";

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;
    private AppBarLayout.LayoutParams mAppBarLayoutParams = null;
    private File mFilePhoto = null;
    private Uri mSelectImage = null;


    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation_drawer_container)
    DrawerLayout mDrawerLayoutContainer;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindViews({R.id.mobile_phone_edit_text,
            R.id.email_edit_text, R.id.vk_edit_text
            , R.id.about_me_edit_text, R.id.git_hub_edit_text,
            R.id.git_hub_2_edit_text, R.id.git_hub_3_edit_text})
    List<EditText> mUserInfoViews;
    @BindView(R.id.profile_placeholder)
    ConstraintLayout mConstraintLayoutPlaceHolder;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_main)
    ImageView mImageViewPhoto;
    @BindView(R.id.text_input_layout_phone)
    TextInputLayout mTextInputLayoutPhone;
    @BindView(R.id.header_view_count_code)
    LinearLayout mLinearLayoutHeader;


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
        initEditTextMask();
        loadUserInfoValue();
        insertProfilePhoto(mDataManager.getDevPreferencesManager().loadUserPhotoUri());

    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarLayoutParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
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
                hideInputMethod(mUserInfoViews.get(0));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.app_bar_layout)
    public void clickAppBar(View view) {
        if (mCurrentEditMode == 1) {
            switch (view.getId()) {
                case R.id.app_bar_layout:
                    showDialog(ConstantManager.LOAD_PROFILE_PHOTO_DIALOG);
                    break;
            }
        }

    }

    @OnClick(R.id.fab)
    public void clickFab(View view) {

        if (mCurrentEditMode == 0) {
            changeEditMode(1);
            mCurrentEditMode = 1;
            mTextInputLayoutPhone.setFocusable(true);
            showInputMethod(mUserInfoViews.get(0));
            mLinearLayoutHeader.setVisibility(View.GONE);
            fab.setImageResource(R.drawable.fab_view_mode_done);
        } else {
            mTextInputLayoutPhone.setFocusable(false);
            mLinearLayoutHeader.setVisibility(View.VISIBLE);
            changeEditMode(0);
            mCurrentEditMode = 0;
            fab.setImageResource(R.drawable.fab_create_edit);

        }
        saveUserInfoValue();
    }

    @OnClick(R.id.phone_image_call_right)
    public void clickPhoneCall(View view) {
        setAnimationView(view);
        Intent takeCallIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mUserInfoViews.get(0).getText()));
        checkResolveStartActivity(takeCallIntent);
    }

    @OnClick(R.id.email_send_image_right)
    public void clickEmailSend(View view) {
        setAnimationView(view);
        Intent takeEmailIntent = new Intent(Intent.ACTION_SENDTO);
        takeEmailIntent.setData(Uri.parse("mailto:" + mUserInfoViews.get(1).getText()));
        checkResolveStartActivity(Intent.createChooser(takeEmailIntent, getString(R.string.title_chooset_email)));

    }

    @OnClick(R.id.vk_profile_image_right)
    public void clickProfileVK(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.https_text) + mUserInfoViews.get(2).getText()));
        setAnimationView(view);
        checkResolveStartActivity(intent);
    }

    @OnClick({R.id.git_hub_rep_image_right, R.id.git_hub_2_rep_image_right, R.id.git_hub_3_rep_image_right})
    public void clickGithubRep(View view) {
        String gitRep = null;
        switch (view.getId()) {
            case R.id.git_hub_rep_image_right:
                gitRep = String.valueOf(mUserInfoViews.get(4).getText());
                break;
            case R.id.git_hub_2_rep_image_right:
                gitRep = String.valueOf(mUserInfoViews.get(5).getText());
                break;
            case R.id.git_hub_3_rep_image_right:
                gitRep = String.valueOf(mUserInfoViews.get(6).getText());
                break;
        }
        setAnimationView(view);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.https_text) + gitRep));
        checkResolveStartActivity(intent);
    }


    private void initEditTextMask() {
        setMaskPhone(mUserInfoViews.get(0));
        validFieldEmail(mUserInfoViews.get(1));

    }

    private void validFieldEmail(EditText view){
        view.addTextChangedListener(new TextWatherValidField(view,"Error"));
    }

    /**
     * mask номера телефона
     */
    public void setMaskPhone(EditText view) {
        Slot[] parser = new PhoneNumberUnderscoreSlotsParser().parseSlots("+7(9__)-___-__-__");
        FormatWatcher watcher = new MaskFormatWatcher(MaskImpl.createNonTerminated(parser));
        watcher.installOn(view);
    }


    /**
     * показать клавиатуру
     *
     * @param view элемент фокуса
     */
    private void showInputMethod(EditText view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null) {
            view.setSelection(view.getText().length());
            manager.showSoftInput(view, 0);
        }
    }

    /**
     * прячем клавиатуру
     *
     * @param view элемент фокуса
     */
    private void hideInputMethod(EditText view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null) {

            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Анимация кнопок
     *
     * @param view любой наследник класса View
     */
    private void setAnimationView(View view) {
        Animation jumpAnim = AnimationUtils.loadAnimation(this, R.anim.animation_image_view);
        view.startAnimation(jumpAnim);
    }

    /**
     * проверка на даступность
     *
     * @param intent проверка и передача в startActivity
     */
    private void checkResolveStartActivity(Intent intent) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            showMessage(getString(R.string.error_text_sneckbar));
        }
    }

    /**
     * switching mode
     *
     * @param mode if 1 edit mode, 0 view mode
     */
    private void changeEditMode(int mode) {
        for (EditText infoValue : mUserInfoViews) {
            if (mode == 1) {
                changeEditText(infoValue, true);
            } else {
                changeEditText(infoValue, false);

            }
        }
        if (mode == 1) {
            showProfilePlaceHolder();
            lockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            mToolbar.setTitle(R.string.change_title_dialog);
        } else {
            unlockToolbar();
            hideProfilePlaceHolder();
            saveUserInfoValue();
            mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        }
    }

    /**
     * блокировка view; enable, focus and selection
     */
    private void changeEditText(EditText infoValue, boolean enabled) {
        infoValue.setEnabled(enabled);
        infoValue.setFocusable(enabled);
        infoValue.setFocusableInTouchMode(enabled);
        infoValue.setSelection(infoValue.getText().length());
    }

    /**
     * load user info
     */
    private void loadUserInfoValue() {
        if (DevIntensiveApplication.getSharedPreferences().contains(ConstantManager.INIT_CHECK_PREF)) {
            List<String> userData = mDataManager.getDevPreferencesManager().loadUserProfileData();
            for (int i = 0; i < userData.size(); i++) {
                mUserInfoViews.get(i).setText(userData.get(i));
            }
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
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
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

    /**
     * load file from gallery
     */
    private void loadPhotoFromGallery() {
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.chooser_text_title))
                , ConstantManager.REQUEST_GALLERY_PICTURE);


    }

    /**
     * create file from camera
     */
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mFilePhoto = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mFilePhoto != null) {
                // TODO: 26.02.18 передать фото в интент
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFilePhoto));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.REQUEST_PERMISSION_CAMERA_STORAGE);

            showPermissionMessage(R.string.permission_accept_text, R.string.snackbar_button_action_text);
        }

    }

    private void showPermissionMessage(int resIdDescription, int resIdTextAction) {
        Snackbar.make(mCoordinatorLayout, resIdDescription, Snackbar.LENGTH_LONG)
                .setAction(resIdTextAction, view -> openSettingApplication()).show();
    }

    /**
     * profile holder gone
     */
    private void hideProfilePlaceHolder() {
        mConstraintLayoutPlaceHolder.setVisibility(View.GONE);
    }

    /**
     * profile holder visible
     */
    private void showProfilePlaceHolder() {
        mConstraintLayoutPlaceHolder.setVisibility(View.VISIBLE);
    }

    private void lockToolbar() {
        mAppBarLayout.setExpanded(false, true);
        // mAppBarLayoutParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarLayoutParams);
    }

    private void unlockToolbar() {
        mAppBarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarLayoutParams);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    /**
     * вставка в ImageView основной фото
     */
    private void insertProfilePhoto(Uri selectImage) {
        Picasso.with(this)
                .load(selectImage)
                .placeholder(R.drawable.deni_profile)
                .into(mImageViewPhoto);
        mDataManager.getDevPreferencesManager().saveUserPhotoUri(selectImage);
    }

    private void openSettingApplication() {
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingIntent, ConstantManager.REQUEST_SETTING_APP);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantManager.REQUEST_PERMISSION_CAMERA_STORAGE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 27.02.18 логика для обработки разрешений   Manifest.permission.CAMERA
            } else {
                // TODO: 27.02.18 логика для обработки разрешений нажата отмена Manifest.permission.CAMERA
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 27.02.18 логика для обработки разрешений  Manifest.permission.WRITE_EXTERNAL_STORAGE
            } else {
                // TODO: 27.02.18 логика для обработки разрешений нажата отмена Manifest.permission.WRITE_EXTERNAL_STORAGE
            }
        }


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO_DIALOG:
                String[] select = {getString(R.string.user_profile_dialog_gallery)
                        , getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_cancel_dialog),};
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle(R.string.change_title_dialog)
                        .setItems(select, (dialogInterface, i) -> {
                            switch (i) {
                                case 0:
                                    // TODO: 26.02.18 загрузить из галереи
                                    loadPhotoFromGallery();
                                    break;
                                case 1:
                                    // TODO: 26.02.18 сделать фото
                                    loadPhotoFromCamera();
                                    break;
                                case 2:
                                    // TODO: 26.02.18 отмена
                                    dialogInterface.cancel();
                                    break;
                            }
                        });
                return builder.create();
            default:
                return super.onCreateDialog(id);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectImage = data.getData();
                    insertProfilePhoto(mSelectImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mFilePhoto != null) {
                    mSelectImage = Uri.fromFile(mFilePhoto);
                    insertProfilePhoto(mSelectImage);
                }
                break;

        }
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
        if (this.mDrawerLayoutContainer.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayoutContainer.closeDrawer(GravityCompat.START);
        } else {
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
