package com.dreamer.mymy_moneytrack.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dreamer.mymy_moneytrack.R
import java.util.*

/**
 *
 */
class ChangeDateDialog(
    context: Context?,
    private val date: Date,
    private val listener: OnDateChangedListener
) : AlertDialog(context) {
    @BindView(R.id.datePicker)
    var datePicker: DatePicker? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_change_date)
        ButterKnife.bind(this@ChangeDateDialog)
        val cal = Calendar.getInstance()
        cal.time = date
        datePicker!!.init(cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH], null)
    }

    @OnClick(R.id.b_ok)
    fun ok() {
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = datePicker!!.year
        cal[Calendar.MONTH] = datePicker!!.month
        cal[Calendar.DAY_OF_MONTH] = datePicker!!.dayOfMonth
        listener.OnDataChanged(cal.time)
        dismiss()
    }

    @OnClick(R.id.b_cancel)
    override fun cancel() {
        dismiss()
    }

    interface OnDateChangedListener {
        fun OnDataChanged(date: Date?)
    }
}