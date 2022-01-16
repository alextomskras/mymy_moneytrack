package com.dreamer.mymy_moneytrack.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatSpinner
import com.dreamer.mymy_moneytrack.MtApp
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.controller.PeriodController
import com.dreamer.mymy_moneytrack.entity.Period
import java.util.*
import javax.inject.Inject

/**
 * Custom Spinner view to encapsulate a Period logic.
 *
 */
class PeriodSpinner : AppCompatSpinner {
//    var context: Context? = null


    var periodController: PeriodController? = null
    @Inject set
    private var periodSelectedListener: OnPeriodSelectedListener? = null
        @Inject set
    private var listener: OnItemSelectedListener? = null
    private var lastPeriod: Period? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    fun updatePeriod(period: Period) {
        if (lastPeriod != null && lastPeriod == period) return
        if (periodSelectedListener != null) periodSelectedListener!!.onPeriodSelected(period)
        lastPeriod = period
    }

    fun setPeriod(period: Period) {
        when (period.type) {
            Period.TYPE_DAY -> setSelection(0)
            Period.TYPE_WEEK -> setSelection(1)
            Period.TYPE_MONTH -> setSelection(2)
            Period.TYPE_YEAR -> setSelection(3)
            Period.TYPE_ALL_TIME -> setSelection(4)
            Period.TYPE_CUSTOM -> {
                super.setSelection(5)
                updatePeriod(period)
            }
        }
    }

    fun setPeriodSelectedListener(periodSelectedListener: (Any) -> Unit) {
        this.periodSelectedListener = periodSelectedListener as OnPeriodSelectedListener
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (listener != null) listener!!.onItemSelected(null, null, position, 0)
    }

    fun setOnItemSelectedEvenIfUnchangedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

    private fun init(context: Context) {
        var context = context
        MtApp.get()?.appComponent?.inject(this@PeriodSpinner)
        adapter = ArrayAdapter(
            context, android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.array_periods)
        )
        setOnItemSelectedEvenIfUnchangedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                val calendar = Calendar.getInstance()
                calendar.clear(Calendar.MINUTE)
                calendar.clear(Calendar.SECOND)
                calendar.clear(Calendar.MILLISECOND)
                when (position) {
                    0 -> updatePeriod(periodController!!.dayPeriod())
                    1 -> updatePeriod(periodController!!.weekPeriod())
                    2 -> updatePeriod(periodController!!.monthPeriod())
                    3 -> updatePeriod(periodController!!.yearPeriod())
                    4 -> updatePeriod(periodController!!.allTimePeriod())
                    5 ->                         // Custom period selection
                        showFromDateDialog()
                    else -> {}
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun showFromDateDialog() {
        if (lastPeriod == null) return
        val dialog = ChangeDateDialog(context, lastPeriod!!.first,
            object : ChangeDateDialog.OnDateChangedListener {
                override fun OnDataChanged(fromDate: Date?) {
                    val cal = Calendar.getInstance()
                    cal.time = fromDate
                    cal[Calendar.HOUR_OF_DAY] = 0
                    cal[Calendar.MINUTE] = 0
                    cal[Calendar.SECOND] = 0
                    cal[Calendar.MILLISECOND] = 0
                    showToDateDialog(cal.time)
                }
            })
        dialog.show()
    }

    private fun showToDateDialog(fromDate: Date) {
        if (lastPeriod == null) return
        val dialog = ChangeDateDialog(context, lastPeriod!!.last,
            object : ChangeDateDialog.OnDateChangedListener {
                override fun OnDataChanged(toDate: Date?) {
                    val cal = Calendar.getInstance()
                    cal.time = toDate
                    cal[Calendar.HOUR_OF_DAY] = 23
                    cal[Calendar.MINUTE] = 59
                    cal[Calendar.SECOND] = 59
                    cal[Calendar.MILLISECOND] = 999
                    if (cal.time.time < fromDate.time) {
                        Toast.makeText(context, R.string.start_earlier_end, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        updatePeriod(Period(fromDate, cal.time, Period.TYPE_CUSTOM))
                    }
                }
            })
        dialog.show()
    }

    interface OnPeriodSelectedListener {
        fun onPeriodSelected(period: Period?)
    }
}