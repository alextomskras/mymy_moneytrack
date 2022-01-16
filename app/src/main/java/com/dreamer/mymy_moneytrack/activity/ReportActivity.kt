package com.dreamer.mymy_moneytrack.activity

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dreamer.mymy_moneytrack.MtApp
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity
import com.dreamer.mymy_moneytrack.adapter.RecordReportAdapter
import com.dreamer.mymy_moneytrack.controller.CurrencyController
import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.controller.data.RecordController
import com.dreamer.mymy_moneytrack.entity.Period
import com.dreamer.mymy_moneytrack.entity.RecordReportItem
import com.dreamer.mymy_moneytrack.entity.data.Record
import com.dreamer.mymy_moneytrack.report.ReportMaker
import com.dreamer.mymy_moneytrack.report.record.IRecordReport
import com.dreamer.mymy_moneytrack.ui.presenter.ShortSummaryPresenter
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*
import javax.inject.Inject

class ReportActivity : BaseBackActivity() {


    lateinit var recordController: RecordController
        @Inject set

    lateinit var rateController: ExchangeRateController
        @Inject set

    lateinit var currencyController: CurrencyController
        @Inject set

    private var recordList: List<Record> = listOf()
    private var period: Period? = null
    private lateinit var adapter: RecordReportAdapter
    private lateinit var recordReportConverter: RecordReportConverter

    private lateinit var shortSummaryPresenter: ShortSummaryPresenter

    override fun getContentViewId() = R.layout.activity_report

    override fun initData(): Boolean {
        super.initData()
        appComponent.inject(this)

        period = intent.getParcelableExtra(KEY_PERIOD)
        if (period == null) return false

        recordList = recordController.getRecordsForPeriod(period)
        shortSummaryPresenter = ShortSummaryPresenter(this)
        adapter = RecordReportAdapter(mutableListOf(), hashMapOf(), this)
        recordReportConverter = RecordReportConverter()

        return true
    }

    override fun initViews() {
        super.initViews()

        initSpinnerCurrency()

        shortSummaryPresenter.create(shortSummary = false, itemClickListener = null)
            ?.let { adapter.setSummaryView(it) }
        recyclerView.adapter = adapter
    }

    private fun update(currency: String) {
        val reportMaker = ReportMaker(rateController)
        val report = reportMaker.getRecordReport(currency, period, recordList)

        adapter.setData(recordReportConverter.getItemsFromReport(report), recordReportConverter.getDataFromReport(report))
        shortSummaryPresenter.update(report, currency, reportMaker.currencyNeeded(currency, recordList))
    }

    private fun initSpinnerCurrency() {
        val currencyList = currencyController.readAll()

        spinnerCurrency.adapter = ArrayAdapter(this, R.layout.view_spinner_item, currencyList)
        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) =
                    update(spinnerCurrency.selectedItem.toString())

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val currency = currencyController.readDefaultCurrency()

        spinnerCurrency.setSelection(currencyList.indexOf(currency))
    }

    class RecordReportConverter {

        @Inject
        lateinit var formatController: FormatController

        init {
            MtApp.get()?.appComponent?.inject(this)
        }

        fun getItemsFromReport(report: IRecordReport?): MutableList<RecordReportItem> {
            val items: MutableList<RecordReportItem> = mutableListOf()

            if (report == null) return items

            for (categoryRecord in report.summary) {
                val parentRow = categoryRecord?.let {
                    categoryRecord.let { formatController.formatSignedAmount(it.amount) }
                        ?.let { it1 ->
                            RecordReportItem.ParentRow(
                                it.title,
                                it1, false
                            )
                        }
                }
                if (parentRow != null) {
                    items.add(parentRow)
                }
            }
            return items
        }

        fun getDataFromReport(report: IRecordReport?): HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>> {
            val data: HashMap<RecordReportItem.ParentRow, List<RecordReportItem.ChildRow>> = hashMapOf()

            if (report == null) return data

            for (categoryRecord in report.summary) {
                val parentRow = RecordReportItem.ParentRow(
                    categoryRecord?.title.toString(), formatController.formatSignedAmount(
                        categoryRecord?.amount!!
                    ), false
                )
                val childRows: MutableList<RecordReportItem.ChildRow> = mutableListOf()

                for (summaryRecord in categoryRecord.summaryRecordList) {
                    val childRow = RecordReportItem.ChildRow(
                        summaryRecord.title,
                        formatController.formatSignedAmount(summaryRecord.amount)
                    )
                    childRows.add(childRow)
                }

                data[parentRow] = childRows
            }
            return data
        }

    }

    companion object {

        const val KEY_PERIOD = "key_period"
    }
}
