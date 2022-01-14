package com.dreamer.mymy_moneytrack.activity.account


import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import butterknife.BindView
import butterknife.OnClick
import butterknife.OnItemClick
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.account.edit.EditAccountActivity.Companion.newIntent
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity
import com.dreamer.mymy_moneytrack.adapter.AccountAdapter
import com.dreamer.mymy_moneytrack.controller.data.AccountController
import com.dreamer.mymy_moneytrack.ui.presenter.AccountsSummaryPresenter
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy.Companion.get
import javax.inject.Inject

//import static androidx.core.app.ActivityCompat.startActivityForResult;
class AccountsActivity : BaseBackActivity() {
    @Inject
    var accountController: AccountController? = null
    private var summaryPresenter: AccountsSummaryPresenter? = null

    @BindView(R.id.listView)
    var listView: ListView? = null
    override fun getContentViewId(): Int {
        return R.layout.activity_accounts
    }

    override fun initData(): Boolean {
        val result = super.initData()
        appComponent.inject(this@AccountsActivity)
        summaryPresenter = AccountsSummaryPresenter(this)
        return result
    }

    override fun initViews() {
        super.initViews()
        listView!!.addHeaderView(summaryPresenter!!.create())
        registerForContextMenu(listView)
        update()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_accounts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_transfer) {
            makeTransfer()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @OnItemClick(R.id.listView)
    fun onAccountClick(position: Int) {
        val account = accountController!!.readAll()[position - 1]
        startActivityForResult(newIntent(this, account), REQUEST_EDIT_ACCOUNT)
    }

    fun makeTransfer() {
        get()!!.logButton("Add Transfer")
        startActivityForResult(
            Intent(this@AccountsActivity, TransferActivity::class.java),
            REQUEST_TRANSFER
        )
    }

    @OnClick(R.id.btn_add_account)
    fun addAccount() {
        get()!!.logButton("Add Account")
        val intent = Intent(this@AccountsActivity, AddAccountActivity::class.java)
        startActivityForResult(intent, REQUEST_ADD_ACCOUNT)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_ADD_ACCOUNT -> update()
                REQUEST_TRANSFER, REQUEST_EDIT_ACCOUNT -> {
                    update()
                    setResult(RESULT_OK)
                }
                else -> {}
            }
        }
    }

    private fun update() {
        listView!!.adapter = AccountAdapter(this@AccountsActivity, accountController!!.readAll())
        summaryPresenter!!.update()
    }

    companion object {
        private const val TAG = "AccountsActivity"
        private const val REQUEST_ADD_ACCOUNT = 1
        private const val REQUEST_TRANSFER = 2
        private const val REQUEST_EDIT_ACCOUNT = 3
    }
}