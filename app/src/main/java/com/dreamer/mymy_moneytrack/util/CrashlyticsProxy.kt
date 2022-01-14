package com.dreamer.mymy_moneytrack.util

import android.content.Context

class CrashlyticsProxy private constructor() {
    var isEnabled: Boolean
        get() = false
        set(enabled) {}

    fun logEvent(eventName: String?): Boolean {
        return false
    }

    fun logButton(buttonName: String?): Boolean {
        return false
    }

    companion object {
        private var instance: CrashlyticsProxy? = null

        @JvmStatic
        fun get(): CrashlyticsProxy? {
            if (instance == null) {
                instance = CrashlyticsProxy()
            }
            return instance
        }

        fun startCrashlytics(context: Context?) {}
    }
}