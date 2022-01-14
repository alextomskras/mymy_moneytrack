package com.dreamer.mymy_moneytrack.report

import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.dreamer.mymy_moneytrack.entity.data.ExchangeRate
import com.dreamer.mymy_moneytrack.entity.data.Record
import com.dreamer.mymy_moneytrack.report.base.IExchangeRateProvider
import java.util.*

/**
 * First [IExchangeRateProvider] implementation.
 * Created on 2/25/16.
 *
 * @author Evgenii Kanivets
 */
class ExchangeRateProvider(private val toCurrency: String, controller: ExchangeRateController) :
    IExchangeRateProvider {
    private val rateMap: Map<String, ExchangeRate>
    override fun getRate(record: Record?): ExchangeRate? {
        if (record == null) return null
        val fromCurrency = record.currency
        return rateMap[fromCurrency]
    }

    override fun getRate(account: Account?): ExchangeRate? {
        if (account == null) return null
        val fromCurrency = account.currency
        return rateMap[fromCurrency]
    }

    private fun getRateMap(exchangeRateList: List<ExchangeRate>): Map<String, ExchangeRate> {
        val rateMap: MutableMap<String, ExchangeRate> = TreeMap()
        Collections.sort(exchangeRateList) { lhs, rhs -> if (lhs.createdAt < rhs.createdAt) -1 else if (lhs.createdAt == rhs.createdAt) 0 else 1 }
        for (rate in exchangeRateList) {
            if (toCurrency != rate.toCurrency) continue
            rateMap[rate.fromCurrency] = rate
        }
        return rateMap
    }

    companion object {
        private const val TAG = "ExchangeRateProvider"
    }

    /**
     * @param toCurrency code of toCurrency to convert to
     * @param controller dependency to get needed rates
     */
    init {
        rateMap = getRateMap(controller.readAll())
    }
}