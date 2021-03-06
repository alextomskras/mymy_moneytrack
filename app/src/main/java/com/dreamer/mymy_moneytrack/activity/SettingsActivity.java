package com.dreamer.mymy_moneytrack.activity;

import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.dreamer.mymy_moneytrack.BuildConfig;
import com.dreamer.mymy_moneytrack.MtApp;
import com.dreamer.mymy_moneytrack.R;
import com.dreamer.mymy_moneytrack.activity.base.BaseBackActivity;
import com.dreamer.mymy_moneytrack.controller.CurrencyController;
import com.dreamer.mymy_moneytrack.controller.FormatController;
import com.dreamer.mymy_moneytrack.controller.PreferenceController;
import com.dreamer.mymy_moneytrack.controller.data.AccountController;
import com.dreamer.mymy_moneytrack.entity.data.Account;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SettingsActivity extends BaseBackActivity {
    @SuppressWarnings("unused") private static final String TAG = "SettingsActivity";

    @Override protected int getContentViewId() {
        return R.layout.activity_settings;
    }

    @Override protected void initViews() {
        super.initViews();

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(R.id.contentView, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Inject AccountController accountController;
        @Inject CurrencyController currencyController;
        @Inject PreferenceController preferenceController;

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            MtApp.get().getAppComponent().inject(SettingsFragment.this);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            setupDefaultAccountPref();
            setupDefaultCurrencyPref();
            setupNonSubstitutionCurrencyPref();
            setupDisplayPrecision();
            setupAboutPref();
        }

        private void setupDefaultAccountPref() {
            ListPreference defaultAccountPref =
                    (ListPreference) findPreference(getString(R.string.pref_default_account));
            defaultAccountPref.setOnPreferenceChangeListener(preferenceChangeListener);

            List<Account> accountList = accountController.readActiveAccounts();
            defaultAccountPref.setEntries(getEntries(accountList));
            defaultAccountPref.setEntryValues(getEntryValues(accountList));

            Account defaultAccount = accountController.readDefaultAccount();
            if (defaultAccount == null) {
                defaultAccountPref.setDefaultValue("");
                defaultAccountPref.setSummary("");
            } else {
                defaultAccountPref.setDefaultValue(defaultAccount.getTitle());
                defaultAccountPref.setSummary(defaultAccount.getTitle());
            }
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument") private void setupDefaultCurrencyPref() {
            ListPreference defaultCurrencyPref =
                    (ListPreference) findPreference(getString(R.string.pref_default_currency));
            defaultCurrencyPref.setOnPreferenceChangeListener(preferenceChangeListener);

            List<String> currencyList = currencyController.readAll();
            defaultCurrencyPref.setEntries(currencyList.toArray(new String[0]));
            defaultCurrencyPref.setEntryValues(currencyList.toArray(new String[0]));

            String defaultCurrency = currencyController.readDefaultCurrency();
            defaultCurrencyPref.setDefaultValue(defaultCurrency);
            defaultCurrencyPref.setSummary(defaultCurrency);
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument") private void setupNonSubstitutionCurrencyPref() {
            ListPreference nonSubstitutionCurrencyPref =
                    (ListPreference) findPreference(getString(R.string.pref_non_substitution_currency));
            nonSubstitutionCurrencyPref.setOnPreferenceChangeListener(preferenceChangeListener);

            List<String> currencyList = currencyController.readAll();
            nonSubstitutionCurrencyPref.setEntries(currencyList.toArray(new String[0]));
            nonSubstitutionCurrencyPref.setEntryValues(currencyList.toArray(new String[0]));

            String nonSubstitutionCurrency = preferenceController.readNonSubstitutionCurrency();
            nonSubstitutionCurrencyPref.setDefaultValue(nonSubstitutionCurrency);
            nonSubstitutionCurrencyPref.setSummary(nonSubstitutionCurrency);
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument") private void setupDisplayPrecision() {
            ListPreference displayPrecisionPref =
                    (ListPreference) findPreference(getString(R.string.pref_display_precision));
            displayPrecisionPref.setOnPreferenceChangeListener(preferenceChangeListener);

            List<String> precisionListValues = new ArrayList<>();
            precisionListValues.add(FormatController.PRECISION_MATH);
            precisionListValues.add(FormatController.PRECISION_INT);
            precisionListValues.add(FormatController.PRECISION_NONE);
            displayPrecisionPref.setEntryValues(precisionListValues.toArray(new String[0]));

            List<String> precisionList = new ArrayList<>();
            precisionList.add(getString(R.string.precision_math));
            precisionList.add(getString(R.string.precision_int));
            precisionList.add(getString(R.string.precision_none));
            displayPrecisionPref.setEntries(precisionList.toArray(new String[0]));

            if (FormatController.PRECISION_MATH.equals(preferenceController.readDisplayPrecision())) {
                displayPrecisionPref.setDefaultValue(getString(R.string.precision_math));
                displayPrecisionPref.setSummary(getString(R.string.precision_math));
            }
        }

        private void setupAboutPref() {
            Preference preference = findPreference(getString(R.string.pref_about));
            preference.setSummary(getString(R.string.about_summary, BuildConfig.VERSION_NAME, Build.VERSION.RELEASE));
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        private String[] getEntries(List<Account> accountList) {
            List<String> result = new ArrayList<>();

            for (Account account : accountList) {
                result.add(account.getTitle());
            }

            return result.toArray(new String[0]);
        }

        @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
        private String[] getEntryValues(List<Account> accountList) {
            List<String> result = new ArrayList<>();

            for (Account account : accountList) {
                result.add(Long.toString(account.getId()));
            }

            return result.toArray(new String[0]);
        }

        private final Preference.OnPreferenceChangeListener preferenceChangeListener =
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        // Previously we could set summary to default value,
                        // but now it's needed to display selected entry
                        preference.setSummary("%s");
                        getActivity().setResult(RESULT_OK);
                        return true;
                    }
                };
    }
}
