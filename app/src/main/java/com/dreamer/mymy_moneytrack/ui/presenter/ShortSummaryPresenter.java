package com.dreamer.mymy_moneytrack.ui.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dreamer.mymy_moneytrack.MtApp;
import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.controller.FormatController;
import com.dreamer.mymy_moneytrack.entity.Period;
import com.dreamer.mymy_moneytrack.report.record.IRecordReport;
import com.dreamer.mymy_moneytrack.ui.presenter.base.BaseSummaryPresenter;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Util class to create and manage summary header view for .

 */
public class ShortSummaryPresenter extends BaseSummaryPresenter {

    @Inject
    FormatController formatController;

    private final int red;
    private final int green;
    private View view;

    public ShortSummaryPresenter(Context context) {
        this.context = context;
        MtApp.get().getAppComponent().inject(ShortSummaryPresenter.this);

        layoutInflater = LayoutInflater.from(context);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
    }

    public interface ItemClickListener {
        void invoke();
    }

    public View create(boolean shortSummary, ItemClickListener itemClickListener) {
        view = layoutInflater.inflate(R.layout.view_summary_records, null);
        view.findViewById(R.id.iv_more).setVisibility(shortSummary ? View.VISIBLE : View.INVISIBLE);
        view.setEnabled(false);
        view.findViewById(R.id.lvSummary).setClickable(false);
        view.findViewById(R.id.cvSummary).setClickable(true);
        view.setTag(new ViewHolder(view, itemClickListener));

        return view;
    }

    public void update(IRecordReport report, String currency, List<String> ratesNeeded) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (report == null) {
            viewHolder.tvTotalIncome.setText("");
            viewHolder.tvTotalExpense.setText("");

            viewHolder.tvTotal.setTextColor(red);
            viewHolder.tvTotal.setText(createRatesNeededList(currency, ratesNeeded));
        } else {
            viewHolder.tvPeriod.setText(formatPeriod(report.getPeriod()));

            viewHolder.tvTotalIncome.setTextColor(report.getTotalIncome() >= 0 ? green : red);
            viewHolder.tvTotalIncome.setText(formatController.formatIncome(report.getTotalIncome(),
                    report.getCurrency()));

            viewHolder.tvTotalExpense.setTextColor(report.getTotalExpense() > 0 ? green : red);
            viewHolder.tvTotalExpense.setText(formatController.formatExpense(report.getTotalExpense(),
                    report.getCurrency()));

            viewHolder.tvTotal.setTextColor(report.getTotal() >= 0 ? green : red);
            viewHolder.tvTotal.setText(formatController.formatIncome(report.getTotal(),
                    report.getCurrency()));
        }
    }

    private String formatPeriod(Period period) {
        switch (period.getType()) {
            case Period.TYPE_DAY:
                return period.getFirstDay();

            case Period.TYPE_MONTH:
                return new SimpleDateFormat("MMMM, yyyy").format(period.getFirst());

            case Period.TYPE_YEAR:
                return new SimpleDateFormat("yyyy").format(period.getFirst());

            case Period.TYPE_ALL_TIME:
                return context.getString(R.string.all_time);

            default:
                return context.getString(R.string.period_from_to, period.getFirstDay(),
                        period.getLastDay());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvPeriod)
        TextView tvPeriod;
        @BindView(R.id.tvTotalIncome)
        TextView tvTotalIncome;
        @BindView(R.id.tvTotalExpense)
        TextView tvTotalExpense;
        @BindView(R.id.tvTotal)
        TextView tvTotal;

        public ViewHolder(View view, final ItemClickListener itemClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.findViewById(R.id.cvSummary).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.invoke();
                }
            });
        }
    }

}