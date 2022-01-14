package com.dreamer.mymy_moneytrack.util.validator;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class for EditAccount validation.
 * Created on 16.09.2018.
 *
 * @author Evgenii Kanivets
 */

public class EditAccountValidator implements IValidator<Account> {

    @NonNull private final Context context;

    @BindView(R.id.tilTitle) TextInputLayout tilTitle;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.tilGoal) TextInputLayout tilGoal;
    @BindView(R.id.etGoal) EditText etGoal;

    public EditAccountValidator(@NonNull Context context, @NonNull View view) {
        this.context = context;
        ButterKnife.bind(this, view);
        initTextWatchers();
    }

    @Override public boolean validate() {
        String title = etTitle.getText().toString().trim();
        double goal = Double.MAX_VALUE;

        try {
            goal = Double.parseDouble(etGoal.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        boolean valid = true;

        if (title.isEmpty()) {
            tilTitle.setError(context.getString(R.string.field_cant_be_empty));
            valid = false;
        }

        if (goal == Double.MAX_VALUE) {
            tilGoal.setError(context.getString(R.string.field_cant_be_empty));
            goal = 0;
            valid = false;
        }

        if (Math.abs(goal) > MAX_ABS_VALUE) {
            tilGoal.setError(context.getString(R.string.too_rich_or_poor));
            valid = false;
        }

        return valid;
    }

    private void initTextWatchers() {
        etTitle.addTextChangedListener(new ClearErrorTextWatcher(tilTitle));
        etGoal.addTextChangedListener(new ClearErrorTextWatcher(tilGoal));
    }
}
