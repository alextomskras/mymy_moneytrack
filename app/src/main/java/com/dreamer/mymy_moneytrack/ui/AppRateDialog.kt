package com.dreamer.mymy_moneytrack.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.OnClick
import com.dreamer.mymy_moneytrack.MtApp
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.controller.PreferenceController
import javax.inject.Inject

class AppRateDialog(context: Context) : AlertDialog(
    context
) {
    @Inject
    var preferenceController: PreferenceController? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_rate)
        ButterKnife.bind(this@AppRateDialog)
    }

    @OnClick(R.id.yes_button)
    fun yes() {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(GP_MARKET + context.packageName)
            )
        )
        preferenceController!!.appRated()
        dismiss()
    }

    @OnClick(R.id.maybeButton)
    fun maybe() {
        dismiss()
    }

    @OnClick(R.id.thanksButton)
    fun thanks() {
        preferenceController!!.appRated()
        dismiss()
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val GP_MARKET = "market://details?id="
    }

    init {
        MtApp.get().appComponent.inject(this@AppRateDialog)
    }
}