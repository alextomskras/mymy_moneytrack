package com.dreamer.mymy_moneytrack.report.base;


import androidx.annotation.Nullable;

import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.dreamer.mymy_moneytrack.entity.data.ExchangeRate;
import com.dreamer.mymy_moneytrack.entity.data.Record;

/**
 * Interface that represents a contract of access to currency exchange rate.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
public interface IExchangeRateProvider {
    /**
     * Gives an exchange rate for given record.
     *
     * @param record to request an exchange rate for
     * @return exchange rate
     */
    @Nullable
    ExchangeRate getRate(@Nullable Record record);

    /**
     * Gives an exchange rate for given account.
     *
     * @param account to request an exchange rate for
     * @return exchange rate
     */
    @Nullable
    ExchangeRate getRate(@Nullable Account account);
}