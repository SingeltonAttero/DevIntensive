package com.yakov.weber.devintensive.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by yakov on 28.02.18.
 */

public class TextWatherValidField implements TextWatcher {

    private EditText fieldEdit;
    private String errorText;

    public TextWatherValidField(EditText fieldEdit, String errorText) {
        this.fieldEdit = fieldEdit;
        this.errorText = errorText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
