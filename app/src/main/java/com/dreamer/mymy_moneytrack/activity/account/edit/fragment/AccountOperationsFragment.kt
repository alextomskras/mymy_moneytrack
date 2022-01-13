package com.dreamer.mymy_moneytrack.activity.account.edit.fragment

import android.os.Bundle
import android.view.View
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.base.BaseFragment
import com.dreamer.mymy_moneytrack.adapter.RecordAdapter
import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.controller.data.AccountController
import com.dreamer.mymy_moneytrack.controller.data.RecordController
import com.dreamer.mymy_moneytrack.controller.data.TransferController
import com.dreamer.mymy_moneytrack.entity.RecordItem
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.dreamer.mymy_moneytrack.entity.data.Category
import com.dreamer.mymy_moneytrack.entity.data.Record
import com.dreamer.mymy_moneytrack.entity.data.Transfer
import com.dreamer.mymy_moneytrack.util.RecordItemsBuilder
import kotlinx.android.synthetic.main.fragment_account_operations.*
import javax.inject.Inject

class AccountOperationsFragment : BaseFragment() {

    @Inject
    internal lateinit var accountController: AccountController
    @Inject
    internal lateinit var recordController: RecordController
    @Inject
    internal lateinit var transferController: TransferController
    @Inject
    internal lateinit var formatController: FormatController

    private lateinit var account: Account

    override val contentViewId: Int = R.layout.fragment_account_operations

    override fun initData() {
        appComponent.inject(this@AccountOperationsFragment)
        arguments?.let { arguments -> account = arguments.getParcelable(KEY_ACCOUNT) }
    }

    override fun initViews(view: View) {
        recyclerView.adapter = RecordAdapter(requireContext(), getRecordItems(), false)
    }

    private fun getRecordItems(): List<RecordItem> {
        val accountRecords = recordController.getRecordsForAccount(account)
        val accountTransfers = transferController.getTransfersForAccount(account)

        accountRecords += obtainRecordsFromTransfers(accountTransfers)
        accountRecords.sortByDescending { it.time }

        return RecordItemsBuilder().getRecordItems(accountRecords)
    }

    private fun obtainRecordsFromTransfers(transfers: List<Transfer>): List<Record> {
        val records = mutableListOf<Record>()

        transfers.forEach {
            val type = if (it.fromAccountId == account.id) Record.TYPE_EXPENSE else Record.TYPE_INCOME
            val title = constructRecordTitle(type, it)
            val category = Category(getString(R.string.transfer).toLowerCase())
            val price = if (type == Record.TYPE_EXPENSE) it.fromAmount else it.toAmount
            val decimals = if (type == Record.TYPE_EXPENSE) it.fromDecimals else it.toDecimals

            records += Record(it.id, it.time, type, title, category, price, account, account.currency, decimals)
        }

        return records.toList()
    }

    private fun constructRecordTitle(type: Int, transfer: Transfer): String {
        val titlePrefix = getString(if (type == Record.TYPE_EXPENSE) R.string.to else R.string.from)
        val oppositeAccountId = if (type == Record.TYPE_EXPENSE) transfer.toAccountId else transfer.fromAccountId
        val oppositeAccountTitle = "$titlePrefix ${accountController.read(oppositeAccountId)?.title}"
        return "${getString(R.string.transfer)} $oppositeAccountTitle".toLowerCase()
    }

    companion object {

        private const val KEY_ACCOUNT = "key_account"

        fun newInstance(account: Account): AccountOperationsFragment {
            val fragment = AccountOperationsFragment()
            val arguments = Bundle()
            arguments.putParcelable(KEY_ACCOUNT, account)
            fragment.arguments = arguments
            return fragment
        }

    }

}
