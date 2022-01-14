package com.dreamer.mymy_moneytrack.report.record

import com.dreamer.mymy_moneytrack.entity.Period
import com.dreamer.mymy_moneytrack.report.record.model.CategoryRecord

/**
 * Interface that represents a contract of access to report data.

 */
interface IRecordReport {
    /**
     * @return code of report currency
     */
    val currency: String

    /**
     * @return period of report
     */
    val period: Period

    /**
     * @return total sum in given currency for given period
     */
    val total: Double

    /**
     * @return total of all incomes for given period
     */
    val totalIncome: Double

    /**
     * @return total of all expenses for given period
     */
    val totalExpense: Double

    /**
     * @return summary list
     */
    val summary: List<CategoryRecord?>
}