package com.dreamer.mymy_moneytrack.util.validator

import android.content.Context
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.entity.data.Account
import com.google.android.material.textfield.TextInputLayout

/**
 * Util class for EditAccount validation.

 */
class EditAccountValidator(private val context: Context, view: View) : IValidator<Account?> {
    @BindView(R.id.tilTitle)
    var tilTitle: TextInputLayout? = null

    @BindView(R.id.etTitle)
    var etTitle: EditText? = null

    @BindView(R.id.tilGoal)
    var tilGoal: TextInputLayout? = null

    @BindView(R.id.etGoal)
    var etGoal: EditText? = null
    override fun validate(): Boolean {
        val title = etTitle!!.text.toString().trim { it <= ' ' }
        var goal = Double.MAX_VALUE
        try {
            goal = etGoal!!.text.toString().trim { it <= ' ' }.toDouble()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        var valid = true
        if (title.isEmpty()) {
            tilTitle!!.error = context.getString(R.string.field_cant_be_empty)
            valid = false
        }
        if (goal == Double.MAX_VALUE) {
            tilGoal!!.error = context.getString(R.string.field_cant_be_empty)
            goal = 0.0
            valid = false
        }
        if (Math.abs(goal) > IValidator.MAX_ABS_VALUE) {
            tilGoal!!.error = context.getString(R.string.too_rich_or_poor)
            valid = false
        }
        return valid
    }

    private fun initTextWatchers() {
        etTitle!!.addTextChangedListener(ClearErrorTextWatcher(tilTitle!!))
        etGoal!!.addTextChangedListener(ClearErrorTextWatcher(tilGoal!!))
    }

    init {
        ButterKnife.bind(this, view)
        initTextWatchers()
    }
}