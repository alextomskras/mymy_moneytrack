package com.dreamer.mymy_moneytrack.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.MtApp.Companion.get
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.controller.CurrencyController
import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.controller.data.AccountController
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.report.ReportMaker
import com.dreamer.mymy_moneytrack.ui.presenter.base.BaseSummaryPresenter
import javax.inject.Inject

/**
 * Util class to create and manage summary header view for .
 *
 */
class AccountsSummaryPresenter(context: Context) : BaseSummaryPresenter() {
    @Inject
    var rateController: ExchangeRateController? = null

    @Inject
    var accountController: AccountController? = null

    @Inject
    var currencyController: CurrencyController? = null

    @Inject
    var formatController: FormatController? = null
    private val red: Int
    private val green: Int
    private var view: View? = null
    private val reportMaker: ReportMaker
    fun create(): View? {
        view = layoutInflater.inflate(R.layout.view_summary_accounts, null)
        val viewHolder = ViewHolder(view)
        view?.tag = viewHolder
        val currencyList = currencyController!!.readAll()
        viewHolder.spinnerCurrency!!.adapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1, currencyList
        )
        val currency = currencyController!!.readDefaultCurrency()
        for (i in currencyList.indices) {
            val item = currencyList[i]
            if (item == currency) {
                viewHolder.spinnerCurrency!!.setSelection(i)
                break
            }
        }
        viewHolder.spinnerCurrency!!.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    update()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        return view
    }

    fun update() {
        val viewHolder = view!!.tag as ViewHolder
        val currency = viewHolder.spinnerCurrency!!.selectedItem as String
        val report = reportMaker.getAccountsReport(currency, accountController!!.readAll())
        if (report == null) {
            viewHolder.tvTotal!!.setTextColor(red)
            viewHolder.tvTotal!!.text = createRatesNeededList(
                currency,
                reportMaker.currencyNeededAccounts(currency, accountController!!.readAll())
            )
            viewHolder.tvCurrency!!.text = ""
        } else {
            viewHolder.tvTotal!!.setTextColor(if (report.total >= 0) green else red)
            viewHolder.tvTotal!!.text = formatController!!.formatSignedAmount(report.total)
            viewHolder.tvCurrency!!.setTextColor(if (report.total >= 0) green else red)
            viewHolder.tvCurrency!!.text = report.currency
        }
    }

    class ViewHolder(view: View?) {
        @BindView(R.id.spinnerCurrency)
        var spinnerCurrency: AppCompatSpinner? = null

        @BindView(R.id.tvTotal)
        var tvTotal: TextView? = null

        @BindView(R.id.tvCurrency)
        var tvCurrency: TextView? = null

        init {
            ButterKnife.bind(this, view!!)
        }
    }

    init {
        this.context = context
        layoutInflater = LayoutInflater.from(context)
        red = context.resources.getColor(R.color.red)
        green = context.resources.getColor(R.color.green)
        get()!!.appComponent!!.inject(this@AccountsSummaryPresenter)
        reportMaker = ReportMaker(rateController)
    }
}