package com.dreamer.mymy_moneytrack.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.MtApp.Companion.get
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.controller.FormatController
import com.dreamer.mymy_moneytrack.entity.Period
import com.dreamer.mymy_moneytrack.report.record.IRecordReport
import com.dreamer.mymy_moneytrack.ui.presenter.base.BaseSummaryPresenter
import java.text.SimpleDateFormat
import javax.inject.Inject

/**
 * Util class to create and manage summary header view for .
 *
 */
class ShortSummaryPresenter(context: Context) : BaseSummaryPresenter() {

    var formatController: FormatController? = null
        @Inject set
    private val red: Int
    private val green: Int
    private var view: View? = null

    interface ItemClickListener {
        operator fun invoke()
    }

    fun create(shortSummary: Boolean, itemClickListener: (() -> Unit)?): View? {
        view = layoutInflater?.inflate(R.layout.view_summary_records, null)
        view?.findViewById<View>(R.id.iv_more)?.visibility
            ?: if (shortSummary) View.VISIBLE else View.INVISIBLE
        view?.isEnabled = false
        view?.findViewById<View>(R.id.lvSummary)?.isClickable = false
        view?.findViewById<View>(R.id.cvSummary)?.isClickable = true
        view?.tag = ViewHolder(view, itemClickListener)
        return view
    }

    fun update(report: IRecordReport?, currency: String?, ratesNeeded: List<String?>?) {
        val viewHolder = view!!.tag as ViewHolder
        if (report == null) {
            viewHolder.tvTotalIncome!!.text = ""
            viewHolder.tvTotalExpense!!.text = ""
            viewHolder.tvTotal!!.setTextColor(red)
            viewHolder.tvTotal!!.text = ratesNeeded?.let { createRatesNeededList(currency, it) }
        } else {
            viewHolder.tvPeriod!!.text = formatPeriod(report.period)
            viewHolder.tvTotalIncome!!.setTextColor(if (report.totalIncome >= 0) green else red)
            viewHolder.tvTotalIncome!!.text = formatController!!.formatIncome(
                report.totalIncome,
                report.currency
            )
            viewHolder.tvTotalExpense!!.setTextColor(if (report.totalExpense > 0) green else red)
            viewHolder.tvTotalExpense!!.text = formatController!!.formatExpense(
                report.totalExpense,
                report.currency
            )
            viewHolder.tvTotal!!.setTextColor(if (report.total >= 0) green else red)
            viewHolder.tvTotal!!.text = formatController!!.formatIncome(
                report.total,
                report.currency
            )
        }
    }

    private fun formatPeriod(period: Period): String {
        return when (period.type) {
            Period.TYPE_DAY -> period.firstDay
            Period.TYPE_MONTH -> SimpleDateFormat("MMMM, yyyy").format(period.first)
            Period.TYPE_YEAR -> SimpleDateFormat("yyyy").format(period.first)
            Period.TYPE_ALL_TIME -> context?.getString(R.string.all_time).toString()
            else -> context!!.getString(
                R.string.period_from_to, period.firstDay,
                period.lastDay
            )
        }
    }

    class ViewHolder(view: View?, itemClickListener: (() -> Unit)?) : RecyclerView.ViewHolder(
        view!!
    ) {
        @BindView(R.id.tvPeriod)
        var tvPeriod: TextView? = null

        @BindView(R.id.tvTotalIncome)
        var tvTotalIncome: TextView? = null

        @BindView(R.id.tvTotalExpense)
        var tvTotalExpense: TextView? = null

        @BindView(R.id.tvTotal)
        var tvTotal: TextView? = null

        init {
            ButterKnife.bind(this, view!!)
            view.findViewById<View>(R.id.cvSummary)
                .setOnClickListener { itemClickListener?.invoke() }
        }
    }

    init {
        this.context = context
        get()!!.appComponent!!.inject(this@ShortSummaryPresenter)
        layoutInflater = LayoutInflater.from(context)
        red = context.resources.getColor(R.color.red)
        green = context.resources.getColor(R.color.green)
    }
}