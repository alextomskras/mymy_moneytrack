package com.dreamer.mymy_moneytrack.activity.account;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatSpinner;

import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity;
import com.dreamer.mymy_moneytrack.controller.CurrencyController;
import com.dreamer.mymy_moneytrack.controller.data.AccountController;
import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy;
import com.dreamer.mymy_moneytrack.util.validator.AccountValidator;
import com.dreamer.mymy_moneytrack.util.validator.IValidator;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

public class AddAccountActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AddAccountActivity";

    @Inject
    AccountController accountController;
    @Inject
    CurrencyController currencyController;

    private IValidator<Account> accountValidator;

    @BindView(R.id.contentView)
    View contentView;
    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.et_init_sum)
    EditText etInitSum;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_account;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(AddAccountActivity.this);
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        accountValidator = new AccountValidator(AddAccountActivity.this, contentView);
        spinner.setAdapter(new ArrayAdapter<>(AddAccountActivity.this,
                R.layout.view_spinner_item,
                new ArrayList<>(currencyController.readAll())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                tryAddAccount();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tryAddAccount() {
        CrashlyticsProxy.get().logButton("Done Account");
        if (addAccount()) {
            CrashlyticsProxy.get().logEvent("Done Account");
            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean addAccount() {
        if (accountValidator.validate()) {
            String title = etTitle.getText().toString().trim();
            double initSum = Double.parseDouble(etInitSum.getText().toString().trim());
            String currency = (String) spinner.getSelectedItem();
            double goal = 0;
            int color = 0;

            Account account = new Account(-1, title, initSum, currency, goal, false, color);
            return accountController.create(account) != null;
        } else {
            return false;
        }
    }
}
