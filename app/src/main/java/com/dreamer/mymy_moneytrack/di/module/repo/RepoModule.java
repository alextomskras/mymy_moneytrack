package com.dreamer.mymy_moneytrack.di.module.repo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dreamer.mymy_moneytrack.entity.data.Account;
import com.dreamer.mymy_moneytrack.entity.data.Category;
import com.dreamer.mymy_moneytrack.entity.data.ExchangeRate;
import com.dreamer.mymy_moneytrack.entity.data.Record;
import com.dreamer.mymy_moneytrack.entity.data.Transfer;
import com.dreamer.mymy_moneytrack.repo.DbHelper;
import com.dreamer.mymy_moneytrack.repo.base.IRepo;
import com.dreamer.mymy_moneytrack.repo.data.AccountRepo;
import com.dreamer.mymy_moneytrack.repo.data.CategoryRepo;
import com.dreamer.mymy_moneytrack.repo.data.ExchangeRateRepo;
import com.dreamer.mymy_moneytrack.repo.data.RecordRepo;
import com.dreamer.mymy_moneytrack.repo.data.TransferRepo;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger 2 module to provide {@link IRepo} dependencies.
 * Created on 3/29/16.
 *
 * @author Evgenii Kanivets
 */
@Module
public class RepoModule {
    private final Context context;

    public RepoModule(Context context) {
        this.context = context;
    }

    @Provides
    @NonNull
    @Singleton
    public DbHelper providesDbHelper() {
        return new DbHelper(context);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Account> providesAccountRepo(DbHelper dbHelper) {
        return new AccountRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Category> providesCategoryRepo(DbHelper dbHelper) {
        return new CategoryRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<ExchangeRate> providesExchangeRateRepo(DbHelper dbHelper) {
        return new ExchangeRateRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Record> providesRecordRepo(DbHelper dbHelper) {
        return new RecordRepo(dbHelper);
    }

    @Provides
    @NonNull
    @Singleton
    public IRepo<Transfer> providesTransferRepo(DbHelper dbHelper) {
        return new TransferRepo(dbHelper);
    }
}
