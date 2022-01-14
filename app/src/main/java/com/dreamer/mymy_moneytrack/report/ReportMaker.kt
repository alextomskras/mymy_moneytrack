package com.dreamer.mymy_moneytrack.report

import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.entity.Period
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.dreamer.mymy_moneytrack.entity.data.Record
import com.dreamer.mymy_moneytrack.report.account.AccountsReport
import com.dreamer.mymy_moneytrack.report.account.IAccountsReport
import com.dreamer.mymy_moneytrack.report.base.IExchangeRateProvider
import com.dreamer.mymy_moneytrack.report.chart.IMonthReport
import com.dreamer.mymy_moneytrack.report.chart.MonthReport
import com.dreamer.mymy_moneytrack.report.record.IRecordReport
import com.dreamer.mymy_moneytrack.report.record.RecordReport
import java.util.*

/**
 * Util class to encapsulate [RecordReport] generation logic.

 */
class ReportMaker(private val rateController: ExchangeRateController) {
    fun getRecordReport(
        currency: String,
        period: Period?,
        recordList: List<Record>
    ): IRecordReport? {
        if (currencyNeeded(currency, recordList).size != 0) return null
        val rateProvider: IExchangeRateProvider = ExchangeRateProvider(currency, rateController)
        return RecordReport(currency, period, recordList, rateProvider)
    }

    fun getAccountsReport(currency: String, accountList: List<Account>): IAccountsReport? {
        if (currencyNeededAccounts(currency, accountList).size != 0) return null
        val rateProvider: IExchangeRateProvider = ExchangeRateProvider(currency, rateController)
        return AccountsReport(currency, accountList, rateProvider)
    }

    fun getMonthReport(currency: String, recordList: List<Record>): IMonthReport? {
        if (currencyNeeded(currency, recordList).size != 0) return null
        val rateProvider: IExchangeRateProvider = ExchangeRateProvider(currency, rateController)
        return MonthReport(recordList, currency, rateProvider)
    }

    fun currencyNeeded(currency: String, recordList: List<Record>): List<String> {
        val currencies: MutableSet<String> = TreeSet()
        for (record in recordList) {
            currencies.add(record.currency)
        }
        currencies.remove(currency)
        for (rate in rateController.readAll()) {
            if (rate.toCurrency == currency) currencies.remove(rate.fromCurrency)
        }
        return ArrayList(currencies)
    }

    fun currencyNeededAccounts(currency: String, accountList: List<Account>): List<String> {
        val currencies: MutableSet<String> = TreeSet()
        for (account in accountList) {
            currencies.add(account.currency)
        }
        currencies.remove(currency)
        for (rate in rateController.readAll()) {
            if (rate.toCurrency == currency) currencies.remove(rate.fromCurrency)
        }
        return ArrayList(currencies)
    }
}