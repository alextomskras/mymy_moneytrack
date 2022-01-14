package com.dreamer.mymy_moneytrack.util.validator

import android.content.Context
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.google.android.material.textfield.TextInputLayout

class AccountValidator(private val context: Context, view: View) : IValidator<Account?> {
    @BindView(R.id.til_title)
    var tilTitle: TextInputLayout? = null

    @BindView(R.id.etTitle)
    var etTitle: EditText? = null

    @BindView(R.id.til_init_sum)
    var tilInitSum: TextInputLayout? = null

    @BindView(R.id.et_init_sum)
    var etInitSum: EditText? = null

    @BindView(R.id.spinner)
    var spinner: AppCompatSpinner? = null
    override fun validate(): Boolean {
        val title = etTitle!!.text.toString().trim { it <= ' ' }
        var initSum = Double.MAX_VALUE
        try {
            initSum = etInitSum!!.text.toString().trim { it <= ' ' }.toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        var valid = true
        if (title.isEmpty()) {
            tilTitle!!.error = context.getString(R.string.field_cant_be_empty)
            valid = false
        }
        if (initSum == Double.MAX_VALUE) {
            tilInitSum!!.error = context.getString(R.string.field_cant_be_empty)
            initSum = 0.0
            valid = false
        }
        if (Math.abs(initSum) > IValidator.MAX_ABS_VALUE) {
            tilInitSum!!.error = context.getString(R.string.too_rich_or_poor)
            valid = false
        }
        return valid
    }

    private fun initTextWatchers() {
        etTitle!!.addTextChangedListener(ClearErrorTextWatcher(tilTitle!!))
        etInitSum!!.addTextChangedListener(ClearErrorTextWatcher(tilInitSum!!))
    }

    init {
        ButterKnife.bind(this, view)
        initTextWatchers()
    }
}