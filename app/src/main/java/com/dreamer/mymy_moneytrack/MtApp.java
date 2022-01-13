package com.dreamer.mymy_moneytrack;

import android.app.Application;

import com.dreamer.mymy_moneytrack.di.AppComponent;
import com.dreamer.mymy_moneytrack.di.module.ControllerModule;
import com.dreamer.mymy_moneytrack.di.module.repo.CachedRepoModule;
import com.dreamer.mymy_moneytrack.util.CrashlyticsProxy;

import java.util.stream.DoubleStream;

import timber.log.BuildConfig;
import timber.log.Timber;

//import com.dreamer.mymy_moneytrack.di.DaggerAppComponent;


public class MtApp extends Application {
    private static MtApp mtApp;
//    private DoubleStream DaggerAppComponent;

    public static MtApp get() {
        return mtApp;
    }

    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        mtApp = this;
        buildAppComponent();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            CrashlyticsProxy.get().setEnabled(false);
        } else {
            Timber.plant(new ReleaseTree());
            CrashlyticsProxy.startCrashlytics(this);
            CrashlyticsProxy.get().setEnabled(true);
        }
    }

    public AppComponent getAppComponent() {
        return component;
    }

    public void buildAppComponent() {
        component = buildComponent();
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .cachedRepoModule(new CachedRepoModule(get()))
                .controllerModule(new ControllerModule(get()))
                .build();
    }

    private static class ReleaseTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            // Do nothing fot now
        }
    }
}