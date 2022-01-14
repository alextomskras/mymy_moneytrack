package com.dreamer.mymy_moneytrack.util.validator;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;


public class ClearErrorTextWatcher implements TextWatcher {
    @NonNull
    private final TextInputLayout til;

    public ClearErrorTextWatcher(@NonNull TextInputLayout til) {
        this.til = til;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        til.setErrorEnabled(false);
        til.setError(null);
    }
}
