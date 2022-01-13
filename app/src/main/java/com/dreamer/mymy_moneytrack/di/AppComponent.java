package com.dreamer.mymy_moneytrack.di;

import com.dreamer.mymy_moneytrack.activity.account.edit.EditAccountActivity;
import com.dreamer.mymy_moneytrack.activity.account.edit.fragment.AccountOperationsFragment;
import com.dreamer.mymy_moneytrack.activity.account.edit.fragment.EditAccountFragment;
import com.dreamer.mymy_moneytrack.activity.charts.ChartsActivity;
import com.dreamer.mymy_moneytrack.activity.external.BackupActivity;
import com.dreamer.mymy_moneytrack.activity.external.ImportExportActivity;
import com.dreamer.mymy_moneytrack.activity.ReportActivity;
import com.dreamer.mymy_moneytrack.activity.SettingsActivity;
import com.dreamer.mymy_moneytrack.activity.account.AccountsActivity;
import com.dreamer.mymy_moneytrack.activity.account.AddAccountActivity;
import com.dreamer.mymy_moneytrack.activity.account.TransferActivity;
import com.dreamer.mymy_moneytrack.activity.exchange_rate.AddExchangeRateActivity;
import com.dreamer.mymy_moneytrack.activity.exchange_rate.ExchangeRatesActivity;
import com.dreamer.mymy_moneytrack.activity.record.AddRecordActivity;
import com.dreamer.mymy_moneytrack.activity.record.MainActivity;
import com.dreamer.mymy_moneytrack.adapter.AccountAdapter;
import com.dreamer.mymy_moneytrack.adapter.ExchangeRateAdapter;
import com.dreamer.mymy_moneytrack.adapter.MonthSummaryAdapter;
import com.dreamer.mymy_moneytrack.adapter.RecordAdapter;
import com.dreamer.mymy_moneytrack.di.module.repo.CachedRepoModule;
import com.dreamer.mymy_moneytrack.di.module.ControllerModule;
import com.dreamer.mymy_moneytrack.ui.AppRateDialog;
import com.dreamer.mymy_moneytrack.ui.PeriodSpinner;
import com.dreamer.mymy_moneytrack.ui.presenter.AccountsSummaryPresenter;
import com.dreamer.mymy_moneytrack.ui.presenter.ShortSummaryPresenter;
import com.dreamer.mymy_moneytrack.util.RecordItemsBuilder;

import javax.inject.Singleton;

import dagger.Component;


@Component(modules = {CachedRepoModule.class, ControllerModule.class})
@Singleton
public interface AppComponent {
    void inject(MainActivity mainActivity);

    void inject(AddRecordActivity addRecordActivity);

    void inject(ExchangeRatesActivity exchangeRatesActivity);

    void inject(AddExchangeRateActivity exchangeRateActivity);

    void inject(AccountsActivity accountsActivity);

    void inject(AddAccountActivity addAccountActivity);

    void inject(TransferActivity transferActivity);

    void inject(ImportExportActivity importExportActivity);

    void inject(ReportActivity reportActivity);

    void inject(ChartsActivity chartsActivity);

    void inject(BackupActivity backupActivity);

    void inject(SettingsActivity.SettingsFragment settingsFragment);

    void inject(AccountsSummaryPresenter accountsSummaryPresenter);

    void inject(AppRateDialog appRateDialog);

    void inject(PeriodSpinner periodSpinner);

    void inject(RecordAdapter recordAdapter);

    void inject(AccountAdapter accountAdapter);

    void inject(ShortSummaryPresenter shortSummaryPresenter);

    void inject(ExchangeRateAdapter exchangeRateAdapter);

    void inject(MonthSummaryAdapter monthSummaryAdapter);

    void inject(EditAccountActivity editAccountActivity);

    void inject(EditAccountFragment editAccountFragment);

    void inject(AccountOperationsFragment accountRecordsFragment);

    void inject(RecordItemsBuilder recordItemsBuilder);

    void inject(ReportActivity.RecordReportConverter recordReportConverter);
}
