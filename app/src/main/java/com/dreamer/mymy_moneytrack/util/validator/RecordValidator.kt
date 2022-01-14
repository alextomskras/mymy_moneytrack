package com.dreamer.mymy_moneytrack.util.validator

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.entity.data.Record
import com.google.android.material.textfield.TextInputLayout

/**
 * Util class for Account validation.
 *
 */
class RecordValidator(private val context: Context, view: View) : IValidator<Record?> {
    @BindView(R.id.til_title)
    var tilTitle: TextInputLayout? = null

    @BindView(R.id.etTitle)
    var etTitle: EditText? = null

    @BindView(R.id.til_category)
    var tilCategory: TextInputLayout? = null

    @BindView(R.id.etCategory)
    var etCategory: EditText? = null

    @BindView(R.id.til_price)
    var tilPrice: TextInputLayout? = null

    @BindView(R.id.etPrice)
    var etPrice: EditText? = null

    @BindView(R.id.spinnerAccount)
    var spinnerAccount: AppCompatSpinner? = null
    override fun validate(): Boolean {
        var valid = true
        val category = etCategory!!.text.toString().trim { it <= ' ' }
        if (category.isEmpty()) {
            tilCategory!!.error = context.getString(R.string.field_cant_be_empty)
            valid = false
        }

        //Check if price is valid
        var price = Double.MAX_VALUE
        try {
            price = etPrice!!.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        if (price == Double.MAX_VALUE) {
            tilPrice!!.error = context.getString(R.string.field_cant_be_empty)
            price = 0.0
            valid = false
        }
        if (price > IValidator.MAX_ABS_VALUE) {
            tilPrice!!.error = context.getString(R.string.too_rich)
            valid = false
        }
        if (!spinnerAccount!!.isEnabled) {
            Toast.makeText(context, R.string.one_account_needed, Toast.LENGTH_SHORT).show()
            valid = false
        }
        return valid
    }

    private fun initTextWatchers() {
        etPrice!!.addTextChangedListener(ClearErrorTextWatcher(tilPrice!!))
        etTitle!!.addTextChangedListener(ClearErrorTextWatcher(tilTitle!!))
        etCategory!!.addTextChangedListener(ClearErrorTextWatcher(tilCategory!!))
    }

    init {
        ButterKnife.bind(this, view)
        initTextWatchers()
    }
}