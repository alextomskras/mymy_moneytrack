package com.dreamer.mymy_moneytrack.util.validator

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.entity.ExchangeRatePair
import com.google.android.material.textfield.TextInputLayout

class ExchangeRatePairValidator(private val context: Context, view: View) :
    IValidator<ExchangeRatePair?> {
    @BindView(R.id.spinner_from_currency)
    var spinnerFromCurrency: AppCompatSpinner? = null

    @BindView(R.id.spinner_to_currency)
    var spinnerToCurrency: AppCompatSpinner? = null

    @BindView(R.id.til_buy)
    var tilBuy: TextInputLayout? = null

    @BindView(R.id.et_buy)
    var etBuy: EditText? = null

    @BindView(R.id.til_sell)
    var tilSell: TextInputLayout? = null

    @BindView(R.id.et_sell)
    var etSell: EditText? = null
    override fun validate(): Boolean {
        var valid = true
        var fromCurrency: String? = null
        if (spinnerFromCurrency!!.isEnabled) {
            fromCurrency = spinnerFromCurrency!!.selectedItem as String
        } else {
            valid = false
        }
        var toCurrency: String? = null
        if (spinnerToCurrency!!.isEnabled) {
            toCurrency = spinnerToCurrency!!.selectedItem as String
        } else {
            valid = false
        }
        if (fromCurrency != null && toCurrency != null && fromCurrency == toCurrency) {
            Toast.makeText(context, R.string.same_currencies, Toast.LENGTH_SHORT).show()
            valid = false
        }
        var amountBuy = Double.MAX_VALUE
        try {
            amountBuy = etBuy!!.text.toString().trim { it <= ' ' }.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (amountBuy == Double.MAX_VALUE) {
            tilBuy!!.error = context.getString(R.string.field_cant_be_empty)
            amountBuy = 0.0
            valid = false
        }
        if (amountBuy > IValidator.MAX_ABS_VALUE) {
            tilBuy!!.error = context.getString(R.string.too_much_for_exchange)
            valid = false
        }
        var amountSell = Double.MAX_VALUE
        try {
            amountSell = etSell!!.text.toString().trim { it <= ' ' }.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (amountSell == Double.MAX_VALUE) {
            tilSell!!.error = context.getString(R.string.field_cant_be_empty)
            amountSell = 0.0
            valid = false
        }
        if (amountSell > IValidator.MAX_ABS_VALUE) {
            tilSell!!.error = context.getString(R.string.too_much_for_exchange)
            valid = false
        }
        return valid
    }

    private fun initTextWatchers() {
        etBuy!!.addTextChangedListener(ClearErrorTextWatcher(tilBuy!!))
        etSell!!.addTextChangedListener(ClearErrorTextWatcher(tilSell!!))
    }

    init {
        ButterKnife.bind(this, view)
        initTextWatchers()
    }
}