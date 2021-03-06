package com.dreamer.mymy_moneytrack.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamer.mymy_moneytrack.MtApp;
import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.controller.FormatController;
import com.dreamer.mymy_moneytrack.entity.data.Account;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Custom adapter class for Account entity.
 * Created on 6/3/15.
 *
 * @author Evgenii Kanivets
 */
public class AccountAdapter extends BaseAdapter {
    @Inject
    FormatController formatController;

    private final Context context;
    private final List<Account> accounts;

    private final int whiteRed;
    private final int whiteGreen;
    private final int red;
    private final int green;
    private final int grey;

    @SuppressWarnings("deprecation")
    public AccountAdapter(Context context, List<Account> accounts) {
        MtApp.get().getAppComponent().inject(AccountAdapter.this);

        this.context = context;
        this.accounts = accounts;

        whiteRed = context.getResources().getColor(R.color.white_red);
        whiteGreen = context.getResources().getColor(R.color.white_green);
        red = context.getResources().getColor(R.color.red);
        green = context.getResources().getColor(R.color.green);
        grey = context.getResources().getColor(R.color.grey_inactive);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(R.layout.view_account, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();

        Account account = accounts.get(position);

        if (account.isArchived()) {
            convertView.setBackgroundColor(grey);
        } else {
            convertView.setBackgroundColor(account.getFullSum() >= 0.0 ? whiteGreen : whiteRed);
        }

        viewHolder.tvCurSum.setTextColor(account.getFullSum() >= 0.0 ? green : red);
        viewHolder.tvCurrency.setTextColor(account.getFullSum() >= 0.0 ? green : red);

        viewHolder.tvTitle.setText(account.getTitle());
        viewHolder.tvCurSum.setText(formatController.formatSignedAmount(account.getFullSum()));
        viewHolder.tvCurrency.setText(account.getCurrency());

        return convertView;
    }

    public static class ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tv_cur_sum)
        TextView tvCurSum;
        @BindView(R.id.tvCurrency)
        TextView tvCurrency;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}