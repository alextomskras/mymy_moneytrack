package com.dreamer.mymy_moneytrack.activity.charts.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.adapter.MonthSummaryAdapter
import com.dreamer.mymy_moneytrack.report.chart.IMonthReport

/**
 * A simple [Fragment] subclass.
 * Use the [SummaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SummaryFragment : Fragment() {
    private var monthReport: IMonthReport? = null

    @BindView(R.id.listView)
    var listView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            monthReport = requireArguments().getParcelable(ARG_MONTH_REPORT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_summary, container, false)
        initViews(rootView)
        return rootView
    }

    private fun initViews(rootView: View?) {
        if (rootView == null) return
        ButterKnife.bind(this, rootView)
        if (monthReport != null) {
            listView!!.adapter = activity?.let { MonthSummaryAdapter(it, monthReport!!) }
        }
    }

    companion object {
        private const val ARG_MONTH_REPORT = "arg_month_report"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param monthReport report for some period grouped by months.
         * @return A new instance of fragment SummaryFragment.
         */
        fun newInstance(monthReport: IMonthReport?): Fragment {
            val fragment = SummaryFragment()
            val args = Bundle()
            args.putParcelable(ARG_MONTH_REPORT, monthReport)
            fragment.arguments = args
            return fragment
        }
    }
}