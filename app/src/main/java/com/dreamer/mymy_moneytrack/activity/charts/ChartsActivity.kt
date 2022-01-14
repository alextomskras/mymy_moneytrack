package com.dreamer.mymy_moneytrack.activity.charts


import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity
import com.dreamer.mymy_moneytrack.activity.charts.fragment.GraphFragment
import com.dreamer.mymy_moneytrack.activity.charts.fragment.SummaryFragment
import com.dreamer.mymy_moneytrack.adapter.GeneralViewPagerAdapter
import com.dreamer.mymy_moneytrack.controller.CurrencyController
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.controller.data.RecordController
import com.dreamer.mymy_moneytrack.report.ReportMaker
import com.dreamer.mymy_moneytrack.report.chart.IMonthReport
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

class ChartsActivity : BaseBackActivity() {
    @Inject
    var recordController: RecordController? = null

    @Inject
    var exchangeRateController: ExchangeRateController? = null

    @Inject
    var currencyController: CurrencyController? = null

    @BindView(R.id.tabs)
    var tabLayout: TabLayout? = null

    @BindView(R.id.view_pager)
    var viewPager: ViewPager? = null
    override fun getContentViewId(): Int {
        return R.layout.activity_charts
    }

    override fun initData(): Boolean {
        val result = super.initData()
        appComponent.inject(this@ChartsActivity)
        return result
    }

    override fun initViews() {
        super.initViews()
        setupViewPager(viewPager)
        tabLayout!!.setupWithViewPager(viewPager)
    }

    protected fun createRatesNeededList(currency: String?, ratesNeeded: List<String?>): String {
        val sb = StringBuilder(getString(R.string.error_exchange_rates))
        for (str in ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency)
        }
        return sb.toString()
    }

    private fun setupViewPager(viewPager: ViewPager?) {
        val reportMaker = ReportMaker(exchangeRateController)
        val currency = currencyController!!.readDefaultCurrency()
        val recordList = recordController!!.readAll()
        val currencyNeeded = reportMaker.currencyNeeded(currency, recordList)
        var monthReport: IMonthReport? = null
        if (currencyNeeded.isEmpty()) monthReport = reportMaker.getMonthReport(currency, recordList)
        val graphFragment: GraphFragment
        graphFragment = if (monthReport == null) {
            GraphFragment.newInstance(createRatesNeededList(currency, currencyNeeded))
        } else {
            GraphFragment.newInstance(monthReport)
        }
        val adapter = GeneralViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SummaryFragment.newInstance(monthReport), getString(R.string.summary))
        adapter.addFragment(graphFragment, getString(R.string.graph))
        viewPager!!.adapter = adapter
    }
}