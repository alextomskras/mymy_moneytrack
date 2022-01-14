package com.dreamer.mymy_moneytrack.report.record.model

import com.dreamer.mymy_moneytrack.MtApp.Companion.get
import com.dreamer.mymy_moneytrack.R

/**
 * Entity class.
 *
 */
class SummaryRecord(title: String, currency: String, amount: Double, recordsCount: Int) {
    val title: String
    val currency: String
    val amount: Double
    private fun buildTitle(title: String, recordsCount: Int): String {
        return if (recordsCount <= 1) title else get()!!
            .resources.getString(R.string.title_summary_record, title, recordsCount)
    }

    init {
        this.title = buildTitle(title, recordsCount)
        this.currency = currency
        this.amount = amount
    }
}