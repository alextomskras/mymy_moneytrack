package com.dreamer.mymy_moneytrack.activity.account;

//import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.activity.account.edit.EditAccountActivity;
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity;
import com.dreamer.mymy_moneytrack.adapter.AccountAdapter;
import com.dreamer.mymy_moneytrack.controller.data.AccountController;
import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.dreamer.mymy_moneytrack.ui.presenter.AccountsSummaryPresenter;
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class AccountsActivity extends BaseBackActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "AccountsActivity";

    private static final int REQUEST_ADD_ACCOUNT = 1;
    private static final int REQUEST_TRANSFER = 2;
    private static final int REQUEST_EDIT_ACCOUNT = 3;

    @Inject
    AccountController accountController;

    private AccountsSummaryPresenter summaryPresenter;

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_accounts;
    }

    @Override
    protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(AccountsActivity.this);
        summaryPresenter = new AccountsSummaryPresenter(AccountsActivity.this);
        return result;
    }

    @Override
    protected void initViews() {
        super.initViews();

        listView.addHeaderView(summaryPresenter.create());

        registerForContextMenu(listView);
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_accounts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_transfer) {
            makeTransfer();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnItemClick(R.id.listView)
    public void onAccountClick(int position) {
        Account account = accountController.readAll().get(position - 1);
        startActivityForResult(EditAccountActivity.Companion.newIntent(this, account), REQUEST_EDIT_ACCOUNT);
    }

    public void makeTransfer() {
        CrashlyticsProxy.get().logButton("Add Transfer");
        startActivityForResult(new Intent(AccountsActivity.this, TransferActivity.class), REQUEST_TRANSFER);
    }

    @OnClick(R.id.btn_add_account)
    public void addAccount() {
        CrashlyticsProxy.get().logButton("Add Account");
        Intent intent = new Intent(AccountsActivity.this, AddAccountActivity.class);
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_ACCOUNT:
                    update();
                    break;

                case REQUEST_TRANSFER:

                case REQUEST_EDIT_ACCOUNT:
                    update();
                    setResult(RESULT_OK);
                    break;

                default:
                    break;
            }
        }
    }

    private void update() {
        listView.setAdapter(new AccountAdapter(AccountsActivity.this, accountController.readAll()));
        summaryPresenter.update();
    }
}
