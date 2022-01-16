package com.dreamer.mymy_moneytrack.activity.record

import android.content.Intent


import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.ReportActivity
import com.dreamer.mymy_moneytrack.activity.base.BaseDrawerActivity
import com.dreamer.mymy_moneytrack.adapter.RecordAdapter
import com.dreamer.mymy_moneytrack.controller.CurrencyController
import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.controller.PeriodController
import com.dreamer.mymy_moneytrack.controller.PreferenceController
import com.dreamer.mymy_moneytrack.controller.data.AccountController
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.controller.data.RecordController
import com.dreamer.mymy_moneytrack.entity.Period
import com.dreamer.mymy_moneytrack.entity.RecordItem
import com.dreamer.mymy_moneytrack.entity.data.Record
import com.dreamer.mymy_moneytrack.report.ReportMaker
import com.dreamer.mymy_moneytrack.ui.AppRateDialog
import com.dreamer.mymy_moneytrack.ui.presenter.ShortSummaryPresenter
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy
import com.dreamer.mymy_moneytrack.util.RecordItemsBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : BaseDrawerActivity() {

    private lateinit var recordList: List<Record>
    private var recordItems: List<RecordItem> = listOf()
    private lateinit var period: Period
    private lateinit var recordAdapter: RecordAdapter

    @Inject
    lateinit var recordController: RecordController
    @Inject
    lateinit var rateController: ExchangeRateController
    @Inject
    lateinit var accountController: AccountController
    @Inject
    lateinit var currencyController: CurrencyController
    @Inject
    lateinit var preferenceController: PreferenceController
    @Inject
    lateinit var periodController: PeriodController
    @Inject
    lateinit var formatController: FormatController

    private lateinit var tvDefaultAccountTitle: TextView
    private lateinit var tvDefaultAccountSum: TextView
    private lateinit var tvCurrency: TextView
    private lateinit var summaryPresenter: ShortSummaryPresenter

    override fun getContentViewId(): Int = R.layout.activity_main

    override fun initData(): Boolean {
        super.initData()
        appComponent.inject(this)

        preferenceController.addLaunchCount()

        return super.initData()
    }

    override fun initViews() {
        super.initViews()

        setTitle(R.string.title_records)

        if (preferenceController.checkRateDialog()) showAppRateDialog()

        tvDefaultAccountTitle = navigationView.getHeaderView(0).findViewById(R.id.tvDefaultAccountTitle)
        tvDefaultAccountSum = navigationView.getHeaderView(0).findViewById(R.id.tvDefaultAccountSum)
        tvCurrency = navigationView.getHeaderView(0).findViewById(R.id.tvCurrency)

        recordAdapter = RecordAdapter(this, listOf(), true)
        recordAdapter.itemClickListener =
            { position -> editRecord(getPositionWithoutSummary(position)) }

        summaryPresenter = ShortSummaryPresenter(this)
        val summaryViewHolder =
            summaryPresenter.create(true) { showReport() }?.tag as RecyclerView.ViewHolder
        recordAdapter.summaryViewHolder = summaryViewHolder

        recyclerView.adapter = recordAdapter

        spinner.setPeriodSelectedListener {
            this.period = it as Period
            periodController.writeLastUsedPeriod(it)
            update()
        }

        spinner.setPeriod(periodController.readLastUsedPeriod())

        btnAddExpense.setOnClickListener { addExpense() }
        btnAddIncome.setOnClickListener { addIncome() }
    }

    private fun getPositionWithoutSummary(position: Int) = position - 1

    private fun editRecord(position: Int) {

        CrashlyticsProxy.get()?.logButton("Edit Record")
        val record = recordList[getRecordPosition(position)]

        startAddRecordActivity(record, AddRecordActivity.Mode.MODE_EDIT, record.type)
    }

    private fun addExpense() {
        CrashlyticsProxy.get()?.logButton("Add Expense")
        startAddRecordActivity(null, AddRecordActivity.Mode.MODE_ADD, Record.TYPE_EXPENSE)
    }

    private fun addIncome() {
        CrashlyticsProxy.get()?.logButton("Add Income")
        startAddRecordActivity(null, AddRecordActivity.Mode.MODE_ADD, Record.TYPE_INCOME)
    }

    private fun showReport() {
        CrashlyticsProxy.get()?.logButton("Show Report")
        val intent = Intent(this, ReportActivity::class.java)
        intent.putExtra(ReportActivity.KEY_PERIOD, period)
        startActivity(intent)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                REQUEST_ACTION_RECORD -> update()

                REQUEST_BACKUP -> {
                    appComponent.inject(this)
                    update()
                }

                else -> {
                }
            }
        }
    }

    override fun update() {
        recordList = recordController.getRecordsForPeriod(period)
        recordList = recordList.reversed()
        recordItems = RecordItemsBuilder().getRecordItems(recordList)

        val currency = currencyController.readDefaultCurrency()

        val reportMaker = ReportMaker(rateController)
        val report = reportMaker.getRecordReport(currency, period, recordList)

        summaryPresenter.update(report, currency, reportMaker.currencyNeeded(currency, recordList))
        recordAdapter.setRecords(recordItems)

        fillDefaultAccount()
    }

    private fun getRecordPosition(position: Int): Int {
        var recordPosition = 0

        for (indexOfItem in 0 until position) {
            if (recordItems[indexOfItem] is RecordItem.Record) {
                recordPosition++
            }
        }
        return recordPosition
    }

    private fun showAppRateDialog() {
        CrashlyticsProxy.get()?.logEvent("Show App Rate Dialog")
        val dialog = AppRateDialog(this)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun startAddRecordActivity(record: Record?, mode: AddRecordActivity.Mode, type: Int) {
        val intent = Intent(this, AddRecordActivity::class.java)
        intent.putExtra(AddRecordActivity.KEY_RECORD, record)
        intent.putExtra(AddRecordActivity.KEY_MODE, mode)
        intent.putExtra(AddRecordActivity.KEY_TYPE, type)
        startActivityForResult(intent, REQUEST_ACTION_RECORD)
    }

    private fun fillDefaultAccount() {
        val defaultAccount = accountController.readDefaultAccount() ?: return

        tvDefaultAccountSum.text = defaultAccount.title
        tvDefaultAccountSum.text = formatController.formatAmount(defaultAccount.fullSum)
        tvCurrency.text = defaultAccount.currency
    }

    companion object {
        private const val REQUEST_ACTION_RECORD = 6
    }

}