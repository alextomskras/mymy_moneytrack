package com.dreamer.mymy_moneytrack.activity.charts.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.BindView
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.report.chart.BarChartConverter
import com.dreamer.mymy_moneytrack.report.chart.IMonthReport
import kotlinx.android.synthetic.main.fragment_graph.*


/**
 * A simple [Fragment] subclass.
 * Use the [GraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GraphFragment : Fragment() {
    private var monthReport: IMonthReport? = null
    private var noDataText: String? = null

    //    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bar_chart)
    var barChart: BarChart? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            monthReport = arguments!!.getParcelable(ARG_MONTH_REPORT)
            noDataText = arguments!!.getString(ARG_NO_DATA_TEXT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(
            layout,
            container,
            false
        )
        initViews(rootView)
        return rootView
    }

    private fun initViews(rootView: View?) {
        if (rootView == null) return
        BindView(this, rootView)
        if (monthReport == null) {
            barChart.setNoDataText(noDataText)
        } else {
            val barChartConverter = BarChartConverter(activity, monthReport)
            val barData = BarData(
                barChartConverter.xAxisValueList,
                barChartConverter.barDataSetList
            )
            barData.setDrawValues(false)
            barChart.setData(barData)
            barChart.setDescription(null)
            barChart.setVisibleXRangeMinimum(8)
            barChart.setScaleYEnabled(false)
            barChart.setVisibleXRangeMaximum(34)
            barChart.setHighlightPerDragEnabled(false)
            barChart.setHighlightPerTapEnabled(false)
        }
    }

    companion object {
        private const val ARG_MONTH_REPORT = "arg_month_report"
        private const val ARG_NO_DATA_TEXT = "arg_no_data_text"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param monthReport report for some period grouped by months.
         * @return A new instance of fragment GraphFragment.
         */
        fun newInstance(monthReport: IMonthReport): GraphFragment {
            val fragment = GraphFragment()
            val args = Bundle()
            args.putParcelable(ARG_MONTH_REPORT, monthReport)
            fragment.arguments = args
            return fragment
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param noDataText text that will be displayed in case of error.
         * @return A new instance of fragment GraphFragment.
         */
        fun newInstance(noDataText: String): GraphFragment {
            val fragment = GraphFragment()
            val args = Bundle()
            args.putString(ARG_NO_DATA_TEXT, noDataText)
            fragment.arguments = args
            return fragment
        }
    }
}