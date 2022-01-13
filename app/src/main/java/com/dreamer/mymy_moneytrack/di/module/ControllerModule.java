package com.dreamer.mymy_moneytrack.di.module;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dreamer.mymy_moneytrack.controller.backup.BackupController;
import com.dreamer.mymy_moneytrack.controller.external.ExportController;
import com.dreamer.mymy_moneytrack.controller.FormatController;
import com.dreamer.mymy_moneytrack.controller.PeriodController;
import com.dreamer.mymy_moneytrack.controller.data.AccountController;
import com.dreamer.mymy_moneytrack.controller.data.CategoryController;
import com.dreamer.mymy_moneytrack.controller.CurrencyController;
import com.dreamer.mymy_moneytrack.controller.data.ExchangeRateController;
import com.dreamer.mymy_moneytrack.controller.PreferenceController;
import com.dreamer.mymy_moneytrack.controller.data.RecordController;
import com.dreamer.mymy_moneytrack.controller.data.TransferController;
import com.dreamer.mymy_moneytrack.controller.external.ImportController;
import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.dreamer.mymy_moneytrack.entity.data.Category;
import com.dreamer.mymy_moneytrack.entity.data.ExchangeRate;
import com.dreamer.mymy_moneytrack.entity.data.Record;
import com.dreamer.mymy_moneytrack.entity.data.Transfer;
import com.dreamer.mymy_moneytrack.repo.base.IRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module to provide Controllers dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module public class ControllerModule {
    private Context context;

    public ControllerModule(Context context) {
        this.context = context;
    }

    @Provides @NonNull
    @Singleton public AccountController providesAccountController(IRepo<Account> accountRepo,
                                                                  PreferenceController preferenceController) {
        return new AccountController(accountRepo, preferenceController);
    }

    @Provides @NonNull @Singleton public CategoryController providesCategoryController(IRepo<Category> categoryRepo,
            PreferenceController preferenceController) {
        return new CategoryController(categoryRepo, preferenceController);
    }

    @Provides @NonNull @Singleton
    public ExchangeRateController providesExchangeRateController(IRepo<ExchangeRate> exchangeRateRepo) {
        return new ExchangeRateController(exchangeRateRepo);
    }

    @Provides @NonNull @Singleton
    public RecordController providesRecordController(IRepo<Record> recordRepo, CategoryController categoryController,
            AccountController accountController, PreferenceController preferenceController) {
        return new RecordController(recordRepo, categoryController, accountController, preferenceController);
    }

    @Provides @NonNull @Singleton public TransferController providesTransferController(IRepo<Transfer> transferRepo,
            AccountController accountController) {
        return new TransferController(transferRepo, accountController);
    }

    @Provides @NonNull @Singleton
    public CurrencyController providesCurrencyController(AccountController accountController,
            PreferenceController preferenceController) {
        return new CurrencyController(accountController, preferenceController);
    }

    @Provides @NonNull @Singleton public PreferenceController providesPreferenceController() {
        return new PreferenceController(context);
    }

    @Provides @NonNull @Singleton
    public PeriodController providesPeriodController(PreferenceController preferenceController) {
        return new PeriodController(preferenceController);
    }

    @Provides @NonNull @Singleton
    public FormatController providesFormatController(PreferenceController preferenceController) {
        return new FormatController(preferenceController);
    }

    @Provides @NonNull @Singleton public ExportController providesExportController(RecordController recordController,
            CategoryController categoryController) {
        return new ExportController(recordController, categoryController);
    }

    @Provides @NonNull @Singleton public ImportController providesImportController(RecordController recordController) {
        return new ImportController(recordController);
    }

    @Provides @NonNull @Singleton public BackupController providesBackupController(FormatController formatController) {
        return new BackupController(formatController, context.getApplicationInfo().dataDir);
    }
}
