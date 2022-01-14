package com.dreamer.mymy_moneytrack.util.validator

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.account.TransferActivity
import com.dreamer.mymy_moneytrack.entity.data.Transfer
import com.google.android.material.textfield.TextInputLayout

/**
 * Util class for Transfer validation.
 *
 */
class TransferValidator(context: TransferActivity, view: View) : IValidator<Transfer?> {
    private val context: Context

    @BindView(R.id.spinner_from)
    var spinnerFrom: AppCompatSpinner? = null

    @BindView(R.id.spinner_to)
    var spinnerTo: AppCompatSpinner? = null

    @BindView(R.id.til_from_amount)
    var tilFromAmount: TextInputLayout? = null

    @BindView(R.id.et_from_amount)
    var etFromAmount: EditText? = null

    @BindView(R.id.til_to_amount)
    var tilToAmount: TextInputLayout? = null

    @BindView(R.id.et_to_amount)
    var etToAmount: EditText? = null
    override fun validate(): Boolean {
        var valid = true
        if (!spinnerFrom!!.isEnabled) {
            valid = false
        }
        if (!spinnerTo!!.isEnabled) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show()
            valid = false
        }
        var fromAmount = Double.MAX_VALUE
        try {
            fromAmount = etFromAmount!!.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        if (fromAmount == Double.MAX_VALUE) {
            tilFromAmount!!.error = context.getString(R.string.field_cant_be_empty)
            fromAmount = 0.0
            valid = false
        }
        if (fromAmount > IValidator.MAX_ABS_VALUE) {
            tilFromAmount!!.error = context.getString(R.string.too_much_for_transfer)
            valid = false
        }
        var toAmount = Double.MAX_VALUE
        try {
            toAmount = etToAmount!!.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        if (toAmount == Double.MAX_VALUE) {
            tilToAmount!!.error = context.getString(R.string.field_cant_be_empty)
            toAmount = 0.0
            valid = false
        }
        if (toAmount > IValidator.MAX_ABS_VALUE) {
            tilToAmount!!.error = context.getString(R.string.too_much_for_transfer)
            valid = false
        }
        return valid
    }

    private fun initTextWatchers() {
        etFromAmount!!.addTextChangedListener(ClearErrorTextWatcher(tilFromAmount!!))
        etToAmount!!.addTextChangedListener(ClearErrorTextWatcher(tilToAmount!!))
    }

    init {
        this.context = context
        ButterKnife.bind(this, view)
        initTextWatchers()
    }
}