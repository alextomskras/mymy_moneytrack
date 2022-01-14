package com.dreamer.mymy_moneytrack.ui.presenter.base

import android.content.Context
import android.view.LayoutInflater
import com.dreamer.mymy_moneytrack.R

/**
 * Base summary presenter to encapsulate some common methods.
 * Created on 4/6/16.
 */
abstract class BaseSummaryPresenter {
    protected var context: Context? = null
    protected var layoutInflater: LayoutInflater? = null
    protected fun createRatesNeededList(currency: String?, ratesNeeded: List<String?>): String {
        val sb = StringBuilder(context!!.getString(R.string.error_exchange_rates))
        for (str in ratesNeeded) {
            sb.append("\n").append(str).append(context!!.getString(R.string.arrow)).append(currency)
        }
        return sb.toString()
    }
}