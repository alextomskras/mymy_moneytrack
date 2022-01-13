package com.dreamer.mymy_moneytrack.activity.charts;

import android.support.design.widget.TabLayout;
import androidx.core.app.Fragment;
import androidx.core.view.ViewPager;

import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity;
import com.dreamer.mymy_moneytrack.activity.charts.fragment.GraphFragment;
import com.dreamer.mymy_moneytrack.activity.charts.fragment.SummaryFragment;
import com.dreamer.mymy_moneytrack.adapter.GeneralViewPagerAdapter;
import com.dreamer.mymy_moneytrack.controller.CurrencyController;
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController;
import com.dreamer.mymy_moneytrack.controller.data.RecordController;
import com.dreamer.mymy_moneytrack.entity.data.Record;
import com.dreamer.mymy_moneytrack.report.ReportMaker;
import com.dreamer.mymy_moneytrack.report.chart.IMonthReport;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ChartsActivity extends BaseBackActivity {

    @Inject RecordController recordController;
    @Inject ExchangeRateController exchangeRateController;
    @Inject CurrencyController currencyController;

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override protected int getContentViewId() {
        return R.layout.activity_charts;
    }

    @Override protected boolean initData() {
        boolean result = super.initData();
        getAppComponent().inject(ChartsActivity.this);
        return result;
    }

    @Override protected void initViews() {
        super.initViews();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected String createRatesNeededList(String currency, List<String> ratesNeeded) {
        StringBuilder sb = new StringBuilder(getString(R.string.error_exchange_rates));

        for (String str : ratesNeeded) {
            sb.append("\n").append(str).append(getString(R.string.arrow)).append(currency);
        }

        return sb.toString();
    }

    private void setupViewPager(ViewPager viewPager) {
        ReportMaker reportMaker = new ReportMaker(exchangeRateController);
        String currency = currencyController.readDefaultCurrency();
        List<Record> recordList = recordController.readAll();
        List<String> currencyNeeded = reportMaker.currencyNeeded(currency, recordList);

        IMonthReport monthReport = null;
        if (currencyNeeded.isEmpty()) monthReport = reportMaker.getMonthReport(currency, recordList);

        Fragment graphFragment;
        if (monthReport == null) {
            graphFragment = GraphFragment.newInstance(createRatesNeededList(currency, currencyNeeded));
        } else {
            graphFragment = GraphFragment.newInstance(monthReport);
        }

        GeneralViewPagerAdapter adapter = new GeneralViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SummaryFragment.newInstance(monthReport), getString(R.string.summary));
        adapter.addFragment(graphFragment, getString(R.string.graph));
        viewPager.setAdapter(adapter);
    }
}
