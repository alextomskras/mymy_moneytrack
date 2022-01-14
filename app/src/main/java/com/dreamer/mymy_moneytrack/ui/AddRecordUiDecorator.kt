package com.dreamer.mymy_moneytrack.ui

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.appcompat.app.ActionBar
import com.dreamer.mymy_moneytrack.R
import com.dreamer.mymy_moneytrack.activity.record.AddRecordActivity
import com.dreamer.mymy_moneytrack.entity.data.Record

/**
 * Util class to encapsulate toolbar customization for AddRecordActivity
 */
class AddRecordUiDecorator(private val activity: Activity) {
    @StyleRes
    private var dialogTheme = -1
    private val redLightColor: Int
    private val redDarkColor: Int
    private val greenLightColor: Int
    private val greenDarkColor: Int

    /**
     * @param type of record to handle, may be TYPE_EXPENSE or TYPE_INCOME
     * @return theme res id
     */
    @StyleRes
    fun getTheme(type: Int): Int {
        if (dialogTheme == -1) {
            when (type) {
                Record.TYPE_EXPENSE -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialogTheme = R.style.RedDialogTheme
                }
                Record.TYPE_INCOME -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialogTheme = R.style.GreenDialogTheme
                }
                else -> {}
            }
        }
        return dialogTheme
    }

    /**
     * @param actionBar to decorate
     * @param mode      - MODE_ADD or MODE_EDIT
     * @param type      of record to handle, may be TYPE_EXPENSE or TYPE_INCOME
     */
    fun decorateActionBar(actionBar: ActionBar?, mode: AddRecordActivity.Mode, type: Int) {
        if (actionBar == null) return
        when (type) {
            Record.TYPE_EXPENSE -> {
                if (mode === AddRecordActivity.Mode.MODE_ADD) actionBar.setTitle(R.string.title_add_expense) else actionBar.setTitle(
                    R.string.title_edit_expense
                )
                actionBar.setBackgroundDrawable(ColorDrawable(redLightColor))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = activity.window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = redDarkColor
                }
            }
            Record.TYPE_INCOME -> {
                if (mode === AddRecordActivity.Mode.MODE_ADD) actionBar.setTitle(R.string.title_add_income) else actionBar.setTitle(
                    R.string.title_edit_income
                )
                actionBar.setBackgroundDrawable(ColorDrawable(greenLightColor))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = activity.window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = greenDarkColor
                }
            }
            else -> {}
        }
    }

    init {
        val resources = activity.resources
        redLightColor = resources.getColor(R.color.red_light)
        redDarkColor = resources.getColor(R.color.red_dark)
        greenLightColor = resources.getColor(R.color.green_light)
        greenDarkColor = resources.getColor(R.color.green_dark)
    }
}