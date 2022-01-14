package com.dreamer.mymy_moneytrack.report.record;

import android.support.annotation.NonNull;

import com.dreamer.mymy_moneytrack.entity.Period;
import com.dreamer.mymy_moneytrack.report.record.model.CategoryRecord;

import java.util.List;

/**
 * Interface that represents a contract of access to report data.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public interface IRecordReport {
    /**
     * @return code of report currency
     */
    @NonNull String getCurrency();

    /**
     * @return period of report
     */
    @NonNull Period getPeriod();

    /**
     * @return total sum in given currency for given period
     */
    double getTotal();

    /**
     * @return total of all incomes for given period
     */
    double getTotalIncome();

    /**
     * @return total of all expenses for given period
     */
    double getTotalExpense();

    /**
     * @return summary list
     */
    @NonNull List<CategoryRecord> getSummary();
}