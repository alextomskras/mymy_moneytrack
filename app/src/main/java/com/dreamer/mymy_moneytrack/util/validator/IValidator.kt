package com.dreamer.mymy_moneytrack.util.validator

interface IValidator<T> {
    /**
     * @return true if validation passed or false otherwise
     */
    fun validate(): Boolean

    companion object {
        const val MAX_ABS_VALUE = Int.MAX_VALUE * 1024L
    }
}