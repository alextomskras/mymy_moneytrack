package com.dreamer.mymy_moneytrack.activity.account;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatSpinner;

import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity;
import com.dreamer.mymy_moneytrack.controller.data.AccountController;
import com.dreamer.mymy_moneytrack.controller.data.TransferController;
import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.dreamer.mymy_moneytrack.entity.data.Transfer;
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy;
import com.dreamer.mymy_moneytrack.util.validator.IValidator;
import com.dreamer.mymy_moneytrack.util.validator.TransferValidator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TransferActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "TransferActivity";

    @Inject
    TransferController transferController;
    @Inject
    AccountController accountController;

    private IValidator<Transfer> transferValidator;

    private List<Account> accountList;

    @BindView(R.id.contentView)
    View contentView;
    @BindView(R.id.spinner_from)
    AppCompatSpinner spinnerFrom;
    @BindView(R.id.spinner_to)
    AppCompatSpinner spinnerTo;
    @BindView(R.id.et_from_amount)
    EditText etFromAmount;
    @BindView(R.id.et_to_amount)
    EditText etToAmount;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(TransferActivity.this);
        accountList = accountController.readActiveAccounts();
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        List<String> accounts = new ArrayList<>();
        for (Account account : accountList) {
            accounts.add(account.getTitle());
        }

        transferValidator = new TransferValidator(TransferActivity.this, contentView);

        if (accounts.size() == 0) {
            accounts.add(String.valueOf((R.string.none)));
            spinnerFrom.setEnabled(false);
            spinnerTo.setEnabled(false);
        }

        spinnerFrom.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));

        spinnerTo.setAdapter(new ArrayAdapter<>(TransferActivity.this,
                R.layout.view_spinner_item, accounts));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_transfer, menu);
        getContentViewId();

//                .inflate(R.menu.menu_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                tryTransfer();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void tryTransfer() {
        CrashlyticsProxy.get().logButton("Done Transfer");
        if (doTransfer()) {
            CrashlyticsProxy.get().logEvent("Done Transfer");
            setResult(RESULT_OK);
            finish();
        }
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private boolean doTransfer() {
        if (transferValidator.validate()) {
            Account fromAccount = accountList.get(spinnerFrom.getSelectedItemPosition());
            Account toAccount = accountList.get(spinnerTo.getSelectedItemPosition());
            double fromAmount = Double.parseDouble(etFromAmount.getText().toString());
            double toAmount = Double.parseDouble(etToAmount.getText().toString());

            return transferController.create(new Transfer(System.currentTimeMillis(),
                    fromAccount.getId(), toAccount.getId(), fromAmount, toAmount)) != null;
        } else {
            return false;
        }
    }
}
