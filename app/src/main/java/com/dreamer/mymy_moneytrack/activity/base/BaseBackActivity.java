package com.dreamer.mymy_moneytrack.activity.base;


import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.dreamer.mymy_moneytrack.R;

/**
 * Base implementation of {@link AppCompatActivity} to describe some common
 * methods.

 */
public abstract class BaseBackActivity extends BaseActivity {
    @SuppressWarnings("unused")
    private static final String TAG = "BaseBackActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        return toolbar;
    }
}
